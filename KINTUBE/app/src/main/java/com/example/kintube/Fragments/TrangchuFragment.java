package com.example.kintube.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.kintube.R;
import com.example.kintube.Video;
import com.example.kintube.VideoAdapter;
import java.util.ArrayList;
import java.util.List;

public class TrangchuFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    private String root_path = "android.resource://res/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrangchuFragment() {
        // Required empty public constructor
    }

    public static TrangchuFragment newInstance(String param1, String param2) {
        TrangchuFragment fragment = new TrangchuFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trangchu, container, false);
        // Set up RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        String url = "https://drive.google.com/file/d/1g1VEYOdD7I7CGnagiO4pgTqdw_HivtKw/view?usp=sharing";
        String[] parts = url.split("/");
        String videoId = parts[5];

        // Load video list
        videoList = new ArrayList<>();
        Video vd = new Video();
        vd.setImageVideo(R.drawable.twotone_slow_motion_video_24);
        vd.setTitle("Kamen Rider");
        vd.setDescription("");
        vd.setDuration("15:30");
        vd.setFile_path("");
        vd.setUpload_date("20-12-2002");
        vd.setImageVideoUser(R.drawable.twotone_slow_motion_video_24);
        vd.setFile_path(root_path + R.raw.video1);
        videoList.add(vd);
        // Set up Adapter
        adapter = new VideoAdapter(videoList, getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }


}