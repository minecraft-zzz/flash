package com.google.mlkit.vision.demo.java;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import com.google.mlkit.vision.demo.R;

public class Activity_person extends BaseActivity {

    private boolean isSwitching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().hide();

        LinearLayout homeNav = findViewById(R.id.home);
        LinearLayout diaryNav = findViewById(R.id.diary);
        LinearLayout libraryNav = findViewById(R.id.action);
        // 找到 Switch 组件
//        Switch nightModeSwitch = findViewById(R.id.switchMode);
//        int savedNightMode = getSavedNightModeState();
//        AppCompatDelegate.setDefaultNightMode(savedNightMode);

        // 设置 Switch 组件的状态
//        nightModeSwitch.setChecked(savedNightMode == AppCompatDelegate.MODE_NIGHT_YES);

        // 设置夜间模式切换监听器
//        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // 如果正在进行切换，不执行下面的操作
//                if (isSwitching) {
//                    return;
//                }
//
//                // 标记为正在切换
//                isSwitching = true;
//
//                // 获取新的夜间模式
//                int newNightMode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
//
//                // 设置夜间模式
//                getDelegate().setLocalNightMode(newNightMode);
//
//                // 通知系统重新绘制窗口内容
//                getWindow().getDecorView().invalidate();
//
//                // 添加日志以跟踪事件
//                Log.d("NightModeSwitch", "Switch state changed. New Night Mode: " + newNightMode);
//
//                // 保存夜间模式状态
//                saveNightModeState(newNightMode);
//
//                // 延迟一段时间后，将切换状态标记为 false
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isSwitching = false;
//                    }
//                }, 1000); // 这里的延迟时间可以根据需要调整
//            }
//        });


        homeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "主页" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_person.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        diaryNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "日记" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_person.this, Activity_diary.class);
                startActivity(intent);
                finish();
            }
        });

        libraryNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "动作库" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_person.this, Activity_action.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
