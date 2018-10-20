package my.ak8527.ashu.supersearch.MyDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import my.ak8527.ashu.supersearch.MainActivity;
import my.ak8527.ashu.supersearch.Media.MediaInfo;
import my.ak8527.ashu.supersearch.R;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.ak8527.ashu.supersearch.Adaptor.MediaAdaptor;

import static my.ak8527.ashu.supersearch.setting.SettingActivity.MY_SETTING_PREF;

public class MyPopUpWindow {

    private final String mediaName;
    private final Context context;
    private final MediaInfo mediaInfo;


    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.shareImage)
    ImageView shareIv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.propImage)
    ImageView propIv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.folderImage)
    ImageView openFolderIv;


    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.shareText)
    TextView shareTv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.propText)
    TextView propTv;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.folderText)
    TextView openFolderTv;


    public MyPopUpWindow(String mediaName, Context context, MediaInfo mediaInfo) {
        this.mediaName = mediaName;
        this.context = context;
        this.mediaInfo = mediaInfo;
    }

    private PopupWindow popupWindow;

    public void showPopUpWindow(View v) {
        View popupView = View.inflate(context, R.layout.popup_window, null);
        ButterKnife.bind(this, popupView);


        if (mediaName.equals("App")) {
            setLayoutResources();
        }


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setElevation(10);
        popupWindow.setAnimationStyle(R.style.MyPopUpWindowStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.setOverlapAnchor(true);
        }
        popupWindow.showAsDropDown(v, 0, 0, Gravity.START);
    }

    private void setLayoutResources() {
        openFolderIv.setImageResource(R.drawable.ic_info_black_24dp);
        openFolderTv.setText(R.string.app_info);

        shareIv.setImageResource(R.drawable.ic_icon_google_play);
        shareTv.setText(R.string.play_store_page);

        propIv.setImageResource(R.drawable.ic_delete_black_24dp);
        propTv.setText(R.string.uninstall);
    }

    @SuppressWarnings("WeakerAccess")
    @OnClick(R.id.propLayout)
    public void submit() {
        popupWindow.dismiss();
        if (mediaName.equals("Media")) {
            showPropDialog(mediaInfo);
        } else if (mediaName.equals("App")) {
            uninstallApp(mediaInfo);
        }
    }

    @SuppressWarnings("WeakerAccess")
    @OnClick(R.id.folderLayout)
    public void openMediaFolder() {
        popupWindow.dismiss();
        if (mediaName.equals("Media")) {
            File file = new File(mediaInfo.getMediaPath());
            openFolder(file.getParent());
        } else if (mediaName.equals("App")) {
            openAppInfoPage(mediaInfo);
        }

    }

    @SuppressWarnings("WeakerAccess")
    @OnClick(R.id.shareLayout)
    public void shareMediaFile() {
        popupWindow.dismiss();
        if (mediaName.equals("Media")) {
            shareMedia(new File(mediaInfo.getMediaPath()));
        } else if (mediaName.equals("App")) {
            openAppPlayStorePage(mediaInfo);
        }
    }


    private void openAppInfoPage(MediaInfo mediaInfo) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mediaInfo.getMediaPath()));
        context.startActivity(intent);
    }

    private void openAppPlayStorePage(MediaInfo mediaInfo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + mediaInfo.getMediaPath());
        intent.setData(uri);
        context.startActivity(intent);
    }

    private void uninstallApp(MediaInfo mediaInfo) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + mediaInfo.getMediaPath()));
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_SETTING_PREF, Context.MODE_PRIVATE).edit();
        editor.putString("app_package_name", mediaInfo.getMediaPath());
        editor.apply();
        ((MainActivity) context).startActivityForResult(intent, MediaAdaptor.APP_UNINSTALL_REQUEST_CODE);
    }


    private void openFolder(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        intent.setDataAndType(uri, "resource/folder");
        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
            context.startActivity(intent);
        }
    }


    private String extensionFind(String text) {
        String[] image = new String[]{".jpg", ".jpeg", ".png", ".gif"};
        String[] video = new String[]{".mkv", ".mp4", ".3gp", ".avi"};
        String[] audio = new String[]{".mp3", ".aac", ".wav", ".wma", ".ogg"};
        String[] doc = new String[]{".txt", ".doc", ".docs", ".xls"};
        String[] compress = new String[]{".zip", ".7z", ".rar", ".tar"};

        if (text.toLowerCase(Locale.getDefault()).endsWith(".apk"))
            return "app";

        if (text.toLowerCase(Locale.getDefault()).endsWith(".pdf"))
            return "pdf";

        for (String extension : compress) {
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "compress";
        }

        for (String extension : image) {
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "image";
        }

        for (String extension : video) {
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "video";
        }

        for (String extension : doc) {
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "text";
        }

        for (String extension : audio) {
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "audio";
        }

        return "empty";
    }

    private void shareMedia(File file) {
        String type = extensionFind(file.getPath());
        String intentType = type + "/*";

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(context, "my.ak8527.ashu.supersearch.fileprovider", file);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.setType(intentType);
        context.startActivity(sharingIntent);
    }

    private void showPropDialog(MediaInfo mediaInfo) {
        MyPopDialog newFragment = MyPopDialog.newInstance(mediaInfo);
        Activity activity = (Activity) context;
        newFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "dialog");
    }


}
