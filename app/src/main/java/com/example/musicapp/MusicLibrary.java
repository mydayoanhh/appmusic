package com.example.musicapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {

    private static final String PREFS_NAME = "MusicLibraryPrefs";
    private static final String SONG_LIST_KEY = "SongList";

    private List<Song> songList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public MusicLibrary(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        songList = loadSongsFromPreferences();
    }

    // Thêm bài hát vào thư viện
    public void addSong(Song song) {
        songList.add(song);
        saveSongsToPreferences();
    }

    // Xóa bài hát khỏi thư viện
    public void removeSong(Song song) {
        songList.remove(song);
        saveSongsToPreferences();
    }

    // Lấy tất cả các bài hát trong thư viện
    public List<Song> getAllSongs() {
        return songList;
    }

    // Lưu danh sách bài hát vào SharedPreferences
    private void saveSongsToPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(songList);
        editor.putString(SONG_LIST_KEY, json);
        editor.apply();
    }

    // Tải danh sách bài hát từ SharedPreferences
    private List<Song> loadSongsFromPreferences() {
        String json = sharedPreferences.getString(SONG_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<Song>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }
}
