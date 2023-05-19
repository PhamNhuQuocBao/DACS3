package com.example.kintube.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.kintube.Database.UserDatabase;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.Activity.VideoPlayerActivity;
import com.example.kintube.Model.User.User;
import com.example.kintube.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<Video> videoList;
    private Context context;
    private String strSearch, username, imageUser;
    private int videoSub;

    public VideoAdapter(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Video video = videoList.get(position);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(video.getUser_id())) {
                    User user = snapshot.child(video.getUser_id()).getValue(User.class);
                    videoSub = user.getSub();
                    imageUser = user.getImageUser();
                    holder.usernameVideo.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.imageVideo.setImageResource(R.drawable.baseline_video_library_24);
        holder.titleVideo.setText(video.getTitle());
        holder.dateCreateAtVideo.setText(video.getUpload_date());
        holder.imageUserVieo.setImageResource(R.drawable.profile_default);
        Glide.with(context).load(video.getFile_path()).into(holder.imageVideo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("video_url", video.getFile_path());
                intent.putExtra("video_id", video.getId());
                intent.putExtra("video_name", video.getTitle());
                intent.putExtra("video_upload_date", video.getUpload_date());
                intent.putExtra("username", holder.usernameVideo.getText());
                intent.putExtra("video_image_user", imageUser);
                intent.putExtra("channel_id", video.getUser_id());
                intent.putExtra("video_sub", String.valueOf(videoSub));

                context.startActivity(intent);
            }
        });
//

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setFilteredList(List<Video> filteredList) {
        this.videoList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageVideo;
        public TextView titleVideo;
        public TextView usernameVideo;
        public TextView dateCreateAtVideo;
        public ImageView imageUserVieo;


        public ViewHolder(View itemView) {
            super(itemView);
            imageVideo = itemView.findViewById(R.id.imageVideo);
            titleVideo = itemView.findViewById(R.id.titleVideo);
            usernameVideo = itemView.findViewById(R.id.textUsername);
            dateCreateAtVideo = itemView.findViewById(R.id.date_create_at);
            imageUserVieo = itemView.findViewById(R.id.imageUserVideo);
        }
    }
}
