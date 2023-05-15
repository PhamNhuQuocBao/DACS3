package com.example.kintube.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.R;
import com.example.kintube.Model.Video.Video;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.LocalDateTime;

public class TaoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    private UploadTask uploadTask;
    private StorageReference storageRef;
    private VideoView videoFromGallery;
    private ImageView imageFromGallery;
    private EditText edtName, edtDesc;
    private Button btnUploadImage, btnUploadVideo, btnPost;
    private MediaController mediaController;
    private String root_path = "android.resource://res/";
    private Uri uriDataImage, uriDataVideo;
    private String linkVideo, linkImageVideo;

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

    //Xử lí giao diện chọn ảnh
    ActivityResultLauncher<Intent> activityResultLauncherImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        uriDataImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriDataImage);
                            ImageView imageFromGallery = (ImageView) getView().findViewById(R.id.imageFromGallery);
                            imageFromGallery.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

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
        View view = inflater.inflate(R.layout.fragment_tao, container, false);
        edtName = (EditText) view.findViewById(R.id.edt_name);
        edtDesc = (EditText) view.findViewById(R.id.edt_description);
        videoFromGallery = (VideoView) view.findViewById(R.id.videoFromGallery);
        imageFromGallery = (ImageView) view.findViewById(R.id.imageFromGallery);
        btnUploadImage = (Button) view.findViewById(R.id.btn_uploadImage);
        btnUploadVideo = (Button) view.findViewById(R.id.btn_uploadVideo);
        btnPost = (Button) view.findViewById(R.id.btn_post);
        // Gắn sự kiện cho Button
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryVideo();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryImage();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(uriDataImage == null) && !(uriDataVideo == null)) {
                    ProgressDialog progressDialog = new ProgressDialog(getView().getContext());
                    progressDialog.setTitle("Đang tải lên...");
                    progressDialog.show();

                    storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis());
                    StorageReference videoRef = storageRef.child("video/" + System.currentTimeMillis());

                    uploadTask = imageRef.putFile(uriDataImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(view.getContext(), "Đã có lỗi xảy ra, vui lòng thử lại!!!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    linkImageVideo = uri.toString();
                                }
                            });
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
                        }
                    });
                } else {
                    Toast.makeText(getView().getContext(), "Vui lòng chọn ảnh và video!!!", Toast.LENGTH_SHORT).show();
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

        LocalDateTime currentDateTime = LocalDateTime.now();


        Video video = new Video();
        video.setTitle(strName);
        video.setDescription(strDesc);
        video.setUpload_date(String.valueOf(currentDateTime));
        video.setFile_path(linkVideo);
        video.setImageVideo(linkImageVideo);

        VideoDatabase.getInstance(getView().getContext()).videoDAO().insertVideo(video);

        edtName.setText("");
        edtDesc.setText("");
    }


    private void openGalleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncherImage.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void openGalleryVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncherVideo.launch(Intent.createChooser(intent, "Select Video"));
    }

}