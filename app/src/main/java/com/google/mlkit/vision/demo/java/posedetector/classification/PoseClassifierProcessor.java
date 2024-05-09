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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
  private static final String POSE_ACCURACY_SAMPLES_FILE = "pose/squat_accuracy_csv_out.csv";

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
  private Context mcontext;

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
    mcontext = context;
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
        Log.e(TAG,"完成动作准确度分类");
        if (!poseForThisFrame.equals("standard")){
          outputWrongPose(poseForThisFrame);
        }
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

  public void outputWrongPose(String pose){
    long startTime = readTimestampFromFile();

    // 计算与当前时间的差值
    long currentTime = System.currentTimeMillis();
    long elapsedTime = currentTime - startTime;

    // 将时间戳转换为 hh:mm:ss 格式的时间
    String formattedTime = convertMillisToTimeString(elapsedTime);

    String text = "default";
    switch (pose){
      case "down_hand_wrong": text = "下蹲时手臂未合拢"; break;
      case "down_in": text = "膝盖内扣"; break;
      case "down_out": text = "膝盖外扣"; break;
      case "half_hand_wrong": text = "下蹲过程中手臂未合拢"; break;
      case "half_in": text = "下蹲过程中膝盖内扣"; break;
      case "half_out": text = "下蹲过程中膝盖外扣"; break;
      default: break;
    }

    String advice = "null";
    switch (pose){
      case "down_hand_wrong": advice = "双手自然合拢,放松肩部，有助于稳定身体平衡，减少压力。"; break;
      case "down_in": advice = "确保膝盖与脚尖方向一致，避免内扣，减少膝盖受力，保护关节。"; break;
      case "down_out": advice = "保持膝盖与脚尖轻微外扣，有助于稳定下蹲姿势，减少膝盖压力，降低受伤风险。"; break;
      case "half_hand_wrong": advice = "双手自然合拢，放置在胸前或上半身，有助于保持平衡，加强核心稳定性。"; break;
      case "half_in": advice = "注意膝盖与脚尖方向，避免内扣，保持对齐，降低膝盖压力，减少不适。"; break;
      case "half_out": advice = "确保膝盖与脚尖轻微外扣，保持稳定姿势，减少膝盖受力，降低受伤风险。"; break;
      default: break;
    }

    String outputTime = "在" + formattedTime + "时出现" + text + "\n";
    String outputAdcive = "AI建议：" + advice + "\n";
    String output = outputTime + outputAdcive;

    File file = new File(mcontext.getFilesDir(), "/pose_result/advice.txt");

    try {
      // 如果文件不存在，则创建新文件
      if (!file.exists()) {
        file.createNewFile();
      }

      // 将字符串写入文件
      FileWriter writer = new FileWriter(file);
      writer.write(output);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
      // 处理文件操作异常
    }


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

  private long readTimestampFromFile() {
    BufferedReader reader = null;
    try {
      // 创建文件对象
      File file = new File(mcontext.getFilesDir(), "pose_result/startTime.txt");

      // 创建文件读取器
      reader = new BufferedReader(new FileReader(file));

      // 读取文件中的时间戳
      String timestampString = reader.readLine();
      return Long.parseLong(timestampString);
    } catch (IOException e) {
      e.printStackTrace();
      return -1; // 读取失败，返回 -1 表示错误
    } finally {
      // 关闭读取器
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static String convertMillisToTimeString(long millis) {
    // 计算小时、分钟和秒
    long hours = millis / 3600000;
    long minutes = (millis % 3600000) / 60000;
    long seconds = ((millis % 3600000) % 60000) / 1000;

    // 格式化为 "hh:mm:ss"、"mm:ss" 或 "ss" 格式的字符串
    if (hours > 0) {
      return String.format("%02d时%02d分%02秒", hours, minutes, seconds);
    } else if (minutes > 0) {
      return String.format("%02d分%02d秒", minutes, seconds);
    } else {
      return String.format("%02d秒", seconds);
    }
  }

}
