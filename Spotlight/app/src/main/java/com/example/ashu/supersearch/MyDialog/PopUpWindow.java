package com.example.ashu.supersearch.MyDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopUpWindow {
    private String mediaName;
    private View view;
    private Context context;
    private MediaInfo mediaInfo;
    @BindView(R.id.shareImage)  ImageView shareIv;
    @BindView(R.id.propImage)  ImageView propIv;
    @BindView(R.id.folderImage)  ImageView openFolderIv;


    @BindView(R.id.shareText)  TextView shareTv;
    @BindView(R.id.propText)  TextView propTv;
   @BindView(R.id.folderText)  TextView openFolderTv;

//   @BindView(R.id.folderLayout)
//    ConstraintLayout folderLayout;
//
//   @BindView(R.id.shareLayout)
//    ConstraintLayout shareLayout;
//
   @BindView(R.id.propLayout)
ConstraintLayout propLayout;

    public PopUpWindow(String mediaName, View view, Context context,MediaInfo mediaInfo) {
        this.mediaName = mediaName;
        this.view = view;
        this.context = context;
        this.mediaInfo = mediaInfo;
    }

    public void showPopUpWindow(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popUpView = Objects.requireNonNull(inflater).inflate(R.layout.popup_window,null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int marginStart =Math.round (context.getResources()
                .getDisplayMetrics()
                .density * 16);

        ButterKnife.bind(this,popUpView);


        if (mediaName.equals("Media")){
            shareIv.setImageResource(R.drawable.ic_share_black_24dp);
            shareTv.setText(R.string.share);

            propTv.setText(R.string.properties);
            propIv.setImageResource(R.drawable.ic_info_black_24dp);

            openFolderTv.setText(R.string.open_folder);
            openFolderIv.setImageResource(R.drawable.ic_folder_black_24dp);
        }

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        final PopupWindow popupWindow = new PopupWindow(popUpView,width,height,true);
        popupWindow.setElevation(10);
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.showAtLocation(popUpView, Gravity.NO_GRAVITY, marginStart, location[1]);

    }

   @OnClick(R.id.propLayout)
    public void submit(View v){
//

       DialogFragment newFragment = MyPopDialog.newInstance(
               mediaInfo);
       Activity activity = (Activity) context;
       newFragment.show(activity.getFragmentManager(), "dialog");
   }




}
