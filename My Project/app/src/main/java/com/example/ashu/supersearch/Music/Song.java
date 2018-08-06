package com.example.ashu.supersearch.Music;

public class Song {
    String songName,songPath;

    public Song(String songName, String songPath) {
        this.songName = songName;
        this.songPath = songPath;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongPath() {
        return songPath;
    }
}
