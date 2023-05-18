package com.example.kintube.Fragments.ProfileFrament;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.kintube.Database.UserDatabase;
//import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.Activity.MainActivity;
import com.example.kintube.Activity.ProfileActivity;
import com.example.kintube.Activity.UpdateActivity;
import com.example.kintube.Model.User.User;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.R;
import com.example.kintube.Activity.VideoPlayerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class YourVideosAdapter extends RecyclerView.Adapter<YourVideosAdapter.ViewHolder>{
    private List<Video> yourVideos;
    private Context context;
    private String username, imageUser;

    public YourVideosAdapter(List<Video> yourVideos, Context context) {
        this.yourVideos = yourVideos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yours_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YourVideosAdapter.ViewHolder holder, int position) {
        Video video = yourVideos.get(position);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(video.getUser_id())) {
                    username = snapshot.child(video.getUser_id()).child("name").getValue(String.class);
                    imageUser = snapshot.child(video.getUser_id()).child("imageUser").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.imageVideo.setImageResource(R.drawable.baseline_video_library_24);
        holder.titleVideo.setText(video.getTitle());
        holder.dateCreateAtVideo.setText(video.getUpload_date());
//        holder.moreSettings.setImageResource(R.drawable.baseline_more_vert_24);
        Glide.with(context).load(video.getFile_path()).into(holder.imageVideo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("video_url", video.getFile_path());
                intent.putExtra("video_name", video.getTitle());
                intent.putExtra("video_upload_date", video.getUpload_date());
                intent.putExtra("username", username);
                intent.putExtra("video_image_user", imageUser);

                context.startActivity(intent);
            }
        });

        holder.moreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = new PopupWindow(v.getContext());

                // Lấy LayoutInflater từ Context
                LayoutInflater inflater = LayoutInflater.from(v.getContext());

                // Đặt layout cho PopupWindow từ file XML
                View popupView = inflater.inflate(R.layout.more_setting, null);
                popupWindow.setContentView(popupView);

                // Đặt kích thước cho PopupWindow
                popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

                // Hiển thị popup phía dưới màn hình
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

                // Đặt xử lý sự kiện cho TextView bên trong popup
                TextView delete = popupView.findViewById(R.id.option_delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        System.out.println("=======================" + video.getId() + "================");
                        mDatabase.child("VIDEOS").child(video.getId()).removeValue();
                        popupWindow.dismiss();
                        Toast.makeText(v.getContext(), "Đã xóa video!", Toast.LENGTH_SHORT).show();
                    }
                });
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView update = popupView.findViewById(R.id.option_update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, UpdateActivity.class);
//                        context.startActivity(intent);
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.activity_update_video);
                        popupWindow.dismiss();
                        dialog.show();

                        EditText edtNewName = dialog.findViewById(R.id.edt_newName);
                        EditText edtNewDesc = dialog.findViewById(R.id.edt_newDescription);
                        VideoView videoView = dialog.findViewById(R.id.videoUpdateFromGallery);

                        edtNewName.setText(video.getTitle());
                        edtNewDesc.setText(video.getDescription());
                        MediaController mediaController = new MediaController(context);
                        videoView.setMediaController(mediaController);
                        videoView.setVideoURI(Uri.parse(video.getFile_path()));
                        videoView.start();

                        Button btnUpdate = dialog.findViewById(R.id.btn_updateVideo);
                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String strNewName = edtNewName.getText().toString().trim();
                                String strNewDesc = edtNewDesc.getText().toString().trim();
                                if (strNewName == "") {
                                    Toast.makeText(context, "Vui lòng nhập tên video!", Toast.LENGTH_SHORT).show();
                                } else {
                                    video.setTitle(strNewName);
                                    video.setDescription(strNewDesc);

                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child("VIDEOS").child(video.getId()).setValue(video);
                                    System.out.println(video.getId() + "=====================================================" + video.getTitle());

                                    Toast.makeText(context, "Chỉnh sửa video thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });

                // Đặt xử lý sự kiện cho nút "Đóng"
                ImageView closeButton = popupView.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return yourVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageVideo, moreSettings;
        public TextView titleVideo, dateCreateAtVideo;


        public ViewHolder(View itemView) {
            super(itemView);
            imageVideo = itemView.findViewById(R.id.imageYourVideos);
            titleVideo = itemView.findViewById(R.id.titleYourVideos);
            dateCreateAtVideo = itemView.findViewById(R.id.date_create_at_your_videos);
            moreSettings = itemView.findViewById(R.id.moreSettingYourVideos);
        }
    }
}
