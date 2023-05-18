package com.example.kintube.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kintube.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {
    private EditText edtNewName, edtNewDesc;
    private String strNewName, strNewDesc;
    private Button btnUpdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_video);
        initUI();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strNewName = edtNewName.getText().toString().trim();
                strNewDesc = edtNewDesc.getText().toString().trim();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//                mDatabase.child("VIDEOS").child()
            }
        });
    }

    public void initUI() {
        edtNewName = findViewById(R.id.edt_newName);
        edtNewDesc = findViewById(R.id.edt_newDescription);
        btnUpdate = findViewById(R.id.btn_updateVideo);
    }
}
