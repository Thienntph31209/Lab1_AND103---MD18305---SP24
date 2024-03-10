package com.example.lab1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.Adapter.message_Adapter;
import com.example.lab1.Modal.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Home_Screen extends AppCompatActivity {

    FirebaseUser fuser;
    DatabaseReference chatRef;
    ImageButton btn_send;
    EditText text_send;
    String userId;
    RecyclerView rcv_chat;
    message_Adapter adapter;
    private String imageurl;
    ArrayList<chat> list = new ArrayList<chat>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        rcv_chat = findViewById(R.id.rcv_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rcv_chat.setLayoutManager(layoutManager);

        adapter = new message_Adapter(this, list, imageurl);
        rcv_chat.setAdapter(adapter);

        chatRef = FirebaseDatabase.getInstance().getReference("Chat");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    chat ch = snapshot1.getValue(chat.class);
                    list.add(ch);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home_Screen.this, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        btn_send.setOnClickListener(view -> {
            String mag = text_send.getText().toString();
            if(!mag.equals("")){
                sendMessage(userId, mag);
            }else{
                Toast.makeText(this, "Không được để trống ô text !", Toast.LENGTH_SHORT).show();
            }
            text_send.setText("");
        });

//        btnLogOut.setOnClickListener(view -> {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(Home_Screen.this, Welcome_Screen.class));
//            Toast.makeText(this, "Đăng xuất tài khoản thành công !", Toast.LENGTH_SHORT).show();
//        });
    }

    private void sendMessage(String sender, String message){
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("render", sender);
        hashMap.put("message", message);
        chatRef.child("Chat").push().setValue(hashMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home_Screen.this, Welcome_Screen.class));
            Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}