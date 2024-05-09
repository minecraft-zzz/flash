package com.google.mlkit.vision.demo.java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.mlkit.vision.demo.R;

public class newactivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        ImageView imageView = findViewById(R.id.fab);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "日记" 导航项，启动相应的 Activity
                Intent intent = new Intent(newactivity.this, newactivity.class);
                startActivity(intent);
            }
        });
    }

}
