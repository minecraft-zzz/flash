package com.google.mlkit.vision.demo.java;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.vision.demo.R;

public class Activity_diary extends BaseActivity {

    private TextView titleTextView;
    private TextView contentTextView;
    private VideoAdapter mAdapter;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Objects.requireNonNull(getSupportActionBar()).hide();


        LinearLayout homeNav = findViewById(R.id.home);
        LinearLayout libraryNav = findViewById(R.id.action);
        LinearLayout profileNav = findViewById(R.id.person);
        CalendarView calendarView = findViewById(R.id.calendarView);


        RecyclerView recyclerView = findViewById(R.id.recyclerView); // 更换为 RecyclerView

        // 创建并设置适配器
        mAdapter = new VideoAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // 设置布局管理器
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // 不处理拖动事件
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.removeVideo(position); // 自定义方法，删除特定位置的视频
                // 删除对应的 SharedPreferences 存储的条目
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        titleTextView = findViewById(R.id.title); // 找到用于显示标题的 TextView
        contentTextView = findViewById(R.id.textview); // 找到用于显示内容的 TextView

        int savedNightMode = getSavedNightModeState();
        AppCompatDelegate.setDefaultNightMode(savedNightMode);

        // 接收来自 diary_new 页面的标题和内容
        Intent intent = getIntent();
        if (intent != null) {
            String receivedTitle = intent.getStringExtra("title");
            String receivedContent = intent.getStringExtra("content");

            ArrayList<Integer> videoResourceIds = intent.getIntegerArrayListExtra("videoResourceIds");
            ArrayList<String> videoNames = intent.getStringArrayListExtra("videoNames");
            ArrayList<Integer> groupNumbers = intent.getIntegerArrayListExtra("groupNumbers"); // 接收组号列表
            ArrayList<Integer> numbers = intent.getIntegerArrayListExtra("numbers"); // 接收数字列表
            int year = intent.getIntExtra("year", -1); // 接收 year
            int month = intent.getIntExtra("month", -1); // 接收 month
            int dayOfMonth = intent.getIntExtra("dayOfMonth", -1); // 接收 dayOfMonth

            // 在 Activity_diary 中接收到数据后，创建 DiaryEntry 对象并保存到本地
            DiaryEntry entry = new DiaryEntry(receivedTitle, receivedContent, videoResourceIds, videoNames, groupNumbers, numbers, year, month, dayOfMonth);
            // 使用 SharedPreferences 保存 DiaryEntry 对象到本地
            SharedPreferences sharedPreferences = getSharedPreferences("diary_entries", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String jsonEntry = gson.toJson(entry);
            editor.putString("entry_" + year + "_" + month + "_" + dayOfMonth, jsonEntry);
            editor.putString("dateArray_" + year + "_" + month + "_" + dayOfMonth, gson.toJson(entry.getDateArray()));
            editor.apply();
        }

        // 在你的类中的某个方法里
        Calendar todayCalendar = Calendar.getInstance();
        int currentYear = todayCalendar.get(Calendar.YEAR);
        int currentMonth = todayCalendar.get(Calendar.MONTH);
        int currentDayOfMonth = todayCalendar.get(Calendar.DAY_OF_MONTH);

        lastSelectedYear = currentYear; // 上次选中的年份
        lastSelectedMonth = currentMonth; // 上次选中的月份
        lastSelectedDay = currentDayOfMonth; // 上次选中的日期

        // 构建对应的键值
        String key = "entry_" + currentYear + "_" + currentMonth + "_" + currentDayOfMonth;

        // 从 SharedPreferences 中读取保存的 DiaryEntry 对象的 JSON 字符串
        String jsonEntry = getSharedPreferences("diary_entries", MODE_PRIVATE).getString(key, "");

        // 使用 Gson 库将 JSON 字符串反序列化为 DiaryEntry 对象
        DiaryEntry entry = new Gson().fromJson(jsonEntry, DiaryEntry.class);
        if (entry != null) {
            String title = entry.getTitle();
            String content = entry.getContent();
            ArrayList<Integer> videoResourceIds = entry.getVideoResourceIds();
            ;
            ArrayList<String> videonames = entry.getVideoNames();
            ;
            ArrayList<Integer> GroupNumbers = entry.getGroupNumbers();
            ArrayList<Integer> Numbers = entry.getNumbers();
            titleTextView.setText(title);
            contentTextView.setText(content);
            mAdapter.setVideoResourceIds(videoResourceIds, videonames, GroupNumbers, Numbers);
        }
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (year == lastSelectedYear && month == lastSelectedMonth && dayOfMonth == lastSelectedDay) {
                    Intent intent = new Intent(Activity_diary.this, diary_new.class);
                    intent.putExtra("selected_year", year);
                    intent.putExtra("selected_month", month);
                    intent.putExtra("selected_day", dayOfMonth);
                    startActivity(intent);
                } else {
                    lastSelectedYear = year;
                    lastSelectedMonth = month;
                    lastSelectedDay = dayOfMonth;
                    String key = "entry_" + year + "_" + month + "_" + dayOfMonth;
                    String jsonEntry = getSharedPreferences("diary_entries", MODE_PRIVATE).getString(key, "");
                    DiaryEntry entry = new Gson().fromJson(jsonEntry, DiaryEntry.class);
                    if (entry != null) {
                        String title = entry.getTitle();
                        String content = entry.getContent();
                        ArrayList<Integer> videoResourceIds = entry.getVideoResourceIds();
                        ;
                        ArrayList<String> videonames = entry.getVideoNames();
                        ;
                        ArrayList<Integer> GroupNumbers = entry.getGroupNumbers();
                        ArrayList<Integer> Numbers = entry.getNumbers();
                        titleTextView.setText(title);
                        contentTextView.setText(content);
                        mAdapter.setVideoResourceIds(videoResourceIds, videonames, GroupNumbers, Numbers);
                        ;
                    } else {
                        titleTextView.setText("");
                        contentTextView.setText("");
                        mAdapter.setVideoResourceIds(null, null, null, null);
                    }
                }
            }
        });
        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTextView.setText("");
                contentTextView.setText("");
                mAdapter.setVideoResourceIds(null, null, null, null);
                String key = "entry_" + lastSelectedYear + "_" + lastSelectedMonth + "_" + lastSelectedDay;
                String dateArrayKey = "dateArray_" + lastSelectedYear + "_" + lastSelectedMonth + "_" + lastSelectedDay; // 添加日期数组的键值
                SharedPreferences sharedPreferences = getSharedPreferences("diary_entries", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(key);
                editor.remove(dateArrayKey);
                editor.apply();
                mAdapter.notifyDataSetChanged();
            }
        });

        homeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "主页" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_diary.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        libraryNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "动作库" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_diary.this, Activity_action.class);
                startActivity(intent);
                finish();
            }
        });

        profileNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "个人" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_diary.this, Activity_person.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
        private final Context mContext;
        private ArrayList<Integer> mVideoResourceIds;
        private ArrayList<String> mVideoNames;
        private ArrayList<Integer> mGroupNumbers;
        private ArrayList<Integer> mNumbers;

        public VideoAdapter(Context context) {
            mContext = context;
            mVideoResourceIds = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_video1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int groupNumber = mGroupNumbers != null && mGroupNumbers.size() > position ? mGroupNumbers.get(position) : 0;
            int number = mNumbers != null && mNumbers.size() > position ? mNumbers.get(position) : 0;
            holder.groupEditText.setText(String.valueOf(groupNumber));
            holder.numberEditText.setText(String.valueOf(number));
            String videoName = mVideoNames != null && mVideoNames.size() > position ? mVideoNames.get(position) : "";
            String chineseName = VideoNameConverter.convertToChinese(videoName);
            holder.nameTextView.setText(chineseName);
            int videoResourceId = mVideoResourceIds.get(position);
            String uri = "android.resource://" + mContext.getPackageName() + "/" + videoResourceId;
            holder.videoView.setVideoURI(Uri.parse(uri));
            holder.videoView.start();
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mVideoResourceIds != null ? mVideoResourceIds.size() : 0;
        }

        public void setVideoResourceIds(ArrayList<Integer> videoResourceIds, ArrayList<String> videonames, ArrayList<Integer> GroupNumbers, ArrayList<Integer> Numbers) {
            mVideoResourceIds = videoResourceIds;
            mVideoNames = videonames;
            mGroupNumbers = GroupNumbers;
            mNumbers = Numbers;
            notifyDataSetChanged();
        }

        public void removeVideo(int position) {
            if (mVideoResourceIds != null && position < mVideoResourceIds.size()) {
                mVideoResourceIds.remove(position);
                mVideoNames.remove(position);
                mGroupNumbers.remove(position);
                mNumbers.remove(position);
                notifyItemRemoved(position);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            EditText groupEditText;
            EditText numberEditText;
            TextView nameTextView;
            VideoView videoView;

            public ViewHolder(View itemView) {
                super(itemView);
                videoView = itemView.findViewById(R.id.video);
                nameTextView = itemView.findViewById(R.id.name);
                groupEditText = itemView.findViewById(R.id.group);
                numberEditText = itemView.findViewById(R.id.number);
            }
        }
    }
}

