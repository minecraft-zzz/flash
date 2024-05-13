package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.mlkit.vision.demo.R;
import java.util.ArrayList;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class Activity_diary extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Objects.requireNonNull(getSupportActionBar()).hide();

        LinearLayout homeNav = findViewById(R.id.home);
        LinearLayout libraryNav = findViewById(R.id.action);
        LinearLayout profileNav = findViewById(R.id.person);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            ArrayList<Integer> videoResourceIds = intent.getIntegerArrayListExtra("videoResourceIds");
            ArrayList<String> videoNames = intent.getStringArrayListExtra("videoNames");
            ArrayList<Integer> groupNumbers = intent.getIntegerArrayListExtra("groupNumbers");
            ArrayList<Integer> numbers = intent.getIntegerArrayListExtra("numbers");
            int selected_year = intent.getIntExtra("selected_year", 0);
            int selected_month = intent.getIntExtra("selected_month", 0);
            int selected_day = intent.getIntExtra("selected_day", 0);

            bundle.putString("title", title);
            bundle.putString("content", content);
            bundle.putIntegerArrayList("videoResourceIds", videoResourceIds);
            bundle.putStringArrayList("videoNames", videoNames);
            bundle.putIntegerArrayList("groupNumbers", groupNumbers);
            bundle.putIntegerArrayList("numbers", numbers);
            bundle.putInt("year", selected_year);
            bundle.putInt("month", selected_month);
            bundle.putInt("dayOfMonth", selected_day);
        }

        // Create an instance of your ViewPager2 adapter and set it to the viewPager2
        MyPagerAdapter1 pagerAdapter = new MyPagerAdapter1(this);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setBundle(bundle);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the tab text based on the position
            switch (position) {
                case 0:
                    tab.setText("Diary");
                    break;
                case 1:
                    tab.setText("Chart");
                    break;
                // Add more cases if you have more tabs
            }
        }).attach();

        homeNav.setOnClickListener(v -> {
            Intent intent12 = new Intent(Activity_diary.this, MainActivity.class);
            startActivity(intent12);
            finish();
        });

        libraryNav.setOnClickListener(v -> {
            Intent intent13 = new Intent(Activity_diary.this, Activity_action.class);
            startActivity(intent13);
            finish();
        });

        profileNav.setOnClickListener(v -> {
            Intent intent14 = new Intent(Activity_diary.this, Activity_person.class);
            startActivity(intent14);
            finish();
        });
    }
}

