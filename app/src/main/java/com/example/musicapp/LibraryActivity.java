package com.example.musicapp;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private MusicLibrary musicLibrary;
    private SongAdapter songAdapter;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    private FirebaseAuth mAuth;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.imageView);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LibraryActivity.this, DangNhap.class);
                    startActivity(intent);
                }
            });
            Glide.with(this).load(R.drawable.n3).into(imageView);
        } else {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LibraryActivity.this, ProfileActivity.class);
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
        imageView = findViewById(R.id.imageView4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, LibraryActivity.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, TrangChu.class);
                startActivity(intent);
            }
        });

        // Initialize MusicLibrary
        musicLibrary = new MusicLibrary(this);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Song> allSongs = musicLibrary.getAllSongs();
        songAdapter = new SongAdapter(allSongs, musicLibrary);
        recyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                playMusic(song.getSongLink());
            }
        });
    }

    public void playMusic(String url) {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            Toast.makeText(LibraryActivity.this, "Tạm dừng nhạc", Toast.LENGTH_SHORT).show();
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        isPlaying = true;
                        Toast.makeText(LibraryActivity.this, "Đang phát nhạc...", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                Toast.makeText(LibraryActivity.this, "Lỗi khi phát nhạc", Toast.LENGTH_SHORT).show();
            }
        }
    }
}