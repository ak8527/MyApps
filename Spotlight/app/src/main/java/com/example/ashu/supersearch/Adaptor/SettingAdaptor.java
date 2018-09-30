package com.example.ashu.supersearch.Adaptor;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
        View view = LayoutInflater.from(context).inflate(R.layout.media_list_item, parent, false);
        return (new StorageHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull StorageHolder holder, int position) {
        MediaInfo app = myAppArrayList.get(position);

        String appName = app.getMediaName();
        SpannableString str = new SpannableString(appName);


        String testText = appName.toLowerCase(Locale.getDefault());
        String testTextToBold = spannableText.toLowerCase(Locale.getDefault());
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
        if (myAppArrayList.size() <= 3){
            return myAppArrayList.size();

        } else {
            return 3;
        }
    }

    class StorageHolder extends RecyclerView.ViewHolder {
        final TextView storageNameTv;
        final CircleImageView imageView;

        StorageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mediaImage);
            storageNameTv = itemView.findViewById(R.id.mediaName);
            imageView.setImageResource(R.drawable.ic_action_setting);
            imageView.setCircleBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        }
    }

    public boolean filter(String text) {
        spannableText = text;
        boolean ans = false;
        text = text.toLowerCase(Locale.getDefault());
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
