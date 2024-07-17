package com.example.musicapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> songList;
    private final MusicLibrary musicLibrary;
    private OnItemClickListener onItemClickListener;

    public SongAdapter(List<Song> songList, MusicLibrary musicLibrary) {
        this.songList = songList;
        this.musicLibrary = musicLibrary;
    }

    public interface OnItemClickListener {
        void onItemClick(Song song);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(song);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle, textViewArtist, textViewUrl;
        public ImageView addToLibraryButton;

        public SongViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewArtist = view.findViewById(R.id.textViewArtist);
            textViewUrl = view.findViewById(R.id.textViewUrl);
            addToLibraryButton = view.findViewById(R.id.addToLibraryButton);
        }
    }
}
