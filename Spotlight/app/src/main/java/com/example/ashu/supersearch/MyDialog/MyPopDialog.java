package com.example.ashu.supersearch.MyDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPopDialog extends DialogFragment {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.displayNameTv) TextView displayTv;
    @BindView(R.id.locationNameTV) TextView locationTv;
    @BindView(R.id.sizeNameTv) TextView sizeTv;
    @BindView(R.id.lastModifiedTv) TextView modifiedTv;

    public static MyPopDialog newInstance(MediaInfo mediaInfo) {
        MyPopDialog frag = new MyPopDialog();
        Bundle args = new Bundle();
        args.putString("mediaName", mediaInfo.getMediaName());
        args.putString("mediaPath",mediaInfo.getMediaPath());
        Log.e("Test123", "newInstance: " + mediaInfo.getMediaName());
        frag.setArguments(args);
        return frag;
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String mediaName = getArguments() != null ? getArguments().getString("mediaName") : null;
        Log.e("Test1234", "onCreateDialog: "+ mediaName);
        String mediaPath = getArguments() != null ? getArguments().getString("mediaPath") : null;
        Log.e("Test1234", "onCreateDialog: "+ mediaPath);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.prop_dialog, null);
        ButterKnife.bind(this,view);

        builder.setTitle("Properties")
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        File file;
        if (mediaPath != null) {
            file = new File(mediaPath);
            displayTv.setText(mediaName);
            locationTv.setText(getMediaFolder(file));
            modifiedTv.setText(getLastModifiedDate(file.lastModified()));
            sizeTv.setText(getMediaSize(file));

        }

        return builder.create();
    }



    private String getLastModifiedDate(Long lastModified){
        Date date = new Date(lastModified);
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyy ", Locale.getDefault());
        return dateFormat.format(date);
    }


    private String getMediaSize(File file){
        return String.valueOf(file.length()/(1024*1024)) + " MB";
    }

    public String getMediaFolder(File file) {
        return file.getParent();
    }
}
