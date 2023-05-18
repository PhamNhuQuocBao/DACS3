package com.example.kintube.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.kintube.Activity.MainActivity;
import com.example.kintube.DataLocal.DataLocalManager;
//import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.R;
import com.example.kintube.Model.Video.Video;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    private UploadTask uploadTask;
    private StorageReference storageRef;
    private VideoView videoFromGallery;
    private EditText edtName, edtDesc;
    private Button btnUploadVideo, btnPost;
    private MediaController mediaController;
    private Uri uriDataVideo;
    private String linkVideo;

    public TaoFragment() {
        // Required empty public constructor
    }

    public static TaoFragment newInstance(String param1, String param2) {
        TaoFragment fragment = new TaoFragment();
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

    //Xử lí giao diện chọn video
    ActivityResultLauncher<Intent> activityResultLauncherVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        uriDataVideo = data.getData();

                        videoFromGallery = getView().findViewById(R.id.videoFromGallery);
                        mediaController = new MediaController(getContext());
                        videoFromGallery.setMediaController(mediaController);
                        videoFromGallery.setVideoURI(uriDataVideo);
                        videoFromGallery.start();
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tao, container, false);
        edtName = (EditText) view.findViewById(R.id.edt_name);
        edtDesc = (EditText) view.findViewById(R.id.edt_description);
        videoFromGallery = (VideoView) view.findViewById(R.id.videoFromGallery);
        btnUploadVideo = (Button) view.findViewById(R.id.btn_uploadVideo);
        btnPost = (Button) view.findViewById(R.id.btn_post);
        // Gắn sự kiện cho Button
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryVideo();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataLocalManager.getIdAccountLogin() != "") {
                    if (!(uriDataVideo == null)) {
                        ProgressDialog progressDialog = new ProgressDialog(getView().getContext());
                        progressDialog.setTitle("Đang tải lên...");
                        progressDialog.show();

                        storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference videoRef = storageRef.child("video/" + System.currentTimeMillis());

                        uploadTask = videoRef.putFile(uriDataVideo);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(view.getContext(), "Đã có lỗi xảy ra, vui lòng thử lại!!!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        linkVideo = uri.toString();
                                        addNewVideo();
                                    }
                                });

                                Toast.makeText(view.getContext(), "Đã xong!!!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(getView().getContext(), "Vui lòng chọn ảnh và video!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TaoFragment.this.getContext(), "Vui lòng đăng nhập trước khi đăng tải video!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNewVideo() {
        String strName = edtName.getText().toString().trim();
        String strDesc = edtDesc.getText().toString().trim();

        if (TextUtils.isEmpty(strName)) {
            return;
        }

        LocalDate currentDate = LocalDate.now();
        String user_id = DataLocalManager.getIdAccountLogin();
        String video_id = "video" + System.currentTimeMillis();

        Video video = new Video();
        video.setId(video_id);
        video.setTitle(strName);
        video.setDescription(strDesc);
        video.setUpload_date(String.valueOf(currentDate));
        video.setFile_path(linkVideo);
        video.setUser_id(user_id);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("VIDEOS").child(video_id).setValue(video);

        edtName.setText("");
        edtDesc.setText("");
        videoFromGallery.setVideoURI(null);
    }

    private void openGalleryVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncherVideo.launch(Intent.createChooser(intent, "Select Video"));
    }

}