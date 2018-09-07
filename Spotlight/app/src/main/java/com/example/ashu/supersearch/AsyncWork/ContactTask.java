package com.example.ashu.supersearch.AsyncWork;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ashu.supersearch.Info.InfoList;
import com.example.ashu.supersearch.Interface.MediaResponse;
import com.example.ashu.supersearch.Media.MediaInfo;

import java.util.ArrayList;

public class ContactTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<MediaInfo> contactArrayList;
    private final InfoList infoList;
    private final MediaResponse delegate ;


    public ContactTask(Context context, MediaResponse delegate){
        infoList = new InfoList(context);
        this.delegate = delegate;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        contactArrayList =  infoList.getAllContact();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        delegate.getContactResponse(contactArrayList);
    }
}