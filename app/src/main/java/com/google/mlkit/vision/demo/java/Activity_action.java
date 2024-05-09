package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.mlkit.vision.demo.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Activity_action extends BaseActivity {
    private static final String TAG = "Activity_action";

    private GridView mGridView;
    private ListView mListView;
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        // Find ListView
        mListView = findViewById(R.id.muscle_list);

        // Set adapter
        String[] data1 = {"胸", "肩", "背", "臀腿", "手臂", "核心", "综合"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data1);
        mListView.setAdapter(adapter1);

        String[] data = {"a", "b", "c", "d", "e", "f", "g"};
        // Find GridView
        mGridView = findViewById(R.id.exercise_list);

        // Create and set adapter
        mAdapter = new VideoAdapter(this);
        mGridView.setAdapter(mAdapter);

        // Set item click listener for muscle_list
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Update exercise_list based on the selected item
                String selectedItem = data[position];
                mAdapter.setSelectedItem(selectedItem);
                mAdapter.notifyDataSetChanged();
            }
        });

        LinearLayout homeNav = findViewById(R.id.home);
        LinearLayout diaryNav = findViewById(R.id.diary);
        LinearLayout profileNav = findViewById(R.id.person);

        int savedNightMode = getSavedNightModeState();
        AppCompatDelegate.setDefaultNightMode(savedNightMode);

        homeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_action.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        diaryNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_action.this, Activity_diary.class);
                startActivity(intent);
                finish();
            }
        });

        profileNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_action.this, Activity_person.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private static class VideoLoadTask implements Runnable {
        private final WeakReference<Activity_action> activityRef;
        private final int position;
        private final String videoName;
        private final String chineseName;
        private final VideoView videoView;

        public VideoLoadTask(Activity_action activity, int position, String videoName, String chineseName, VideoView videoView) {
            this.activityRef = new WeakReference<>(activity);
            this.position = position;
            this.videoName = videoName;
            this.chineseName = chineseName;
            this.videoView = videoView;
        }

        @Override
        public void run() {
            final Activity_action activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            int videoResourceId = activity.getResources().getIdentifier(videoName, "raw", activity.getPackageName());
            final String uri = "android.resource://" + activity.getPackageName() + "/" + videoResourceId;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videoView.setVideoURI(Uri.parse(uri));
                    videoView.start();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                        }
                    });
                }
            });
        }
    }

    private class VideoAdapter extends BaseAdapter {
        private Context mContext;
        private String mSelectedItem = "a";
        private int mVideoCount = 1; // 默认每个字母对应的视频数量
        private final Executor executor = Executors.newSingleThreadExecutor();

        public VideoAdapter(Context context) {
            mContext = context;
        }

        // 设置选定的字母
        public void setSelectedItem(String selectedItem) {
            mSelectedItem = selectedItem;
            // 根据选定的字母更新视频数量
            updateVideoCount();
        }

        // 根据选定的字母更新视频数量
        private void updateVideoCount() {
            // 根据字母确定视频数量的逻辑
            // 这里假设您有一种方法可以确定每个字母对应的视频数量
//            switch (mSelectedItem) {
//                case "a":
//                    mVideoCount = 18;
//
//                    break;
//                case "b":
//                case "e":
//                    mVideoCount = 16;
//                    break;
//                case "c":
//                    mVideoCount = 14;
//                    break;
//                case "d":
//                    mVideoCount = 29;
//                    break;
//                case "f":
//                    mVideoCount = 12;
//                    break;
//                case "g":
//                    mVideoCount = 7;
//                    break;
//                default:
//                    mVideoCount = 0; // 默认情况下没有视频
//                    break;
//            }
            mVideoCount = 1;
        }

        @Override
        public int getCount() {
            return mVideoCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_video, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.videoView = convertView.findViewById(R.id.video);
                viewHolder.nameTextView = convertView.findViewById(R.id.name); // 找到 TextView
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String videoName = mSelectedItem + (position + 1);
            String chineseName = VideoNameConverter.convertToChinese(videoName);

            viewHolder.nameTextView.setText(chineseName); // 将 TextView 的文本设置为 videoName

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoDetailsActivity.class);
                    intent.putExtra("videoName", videoName);
                    mContext.startActivity(intent);
                }
            });

            executor.execute(new VideoLoadTask(Activity_action.this, position, videoName, chineseName, viewHolder.videoView));

            return convertView;
        }

        private class ViewHolder {
            VideoView videoView;
            TextView nameTextView; // 添加 TextView 引用
        }
    }
}