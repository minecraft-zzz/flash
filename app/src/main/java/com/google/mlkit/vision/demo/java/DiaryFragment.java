package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.mlkit.vision.demo.R;
import java.util.ArrayList;
import java.util.Calendar;

public class DiaryFragment extends Fragment {

    private TextView titleTextView;
    private TextView contentTextView;
    private VideoAdapter mAdapter;
    private static int lastSelectedYear;
    private static int lastSelectedMonth;
    private static int lastSelectedDay;

    public static DiaryFragment newInstance(Bundle dataBundle) {
        DiaryFragment fragment = new DiaryFragment();
        fragment.setArguments(dataBundle); // 设置 Bundle 到 Fragment
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        titleTextView = view.findViewById(R.id.title);
        contentTextView = view.findViewById(R.id.textview);
        mAdapter = new VideoAdapter(requireContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.removeVideo(position);
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
//        int savedNightMode = getSavedNightModeState();
//        AppCompatDelegate.setDefaultNightMode(savedNightMode);

        Bundle args = getArguments();
        if (args != null) {
            String receivedTitle = args.getString("title");
            String receivedContent = args.getString("content");
            ArrayList<Integer> videoResourceIds = args.getIntegerArrayList("videoResourceIds");
            ArrayList<String> videoNames = args.getStringArrayList("videoNames");
            ArrayList<Integer> groupNumbers = args.getIntegerArrayList("groupNumbers");
            ArrayList<Integer> numbers = args.getIntegerArrayList("numbers");
            int year = args.getInt("year", -1);
            int month = args.getInt("month", -1);
            int dayOfMonth = args.getInt("dayOfMonth", -1);

            Log.d("ReceivedData", "Title: " + receivedTitle);
            Log.d("ReceivedData", "Content: " + receivedContent);
            Log.d("ReceivedData", "Video Resource IDs: " + videoResourceIds);
            Log.d("ReceivedData", "Video Names: " + videoNames);
            Log.d("ReceivedData", "Group Numbers: " + groupNumbers);
            Log.d("ReceivedData", "Numbers: " + numbers);
            Log.d("ReceivedData", "Year: " + year);
            Log.d("ReceivedData", "Month: " + month);
            Log.d("ReceivedData", "Day of Month: " + dayOfMonth);

            DiaryEntry entry = new DiaryEntry(receivedTitle, receivedContent, videoResourceIds, videoNames, groupNumbers, numbers, year, month, dayOfMonth);
            new SaveDiaryEntryTask().execute(entry);
        }

        Calendar todayCalendar = Calendar.getInstance();
        int currentYear = todayCalendar.get(Calendar.YEAR);
        int currentMonth = todayCalendar.get(Calendar.MONTH);
        int currentDayOfMonth = todayCalendar.get(Calendar.DAY_OF_MONTH);

        lastSelectedYear = currentYear;
        lastSelectedMonth = currentMonth;
        lastSelectedDay = currentDayOfMonth;

        String key = "entry_" + currentYear + "_" + currentMonth + "_" + currentDayOfMonth;
        new DiaryFragment.LoadDiaryEntryTask().execute(key);

        calendarView.setOnDateChangeListener((view2, year, month, dayOfMonth) -> {
            if (year == lastSelectedYear && month == lastSelectedMonth && dayOfMonth == lastSelectedDay) {
                Intent newIntent = new Intent(requireActivity(), diary_new.class);
                String key1 = "entry_" + year + "_" + month + "_" + dayOfMonth;
                DiaryEntry entry = getDiaryEntryByKey(key1);
                newIntent.putExtra("selected_year", year);
                newIntent.putExtra("selected_month", month);
                newIntent.putExtra("selected_day", dayOfMonth);
                if (entry != null) {
                    newIntent.putExtra("diary_entry_title", entry.getTitle());
                    newIntent.putExtra("diary_entry_content", entry.getContent());
                    newIntent.putIntegerArrayListExtra("diary_entry_video_resource_ids", entry.getVideoResourceIds());
                    newIntent.putStringArrayListExtra("diary_entry_video_names", entry.getVideoNames());
                    newIntent.putIntegerArrayListExtra("diary_entry_group_numbers", entry.getGroupNumbers());
                    newIntent.putIntegerArrayListExtra("diary_entry_numbers", entry.getNumbers());
                } else {
                    newIntent.putExtra("diary_entry_title", "");
                    newIntent.putExtra("diary_entry_content", "");
                    newIntent.putIntegerArrayListExtra("diary_entry_video_resource_ids", new ArrayList<>());
                    newIntent.putStringArrayListExtra("diary_entry_video_names", new ArrayList<>());
                    newIntent.putIntegerArrayListExtra("diary_entry_group_numbers", new ArrayList<>());
                    newIntent.putIntegerArrayListExtra("diary_entry_numbers", new ArrayList<>());
                }
                startActivity(newIntent);
            } else {
                lastSelectedYear = year;
                lastSelectedMonth = month;
                lastSelectedDay = dayOfMonth;
                String newKey = "entry_" + year + "_" + month + "_" + dayOfMonth;
                new LoadDiaryEntryTask().execute(newKey); // You don't need to qualify the class name here
            }
        });
        return view;
    }

    public static DiaryFragment newInstance(String title, String content, ArrayList<Integer> videoResourceIds, ArrayList<String> videoNames, ArrayList<Integer> groupNumbers, ArrayList<Integer> numbers) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putIntegerArrayList("videoResourceIds", videoResourceIds);
        args.putStringArrayList("videoNames", videoNames);
        args.putIntegerArrayList("groupNumbers", groupNumbers);
        args.putIntegerArrayList("numbers", numbers);
        fragment.setArguments(args);
        return fragment;
    }

