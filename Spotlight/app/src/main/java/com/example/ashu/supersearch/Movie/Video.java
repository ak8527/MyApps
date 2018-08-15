package com.example.ashu.supersearch.Movie;

public class Video {
    private final String videoName;
    private final String videoPath;

    public Video(String videoName, String videoPath) {
        this.videoName = videoName;
        this.videoPath = videoPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
