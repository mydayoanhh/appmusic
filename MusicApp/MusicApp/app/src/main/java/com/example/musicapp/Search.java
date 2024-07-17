package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.R;


public class Search extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
    }
    public void thuvien1(View view){
        Intent intent = new Intent(Search.this,ThuVien.class);
        startActivity(intent);
    }
    public void trangchu2(View view){
        Intent intent = new Intent(Search.this,TrangChu.class);
        startActivity(intent);
    }
}
