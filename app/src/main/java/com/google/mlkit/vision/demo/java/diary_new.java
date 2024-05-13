package com.google.mlkit.vision.demo.java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.List;
import java.util.Objects;

public class diary_new extends Activity {
    private EditText title;
    private VideoAdapter mAdapter;
    private static final int REQUEST_CODE_ADD_ACTIVITY = 1001;
    private final List<VideoItem> mVideoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_new);

        title = findViewById(R.id.title);
        EditText contentEditText = findViewById(R.id.editText);
        GridView mGridView = findViewById(R.id.gridView);

        Intent intent_old = getIntent();

        String diary_entry_title = intent_old.getStringExtra("diary_entry_title");
        String diary_entry_content = intent_old.getStringExtra("diary_entry_content");
        ArrayList<Integer> initialVideoResourceIds = intent_old.getIntegerArrayListExtra("diary_entry_video_resource_ids");
        ArrayList<String> initialVideoNames = intent_old.getStringArrayListExtra("diary_entry_video_names");
        ArrayList<Integer> initialGroupNumbers = intent_old.getIntegerArrayListExtra("diary_entry_group_numbers");
        ArrayList<Integer> initialNumbers = intent_old.getIntegerArrayListExtra("diary_entry_numbers");

        int selected_year = intent_old.getIntExtra("selected_year", 0); // 默认值为0，你可以根据需要修改
        int selected_month = intent_old.getIntExtra("selected_month", 0);
        int selected_day = intent_old.getIntExtra("selected_day", 0);

        for (int i = 0; i < Objects.requireNonNull(initialVideoResourceIds).size(); i++) {
            assert initialVideoNames != null;
            assert initialGroupNumbers != null;
            assert initialNumbers != null;
            mVideoItems.add(new VideoItem(initialVideoResourceIds.get(i), initialVideoNames.get(i), initialGroupNumbers.get(i), initialNumbers.get(i)));
        }

        mAdapter = new VideoAdapter(this, mVideoItems);

        title.setText(diary_entry_title);
        contentEditText.setText(diary_entry_content);

        mGridView.setAdapter(mAdapter);

        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button1);

        button.setOnClickListener(v -> {
            // 保存
            String enteredTitle = title.getText().toString();
            String enteredContent = contentEditText.getText().toString();

            Intent resultIntent = new Intent(this, Activity_diary.class);
            resultIntent.putExtra("title", enteredTitle);
            resultIntent.putExtra("content", enteredContent);

            ArrayList<Integer> allVideoResourceIds = new ArrayList<>();
            ArrayList<String> allVideoNames = new ArrayList<>();
            ArrayList<Integer> allGroupNumbers = new ArrayList<>();
            ArrayList<Integer> allNumbers = new ArrayList<>();

            for (VideoItem item : mVideoItems) {
                allVideoResourceIds.add(item.videoResourceId);
                allVideoNames.add(item.videoName);
                allGroupNumbers.add(item.groupNumber);
                allNumbers.add(item.number);
            }

            resultIntent.putIntegerArrayListExtra("videoResourceIds", allVideoResourceIds);
            resultIntent.putStringArrayListExtra("videoNames", allVideoNames);
            resultIntent.putIntegerArrayListExtra("groupNumbers", allGroupNumbers);
            resultIntent.putIntegerArrayListExtra("numbers", allNumbers);

            resultIntent.putExtra("selected_year", selected_year);
            resultIntent.putExtra("selected_month", selected_month);
            resultIntent.putExtra("selected_day", selected_day);
            startActivity(resultIntent);
            finish();
        });

        button1.setOnClickListener(v -> {
            Intent intent = new Intent(diary_new.this, AddActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ACTIVITY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ACTIVITY && resultCode == RESULT_OK) {
            int videoResourceId = data.getIntExtra("videoResourceId", 0);
            String videoName = data.getStringExtra("videoName");
            int groupNumber = 0; // 假设用户未填写组号，默认为0
            int number = 0; // 假设用户未填写数目，默认为0
            mVideoItems.add(new VideoItem(videoResourceId, videoName, groupNumber, number));
            mAdapter.notifyDataSetChanged();
        }
    }

    private static class VideoItem {
        int videoResourceId;
        String videoName;
        int groupNumber;
        int number;

        public VideoItem(int videoResourceId, String videoName, int groupNumber, int number) {
            this.videoResourceId = videoResourceId;
            this.videoName = videoName;
            this.groupNumber = groupNumber;
            this.number = number;
        }
    }

    private static class VideoAdapter extends BaseAdapter {
        private final Context mContext;
        private final List<VideoItem> mVideoItems;

        public VideoAdapter(Context context, List<VideoItem> videoItems) {
            mContext = context;
            mVideoItems = videoItems;
        }

        @Override
        public int getCount() {
            return mVideoItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mVideoItems.get(position);
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
                viewHolder.nameTextView = convertView.findViewById(R.id.name);
                viewHolder.groupEditText = convertView.findViewById(R.id.group);
                viewHolder.numberEditText = convertView.findViewById(R.id.number);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            VideoItem item = mVideoItems.get(position);

            viewHolder.groupEditText.setText(String.valueOf(item.groupNumber));
            viewHolder.numberEditText.setText(String.valueOf(item.number));

            String chineseName = VideoNameConverter.convertToChinese(item.videoName);
            viewHolder.nameTextView.setText(chineseName);
            final String uri = "android.resource://" + mContext.getPackageName() + "/" + item.videoResourceId;
            viewHolder.videoView.setVideoURI(Uri.parse(uri));
            viewHolder.videoView.start();
            viewHolder.videoView.setOnPreparedListener(mp -> mp.setLooping(true));

            return convertView;
        }

        private static class ViewHolder {
            EditText groupEditText;
            EditText numberEditText;
            VideoView videoView;
            TextView nameTextView;
        }
    }
}
