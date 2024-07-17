package com.example.musicapp;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private TextView artistTextView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private List<Song> songList;
    private int currentSongIndex = 0;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmusic);

        songTitleTextView = findViewById(R.id.songTitleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        seekBar = findViewById(R.id.seekBar);
        playPauseButton = findViewById(R.id.playPauseButton);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("songs");

        songList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("MusicAppPrefs", MODE_PRIVATE);

        // Nhận currentSongIndex từ Intent nếu có, nếu không thì lấy từ SharedPreferences
        currentSongIndex = getIntent().getIntExtra("currentSongIndex", sharedPreferences.getInt("currentSongIndex", 0));

        fetchSongsFromFirebase();

        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.pause);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.newpause);
            }
            isPlaying = !isPlaying;
        });

        previousButton.setOnClickListener(v -> {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                playSong(currentSongIndex);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentSongIndex < songList.size() - 1) {
                currentSongIndex++;
                playSong(currentSongIndex);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        new Thread(() -> {
            while (mediaPlayer != null) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        runOnUiThread(() -> seekBar.setProgress(mediaPlayer.getCurrentPosition()));
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void fetchSongsFromFirebase() {
        Query query = mDatabase.orderByChild("songsCategory").equalTo("Right Place");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Song song = postSnapshot.getValue(Song.class);
                    songList.add(song);
                }
                // Phát bài hát theo chỉ số hiện tại sau khi tải xong từ Firebase
                playSong(currentSongIndex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    private void playSong(int songIndex) {
        Song song = songList.get(songIndex);
        if (song == null) return;

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        try {
            mediaPlayer.setDataSource(song.getSongLink());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.newpause);
        } catch (Exception e) {
            e.printStackTrace();
        }

        songTitleTextView.setText(song.getSongTitle());
        artistTextView.setText(song.getArtist());
        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(mp -> {
            if (currentSongIndex < songList.size() - 1) {
                currentSongIndex++;
                playSong(currentSongIndex);
            } else {
                playPauseButton.setImageResource(R.drawable.pause);
                isPlaying = false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MusicAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentSongIndex", currentSongIndex);
        editor.apply();
    }
}
