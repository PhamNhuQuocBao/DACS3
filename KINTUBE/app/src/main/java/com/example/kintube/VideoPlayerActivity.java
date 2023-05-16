package com.example.kintube;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.Model.Video.VideoAdapter;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private String videoUrl, videoName, videoUploadDate, username;
    private RecyclerView recyclerViewVideoPlayer;
    private TextView tvVideoName, tvUploadDate, tvUsername;
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

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        tvVideoName.setText(videoName);
        tvUploadDate.setText(videoUploadDate);
        tvUsername.setText(username);
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
    }

    public void onCreateView() {
        recyclerViewVideoPlayer = findViewById(R.id.recyclerViewVideoPlayer);
        recyclerViewVideoPlayer.setLayoutManager(new LinearLayoutManager(VideoPlayerActivity.this));
        recyclerViewVideoPlayer.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideoPlayer.setNestedScrollingEnabled(false);

        videoList = new ArrayList<>();
        videoList = VideoDatabase.getInstance(this).videoDAO().getListVideo();

        VideoAdapter adapter = new VideoAdapter(videoList, VideoPlayerActivity.this);
        recyclerViewVideoPlayer.setAdapter(adapter);
    }
}
