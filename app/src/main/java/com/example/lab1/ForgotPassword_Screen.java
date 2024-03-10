package com.example.lab1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword_Screen extends AppCompatActivity {
    ImageView back_ForgotPassword;
    TextInputEditText ed_gmailForgot;
    Button Reset_Password;
    FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        back_ForgotPassword = findViewById(R.id.back_ForgotPassword);
        ed_gmailForgot = findViewById(R.id.ed_gmailForgot);
        Reset_Password = findViewById(R.id.Reset_Password);
        mAuth = FirebaseAuth.getInstance();

        Reset_Password.setOnClickListener(view -> {
            String gmailAddress = ed_gmailForgot.getText().toString().trim();

            if (gmailAddress.isEmpty()) {
                Toast.makeText(ForgotPassword_Screen.this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(gmailAddress)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword_Screen.this, "Vui lòng kiểm tra hộp thư để cập nhật lại mâ khẩu !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPassword_Screen.this, LoginGoogle_Screen.class));
                        }else{
                            Toast.makeText(ForgotPassword_Screen.this, "Lỗi gửi gmail !", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}