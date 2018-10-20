package my.ak8527.ashu.supersearch.AsyncWork;


import android.content.Context;
import android.os.AsyncTask;

import my.ak8527.ashu.supersearch.Info.InfoList;
import my.ak8527.ashu.supersearch.Interface.MediaResponse;
import my.ak8527.ashu.supersearch.Media.MediaInfo;

import java.util.ArrayList;

public class AppTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<MediaInfo> appArrayList,settingArrayList;
    private final InfoList infoList;
    private final MediaResponse delegate ;


    public AppTask(Context context,MediaResponse delegate){
        infoList = new InfoList(context);
        this.delegate = delegate;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        appArrayList = (ArrayList<MediaInfo>) infoList.GetAllInstalledApkInfo();
        settingArrayList = infoList.getAllSettingList();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        delegate.getAppResponse(appArrayList,settingArrayList);
    }
}