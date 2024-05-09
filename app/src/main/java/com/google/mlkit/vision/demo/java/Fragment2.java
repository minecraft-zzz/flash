package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.VideoView;


import androidx.fragment.app.Fragment;

import com.google.mlkit.vision.demo.R;

/**
 * A simple {@link Fragment} subclass.
<<<<<<< HEAD
 * Use the {@link Fragment3#newInstance} factory method to
=======
 * Use the {@link Fragment2#newInstance} factory method to
>>>>>>> 13cb3a472218f81a7eec35ac43c7a4f0a8e1e7b6
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private VideoView mVideoView;
    private MediaController mMediaController;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.

     * @return A new instance of fragment Fragment2.
     **/
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2, container, false);

        // Find views
        GridView gridView = view.findViewById(R.id.gridview);

        // Create adapter
        VideoAdapter adapter = new VideoAdapter(getActivity());
        gridView.setAdapter(adapter);

        return view;
    }

    // Adapter for GridView
    private class VideoAdapter extends BaseAdapter {
        private Context mContext;

        public VideoAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 5; // Number of items
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

            // Set video URI for each item
            String uri = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.a1;
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