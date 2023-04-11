package com.example.kintube.Fragments;

import android.net.Uri;
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
import com.google.android.gms.drive.*;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrangchuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrangchuFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrangchuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlgorithmFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        videoList.add(new Video(R.drawable.twotone_slow_motion_video_24,"Video1","15:30",R.drawable.twotone_slow_motion_video_24,"android.resource://res/" + R.raw.video1));
        videoList.add(new Video(R.drawable.twotone_slow_motion_video_24,"Video2","15:30",R.drawable.twotone_slow_motion_video_24,"android.resource://res/" + R.raw.video2));
        videoList.add(new Video(R.drawable.twotone_slow_motion_video_24,"Video3","15:30",R.drawable.twotone_slow_motion_video_24,"https://drive.google.com/uc?id=1R6hFYl3FgL966Jc2-rd_vdJKIZlGcM4h"));
        videoList.add(new Video(R.drawable.twotone_slow_motion_video_24,"Video4","15:30",R.drawable.twotone_slow_motion_video_24,"https://drive.google.com/uc?id="+videoId));
        // Set up Adapter
        adapter = new VideoAdapter(videoList, getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }


}