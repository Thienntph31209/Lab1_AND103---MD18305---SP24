package com.example.lab1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class LoginGoogle_Screen extends AppCompatActivity {
    TextInputEditText ed_gmail, ed_password;
    Button btnLogin, btnLoginPhone_gmail;
    TextView Sign_Up, forgot;
    String token;
    private FirebaseAuth mAuth;
    private static AtomicInteger msgId = new AtomicInteger();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google_screen);
        ed_gmail = findViewById(R.id.ed_gmail);
        ed_password = findViewById(R.id.ed_password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginPhone_gmail = findViewById(R.id.btnLoginPhone_gmail);
        Sign_Up = findViewById(R.id.Sign_Up);
        forgot = findViewById(R.id.forgot);

        mAuth = FirebaseAuth.getInstance();

        forgot.setOnClickListener(view -> {
            startActivity(new Intent(LoginGoogle_Screen.this, ForgotPassword_Screen.class));
        });

        Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginGoogle_Screen.this, SignUp_Screen.class));
            }
        });

        btnLoginPhone_gmail.setOnClickListener(view -> {
            startActivity(new Intent(LoginGoogle_Screen.this, LoginPhone_Screen.class));
        });

        FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if(task.isSuccessful()){
                                    token = task.getResult();
                                }else{
                                    Toast.makeText(LoginGoogle_Screen.this, "Lấy token thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

        btnLogin.setOnClickListener(view -> {
            String gmail = ed_gmail.getText().toString().trim();
            String password = ed_password.getText().toString().trim();

            if (gmail.isEmpty()) {
                Toast.makeText(LoginGoogle_Screen.this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(LoginGoogle_Screen.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(gmail, password)
                    .addOnCompleteListener(LoginGoogle_Screen.this, task -> {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            Toast.makeText(LoginGoogle_Screen.this, "Đăng nhập tài khoản thành công !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginGoogle_Screen.this, Home_Screen.class);
                            intent.putExtra("userId", userId);
                            FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("379859866627@fcm.googleapis.com")
                                    .setMessageId(Integer.toString(msgId.incrementAndGet()))
                                    .addData("my_message", "Chúc mừng bạn đã đăng nập thành công")
                                    .addData("my_action", "com.example.lab1.LOGIN_SUCCESS")
                                    .build()
                            );
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginGoogle_Screen.this, "Đăng nhập tài khoản thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }


}