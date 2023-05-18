package com.example.kintube.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kintube.DataLocal.DataLocalManager;
import com.example.kintube.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText edtPhone, edtPassword;
    private Button btnRegister, btnLogin;
    private String strEmail, strPhone, strName, strPassword, strId, imgUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        units();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login() {
        strPhone = edtPhone.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();
        strId = "user" + strPhone;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(strId)) {
                    String passwordFromDB = snapshot.child(strId).child("password").getValue(String.class);
                    if (Objects.equals(passwordFromDB, strPassword)) {
                        strEmail = snapshot.child(strId).child("email").getValue(String.class);
                        strName = snapshot.child(strId).child("name").getValue(String.class);
                        imgUser = snapshot.child(strId).child("imageUser").getValue(String.class);

                        DataLocalManager.setIdAccountLogin(strId);
                        DataLocalManager.setNameAccountLogin(strName);
                        DataLocalManager.setEmailAccountLogin(strEmail);
                        DataLocalManager.setImageAccountLogin(imgUser);

                        System.out.println("Account login: " + DataLocalManager.getNameAccountLogin());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void units() {
        edtPhone = findViewById(R.id.edtPhoneLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegisterLogin);
    }
}
