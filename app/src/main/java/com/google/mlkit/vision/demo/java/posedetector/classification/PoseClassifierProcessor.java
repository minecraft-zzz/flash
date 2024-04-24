/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.java.posedetector.classification;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.WorkerThread;
import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Accepts a stream of {@link Pose} for classification and Rep counting.
 */
public class PoseClassifierProcessor {
  private static final String TAG = "PoseClassifierProcessor";
  private static final String POSE_SAMPLES_FILE = "pose/fitness_pose_samples.csv";
  private static final String POSE_ACCURACY_SAMPLES_FILE = "pose/squat_down_wrong.csv";

  // Specify classes for which we want rep counting.
  // These are the labels in the given {@code POSE_SAMPLES_FILE}. You can set your own class labels
  // for your pose samples.
  private static final String PUSHUPS_CLASS = "pushups_down";
  private static final String SQUATS_CLASS = "squats_down";
  private static final String[] POSE_CLASSES = {
    PUSHUPS_CLASS, SQUATS_CLASS
  };

  private final boolean isStreamMode;

  private EMASmoothing emaSmoothing;
  private List<RepetitionCounter> repCounters;
  private PoseClassifier poseClassifier;
  private PoseAccuracyClassifier poseAccuracyClassifier;
  private String lastRepResult;

  private boolean isEnteredPose;
  private int residualFrames;
  private static final double threshold = 0.9;
  private List<String> poseAccuracy;
  private static final float duration = 1;
  private static final int detectFrames = 10;
  private long startTime;
  private String retPose;

  @WorkerThread
  public PoseClassifierProcessor(Context context, boolean isStreamMode) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    this.isStreamMode = isStreamMode;
    if (isStreamMode) {
      emaSmoothing = new EMASmoothing();
      repCounters = new ArrayList<>();
      lastRepResult = "";
    }
    isEnteredPose = false;
    residualFrames = detectFrames;
    poseAccuracy = new ArrayList<>();
    startTime = System.currentTimeMillis();
    retPose = "";
    loadPoseSamples(context);
  }

  private void loadPoseSamples(Context context) {
    List<PoseSample> poseSamples = new ArrayList<>();
    List<PoseSample> poseAccuracySamples = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
      String csvLine = reader.readLine();
      while (csvLine != null) {
        // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
          poseSamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose samples.\n" + e);
    }
    try {
      BufferedReader reader = new BufferedReader(
              new InputStreamReader(context.getAssets().open(POSE_ACCURACY_SAMPLES_FILE)));
      String csvLine = reader.readLine();
      while (csvLine != null) {
        // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
          poseAccuracySamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose accuracy samples.\n" + e);
    }
    poseClassifier = new PoseClassifier(poseSamples);
    poseAccuracyClassifier = new PoseAccuracyClassifier(poseAccuracySamples);
    if (isStreamMode) {
      for (String className : POSE_CLASSES) {
        repCounters.add(new RepetitionCounter(className));
      }
    }
  }

  /**
   * Given a new {@link Pose} input, returns a list of formatted {@link String}s with Pose
   * classification results.
   *
   * <p>Currently it returns up to 2 strings as following:
   * 0: PoseClass : X reps
   * 1: PoseClass : [0.0-1.0] confidence
   */
  @WorkerThread
  public List<String> getPoseResult(Pose pose) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    List<String> result = new ArrayList<>();
    ClassificationResult classification = poseClassifier.classify(pose);

    // Update {@link RepetitionCounter}s if {@code isStreamMode}.
    if (isStreamMode) {
      // Feed pose to smoothing even if no pose found.
      classification = emaSmoothing.getSmoothedResult(classification);

      // Return early without updating repCounter if no pose found.
      if (pose.getAllPoseLandmarks().isEmpty()) {
        result.add(lastRepResult);
        return result;
      }

      for (RepetitionCounter repCounter : repCounters) {
        int repsBefore = repCounter.getNumRepeats();
        int repsAfter = repCounter.addClassificationResult(classification);
        if (repsAfter > repsBefore) {
          // Play a fun beep when rep counter updates.
          ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
          tg.startTone(ToneGenerator.TONE_PROP_BEEP);
          lastRepResult = String.format(
              Locale.US, "%s : %d reps", repCounter.getClassName(), repsAfter);
          break;
        }
      }
      result.add(lastRepResult);
      }

    // Add maxConfidence class of current frame to result if pose is found.
    if (!pose.getAllPoseLandmarks().isEmpty()) {
      //String maxConfidenceClass = classification.getMaxConfidenceClass();
      String maxConfidenceClass = SQUATS_CLASS;
      String maxConfidenceClassResult = String.format(
          Locale.US,
          "%s : %.2f confidence",
          maxConfidenceClass,
          classification.getClassConfidence(maxConfidenceClass)
              / poseClassifier.confidenceRange());
      result.add(maxConfidenceClassResult);
    }

//    lastRepResult = String.format(
//            Locale.US, "a : 100 reps");
//    result.add(lastRepResult);
//    String maxConfidenceClassResult = String.format(
//            Locale.US,
//            "b : 100 confidence"
//            );
//    result.add(maxConfidenceClassResult);
    return result;
  }

  @WorkerThread
  public String getPoseAccuracy(Pose pose,double conf){
    if (!isEnteredPose && conf >= threshold){
      isEnteredPose = true;
      residualFrames = detectFrames;
      poseAccuracy = new ArrayList<>();
      retPose = "";
    }
    if (isEnteredPose && conf >= threshold){
      if (residualFrames > 0){
        residualFrames--;
        String poseForThisFrame = poseAccuracyClassifier.getPoseAccuracy(pose);
        poseAccuracy.add(poseForThisFrame);
      }
      else if (residualFrames == 0){
        retPose = getMostCommonElement(poseAccuracy);
        startTime = System.currentTimeMillis();
      }
    }
    if(isEnteredPose && conf < threshold){
      if (residualFrames > 0){
        retPose = "too fast";
      }
      isEnteredPose = false;
    }
    if (updateReturnPose(startTime)){retPose = "";}
    return retPose;
  }


  public static String getMostCommonElement(List<String> list) {
    // 创建一个 HashMap 来统计每个字符串出现的次数
    Map<String, Integer> frequencyMap = new HashMap<>();

    // 统计每个字符串出现的次数
    for (String str : list) {
      Integer frequency = frequencyMap.get(str);
      if (frequency == null) {
        frequency = 0;
      }
      frequencyMap.put(str, frequency + 1);
    }

    // 找出出现次数最多的字符串
    String mostCommonElement = ""; // 默认为空字符串
    int maxFrequency = 0;

    for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
      String key = entry.getKey();
      int frequency = entry.getValue();

      if (frequency > maxFrequency) {
        mostCommonElement = key;
        maxFrequency = frequency;
      }
    }

    return mostCommonElement;
  }


  public boolean updateReturnPose(long start){
    long now = System.currentTimeMillis();
    float time = (float) ((now - start) / 1000.0);
    return time > duration;
  }
}
