package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.mlkit.vision.demo.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PersonalInfoActivity extends AppCompatActivity {
    private LinearLayout homeNav, diaryNav, libraryNav;
    private TextView tvName, tvUsername, tvAge, tvPhone;
    private Button btnChangePhoto, btnLogout;
    private ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_activity);

        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvAge = findViewById(R.id.tvAge);
        tvPhone = findViewById(R.id.tvPhone);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnLogout = findViewById(R.id.btnLogout);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    ((CircleImageView) findViewById(R.id.imgProfile)).setImageURI(uri);
                    storeImageLocally(uri);
                }
            }
        });

        btnChangePhoto.setOnClickListener(v -> mGetContent.launch("image/*"));
        btnLogout.setOnClickListener(this::logout);

        setupNavigation();
        loadUserInfoFromPrefs();
        loadProfileImage();
    }

    private void storeImageLocally(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            FileOutputStream fos = openFileOutput("profile_picture.png", MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfileImage() {
        Bitmap bitmap = loadImageFromStorage();
        if (bitmap != null) {
            ((CircleImageView) findViewById(R.id.imgProfile)).setImageBitmap(bitmap);
        }
    }

    private Bitmap loadImageFromStorage() {
        try {
            FileInputStream fis = openFileInput("profile_picture.png");
            return BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupNavigation() {
        homeNav = findViewById(R.id.home);
        diaryNav = findViewById(R.id.diary);
        libraryNav = findViewById(R.id.action);

        homeNav.setOnClickListener(v -> navigateTo(MainActivity.class));
        diaryNav.setOnClickListener(v -> navigateTo(Activity_diary.class));
        libraryNav.setOnClickListener(v -> navigateTo(Activity_action.class));
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(PersonalInfoActivity.this, activityClass);
        startActivity(intent);
        finish();
    }

    private void loadUserInfoFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("com.google.mlkit.vision.demo.preferences", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "N/A");
        String username = prefs.getString("username", "N/A");
        int age = prefs.getInt("age", 0);
        String phone = prefs.getString("phone", "N/A");

        tvName.setText("Name: " + name);
        tvUsername.setText("Username: " + username);
        tvAge.setText("Age: " + age);
        tvPhone.setText("Phone: " + phone);
    }

    public void logout(View view) {
        SharedPreferences prefs = getSharedPreferences("com.google.mlkit.vision.demo.preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // 清除所有存储的数据
        editor.apply();

        Intent intent = new Intent(this, Activity_person.class);
        startActivity(intent);
        finish(); // 注销后结束这个活动
    }
}
