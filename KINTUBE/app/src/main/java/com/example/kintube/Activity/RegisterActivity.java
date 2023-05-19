package com.example.kintube.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kintube.Database.DBHelper;
import com.example.kintube.Model.User.User;
import com.example.kintube.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private final static String IMAGE_DEFAULT = "https://firebasestorage.googleapis.com/v0/b/kintube-86e53.appspot.com/o/image%2Fmale-profile-flat-blue-simple-icon-with-long-shadowxa_159242-10092.avif?alt=media&token=6793f7ac-ee09-453e-9074-7e83c6553b33";
    private EditText edtEmail, edtName, edtPhone, edtPassword, edtPasswordAgain;
    private Button btnRegister;
    private String strId, strEmail, strName, strPhone, strPassword, strPasswordAgain;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // ...
        units();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = edtEmail.getText().toString().trim();
                strName = edtName.getText().toString().trim();
                strPhone = edtPhone.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strPasswordAgain = edtPasswordAgain.getText().toString().trim();
                strId = "user" + strPhone;

                if (isValidEmail(strEmail)) {
                    if (strName.equals("")) {
                        Toast.makeText(RegisterActivity.this, "Vui lòng nhập tên của bạn!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Objects.equals(strPassword, strPasswordAgain)) {
                            mDatabase.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(strId)) {
                                        Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        User user = new User();
                                        user.setId(strId);
                                        user.setEmail(strEmail);
                                        user.setName(strName);
                                        user.setPhone(strPhone);
                                        user.setPassword(strPassword);
                                        user.setSub(0);
                                        user.setImageUser(String.valueOf(R.drawable.profile_default));

                                        mDatabase.child("USERS").child(strId).setValue(user);

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                        Toast.makeText(RegisterActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //khởi tạo các giá trị UI
    public void units() {
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtName = findViewById(R.id.edtNameRegister);
        edtPhone = findViewById(R.id.edtPhoneRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        edtPasswordAgain = findViewById(R.id.edtPasswordAgainRegister);
        btnRegister = findViewById(R.id.btnRegister);
    }

    //hàm kiểm tra định dạng email
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
