package com.google.mlkit.vision.demo.java;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.mlkit.vision.demo.R;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.SeekBar;


import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {
    private VideoView videoView;

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


        TextView time = findViewById(R.id.text_view);
        //String dynamicText =  "st"+ String.valueOf(startTime) + "end"+ String.valueOf(currentTime);
        String dynamicText = "锻炼时间是：" + formattedTime;
        time.setText(dynamicText);




        videoView = findViewById(R.id.video_view);

        File poseResultFolder = new File(getFilesDir(), "pose_result_video");
        File recordedVideoFile = new File(poseResultFolder, "recorded_video.mp4");
        // 指定视频文件的路径
        String videoPath = recordedVideoFile.getAbsolutePath();

        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();

        Log.e("aaa","展示");

        TextView noticeTextView = findViewById(R.id.notice_text_view);


//        // 创建一个 StringBuilder 对象来保存文件名
//        StringBuilder stringBuilder = new StringBuilder();
//// 检查文件夹是否存在
//        if (poseResultFolder.exists() && poseResultFolder.isDirectory()) {
//            // 获取pose_result文件夹中的所有文件
//            File[] files = poseResultFolder.listFiles();
//            // 遍历所有文件，并将文件名添加到 stringBuilder 中
//            for (File file : files) {
//                if (file.isFile()) {
//                    stringBuilder.append(file.getName()).append("\n");
//                }
//            }
//        }
//
//            // 将 stringBuilder 中的内容转换为字符串
//        String fileList = stringBuilder.toString();
//        // 设置文本内容
//        noticeTextView.setText(fileList);

        String noticeText = "所有动作都非常标准！";
        File file = new File(getFilesDir(), "/pose_result/advice.txt");
        try {
            // 如果文件不存在，则创建新文件
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                // 创建字节数组，用于存储文件内容
                byte[] bytes = new byte[(int) file.length()];
                // 读取文件内容到字节数组中
                fis.read(bytes);
                // 关闭文件输入流
                fis.close();
                // 将字节数组转换为字符串
                noticeText = new String(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件操作异常
        }

        noticeTextView.setText(noticeText);


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

    private void playVideo(String path) {
        if (videoView != null) {
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
        }
    }
}