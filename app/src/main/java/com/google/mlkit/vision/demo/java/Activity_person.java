package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.dao.UserDao;
import com.google.mlkit.vision.demo.entity.User;

public class Activity_person extends AppCompatActivity {

    private EditText editTextName, editTextPassword;
    private LinearLayout homeNav, diaryNav, libraryNav;

    private static final String TAG = "Activity_person"; // 添加一个TAG用于日志打印

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkLoginState()) {
            Intent intent = new Intent(this, PersonalInfoActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_person);
            initializeLoginComponents();
            setupNavigation();
        }

        ImageView imageView = findViewById(R.id.fab);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "日记" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_person.this, CameraXLivePreviewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeLoginComponents() {
        editTextName = findViewById(R.id.name);
        editTextPassword = findViewById(R.id.password);
        findViewById(R.id.button2).setOnClickListener(this::login);
        findViewById(R.id.button3).setOnClickListener(this::reg);
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
        Intent intent = new Intent(Activity_person.this, activityClass);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString();
        new Thread(() -> {
            UserDao userDao = new UserDao();
            boolean loginSuccess = userDao.login(name, password);
            if (loginSuccess) {
                Log.d(TAG, "登录验证成功，正在从数据库获取用户信息");
                User user = userDao.findUser(name);
                if (user != null) {
                    Log.d(TAG, "从数据库成功获取用户信息");
                    saveUserInfo(user);
                } else {
                    Log.d(TAG, "未能从数据库获取用户信息");
                }
                hand1.sendEmptyMessage(1);
            } else {
                Log.d(TAG, "登录验证失败");
                hand1.sendEmptyMessage(0);
            }
        }).start();
    }

    public void reg(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    final Handler hand1 = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                navigateTo(MainActivity.class);
            } else {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
            }
        }
    };

    private static final String PREF_FILE_NAME = "com.google.mlkit.vision.demo.preferences";

    private void saveUserInfo(User user) {
        SharedPreferences prefs = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putInt("age", user.getAge());
        editor.putString("phone", user.getPhone());
        editor.apply();
        Log.d(TAG, "用户信息已存储到本地SharedPreferences");
    }

    private boolean checkLoginState() {
        SharedPreferences prefs = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }
}
