package my.ak8527.ashu.supersearch.Interface;

import my.ak8527.ashu.supersearch.Media.MediaInfo;

import java.util.ArrayList;

public interface MediaResponse {
    void getAppResponse(ArrayList<MediaInfo> appArrayList, ArrayList<MediaInfo> settingArrayList);
    void getContactResponse(ArrayList<MediaInfo> contactArrayList);
    void getStorageResponse(ArrayList<MediaInfo> storageArrayList);
    void getMediaResponse(ArrayList<MediaInfo> songArrayList,ArrayList<MediaInfo> videoArrayList);
}
