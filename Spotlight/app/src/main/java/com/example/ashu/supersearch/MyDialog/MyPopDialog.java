package com.example.ashu.supersearch.MyDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPopDialog extends DialogFragment {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.displayNameTv) TextView displayTv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.locationNameTV) TextView locationTv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.sizeNameTv) TextView sizeTv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.lastModifiedTv) TextView modifiedTv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.sizeHelpTv) TextView sizeHelpTv;

    public static MyPopDialog newInstance(MediaInfo mediaInfo) {
        MyPopDialog frag = new MyPopDialog();
        Bundle args = new Bundle();
        args.putString("mediaName", mediaInfo.getMediaName());
        args.putString("mediaPath",mediaInfo.getMediaPath());
        frag.setArguments(args);
        return frag;
    }


    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String mediaName = getArguments() != null ? getArguments().getString("mediaName") : null;
        String mediaPath = getArguments() != null ? getArguments().getString("mediaPath") : null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),  R.style.MyAlertDialog);
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
            if (file.isFile()){
                sizeTv.setText(getMediaSize(file));
            } else {
                sizeTv.setVisibility(View.GONE);
                sizeHelpTv.setVisibility(View.GONE);
            }

        }

        return builder.create();
    }



    private String getLastModifiedDate(Long lastModified){
        Date date = new Date(lastModified);
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyy ", Locale.getDefault());
        return dateFormat.format(date);
    }


    private String getMediaSize(File file){
        float size = (float) file.length()/(1024*1024);
        return size < 1000 ? String.valueOf(new DecimalFormat("##.##").format(size) + " MB") : String.valueOf(new DecimalFormat("##.##").format(size / 1024) + " GB");
    }

    private String getMediaFolder(File file) {
        return file.getParent();
    }
}
