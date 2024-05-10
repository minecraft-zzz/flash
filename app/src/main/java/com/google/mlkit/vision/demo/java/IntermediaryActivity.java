package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.mlkit.vision.demo.R;
import androidx.appcompat.app.AppCompatActivity;

public class IntermediaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediary);

        ImageView imageView = findViewById(R.id.image_to_camera);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntermediaryActivity.this, CameraXLivePreviewActivity.class);
                startActivity(intent);
            }
        });
    }
}
