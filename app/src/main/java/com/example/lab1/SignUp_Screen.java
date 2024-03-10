package com.example.lab1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SignUp_Screen extends AppCompatActivity {
    ImageView back_SignUp;
    TextInputEditText ed_gmailSignup, ed_passwordSignUp;
    Button btnSignUp;
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        back_SignUp = findViewById(R.id.back_SignUp);
        ed_gmailSignup = findViewById(R.id.ed_gmailSignup);
        ed_passwordSignUp = findViewById(R.id.ed_passwordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_gmailSignup.getText().toString().trim();
                String password = ed_passwordSignUp.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(SignUp_Screen.this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(SignUp_Screen.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp_Screen.this, task -> {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUp_Screen.this, "Đăng ký tài khoản thành công !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp_Screen.this, LoginGoogle_Screen.class));
                            }else{
                                Toast.makeText(SignUp_Screen.this, "Đăng ký tài khoản không thành công !", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}