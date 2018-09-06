package com.example.ashu.supersearch.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.util.ArrayList;
import java.util.Locale;

public class AppAdaptor extends RecyclerView.Adapter<AppAdaptor.AppHolder> {
    private final Context context;
    private final ArrayList<MediaInfo> appArrayList;
    private final ArrayList<MediaInfo> myAppArrayList = new ArrayList<>();
    private String spannableText;

    public AppAdaptor(Context context, ArrayList<MediaInfo> appArrayList) {
        this.context = context;
        this.appArrayList = appArrayList;
    }


    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_list_item, parent, false);
        return (new AppHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder holder, int position) {
            MediaInfo app = myAppArrayList.get(position);

            String appName = app.getMediaName();
            SpannableString str = new SpannableString(appName);


            String testText = appName.toLowerCase(Locale.getDefault());
            String testTextToBold = spannableText.toLowerCase(Locale.getDefault());
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();


            str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.imageView.setImageDrawable(getAppIconByPackageName(app.getMediaPath()));
            holder.textView.setText(str);
            final String packageName = app.getMediaPath();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    context.startActivity(intent);
                }
            });



    }

    private Drawable getAppIconByPackageName(String ApkTempPackageName){

        Drawable drawable;

        try{
            drawable = context.getPackageManager().getApplicationIcon(ApkTempPackageName);

        }
        catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        }
        return drawable;
    }



    @Override
    public int getItemCount() {
        if (myAppArrayList.size() <= 4){
            return myAppArrayList.size();

        } else {
            return 5;
        }
    }

    class AppHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final ImageView imageView;

        AppHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.appNameTv);
            imageView = itemView.findViewById(R.id.appImageView);
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
