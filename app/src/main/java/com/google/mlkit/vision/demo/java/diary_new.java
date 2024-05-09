package com.google.mlkit.vision.demo.java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;

public class diary_new extends Activity {
    private EditText editText;
    private EditText title; // Declare title EditText
    private Button button;
    private Button button1;
    private GridView mGridView;
    private VideoAdapter mAdapter;
    private static final int REQUEST_CODE_ADD_ACTIVITY = 1001; // Request code for AddActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_new);

        editText = findViewById(R.id.editText);
        title = findViewById(R.id.title); // Initialize title EditText

        // Find GridView
        mGridView = findViewById(R.id.gridView);

        // Create and set adapter
        mAdapter = new VideoAdapter(this);
        mGridView.setAdapter(mAdapter);

        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered title from the user
                String enteredTitle = title.getText().toString(); // Use title EditText

                EditText contentEditText = findViewById(R.id.editText);
                String enteredContent = contentEditText.getText().toString();

                // Get the video resource IDs from the adapter
                ArrayList<Integer> videoResourceIds = mAdapter.getVideoResourceIds();
                ArrayList<String> videoNames = mAdapter.getVideoNames();
                ArrayList<Integer> groupNumbers = mAdapter.getGroupNumbers(); // 获取组号列表
                ArrayList<Integer> numbers = mAdapter.getNumbers(); // 获取数字列表

                // Add groupNumber and number to their respective positions
                for (int i = 0; i < mGridView.getChildCount(); i++) {
                    View convertView = mGridView.getChildAt(i);
                    VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) convertView.getTag();
                    String groupNumberString = viewHolder.groupEditText.getText().toString();
                    String numberString = viewHolder.numberEditText.getText().toString();
                    int groupNumber = groupNumberString.isEmpty() ? 0 : Integer.parseInt(groupNumberString);
                    int number = numberString.isEmpty() ? 0 : Integer.parseInt(numberString);
                    groupNumbers.add(groupNumber);
                    numbers.add(number);
                }
                // Create the intent
                Intent intent = new Intent(diary_new.this, Activity_diary.class);

                // Add the title, content, video resource IDs, video names, year, month, and dayOfMonth to the intent
                intent.putExtra("title", enteredTitle);
                intent.putExtra("content", enteredContent);
                intent.putIntegerArrayListExtra("videoResourceIds", videoResourceIds); // Pass the video resource IDs
                intent.putStringArrayListExtra("videoNames", videoNames);
                intent.putIntegerArrayListExtra("groupNumbers", groupNumbers); // 传递组号列表
                intent.putIntegerArrayListExtra("numbers", numbers); // 传递数字列表
                intent.putExtra("year", getIntent().getIntExtra("selected_year", -1)); // Add year
                intent.putExtra("month", getIntent().getIntExtra("selected_month", -1)); // Add month
                intent.putExtra("dayOfMonth", getIntent().getIntExtra("selected_day", -1)); // Add dayOfMonth

                // Start the activity
                startActivity(intent);
                finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(diary_new.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ACTIVITY && resultCode == RESULT_OK) {
            int videoResourceId = data.getIntExtra("videoResourceId", 0);
            String videoName = data.getStringExtra("videoName");
            mAdapter.addVideoResourceId(videoResourceId, videoName); // Add the new video resource ID to the adapter
        }
    }


    private class VideoAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Integer> mVideoResourceIds;
        private ArrayList<String> mVideoNames;
        private ArrayList<Integer> mGroupNumbers; // 新添加的ArrayList用于存储组号
        private ArrayList<Integer> mNumbers; // 新添加的ArrayList用于存储数字

        public VideoAdapter(Context context) {
            mContext = context;
            mVideoResourceIds = new ArrayList<>();
            mVideoNames = new ArrayList<>();
            mGroupNumbers = new ArrayList<>(); // 初始化组号列表
            mNumbers = new ArrayList<>(); // 初始化数字列表
        }
        public void addVideoResourceId(int videoResourceId, String videoName) {
            mVideoResourceIds.add(videoResourceId);
            mVideoNames.add(videoName);
            notifyDataSetChanged(); // 通知适配器数据集已更改
        }
        public ArrayList<Integer> getVideoResourceIds() {
            return mVideoResourceIds;
        }
        public ArrayList<String> getVideoNames() {
            return mVideoNames;
        }
        public ArrayList<Integer> getGroupNumbers() {
            return mGroupNumbers;
        }
        public ArrayList<Integer> getNumbers() {
            return mNumbers;
        }
        @Override
        public int getCount() {
            return mVideoResourceIds.size(); // Return the size of the array
        }
        @Override
        public Object getItem(int position) {
            return mVideoResourceIds.get(position); // Return the video resource ID at the specified position
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_video1, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.videoView = convertView.findViewById(R.id.video);
                viewHolder.nameTextView = convertView.findViewById(R.id.name); // 找到 TextView
                viewHolder.groupEditText = convertView.findViewById(R.id.group); // 找到组 EditText
                viewHolder.numberEditText = convertView.findViewById(R.id.number); // 找到数字 EditText

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 获取groupEditText中的文本并转换为整数，如果为空则设置为0
            int groupNumber = parseEditTextToInt(viewHolder.groupEditText);
            // 获取numberEditText中的文本并转换为整数，如果为空则设置为0
            int number = parseEditTextToInt(viewHolder.numberEditText);

            // 设置groupEditText和numberEditText的文本
            viewHolder.groupEditText.setText(String.valueOf(groupNumber));
            viewHolder.numberEditText.setText(String.valueOf(number));

            int videoResourceId = mVideoResourceIds.get(position); // 从数组中获取视频资源 ID
            String videoName = mVideoNames.get(position); // 从数组中获取视频名称
            String chineseName = VideoNameConverter.convertToChinese(videoName);
            // 设置视频名称到 TextView 中
            viewHolder.nameTextView.setText(chineseName);
            final String uri = "android.resource://" + mContext.getPackageName() + "/" + videoResourceId;
            viewHolder.videoView.setVideoURI(Uri.parse(uri));
            viewHolder.videoView.start();
            viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            return convertView;
        }
        private int parseEditTextToInt(EditText editText) {
            String text = editText.getText().toString();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        }
        private class ViewHolder {
            EditText groupEditText;
            EditText numberEditText;
            VideoView videoView;
            TextView nameTextView; // 添加 TextView 引用
        }
    }
}
