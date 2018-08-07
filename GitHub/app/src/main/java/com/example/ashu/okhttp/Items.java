package com.example.ashu.okhttp;

import com.google.gson.annotations.SerializedName;

public class Items {
    @SerializedName("name")
   private String mName;
    @SerializedName("url")
   private String mProfileUrl;
    @SerializedName("avatar_url")
   private String mImageUrl;
    @SerializedName("bio")
    private String mBio;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("login")
    private String mLogin;
    @SerializedName("company")
    private String mCompany;
    @SerializedName("blog")
    private String mBlog;
    @SerializedName("html_url")
    private String mHtmlUrl;

    public String getmHtmlUrl() {
        return mHtmlUrl;
    }

    public String getmBio() {
        return mBio;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmLogin() {
        return mLogin;
    }

    public String getmCompany() {
        return mCompany;
    }

    public String getmName() {
        return mName;
    }

    public String getmProfileUrl() {
        return mProfileUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmBlog() {
        return mBlog;
    }
}
