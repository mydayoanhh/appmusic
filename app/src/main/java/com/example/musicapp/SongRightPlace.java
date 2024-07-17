package com.example.musicapp;

import static com.example.musicapp.SongActivity.musicLibrary;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class SongRightPlace extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<Song> songList;
    private SongAdapter songAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_activity);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("songs");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList);
        recyclerView.setAdapter(songAdapter);

        imageView = findViewById(R.id.imageView4);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(SongRightPlace.this, LibraryActivity.class);
            startActivity(intent);
        });

        fetchSongsFromFirebase();
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
                songAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error loading Firebase data: " + databaseError.getMessage());
            }
        });
    }

    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

        private final List<Song> songList;

        public SongAdapter(List<Song> songList) {
            this.songList = songList;
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

            if (musicLibrary.getAllSongs().contains(song)) {
                holder.addToLibraryButton.setImageResource(R.drawable.add); // Use your added to library icon
            } else {
                holder.addToLibraryButton.setImageResource(R.drawable.add); // Use your add to library icon
            }

            // Set ImageView click listener
            holder.addToLibraryButton.setOnClickListener(v -> {
                if (musicLibrary.getAllSongs().contains(song)) {
                    musicLibrary.removeSong(song);
                    holder.addToLibraryButton.setImageResource(R.drawable.add); // Change to add icon
                    Toast.makeText(holder.itemView.getContext(), "Đã xóa khỏi thư viện", Toast.LENGTH_SHORT).show();
                } else {
                    musicLibrary.addSong(song);
                    holder.addToLibraryButton.setImageResource(R.drawable.add); // Change to added icon
                    Toast.makeText(holder.itemView.getContext(), "Đã thêm vào thư viện", Toast.LENGTH_SHORT).show();
                }
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(SongRightPlace.this, PlayerActivity.class);
                intent.putExtra("songUrl", song.getSongLink());
                intent.putExtra("songTitle", song.getSongTitle());
                intent.putExtra("artistName", song.getArtist());
                intent.putExtra("currentSongIndex", position); // Truyền vị trí bài hát
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return songList.size();
        }

        public class SongViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewTitle, textViewArtist;
            public ImageView addToLibraryButton;

            public SongViewHolder(View view) {
                super(view);
                textViewTitle = view.findViewById(R.id.textViewTitle);
                textViewArtist = view.findViewById(R.id.textViewArtist);
                addToLibraryButton = view.findViewById(R.id.addToLibraryButton); // Assuming you have this in your item_song layout
            }
        }
    }
}
