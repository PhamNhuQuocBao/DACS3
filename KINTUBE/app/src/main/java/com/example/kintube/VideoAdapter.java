package com.example.kintube;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<Video> videoList;
    private static List<Video> filteredVideoList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Video video);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public VideoAdapter(List<Video> videoList) {

        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.imageButtonVideo.setImageResource(video.getImageVideo());
        holder.titleVideo.setText(video.getVideoTitle());
        holder.imageUserVieo.setImageResource(video.getImageVideoUser());


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageButtonVideo;
        public TextView titleVideo;
        public ImageView imageUserVieo;


        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageButtonVideo = itemView.findViewById(R.id.imageVideo);
            titleVideo = itemView.findViewById(R.id.titleVideo);
            imageUserVieo = itemView.findViewById(R.id.imageUserVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(filteredVideoList.get(position));
                        }
                    }
                }
            });

        }
    }
}
