package com.example.kintube.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.R;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.Model.Video.VideoAdapter;
import java.util.ArrayList;
import java.util.List;

public class TrangchuFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    private String root_path = "android.resource://res/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

        // Load video list
        videoList = new ArrayList<>();
        videoList = VideoDatabase.getInstance(this.getContext()).videoDAO().getListVideo();
        // Set up Adapter
        adapter = new VideoAdapter(videoList, getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }


}