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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.mlkit.vision.demo.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;


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
        String[] data = {"a", "b", "c", "d", "e", "f", "g"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter1);

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

        ImageView imageView = findViewById(R.id.fab);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击 "日记" 导航项，启动相应的 Activity
                Intent intent = new Intent(Activity_action.this, CameraXLivePreviewActivity.class);
                startActivity(intent);
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
            }
        });

        diaryNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_action.this, Activity_diary.class);
                startActivity(intent);
            }
        });

        profileNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_action.this, Activity_person.class);
                startActivity(intent);
            }
        });
    }

    private class VideoAdapter extends BaseAdapter {
        private Context mContext;
        private String mSelectedItem = "a";

        public VideoAdapter(Context context) {
            mContext = context;
        }

        // Method to set the selected item
        public void setSelectedItem(String selectedItem) {
            mSelectedItem = selectedItem;
        }

        @Override
        public int getCount() {
            return 7; // Number of items
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.grid_item_video, parent, false);
            }

            VideoView videoView = view.findViewById(R.id.video);

            // Set video URI based on the selected item

            String videoName = mSelectedItem + (position + 1); // Construct video name based on selected muscle
            int videoResourceId = getResources().getIdentifier(videoName, "raw", getPackageName());
            String uri = "android.resource://" + getPackageName() + "/" + videoResourceId;

            Log.d(TAG, "URI for item at position " + position + ": " + uri); // Print URI to log

            videoView.setVideoURI(Uri.parse(uri));

            // Start video playback
            videoView.start();
            // Enable looping
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });

            return view;
        }
    }
}
