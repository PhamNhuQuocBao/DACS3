package com.example.kintube;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<Video> videoList;
    private Context context;

    public VideoAdapter(List<Video> videoList , Context context) {
        this.videoList = videoList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.imageVideo.setImageResource(video.getImageVideo());
        holder.titleVideo.setText(video.getTitle());
        holder.dateCreateAtVideo.setText(video.getUpload_date());
        holder.imageUserVieo.setImageResource(video.getImageVideoUser());
//        Glide.with(context).load(video.getUrlVideo()).into(holder.imageVideo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("video_url", video.getFile_path());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageVideo;
        public TextView titleVideo;
        public TextView dateCreateAtVideo;
        public ImageView imageUserVieo;


        public ViewHolder(View itemView) {
            super(itemView);
            imageVideo = itemView.findViewById(R.id.imageVideo);
            titleVideo = itemView.findViewById(R.id.titleVideo);
            dateCreateAtVideo = itemView.findViewById(R.id.date_create_at);
            imageUserVieo = itemView.findViewById(R.id.imageUserVideo);
        }
    }
}
