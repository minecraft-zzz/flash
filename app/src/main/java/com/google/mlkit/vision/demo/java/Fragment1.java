package com.google.mlkit.vision.demo.java;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;
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


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.mlkit.vision.demo.R;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView chatTextView;
    private String originalText;
    private ViewPager2 viewPager2;
    private VideoView videoView;

    private static class MyHandler extends Handler {
        private final WeakReference<Fragment1> fragmentRef;

        MyHandler(Fragment1 fragment) {
            fragmentRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Fragment1 fragment = fragmentRef.get();
            if (fragment != null) {
                fragment.handleMessage(msg);
            }
        }
    }

    private MyHandler handler;

    public Fragment1() {
        // Required empty public constructor
    }

    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        viewPager2 = view.findViewById(R.id.viewPager2);
        List<Integer> photoList = new ArrayList<>();
        photoList.add(R.drawable.image1_placeholder);
        photoList.add(R.drawable.image2_placeholder);
        photoList.add(R.drawable.image3_placeholder);

        PhotoAdapter photoAdapter = new PhotoAdapter(photoList, requireContext());
        viewPager2.setAdapter(photoAdapter);

        startAutoCycle();

        CardView cardView = view.findViewById(R.id.imgCard1);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
            }
        });

        chatTextView = view.findViewById(R.id.chattextview);
        chatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });


        videoView = view.findViewById(R.id.video_view);

        File poseResultFolder = new File(requireContext().getFilesDir(), "pose_result_video");
        File recordedVideoFile = new File(poseResultFolder, "recorded_video.mp4");

        // 检查文件是否存在
        if (recordedVideoFile.exists()) {
            // 文件存在，使用文件路径
            Uri uri = Uri.parse(recordedVideoFile.getAbsolutePath());
            videoView.setVideoURI(uri);
        } else {
            // 文件不存在，使用raw资源
            Uri uri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.a1);
            videoView.setVideoURI(uri);
        }

        videoView.start();

        originalText = chatTextView.getText().toString();
        chatTextView.setText("");

        handler = new MyHandler(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeInText(originalText);
            }
        }, 1000);

        return view;
    }

    private void startAutoCycle() {
        final int delay = 3000;
        final int duration = 500;

        final Handler autoCycleHandler = new Handler();
        autoCycleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                int totalItems = viewPager2.getAdapter().getItemCount();

                viewPager2.setCurrentItem((currentItem + 1) % totalItems);

                autoCycleHandler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void fadeInText(final String text) {
        final int length = text.length();
        final int durationPerCharacter = 120;

        for (int i = 0; i < length; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatTextView.append(String.valueOf(text.charAt(index)));

                    if (index == length - 1) {
                        // Additional actions after last character
                    }
                }
            }, i * durationPerCharacter);
        }
    }

    private void handleMessage(Message msg) {
        // Handle messages if necessary
    }
}
