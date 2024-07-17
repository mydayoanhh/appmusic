package com.example.musicapp;

public class Song {
    private String songTitle;
    private String artist;
    private String songLink;
    private String albumArt;

    public Song() {
        // Default constructor required for calls to DataSnapshot.getValue(Song.class)
    }

    public Song(String songTitle, String artist, String songLink, String albumArt) {
        this.songTitle = songTitle;
        this.artist = artist;
        this.songLink = songLink;
        this.albumArt= albumArt;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
public String getAlbumArt(){
        return albumArt;}
    public void setAlbumArt(String albumArt){
        this.albumArt=albumArt;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

}
