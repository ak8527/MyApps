package com.example.ashu.supersearch.Interface;

import com.example.ashu.supersearch.Media.MediaInfo;

import java.util.ArrayList;

public interface MediaResponse {
    void getAppResponse(ArrayList<MediaInfo> appArrayList,ArrayList<MediaInfo> settingArrayList);
    void getContactResponse(ArrayList<MediaInfo> contactArrayList);
    void getStorageResponse(ArrayList<MediaInfo> storageArrayList);
    void getMediaResponse(ArrayList<MediaInfo> songArrayList,ArrayList<MediaInfo> videoArrayList);
}
