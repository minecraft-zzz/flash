package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.mlkit.vision.demo.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;


public class AddActivity extends BaseActivity {
    private static final String TAG = "Activity_add";

    private GridView mGridView;
    private ListView mListView;
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Find ListView
        mListView = findViewById(R.id.muscle_list);

        // Set adapter
        String[] data = {"a", "b", "c", "d", "e", "f", "g"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter1);

        // Find GridView
        mGridView = findViewById(R.id.exercise_list);

        // Create and set adapter
        mAdapter = new VideoAdapter(this);
        mAdapter.updateVideoCount();
        mGridView.setAdapter(mAdapter);
    }

    private class VideoAdapter extends BaseAdapter {
        private Context mContext;
        private String mSelectedItem = "a";
        private int mVideoCount = 1;
        public VideoAdapter(Context context) {
            mContext = context;
        }
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
            return mVideoCount; // Number of items
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
            int videoResourceId = mContext.getResources().getIdentifier(videoName, "raw", mContext.getPackageName());
            final String uri = "android.resource://" + mContext.getPackageName() + "/" + videoResourceId;

            viewHolder.videoView.setVideoURI(Uri.parse(uri));
            viewHolder.videoView.start();

            viewHolder.nameTextView.setText(chineseName); // 将 TextView 的文本设置为 videoName

            viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("videoResourceId", videoResourceId);
                    intent.putExtra("videoName", videoName);
                    setResult(RESULT_OK, intent);
                    finish(); // Close the current activity
                }
            });
            return convertView;
        }
        private class ViewHolder {
            TextView nameTextView;
            VideoView videoView;
        }
    }
}
