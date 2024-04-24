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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.util.Collections.max;

/**
 * Represents Pose classification result as outputted by {@link PoseClassifier}. Can be manipulated.
 */
public class ClassificationResult {
  // For an entry in this map, the key is the class name, and the value is how many times this class
  // appears in the top K nearest neighbors. The value is in range [0, K] and could be a float after
  // EMA smoothing. We use this number to represent the confidence of a pose being in this class.
  private final Map<String, Float> classConfidences;

  public ClassificationResult() {
    classConfidences = new HashMap<>();
  }

  public Set<String> getAllClasses() {
    return classConfidences.keySet();
  }

  public float getClassConfidence(String className) {
    return classConfidences.containsKey(className) ? classConfidences.get(className) : 0;
  }

  public String getMaxConfidenceClass() {
    return max(
        classConfidences.entrySet(),
        (entry1, entry2) -> (int) (entry1.getValue() - entry2.getValue()))
        .getKey();
  }

  public String getMaxConfidenceClasses(){
    List<String> maxClasses = new ArrayList<>();
    double maxValue = Double.MIN_VALUE;

    for (Map.Entry<String, Float> entry : classConfidences.entrySet()) {
      double value = entry.getValue();
      if (value > maxValue) {
        maxClasses.clear(); // 清空列表
        maxClasses.add(entry.getKey());
        maxValue = value;
      } else if (value == maxValue) {
        maxClasses.add(entry.getKey());
      }
    }

    // 从具有最大值的键列表中随机选择一个
    Random random = new Random();
    int randomIndex = random.nextInt(maxClasses.size());
    return maxClasses.get(randomIndex);
  }

  public void incrementClassConfidence(String className) {
    classConfidences.put(className,
        classConfidences.containsKey(className) ? classConfidences.get(className) + 1 : 1);
  }

  public void putClassConfidence(String className, float confidence) {
    classConfidences.put(className, confidence);
  }
}
