package com.example.ashu.supersearch.Media;

import android.graphics.drawable.Drawable;

public class MediaInfo {
    String mediaName,mediaPath;
    Drawable drawableIcon;

    public MediaInfo(String mediaName, String mediaPath) {
        this.mediaName = mediaName;
        this.mediaPath = mediaPath;
    }

    public MediaInfo(String mediaName, String mediaPath, Drawable drawableIcon) {
        this.mediaName = mediaName;
        this.mediaPath = mediaPath;
        this.drawableIcon = drawableIcon;
    }

    public String getMediaName() {
        return mediaName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public Drawable getDrawableIcon() {
        return drawableIcon;
    }
}
