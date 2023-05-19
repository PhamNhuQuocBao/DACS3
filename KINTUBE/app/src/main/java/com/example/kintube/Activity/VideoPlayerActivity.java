package com.example.kintube.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kintube.Adapter.CommentAdapter;
import com.example.kintube.DataLocal.DataLocalManager;
import com.example.kintube.Model.Comment;
import com.example.kintube.Model.Follow;
import com.example.kintube.Model.User.User;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private String videoUrl, videoName, videoUploadDate, username, videoImageUser, channel_id, videoSub, videoId;
    private RecyclerView recyclerViewVideoPlayer, recyclerViewComment;
    private TextView tvVideoName, tvUploadDate, tvUsername, tvVideoSub, btnComment;
    private ImageView imageUser;
    private List<Video> videoList;
    private List<Comment> listComment;
    private Button btnSub;
    DatabaseReference mDatabase;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        units();

        Intent intent = getIntent();
        channel_id = intent.getStringExtra("channel_id");
        videoUrl = intent.getStringExtra("video_url");
        videoName = intent.getStringExtra("video_name");
        videoId = intent.getStringExtra("video_id");
        videoUploadDate = intent.getStringExtra("video_upload_date");
        username = intent.getStringExtra("username");
        videoImageUser = intent.getStringExtra("video_image_user");
        videoSub = intent.getStringExtra("video_sub");

        checkFollow();

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        tvVideoName.setText(videoName);
        tvUploadDate.setText(videoUploadDate);
        tvUsername.setText(username);
        imageUser.setImageResource(R.drawable.profile_default);
        tvVideoSub.setText(videoSub + " người theo dõi");
        Uri uri = Uri.parse(videoUrl);

        MediaSource mediaSource = buildMediaSource(uri);
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);

        btnSub = findViewById(R.id.btn_sub);
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataLocalManager.getIdAccountLogin() != "") {
                    Follow follow = new Follow(channel_id);
                    handleFollow(follow);
                } else {
                    Toast.makeText(VideoPlayerActivity.this, "Vui lòng đăng nhập!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(VideoPlayerActivity.this);
                dialog.setContentView(R.layout.comments);
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());

                    int width = (int) (VideoPlayerActivity.this.getResources().getDisplayMetrics().widthPixels * 0.95); // Thiết lập chiều rộng là 80% của chiều rộng màn hình
                    layoutParams.width = width;

                    window.setAttributes(layoutParams);
                }
                recyclerViewComment = dialog.findViewById(R.id.recyclerViewComment);
                recyclerViewComment.setLayoutManager(new LinearLayoutManager(VideoPlayerActivity.this));
                recyclerViewComment.setItemAnimator(new DefaultItemAnimator());
                recyclerViewComment.setNestedScrollingEnabled(false);

                listComment = new ArrayList<>();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("COMMENTS").child(videoId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            listComment.add(comment);
                            System.out.println("=============================================" + listComment + "=============================================");
                        }
                        System.out.println("Listcomment=============================================" + listComment + "=============================================");
                        CommentAdapter commentAdapter = new CommentAdapter(listComment, context);
                        recyclerViewComment.setAdapter(commentAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Button btnPostComment = dialog.findViewById(R.id.btn_post_comment);
                btnPostComment.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        if (DataLocalManager.getIdAccountLogin() != "") {
                            EditText edtContent = dialog.findViewById(R.id.edtComment);
                            String strContent = edtContent.getText().toString().trim();

                            if (!strContent.isEmpty()) {
                                LocalDate currentTime = LocalDate.now();

                                String id = "comment" + System.currentTimeMillis();
                                Comment comment = new Comment();
                                comment.setId(id);
                                comment.setUser_id(DataLocalManager.getIdAccountLogin());
                                comment.setContent(strContent);
                                comment.setDate_comment(String.valueOf(currentTime));

                                mDatabase.child("COMMENTS").child(videoId).child(id).setValue(comment);
                                Toast.makeText(VideoPlayerActivity.this, "Đã đăng!", Toast.LENGTH_SHORT).show();

                                edtContent.setText("");
                            } else {
                                Toast.makeText(VideoPlayerActivity.this, "Hãy nhập nội dung bình luận!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(VideoPlayerActivity.this, "Vui lòng đăng nhập!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        onCreateView();


    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public void units() {
        playerView = findViewById(R.id.player_view);
        tvVideoName = findViewById(R.id.tvVideoName);
        tvUploadDate = findViewById(R.id.tvUploadDate);
        tvUsername = findViewById(R.id.tvUsername);
        imageUser = findViewById(R.id.imageUserVideoPlayer);
        tvVideoSub = findViewById(R.id.tvFollow);
        btnComment = findViewById(R.id.btn_comment);
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

    public void checkFollow() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (DataLocalManager.getIdAccountLogin() != "") {
            mDatabase.child("FOLLOWS").child(DataLocalManager.getIdAccountLogin()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(channel_id)) {
                        btnSub.setBackgroundColor(2);
                        btnSub.setText("Bỏ theo dõi");
                    } else {
                        btnSub.setBackgroundColor(3);
                        btnSub.setText("Theo dõi");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            btnSub = findViewById(R.id.btn_sub);
            btnSub.setText("Theo dõi");
        }
    }

    public void handleFollow(Follow follow) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("FOLLOWS").child(DataLocalManager.getIdAccountLogin()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(channel_id)) {
                    btnSub.setBackgroundColor(3);
                    btnSub.setText("Theo dõi");
                    mDatabase.child("FOLLOWS").child(DataLocalManager.getIdAccountLogin()).child(channel_id).removeValue();
                    mDatabase.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                if (Objects.equals(user.getId(), channel_id)) {
                                    user.setSub(user.getSub() - 1);
                                    mDatabase.child("USERS").child(channel_id).setValue(user);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    btnSub.setBackgroundColor(2);
                    btnSub.setText("Bỏ theo dõi");
                    mDatabase.child("FOLLOWS").child(DataLocalManager.getIdAccountLogin()).child(channel_id).setValue(follow);
                    mDatabase.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                if (Objects.equals(user.getId(), channel_id)) {
                                    user.setSub(user.getSub() + 1);
                                    mDatabase.child("USERS").child(channel_id).setValue(user);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
