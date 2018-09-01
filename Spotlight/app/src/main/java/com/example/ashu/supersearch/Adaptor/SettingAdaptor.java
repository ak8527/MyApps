package com.example.ashu.supersearch.Adaptor;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingAdaptor extends RecyclerView.Adapter<SettingAdaptor.StorageHolder> {
    private final Context context;
    private final ArrayList<MediaInfo> appArrayList;
    private final ArrayList<MediaInfo> myAppArrayList = new ArrayList<>();
    private String spannableText;

    public SettingAdaptor(Context context, ArrayList<MediaInfo> appArrayList) {
        this.context = context;
        this.appArrayList = appArrayList;
    }


    @NonNull
    @Override
    public StorageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.storage_list_item, parent, false);
        return (new StorageHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull StorageHolder holder, int position) {
        MediaInfo app = myAppArrayList.get(position);

        String appName = app.getMediaName();
        SpannableString str = new SpannableString(appName);


        String testText = appName.toLowerCase(Locale.US);
        String testTextToBold = spannableText.toLowerCase(Locale.US);
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        holder.imageView.setImageResource(R.drawable.ic_action_setting);
        Drawable drawable = null;
        try {
            drawable = context.getPackageManager().getApplicationIcon("com.android.settings");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageDrawable(drawable);
       // holder.imageView.setCircleBackgroundColor(R.color.colorPrimary);\
        holder.storageNameTv.setText(str);
        final String packageName = app.getMediaPath();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(packageName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (myAppArrayList.size() <= 4){
            return myAppArrayList.size();

        } else {
            return 5;
        }
    }

    class StorageHolder extends RecyclerView.ViewHolder {
        final TextView storageNameTv;
        final CircleImageView imageView;

        StorageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.storageImageView);
            storageNameTv = itemView.findViewById(R.id.searchStorageTv);
        }
    }

    public boolean filter(String text) {
        spannableText = text;
        boolean ans = false;
        text = text.toLowerCase();
        myAppArrayList.clear();
        if (!text.isEmpty()) {
            for (MediaInfo app : appArrayList) {
                if (app.getMediaName().toLowerCase(Locale.getDefault()).contains(text)) {
                    myAppArrayList.add(app);
                    ans = true;
                }
            }
            notifyDataSetChanged();
        }
        return ans;
    }
}
