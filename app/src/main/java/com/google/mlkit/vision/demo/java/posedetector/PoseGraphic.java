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

package com.google.mlkit.vision.demo.java.posedetector;


import static java.lang.Math.max;
import static java.lang.Math.min;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.GraphicOverlay.Graphic;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/** Draw the detected pose in preview. */
public class PoseGraphic extends Graphic {

  private static final float DOT_RADIUS = 8.0f;
  private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
  private static final float STROKE_WIDTH = 10.0f;
  private static final float POSE_CLASSIFICATION_TEXT_SIZE = 60.0f;

  private final Pose pose;
  private final boolean showInFrameLikelihood;
  private final boolean visualizeZ;
  private final boolean rescaleZForVisualization;
  private float zMin = Float.MAX_VALUE;
  private float zMax = Float.MIN_VALUE;

  private final List<String> poseClassification;
  private final Paint classificationTextPaint;
  private final Paint leftPaint;
  private final Paint rightPaint;
  private final Paint whitePaint;

  PoseGraphic(
      GraphicOverlay overlay,
      Pose pose,
      boolean showInFrameLikelihood,
      boolean visualizeZ,
      boolean rescaleZForVisualization,
      List<String> poseClassification) {
    super(overlay);
    this.pose = pose;
    this.showInFrameLikelihood = showInFrameLikelihood;
    this.visualizeZ = visualizeZ;
    this.rescaleZForVisualization = rescaleZForVisualization;

    this.poseClassification = poseClassification;
    classificationTextPaint = new Paint();
    classificationTextPaint.setColor(Color.WHITE);
    classificationTextPaint.setTextSize(POSE_CLASSIFICATION_TEXT_SIZE);
    classificationTextPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);

