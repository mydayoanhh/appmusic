package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ThuVien  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);
    }
    public void sign1(View view){
        Intent intent = new Intent(ThuVien.this,DangNhap.class);
        startActivity(intent);
    }
    public void trangchu1(View view){
        Intent intent = new Intent(ThuVien.this,TrangChu.class);
        startActivity(intent);
    }
    public void search1(View view){
        Intent intent = new Intent(ThuVien.this, TheLoai.class);
        startActivity(intent);
    }
}