    private class SaveDiaryEntryTask extends AsyncTask<DiaryEntry, Void, Void> {
        @Override
        protected Void doInBackground(DiaryEntry... entries) {
            if (entries.length > 0) {
                DiaryEntry entry = entries[0];
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("diary_entries", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String jsonEntry = gson.toJson(entry);
                editor.putString("entry_" + entry.getYear() + "_" + entry.getMonth() + "_" + entry.getDayOfMonth(), jsonEntry);
                editor.putString("dateArray_" + entry.getYear() + "_" + entry.getMonth() + "_" + entry.getDayOfMonth(), gson.toJson(entry.getDateArray()));
                editor.apply();
            }
            return null;
        }
    }

    private class LoadDiaryEntryTask extends AsyncTask<String, Void, DiaryEntry> {
        protected DiaryEntry doInBackground(String... keys) {
            if (keys.length > 0) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("diary_entries", Context.MODE_PRIVATE);
                String jsonEntry = sharedPreferences.getString(keys[0], "");
                Log.d("DiaryFragment", "Loaded Diary Entry JSON: " + jsonEntry);
                return new Gson().fromJson(jsonEntry, DiaryEntry.class);
            }
            return null;
        }


        protected void onPostExecute(DiaryEntry entry) {
            if (entry != null) {
                Log.d("DiaryFragment", "Setting Title: " + entry.getTitle());
                Log.d("DiaryFragment", "Setting Content: " + entry.getContent());
                Log.d("DiaryFragment", "Setting Video Resource IDs: " + entry.getVideoResourceIds());
                Log.d("DiaryFragment", "Setting Video Names: " + entry.getVideoNames());
                Log.d("DiaryFragment", "Setting Group Numbers: " + entry.getGroupNumbers());
                Log.d("DiaryFragment", "Setting Numbers: " + entry.getNumbers());

                titleTextView.setText(entry.getTitle());
                contentTextView.setText(entry.getContent());
                mAdapter.setVideoResourceIds(entry.getVideoResourceIds(), entry.getVideoNames(), entry.getGroupNumbers(), entry.getNumbers());
            } else {
                Log.d("DiaryFragment", "No Diary Entry found.");
                titleTextView.setText("");
                contentTextView.setText("");
                mAdapter.setVideoResourceIds(null, null, null, null);
            }
        }

    }

    private DiaryEntry getDiaryEntryByKey(String key) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("diary_entries", Context.MODE_PRIVATE);
        String jsonEntry = sharedPreferences.getString(key, "");
        return new Gson().fromJson(jsonEntry, DiaryEntry.class);
    }

    public static class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
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

                removeDataFromSharedPreferences(position);
            }
        }

        private void removeDataFromSharedPreferences(int position) {
            // 获取SharedPreferences
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("diary_entries", Context.MODE_PRIVATE);

            // 构建键值
            String key = "entry_" + lastSelectedYear + "_" + lastSelectedMonth + "_" + lastSelectedDay;

            // 获取保存的 JSON 字符串
            String jsonEntry = sharedPreferences.getString(key, "");

            // 将 JSON 字符串转换成 DiaryEntry 对象
            Gson gson = new Gson();
            DiaryEntry diaryEntry = gson.fromJson(jsonEntry, DiaryEntry.class);

            // 移除相应位置的数据
            if (diaryEntry != null) {
                diaryEntry.getVideoResourceIds().remove(position);
                diaryEntry.getVideoNames().remove(position);
                diaryEntry.getGroupNumbers().remove(position);
                diaryEntry.getNumbers().remove(position);

                // 将更新后的 DiaryEntry 对象重新保存到 SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String updatedJsonEntry = gson.toJson(diaryEntry);
                editor.putString(key, updatedJsonEntry);
                editor.apply();
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
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