    whitePaint = new Paint();
    whitePaint.setStrokeWidth(STROKE_WIDTH);
    whitePaint.setColor(Color.WHITE);
    whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
    leftPaint = new Paint();
    leftPaint.setStrokeWidth(STROKE_WIDTH);
    leftPaint.setColor(Color.GREEN);
    rightPaint = new Paint();
    rightPaint.setStrokeWidth(STROKE_WIDTH);
    rightPaint.setColor(Color.YELLOW);
  }

  @Override
  public void draw(Canvas canvas) {
    List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
    if (landmarks.isEmpty()) {
      return;
    }

    Paint textPaint = new Paint();
    textPaint.setColor(Color.WHITE); // 设置文本颜色为黑色
    textPaint.setTextSize(40); // 设置文本大小为 40 像素
    textPaint.setAntiAlias(true); // 设置抗锯齿，使文本边缘更加平滑

    // Draw pose classification text.
    float classificationX = POSE_CLASSIFICATION_TEXT_SIZE * 0.5f;
    //canvas.drawText(Integer.toString(poseClassification.size()), x, y, textPaint);
    for (int i = 0; i < poseClassification.size(); i++) {
      float classificationY =
          (canvas.getHeight()
              - POSE_CLASSIFICATION_TEXT_SIZE * 1.5f * (poseClassification.size() - i));
      canvas.drawText(
              poseClassification.get(i), classificationX, classificationY, classificationTextPaint);
      if( i == poseClassification.size() - 1){
        String[] parts = poseClassification.get(i).split(":");
        Log.w("parts",parts[0]);
        double conf = Double.parseDouble(parts[1].trim().split(" ")[0]);
        //showPopText(conf);
        if(parts[0].equals("squats_down ") && conf > 0.9 ){
          drawNotice(canvas);
        }
      }
    }


    // Draw all the points
    for (PoseLandmark landmark : landmarks) {
      drawPoint(canvas, landmark, whitePaint);
      if (visualizeZ && rescaleZForVisualization) {
        zMin = min(zMin, landmark.getPosition3D().getZ());
        zMax = max(zMax, landmark.getPosition3D().getZ());
      }
    }

    PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
    PoseLandmark lefyEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
    PoseLandmark lefyEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
    PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
    PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
    PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
    PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
    PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
    PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
    PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
    PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);

    PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
    PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
    PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
    PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
    PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
    PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
    PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
    PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
    PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
    PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
    PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
    PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

    PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
    PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
    PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
    PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
    PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
    PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
    PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
    PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
    PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
    PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

    // Face
    drawLine(canvas, nose, lefyEyeInner, whitePaint);
    drawLine(canvas, lefyEyeInner, lefyEye, whitePaint);
    drawLine(canvas, lefyEye, leftEyeOuter, whitePaint);
    drawLine(canvas, leftEyeOuter, leftEar, whitePaint);
    drawLine(canvas, nose, rightEyeInner, whitePaint);
    drawLine(canvas, rightEyeInner, rightEye, whitePaint);
    drawLine(canvas, rightEye, rightEyeOuter, whitePaint);
    drawLine(canvas, rightEyeOuter, rightEar, whitePaint);
    drawLine(canvas, leftMouth, rightMouth, whitePaint);

    drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
    drawLine(canvas, leftHip, rightHip, whitePaint);

    // Left body
    drawLine(canvas, leftShoulder, leftElbow, leftPaint);
    drawLine(canvas, leftElbow, leftWrist, leftPaint);
    drawLine(canvas, leftShoulder, leftHip, leftPaint);
    drawLine(canvas, leftHip, leftKnee, leftPaint);
    drawLine(canvas, leftKnee, leftAnkle, leftPaint);
    drawLine(canvas, leftWrist, leftThumb, leftPaint);
    drawLine(canvas, leftWrist, leftPinky, leftPaint);
    drawLine(canvas, leftWrist, leftIndex, leftPaint);
    drawLine(canvas, leftIndex, leftPinky, leftPaint);
    drawLine(canvas, leftAnkle, leftHeel, leftPaint);
    drawLine(canvas, leftHeel, leftFootIndex, leftPaint);

    // Right body
    drawLine(canvas, rightShoulder, rightElbow, rightPaint);
    drawLine(canvas, rightElbow, rightWrist, rightPaint);
    drawLine(canvas, rightShoulder, rightHip, rightPaint);
    drawLine(canvas, rightHip, rightKnee, rightPaint);
    drawLine(canvas, rightKnee, rightAnkle, rightPaint);
    drawLine(canvas, rightWrist, rightThumb, rightPaint);
    drawLine(canvas, rightWrist, rightPinky, rightPaint);
    drawLine(canvas, rightWrist, rightIndex, rightPaint);
    drawLine(canvas, rightIndex, rightPinky, rightPaint);
    drawLine(canvas, rightAnkle, rightHeel, rightPaint);
    drawLine(canvas, rightHeel, rightFootIndex, rightPaint);

    // Draw inFrameLikelihood for all points
    if (showInFrameLikelihood) {
      for (PoseLandmark landmark : landmarks) {
        canvas.drawText(
            String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
            translateX(landmark.getPosition().x),
            translateY(landmark.getPosition().y),
            whitePaint);
      }
    }


  }

  void drawNotice(Canvas canvas){
    String text = "你的动作很标准，请继续保持";

    int viewWidth = canvas.getWidth();
    int viewHeight = canvas.getHeight();
    Paint paint = new Paint();
    // 绘制透明底
    //canvas.drawColor(Color.parseColor("#80000000")); // 半透明黑色背景

    // 绘制白色小矩形
    int rectWidth = viewWidth;
    int rectHeight = 300;
    int rectX = 0;
    int rectY = 0;
    paint.setColor(Color.parseColor("#A0FFFFFF"));
    canvas.drawRect(rectX, rectY, viewWidth, rectHeight, paint);

    // 绘制文字
    paint.setColor(Color.BLACK);
    paint.setTextSize(60);
    float textWidth = paint.measureText(text); // 获取文字的宽度
    float textX = rectX + (rectWidth - textWidth) / 2; // 计算文字的 x 坐标，使其居中显示在矩形内部
    float textY = rectY + rectHeight / 2 + 15; // 计算文字的 y 坐标，使其居中显示在矩形内部
    canvas.drawText(text, textX, textY, paint);
  }

//  public AlertDialog alertDialog;
//  void showPopText(double confidence){
//    AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
//
//    // 可选：设置弹窗的标题、消息等
//    builder.setTitle("弹窗标题");
//    builder.setMessage("这是一个弹窗消息。");
//
//    // 创建并显示弹窗
//    alertDialog = builder.create();
//    alertDialog.show();
//    if (confidence > 0.8) {
//      showPopup();
//    }
//    else{
//      hidePopup();
//    }
//  }
//
//  // 显示弹窗方法
//  private void showPopup() {
//    if (alertDialog != null && !alertDialog.isShowing()) {
//      alertDialog.show();
//    }
//  }
//
//  // 隐藏弹窗方法
//  private void hidePopup() {
//    if (alertDialog != null && alertDialog.isShowing()) {
//      alertDialog.dismiss();
//    }
//  }

  void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
    PointF3D point = landmark.getPosition3D();
    updatePaintColorByZValue(
        paint, canvas, visualizeZ, rescaleZForVisualization, point.getZ(), zMin, zMax);
    canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
  }

  void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
    PointF3D start = startLandmark.getPosition3D();
    PointF3D end = endLandmark.getPosition3D();

    // Gets average z for the current body line
    float avgZInImagePixel = (start.getZ() + end.getZ()) / 2;
    updatePaintColorByZValue(
        paint, canvas, visualizeZ, rescaleZForVisualization, avgZInImagePixel, zMin, zMax);

    canvas.drawLine(
        translateX(start.getX()),
        translateY(start.getY()),
        translateX(end.getX()),
        translateY(end.getY()),
        paint);
  }
}
