package com.example.kintube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kintube.Database.UserDatabase;
import com.example.kintube.Model.Video.User.User;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmail, edtName, edtPassword, edtPasswordAgain;
    private Button btnRegister;
    private String strEmail, strName, strPassword, strPasswordAgain;

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
                strPassword = edtPassword.getText().toString().trim();
                strPasswordAgain = edtPasswordAgain.getText().toString().trim();

                if (isValidEmail(strEmail)) {
                    if (strName.equals("")) {
                        Toast.makeText(RegisterActivity.this, "Vui lòng nhập tên của bạn!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Objects.equals(strPassword, strPasswordAgain)) {
                            User user = new User();
                            user.setName(strName);
                            user.setEmail(strEmail);
                            user.setPassword(strPassword);

                            if (!checkAccountExists(user)) {
                                UserDatabase.getInstance(RegisterActivity.this).userDAO().insertUser(user);

                                edtEmail.setText("");
                                edtName.setText("");
                                edtPassword.setText("");
                                edtPasswordAgain.setText("");

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);

                                Toast.makeText(RegisterActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    //khởi tạo các giá trị UI
    public void units() {
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtName = findViewById(R.id.edtNameRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        edtPasswordAgain = findViewById(R.id.edtPasswordAgainRegister);
        btnRegister = findViewById(R.id.btnRegister);
    }

    //hàm kiểm tra tài khoản đã tồn tại chưa
    public boolean checkAccountExists(User user) {
        List<User> account = UserDatabase.getInstance(this).userDAO().getUserByEmail(user.getEmail());
        return account != null && !account.isEmpty();
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
