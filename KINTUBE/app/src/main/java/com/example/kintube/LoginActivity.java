package com.example.kintube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.kintube.Database.UserDatabase;
import com.example.kintube.Fragments.TrangchuFragment;
import com.example.kintube.Model.Video.User.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnRegister, btnLogin;
    private String strEmail, strPassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // ...

        units();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();

                User user = new User();
                user.setEmail(strEmail);
                user.setPassword(strPassword);

                if (checkAccount(user)) {
                    Login();
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Login() {
        Intent intent = new Intent(this, TrangchuFragment.class);
        startActivity(intent);
        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
    }

    public void units() {
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegisterLogin);
    }

    public boolean checkAccount(User user) {
        List<User> account = UserDatabase.getInstance(this).userDAO().getUserByEmail(user.getEmail());
        return account != null && !account.isEmpty();
    }
}
