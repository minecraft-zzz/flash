package com.google.mlkit.vision.demo.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.mlkit.vision.demo.R;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 读取 startTime.txt 文件中的时间戳
        long startTime = readTimestampFromFile();

        // 计算与当前时间的差值
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        // 将时间戳转换为 hh:mm:ss 格式的时间
        String formattedTime = convertMillisToTimeString(elapsedTime);

        // 获取图片展示框容器
        LinearLayout imageContainer = findViewById(R.id.image_container);

        TextView time = findViewById(R.id.text_view);
        //String dynamicText =  "st"+ String.valueOf(startTime) + "end"+ String.valueOf(currentTime);
        String dynamicText = "锻炼时间是：" + formattedTime;
        time.setText(dynamicText);


        // 假设你有一个图片资源数组和对应的讲解文字数组
        int[] imageResources = {R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark_focused, R.drawable.common_google_signin_btn_icon_light};
        String[] explanations = {"这是图片1的讲解", "这是图片2的讲解", "这是图片3的讲解"};

        // 遍历图片资源数组，为每个图片创建一个布局，包含 ImageView 和 TextView，并添加到容器中
        for (int i = 0; i < imageResources.length; i++) {
            // 创建图片展示框布局
            LinearLayout imageLayout = new LinearLayout(this);
            imageLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            imageLayout.setOrientation(LinearLayout.VERTICAL);

            // 创建 ImageView
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageResources[i]);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // 创建 TextView 用于讲解文字
            TextView textView = new TextView(this);
            textView.setText(explanations[i]);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // 将 ImageView 和 TextView 添加到图片展示框布局中
            imageLayout.addView(imageView);
            imageLayout.addView(textView);

            // 将图片展示框布局添加到图片展示框容器中
            imageContainer.addView(imageLayout);
        }
    }

    private long readTimestampFromFile() {
        BufferedReader reader = null;
        try {
            // 创建文件对象
            File file = new File(getFilesDir(), "pose_result/startTime.txt");

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