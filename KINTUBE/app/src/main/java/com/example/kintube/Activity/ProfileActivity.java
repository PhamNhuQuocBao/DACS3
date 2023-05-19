package com.example.kintube.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kintube.DataLocal.DataLocalManager;
//import com.example.kintube.Database.UserDatabase;
import com.example.kintube.Model.User.User;
import com.example.kintube.Adapter.ProfileAdapter;
import com.example.kintube.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class ProfileActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ImageView profile_image, btn_logout;
    private TextView profile_name;
    private TabLayout tabLayout;
    private ProfileAdapter profileAdapter;
    private String uriDataImage, linkImageUser;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        profile_name = findViewById(R.id.profile_name);
        profile_image = findViewById(R.id.profile_image);
        btn_logout = findViewById(R.id.btn_logout);

        profile_name.setText(DataLocalManager.getNameAccountLogin());
        profile_image.setImageResource(Integer.parseInt(DataLocalManager.getImageAccountLogin()));
        
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setIdAccountLogin("");
                DataLocalManager.setEmailAccountLogin("");
                DataLocalManager.setNameAccountLogin("");
                DataLocalManager.setImageAccountLogin("");
                
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(ProfileActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryImage();
            }
        });

        profileAdapter = new ProfileAdapter(this);
        viewPager.setAdapter(profileAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Video");
                    break;
            }
        }).attach();

    }

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
                        uriDataImage = uri.toString();

                        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                        progressDialog.setTitle("Đang tải lên...");
                        progressDialog.show();

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference imageRef = storageReference.child("image/" + System.currentTimeMillis());
                        UploadTask uploadTask = imageRef.putFile(Uri.parse(uriDataImage));

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        linkImageUser = uri.toString();
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                            profile_image.setImageBitmap(bitmap);

                                            User user = new User();
                                            user.setImageUser(linkImageUser);
                                            DataLocalManager.setImageAccountLogin(linkImageUser);
                                            //Update image user here
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(ProfileActivity.this, "Đổi avatar thành công!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại!!!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
    );

    private void openGalleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncherImage.launch(Intent.createChooser(intent, "Select Image"));
    }
}
