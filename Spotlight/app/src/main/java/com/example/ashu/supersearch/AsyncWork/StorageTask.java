package com.example.ashu.supersearch.AsyncWork;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.ashu.supersearch.Info.InfoList;
import com.example.ashu.supersearch.Interface.MediaResponse;
import com.example.ashu.supersearch.Media.MediaInfo;

import java.io.File;
import java.util.ArrayList;

public class StorageTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<MediaInfo> filesList;
    private final InfoList infoList;
    private final MediaResponse delegate ;


    public StorageTask(Context context, MediaResponse delegate){
        infoList = new InfoList(context);
        this.delegate = delegate;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            filesList = infoList.getMyStorageList(new File("/storage"));
            ArrayList<MediaInfo> filesList1 = infoList.getMyStorageList(Environment.getExternalStorageDirectory());
            filesList.addAll(filesList1);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        delegate.getStorageResponse(filesList);
    }
}