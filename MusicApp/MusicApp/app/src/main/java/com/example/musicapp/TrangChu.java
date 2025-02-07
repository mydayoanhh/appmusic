package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.musicapp.R;


public class TrangChu extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.trangchu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void search(View view){
        Intent intent = new Intent(TrangChu.this,Search.class);
        startActivity(intent);
    }
    public void thuvien(View view){
        Intent intent = new Intent(TrangChu.this,ThuVien.class);
        startActivity(intent);
    }
    public void Sign(View view){
        Intent intent = new Intent(TrangChu.this,DangNhap.class);
        startActivity(intent);
    }
}
