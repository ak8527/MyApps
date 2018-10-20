package com.example.ashu.supersearch.Adaptor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.MyDialog.MyPopDialog;
import com.example.ashu.supersearch.MyDialog.MyPopUpWindow;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static com.example.ashu.supersearch.setting.SettingActivity.MY_SETTING_PREF;

public class MediaAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int APP_ID = 0;
    public static final int CONTACT_ID = 1;
    public static final int SETTING_ID = 2;
    public static final int AUDIO_ID = 3;
    public static final int VIDEO_ID = 4;
    public static final int FILE_ID = 5;
    public static final int SEARCH_APP_ID = 6;
    public static final int APP_UNINSTALL_REQUEST_CODE = 200;
    private String spannableText;
    private final ArrayList<MediaInfo> myAppArrayList = new ArrayList<>();
    private static final int MY_TELEPHONE_REQUEST_CODE = 111;
    private String searchText = "";
    private boolean moreFiles = false;
    private SharedPreferences.Editor editor;


    private final Context context;
    private final ArrayList<MediaInfo> mediaInfoArrayList;
    private final int mediaId;
    private String phoneNumber;

    public MediaAdaptor(Context context, ArrayList<MediaInfo> mediaInfoArrayList, int mediaId) {
        this.context = context;
        this.mediaInfoArrayList = mediaInfoArrayList;
        this.mediaId = mediaId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;

        switch (mediaId) {
            case APP_ID:
            case SEARCH_APP_ID:
                view = layoutInflater.inflate(R.layout.app_list_item, parent, false);
                return new AppHolder(view);
            case SETTING_ID:
            case AUDIO_ID:
            case VIDEO_ID:
            case FILE_ID:
            case CONTACT_ID:

                view = layoutInflater.inflate(R.layout.media_list_item, parent, false);
                return new MediaHolder(view);

            default:
                view = layoutInflater.inflate(R.layout.media_list_item, parent, false);
                return new MediaHolder(view);
        }

    }

    private SpannableString str;


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final MediaInfo mediaInfo;
        int itemId = getItemViewType(position);
        if (itemId == SEARCH_APP_ID) {
            mediaInfo = mediaInfoArrayList.get(position);
        } else {
            mediaInfo = myAppArrayList.get(position);
            String mediaName = mediaInfo.getMediaName();
            str = new SpannableString(mediaName);
            String testText = mediaName.toLowerCase(Locale.getDefault());
            String testTextToBold = spannableText.toLowerCase(Locale.getDefault());
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();
            str.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        switch (itemId) {


            case APP_ID: {
                final AppHolder appHolder = (AppHolder) holder;
                final String packageName = mediaInfo.getMediaPath();
                appHolder.imageView.setImageDrawable(getAppIconByPackageName(packageName));
                appHolder.textView.setText(str);
                appHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                        context.startActivity(intent);
                    }
                });

                appHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showMyPopUpWindow("App", appHolder.imageView, mediaInfo);
                        editor = context.getSharedPreferences(MY_SETTING_PREF, Context.MODE_PRIVATE).edit();
                        editor.putInt("app_position", holder.getAdapterPosition());
                        editor.apply();
                        return false;
                    }
                });
                break;
            }


            case CONTACT_ID: {
                MediaHolder contactHolder = (MediaHolder) holder;
                contactHolder.mediaNameTv.setText(str);
                phoneNumber = mediaInfo.getMediaPath();
                contactHolder.callImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            context.startActivity(intent);
                        } else {
                            requestPermissions(
                                    (Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_TELEPHONE_REQUEST_CODE);
                        }
                    }
                });

                contactHolder.chatImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }
                    }
                });

                contactHolder.callImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            context.startActivity(intent);

                        } else {
                            requestPermissions(
                                    (Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_TELEPHONE_REQUEST_CODE);


                        }
                    }
                });

                break;
            }


            case SETTING_ID: {
                MediaHolder mediaHolder = (MediaHolder) holder;
                mediaHolder.mediaNameTv.setText(str);
                final String packageName = mediaInfo.getMediaPath();
                mediaHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(packageName);
                        context.startActivity(intent);
                    }
                });
                break;
            }


            case AUDIO_ID: {
                final MediaHolder mediaHolder = (MediaHolder) holder;
                final File file = new File(mediaInfo.getMediaPath());

                mediaHolder.mediaNameTv.setText(str);

                try {
                    MediaMetadataRetriever albumArt = new MediaMetadataRetriever();
                    albumArt.setDataSource(mediaInfo.getMediaPath());
                    byte[] art = albumArt.getEmbeddedPicture();
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 2;
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length, opt);
                    Drawable drawable = new BitmapDrawable(context.getResources(), songImage);
                    Glide.with(context)
                            .load(drawable)
                            .into(mediaHolder.mediaIv);
                } catch (Exception e) {
                    mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_song_48dp);
                    mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.audioColor));
                }


                mediaHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaOpen("audio/*", file);
                    }
                });

                mediaHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showMyPopUpWindow("Media", mediaHolder.mediaIv, mediaInfo);
                        return false;
                    }
                });

                break;
            }


            case VIDEO_ID: {
                final MediaHolder mediaHolder = (MediaHolder) holder;

                mediaHolder.mediaNameTv.setText(str);

                try {
                    Glide.with(context)
                            .asBitmap()
                            .load(mediaInfo.getMediaPath())
                            .into(mediaHolder.mediaIv);
                } catch (Exception e) {
                    mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.movieColor));
                    mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_movie_48dp);
                }


                mediaHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = new File(mediaInfo.getMediaPath());
                        mediaOpen("video/*", file);
                    }
                });

                mediaHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showMyPopUpWindow("Media", mediaHolder.mediaIv, mediaInfo);
                        return false;
                    }
                });
                break;
            }


            case FILE_ID: {
                final MediaHolder mediaHolder = (MediaHolder) holder;
                mediaHolder.mediaNameTv.setText(str);
                final File file = new File(mediaInfo.getMediaPath());

                if (file.isDirectory()){
                    mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_folder_48dp);
                    mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.folderColor));

                } else {

                    String extension = extensionFind(mediaInfo.getMediaName());
                    switch (extension) {
                        case "app":
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_android_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.appColor));
                            break;
                        case "pdf":
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_pdf_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.pdfColor));
                            break;
                        case "compress":
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_zip_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.compressColor));
                            break;
                        case "image": {
                            try {
                                Glide.with(context)
                                        .load(mediaInfo.getMediaPath())
                                        .into(mediaHolder.mediaIv);
                            } catch (Exception e) {
                                mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_image_48dp);
                                mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.imageColor));
                            }
                        }
                        break;

                        case "text":
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_file_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.fileColor));
                            break;
                        case "video":
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_movie_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.movieColor));
                        case "audio":
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_song_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.audioColor));
                            break;
                        default:
                            mediaHolder.mediaIv.setImageResource(R.drawable.ic_action_file_48dp);
                            mediaHolder.mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.fileColor));

                            break;
                    }

                }

                final String filePath = mediaInfo.getMediaPath();
                mediaHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (file.isFile()) {
                            if (mediaInfo.getMediaName().toLowerCase(Locale.getDefault()).endsWith(".pdf"))
                                mediaOpen("application/pdf", file);
                            else if (mediaInfo.getMediaName().toLowerCase(Locale.getDefault()).endsWith(".zip"))
                                mediaOpen("application/zip", file);
                            else if (mediaInfo.getMediaName().toLowerCase(Locale.getDefault()).endsWith(".apk"))
                                installApp(new File(filePath));
                            else
                                resultDialog(filePath);
                        } else {
                            openFolder(filePath);
                        }
                    }
                });


                mediaHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (file.isFile()) {
                            showMyPopUpWindow("Media", mediaHolder.mediaIv, mediaInfo);
                        } else {
                            showPropDialog(mediaInfo);
                        }
                        return false;
                    }
                });

                break;
            }

            case SEARCH_APP_ID: {
                AppHolder mediaHolder = (AppHolder) holder;
                Drawable drawable = getAppIconByPackageName(mediaInfo.getMediaPath());
                mediaHolder.imageView.setImageDrawable(drawable);
                mediaHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchAppOpen(mediaInfo);
                    }
                });
            }
        }
    }

    private void installApp(File file) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.example.ashu.supersearch.fileprovider", file);
            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            Uri apkUri = Uri.fromFile(file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public void calling() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }

    public void notifyChange(int position) {
        myAppArrayList.remove(position);
        notifyItemRemoved(position);

    }

    public boolean filter(String text) {
        spannableText = text;
        boolean ans = false;
        text = text.toLowerCase(Locale.getDefault());
        myAppArrayList.clear();
        if (!text.isEmpty()) {
            for (MediaInfo mediaInfo : mediaInfoArrayList) {
                if (mediaInfo.getMediaName().toLowerCase(Locale.getDefault()).contains(text)) {
                    myAppArrayList.add(mediaInfo);
                    ans = true;
                }
            }
            notifyDataSetChanged();
        }
        return ans;
    }

    public void filterSearchApp(String text) {
        searchText = text;
        notifyDataSetChanged();
    }


    private Drawable getAppIconByPackageName(String ApkTempPackageName) {
        Drawable drawable;
        try {
            drawable = context.getPackageManager().getApplicationIcon(ApkTempPackageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        }
        return drawable;
    }


    @Override
    public int getItemViewType(int position) {
        return mediaId;
    }

    @Override
    public int getItemCount() {
        if (mediaId == APP_ID) {
            return myAppArrayList.size() < 5 ? myAppArrayList.size() : 5;
        } else if (mediaId == SEARCH_APP_ID) {
            return mediaInfoArrayList.size() <= 6 ? mediaInfoArrayList.size() : 6;
        } else if (mediaId == FILE_ID) {
            return myAppArrayList.size() < 4 ? myAppArrayList.size() : moreFiles ? myAppArrayList.size() : 4;
        } else {
            return myAppArrayList.size() <= 3 ? myAppArrayList.size() : 3;
        }
    }

    public void moreFiles(boolean moreFiles) {
        this.moreFiles = moreFiles;
        notifyDataSetChanged();
    }


    public boolean isMoreFiles() {
        return myAppArrayList.size() > 4 && myAppArrayList.size() < 15;
    }


    class MediaHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mediaName)
        TextView mediaNameTv;
        @BindView(R.id.mediaImage)
        CircleImageView mediaIv;
        @BindView(R.id.callIcon)
        ImageView callImageView;
        @BindView(R.id.chatIcon)
        ImageView chatImageView;

        MediaHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mediaId == CONTACT_ID) {
                mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.phoneColor));
                mediaIv.setImageResource(R.drawable.ic_action_contact_48dp);
                callImageView.setVisibility(View.VISIBLE);
                chatImageView.setVisibility(View.VISIBLE);
            } else if (mediaId == SETTING_ID) {
                mediaIv.setImageResource(R.drawable.ic_action_setting_48dp);
                mediaIv.setCircleBackgroundColor(context.getResources().getColor(R.color.settingColor));
            }
        }
    }


    class AppHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.appNameTv)
        TextView textView;
        @BindView(R.id.appImageView)
        ImageView imageView;

        AppHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mediaId == SEARCH_APP_ID) {
                textView.setVisibility(View.GONE);
                imageView.getLayoutParams().height = dpToPixel();
                imageView.getLayoutParams().width = dpToPixel();
            }

        }
    }


    private int dpToPixel() {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) 32 * density);
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

    private void resultDialog(final String path) {

        View dialogView = View.inflate(context, R.layout.dialog_view, null);
        final AlertDialog builder = new AlertDialog.Builder(context)
                .setTitle("Open As")
                .setView(dialogView)
                .show();
        LinearLayout imageView, audioView, videoView, textView;
        imageView = dialogView.findViewById(R.id.dialogImageView);
        audioView = dialogView.findViewById(R.id.dialogAudioView);
        videoView = dialogView.findViewById(R.id.dialogVideoView);
        textView = dialogView.findViewById(R.id.dialogTextView);

        final File file = new File(path);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaOpen("image/*", file);
                builder.dismiss();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaOpen("video/*", file);
                builder.dismiss();

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaOpen("text/*", file);
                builder.dismiss();

            }
        });

        audioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaOpen("audio/*", file);
                builder.dismiss();

            }
        });
    }


    private void mediaOpen(String type, File file) {
        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, "com.example.ashu.supersearch.fileprovider", file);
        mediaIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mediaIntent.setDataAndType(uri, type);
        context.startActivity(mediaIntent);

    }


    private void searchAppOpen(MediaInfo mediaInfo) {
        Intent intent;
        switch (mediaInfo.getMediaName()) {
            case "Google Play Store":
            case "YouTube":
                intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage(mediaInfo.getMediaPath());
                intent.putExtra("query", searchText);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                break;
            case "Google":
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, searchText);
                break;
            default:
                Uri uri = Uri.parse("http://www.google.com/search?q=" + searchText);
                intent = context.getPackageManager().getLaunchIntentForPackage(mediaInfo.getMediaPath());
                if (intent != null) {
                    intent.setData(uri);
                }
                break;
        }
        context.startActivity(intent);
    }

    private void openFolder(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        intent.setDataAndType(uri, "resource/folder");
        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
            context.startActivity(intent);
        }
    }

    private void showPropDialog(MediaInfo mediaInfo) {
        MyPopDialog newFragment = MyPopDialog.newInstance(mediaInfo);
        Activity activity = (Activity) context;
        newFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "dialog");
    }


    private void showMyPopUpWindow(String mediaName, View view, MediaInfo mediaInfo) {
        MyPopUpWindow myPopUpWindow = new MyPopUpWindow(mediaName, context, mediaInfo);
        myPopUpWindow.showPopUpWindow(view);
    }


}
