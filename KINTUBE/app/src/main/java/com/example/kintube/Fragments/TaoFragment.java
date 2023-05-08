package com.example.kintube.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.kintube.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;

public class TaoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1, mParam2;
    private VideoView videoFromGallery;
    private ImageView imageFromGallery;
    private EditText edtName, edtDesc;
    private Button btnUploadImage, btnUploadVideo, btnPost;
    private MediaController mediaController;
    private String root_path = "android.resource://res/";
    private String uriImage, uriVideo;

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
                        Uri uri = data.getData();
                        System.out.println(uri);
                        uriImage = String.valueOf(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
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
                        Uri uri = data.getData();
                        System.out.println(uri);
                        uriVideo = String.valueOf(uri);

                        videoFromGallery = getView().findViewById(R.id.videoFromGallery);
                        mediaController = new MediaController(getContext());
                        videoFromGallery.setMediaController(mediaController);
                        videoFromGallery.setVideoURI(uri);
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
        view = inflater.inflate(R.layout.fragment_tao, container, false);
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
//                if (!uriImage.isEmpty() && !uriVideo.isEmpty()) {
//                    ProgressDialog progressDialog = new ProgressDialog(getView().getContext());
//                    progressDialog.setTitle("Đang tải lên...");
//                    progressDialog.show();
//
//                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                    StorageReference imageRef = storageRef.child("Image/" + System.currentTimeMillis() + ".jpg");
//                    StorageReference videoRef = storageRef.child("Video/" + System.currentTimeMillis() + ".mp4");
//
//                    imageRef.putFile(Uri.parse(uriImage)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                            while (!uriTask.isCanceled());
////                            Uri url = uriTask.getResult();
//
//                            Toast.makeText(getView().getContext(), "Đã xong!!!", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            double progress = (100 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
//                            progressDialog.setMessage("Uploaded " + (int)progress);
//                        }
//                    });
//                    videoRef.putFile(Uri.parse(uriVideo)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                            while (!uriTask.isCanceled());
////                            Uri url = uriTask.getResult();
//
//                            Toast.makeText(getView().getContext(), "Đã xong!!!", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            double progress = (100 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
//                            progressDialog.setMessage("Uploaded " + (int)progress);
//                        }
//                    });
//                }
            }
        });
        return view;
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