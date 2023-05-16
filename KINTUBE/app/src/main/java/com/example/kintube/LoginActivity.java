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

import com.example.kintube.DataLocal.DataLocalManager;
import com.example.kintube.Database.UserDatabase;
import com.example.kintube.Fragments.TrangchuFragment;
import com.example.kintube.Model.Video.User.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtName, edtPassword;
    private Button btnRegister, btnLogin;
    private String strEmail, strName, strPassword;

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
                    Login(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }
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

    private void Login(User user) {
        List<User> accounts = UserDatabase.getInstance(this).userDAO().getUserByEmail(user.getEmail());
        for (User account : accounts) {
            System.out.println("Info: " + account.getId() + account.getEmail() + account.getName());
            DataLocalManager.setIdAccountLogin(String.valueOf(account.getId()));
            DataLocalManager.setNameAccountLogin(account.getName());
            DataLocalManager.setEmailAccountLogin(account.getEmail());
        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
