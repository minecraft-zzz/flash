package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;

public class VideoDetailsActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        // Find views
        mVideoView = findViewById(R.id.videoView);

        // Get video information from intent
        Intent intent = getIntent();
        if (intent != null) {
            String videoName = intent.getStringExtra("videoName");
            if (videoName != null) {
                int videoResourceId = getResources().getIdentifier(videoName, "raw", getPackageName());
                String uri = "android.resource://" + getPackageName() + "/" + videoResourceId;
                mVideoView.setVideoURI(Uri.parse(uri));
                mVideoView.start();
            }
        }
    }
}
