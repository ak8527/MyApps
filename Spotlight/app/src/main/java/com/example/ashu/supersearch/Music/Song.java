package com.example.ashu.supersearch.Music;

public class Song {
    private final String songName;
    private final String songPath;

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
