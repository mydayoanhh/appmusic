package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TrangChu extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imageView; // ImageView for profile image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangchu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        imageView = findViewById(R.id.imageView);

        if (currentUser == null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TrangChu.this, DangNhap.class);
                    startActivity(intent);
                }
            });
            Glide.with(this).load(R.drawable.n3).into(imageView);
        } else {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TrangChu.this, ProfileActivity.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra("profileImageUrl")) {
                String profileImageUrl = getIntent().getStringExtra("profileImageUrl");
                Glide.with(this).load(profileImageUrl).into(imageView);
            } else if (currentUser.getPhotoUrl() != null) {
                // Load Firebase
                Glide.with(this).load(currentUser.getPhotoUrl()).into(imageView);
            } else {
                // User has no profile image, show default image
                Glide.with(this).load(R.drawable.n3).into(imageView);
            }
        }

        // Handle click event to navigate to library
        ImageView imageViewLibrary = findViewById(R.id.imageView4);
        imageViewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this, LibraryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void search(View view) {
        Intent intent = new Intent(TrangChu.this, TheLoai.class);
        startActivity(intent);
    }

    public void Sign(View view) {
        Intent intent = new Intent(TrangChu.this, DangNhap.class);
        startActivity(intent);
    }

    public void next(View view) {
        Intent intent = new Intent(TrangChu.this, SongActivity.class);
        startActivity(intent);
    }

    public void next1(View view) {
        Intent intent = new Intent(TrangChu.this, SongRightPlace.class);
        startActivity(intent);
    }
}
