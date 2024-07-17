package com.example.musicapp;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private SongAdapter songAdapter;
    private ImageView imageView;
    public static MusicLibrary musicLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_activity);

        // Initialize MusicLibrary instance
        musicLibrary = new MusicLibrary(this);

        // Initialize DatabaseReference to access the "songs" node on Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("songs");

        // Initialize MediaPlayer to play music from URL
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, musicLibrary);
        recyclerView.setAdapter(songAdapter);

        // Initialize ImageView for navigation
        imageView = findViewById(R.id.imageView4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongActivity.this, LibraryActivity.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongActivity.this, TheLoai.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongActivity.this, TrangChu.class);
                startActivity(intent);
            }
        });

        // Fetch data from Firebase and update RecyclerView when data changes
        fetchSongsFromFirebase();
    }

    private void fetchSongsFromFirebase() {
        // Create a query to filter songs with category "Anime"
        Query query = mDatabase.orderByChild("songsCategory").equalTo("Anime");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Song song = postSnapshot.getValue(Song.class);
                    songList.add(song);
                }
                songAdapter.notifyDataSetChanged(); // Update RecyclerView when new data is available
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error loading Firebase data: " + databaseError.getMessage());
            }
        });
    }

    // Method to play music from URL
    public void playMusic(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    Toast.makeText(SongActivity.this, "Đang phát nhạc...", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Log.e("MediaPlayer", "Lỗi khi phát nhạc: " + e.getMessage());
            Toast.makeText(SongActivity.this, "Lỗi khi phát nhạc", Toast.LENGTH_SHORT).show();
        }
    }

    // Static method to retrieve MusicLibrary
    public static MusicLibrary getMusicLibrary() {
        return musicLibrary;
    }

    // Adapter for RecyclerView
    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

        private final List<Song> songList;
        private final MusicLibrary musicLibrary;

        public SongAdapter(List<Song> songList, MusicLibrary musicLibrary) {
            this.songList = songList;
            this.musicLibrary = musicLibrary;
        }

        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_song, parent, false);
            return new SongViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            Song song = songList.get(position);
            holder.textViewTitle.setText(song.getSongTitle());
            holder.textViewArtist.setText(song.getArtist());
            holder.textViewUrl.setText(song.getSongLink());

            // Set ImageView state based on whether the song is already in the library
            if (musicLibrary.getAllSongs().contains(song)) {
                holder.addToLibraryButton.setImageResource(R.drawable.add); // Example: Use your added to library icon
            } else {
                holder.addToLibraryButton.setImageResource(R.drawable.add); // Example: Use your add to library icon
            }

            // Set ImageView click listener
            holder.addToLibraryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (musicLibrary.getAllSongs().contains(song)) {
                        musicLibrary.removeSong(song);
                        holder.addToLibraryButton.setImageResource(R.drawable.add); // Change to add icon
                        Toast.makeText(holder.itemView.getContext(), "Đã xóa khỏi thư viện", Toast.LENGTH_SHORT).show();
                    } else {
                        musicLibrary.addSong(song);
                        holder.addToLibraryButton.setImageResource(R.drawable.add); // Change to added icon
                        Toast.makeText(holder.itemView.getContext(), "Đã thêm vào thư viện", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the SongActivity from the itemView's context
                    SongActivity songActivity = (SongActivity) v.getContext();
                    // Call playMusic method to play music from the selected song's URL
                    songActivity.playMusic(song.getSongLink());
                }
            });
        }

        @Override
        public int getItemCount() {
            return songList.size();
        }

        public class SongViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewTitle, textViewArtist, textViewUrl;
            public ImageView addToLibraryButton; // Change CheckBox to ImageView

            public SongViewHolder(View view) {
                super(view);
                textViewTitle = view.findViewById(R.id.textViewTitle);
                textViewArtist = view.findViewById(R.id.textViewArtist);
                textViewUrl = view.findViewById(R.id.textViewUrl);
                addToLibraryButton = view.findViewById(R.id.addToLibraryButton); // Use ImageView id
            }
        }
    }
}
