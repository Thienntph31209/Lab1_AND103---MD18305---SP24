package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhone_Screen extends AppCompatActivity {
    TextInputEditText ed_phoneNumber, ed_Otp;
    Button btnOtp, btnLoginPhone;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_screen);
        ed_phoneNumber = findViewById(R.id.ed_phoneNumber);
        ed_Otp = findViewById(R.id.ed_Otp);
        btnOtp = findViewById(R.id.btnOtp);
        btnLoginPhone = findViewById(R.id.btnLoginPhone);

        mAuth = FirebaseAuth.getInstance();

        btnOtp.setOnClickListener(view -> {
            String phoneNumber = ed_phoneNumber.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                getOTP(phoneNumber);
            } else {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoginPhone.setOnClickListener(view -> {
            String otp = ed_Otp.getText().toString().trim();
            if (!otp.isEmpty()) {
                verifyOTP(otp);
            } else {
                Toast.makeText(LoginPhone_Screen.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                ed_Otp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginPhone_Screen.this, "Xác thực không thành công: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(LoginPhone_Screen.this, "Mã OTP đã được gửi", Toast.LENGTH_SHORT).show();
                mVerificationId = s;
            }
        };
    }

    private void getOTP(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPhone_Screen.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPhone_Screen.this, Home_Screen.class));
                        finish();
                    } else {
                        Toast.makeText(LoginPhone_Screen.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}