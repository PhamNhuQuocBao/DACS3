package com.example.kintube.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.R;
import com.example.kintube.Adapter.VideoAdapter;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private String videoUrl, videoName, videoUploadDate, username, videoImageUser;
    private RecyclerView recyclerViewVideoPlayer;
    private TextView tvVideoName, tvUploadDate, tvUsername;
    private ImageView imageUser;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        units();

        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("video_url");
        videoName = intent.getStringExtra("video_name");
        videoUploadDate = intent.getStringExtra("video_upload_date");
        username = intent.getStringExtra("username");
        videoImageUser = intent.getStringExtra("video_image_user");

        System.out.println("username2:"+username);

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        tvVideoName.setText(videoName);
        tvUploadDate.setText(videoUploadDate);
        tvUsername.setText(username);
        imageUser.setImageResource(R.drawable.profile_default);
        Uri uri = Uri.parse(videoUrl);

        MediaSource mediaSource = buildMediaSource(uri);
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);

        onCreateView();
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    public void units() {
        playerView = findViewById(R.id.player_view);
        tvVideoName = findViewById(R.id.tvVideoName);
        tvUploadDate = findViewById(R.id.tvUploadDate);
        tvUsername = findViewById(R.id.tvUsername);
        imageUser = findViewById(R.id.imageUserVideoPlayer);
    }

    public void onCreateView() {
        recyclerViewVideoPlayer = findViewById(R.id.recyclerViewVideoPlayer);
        recyclerViewVideoPlayer.setLayoutManager(new LinearLayoutManager(VideoPlayerActivity.this));
        recyclerViewVideoPlayer.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideoPlayer.setNestedScrollingEnabled(false);

        readData();
    }

    public void readData() {
        videoList = new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("VIDEOS").orderByChild("upload_date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Video video = itemSnapshot.getValue(Video.class);
                    if (!Objects.equals(videoUrl, video.getFile_path())) {
                        videoList.add(video);
                    }
                }
                VideoAdapter adapter = new VideoAdapter(videoList, VideoPlayerActivity.this);
                recyclerViewVideoPlayer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
