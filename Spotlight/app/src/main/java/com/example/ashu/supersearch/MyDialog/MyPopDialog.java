package com.example.ashu.supersearch.MyDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyPopDialog extends DialogFragment {
    public static final String TAG = "MyPopUpTag";
    View view;
    TextView displayTv,locationTv,sizeTv,modifiedTv;
    MediaInfo mediaInfo;
    String mediaName,mediaFolder,mediaSize,mediaLastModifiedDate,mediaPath;
    public MyPopDialog(){

    }

    public static MyPopDialog newInstance(MediaInfo mediaInfo) {
        MyPopDialog frag = new MyPopDialog();
        Bundle args = new Bundle();
        args.putString("mediaName", mediaInfo.getMediaName());
        args.putString("mediaPath",mediaInfo.getMediaPath());
        frag.setArguments(args);
        return frag;
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;
        mediaName = getArguments().getString("mediaName");
        mediaPath = getArguments().getString("mediaPath");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window,null);
        displayTv = view.findViewById(R.id.displayNameTv);
        locationTv = view.findViewById(R.id.locationNameTV);
        sizeTv = view.findViewById(R.id.sizeNameTv);
        modifiedTv = view.findViewById(R.id.lastModifiedTv);

        builder.setTitle("Properties")
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        getMediaInfo(mediaPath);

        return builder.create();
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        AlertDialog alertDialog = (AlertDialog) getDialog();
//        getMediaInfo(path);
//        displayTv.setText(mediaName);
//        locationTv.setText(mediaFolder);
//        modifiedTv.setText(mediaLastModifiedDate);
//        sizeTv.setText(mediaSize);
//    }
//
//    public void getMedia(MediaInfo mediaInfo){
//        this.mediaInfo = mediaInfo;
//    }

    public void getMediaInfo(String path){
        File file = new File(path);
        mediaName = mediaInfo.getMediaName();
        mediaLastModifiedDate = getLastModifiedDate(file.lastModified());
        mediaSize = String.valueOf(file.length()/(1024*1024)) + "MB";
        mediaFolder = file.getParent();
        setDialog();
    }

    public String getLastModifiedDate(Long lastModified){
        Date date = new Date(lastModified);
        DateFormat dateFormat = new SimpleDateFormat("EEE mmm DDD yyy ", Locale.getDefault());
        return dateFormat.format(date);
    }

    public void setDialog(){
        displayTv.setText(mediaName);
        locationTv.setText(mediaFolder);
        modifiedTv.setText(mediaLastModifiedDate);
        sizeTv.setText(mediaSize);
    }


}
