package com.example.ashu.supersearch.MyDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("WeakerAccess")
public class PopUpWindow {
    private final String mediaName;
    private final View view;
    private final Context context;
    private final MediaInfo mediaInfo;
    @BindView(R.id.shareImage)
    ImageView shareIv;
    @BindView(R.id.propImage)
    ImageView propIv;
    @BindView(R.id.folderImage)
    ImageView openFolderIv;


    @BindView(R.id.shareText)
    TextView shareTv;
    @BindView(R.id.propText)
    TextView propTv;
    @BindView(R.id.folderText)
    TextView openFolderTv;


    final PopupWindow popupWindow;

    protected final WindowManager mWindowManager;

    public PopUpWindow(String mediaName, View view, Context context, MediaInfo mediaInfo) {
        this.mediaName = mediaName;
        this.view = view;
        this.context = context;
        this.mediaInfo = mediaInfo;
        popupWindow =new PopupWindow(context);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    }

    public void showPopUpWindow() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popUpView = Objects.requireNonNull(inflater).inflate(R.layout.popup_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;



        ButterKnife.bind(this, popUpView);


        if (mediaName.equals("Media")) {
            shareIv.setImageResource(R.drawable.ic_share_black_24dp);
            shareTv.setText(R.string.share);

            propTv.setText(R.string.properties);
            propIv.setImageResource(R.drawable.ic_info_black_24dp);

            openFolderTv.setText(R.string.open_folder);
            openFolderIv.setImageResource(R.drawable.ic_folder_black_24dp);
        } else if (mediaName.equals("App")){
            openFolderIv.setImageResource(R.drawable.ic_info_black_24dp);
            openFolderTv.setText(R.string.app_info);

            shareIv.setImageResource(R.drawable.ic_icon_google_play);
            shareTv.setText(R.string.play_store_page);

            propIv.setImageResource(R.drawable.ic_delete_black_24dp);
            propTv.setText(R.string.uninstall);
        }

        int[] location = new int[2];
        view.getLocationInWindow(location);
//        popupWindow = new PopupWindow(popUpView, width, height, true);
        popupWindow.setElevation(10);
        popupWindow.setContentView(popUpView);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.showAtLocation(popUpView, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });


    }

    @OnClick(R.id.propLayout)
    public void submit() {
        popupWindow.dismiss();
        if (mediaName.equals("Media")){
            MyPopDialog newFragment = MyPopDialog.newInstance(mediaInfo);
            Activity activity = (Activity) context;
            newFragment.show(((FragmentActivity)activity).getSupportFragmentManager(), "dialog");

        } else if (mediaName.equals("App")){
            Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            intent.setData(Uri.parse("package:" + mediaInfo.getMediaPath()));
            context.startActivity(intent);
        }

    }

    @OnClick(R.id.folderLayout)
    public void openFolder() {
        popupWindow.dismiss();
        if (mediaName.equals("Media")){
            File file = new File(mediaInfo.getMediaPath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(file.getParent());
            intent.setDataAndType(uri,"resource/folder");
            if (intent.resolveActivityInfo(context.getPackageManager(),0) != null){
                context.startActivity(intent);
            }
        } else if (mediaName.equals("App")){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mediaInfo.getMediaPath()));
            context.startActivity(intent);
        }

    }

    @OnClick(R.id.shareLayout)
    public void shareFile() {
        popupWindow.dismiss();
        if (mediaName.equals("Media")){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            File file = new File(mediaInfo.getMediaPath());
            Uri uri = FileProvider.getUriForFile(context,"com.example.ashu.supersearch.fileprovider",file);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.setType("*/*");
            context.startActivity(sharingIntent);
        } else if (mediaName.equals("App")){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + mediaInfo.getMediaPath());
            intent.setData(uri);
            context.startActivity(intent);
        }

        }



}
