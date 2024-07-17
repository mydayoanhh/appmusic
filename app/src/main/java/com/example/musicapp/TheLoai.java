package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class TheLoai extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theloai);
    }
    public void thuvien1(View view){
        Intent intent = new Intent(TheLoai.this,LibraryActivity.class);
        startActivity(intent);
    }
    public void trangchu2(View view){
        Intent intent = new Intent(TheLoai.this, TrangChu.class);
        startActivity(intent);
    }
}