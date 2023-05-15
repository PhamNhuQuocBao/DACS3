package com.example.kintube.Model.Video;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.Model.Video.User.User;
import com.example.kintube.R;
import com.example.kintube.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements Filterable {
    private List<Video> videoList;
    private List<Video> videoListSearch;
    private Context context;
    private String strSearch;

    public VideoAdapter(List<Video> videoList , Context context) {
        this.videoList = videoList;
        this.videoListSearch = videoList;
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
        holder.imageVideo.setImageResource(R.drawable.baseline_video_library_24);
        holder.titleVideo.setText(video.getTitle());
        holder.dateCreateAtVideo.setText(video.getUpload_date());
        holder.imageUserVieo.setImageResource(R.drawable.baseline_video_library_24);
        Glide.with(context).load(video.getFile_path()).into(holder.imageVideo);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    videoList = videoListSearch;
                } else {
//                    videoList = VideoDatabase.getInstance(context).videoDAO().getListVideoSearch(strSearch);
                    List<Video> list = new ArrayList<>();
                    for (Video video : videoListSearch) {
                        if (video.getTitle().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(video);
                        }
                    }

                    videoList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = videoList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                videoList = (List<Video>) results.values;
                notifyDataSetChanged();
            }
        };
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
