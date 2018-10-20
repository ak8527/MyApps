package my.ak8527.ashu.supersearch.AsyncWork;


import android.content.Context;
import android.os.AsyncTask;

import my.ak8527.ashu.supersearch.Info.InfoList;
import my.ak8527.ashu.supersearch.Interface.MediaResponse;
import my.ak8527.ashu.supersearch.Media.MediaInfo;

import java.util.ArrayList;

public class MediaTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<MediaInfo> songList,videoList;
    private final InfoList infoList;
    private final MediaResponse delegate ;


    public MediaTask(Context context, MediaResponse delegate){
        infoList = new InfoList(context);
        this.delegate = delegate;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        songList = (ArrayList<MediaInfo>) infoList.getAllAudioFromDevice();
        videoList = (ArrayList<MediaInfo>) infoList.getAllVideoFromDevice();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        delegate.getMediaResponse(songList,videoList);
    }
}