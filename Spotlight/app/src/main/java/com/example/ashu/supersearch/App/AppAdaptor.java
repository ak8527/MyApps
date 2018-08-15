package com.example.ashu.supersearch.App;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashu.supersearch.R;

import java.util.ArrayList;
import java.util.Locale;

public class AppAdaptor extends RecyclerView.Adapter<AppAdaptor.AppHolder> {
    private final Context context;
    private final ArrayList<App> appArrayList;
    private final ArrayList<App> myAppArrayList = new ArrayList<>();
    private String spannableText;

    public AppAdaptor(Context context, ArrayList<App> appArrayList) {
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
        App app = myAppArrayList.get(position);

        String appName = app.getAppName();
        SpannableString str = new SpannableString(appName);


        String testText = appName.toLowerCase(Locale.US);
        String testTextToBold = spannableText.toLowerCase(Locale.US);
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.imageView.setImageDrawable(app.getIcon());
        holder.textView.setText(str);
        final String packageName = app.getPackageName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
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
        text = text.toLowerCase();
        myAppArrayList.clear();
        if (!text.isEmpty()) {
            for (App wp : appArrayList) {
                if (wp.getAppName().toLowerCase(Locale.getDefault()).contains(text)) {
                    myAppArrayList.add(wp);
                    ans = true;
                }
            }
            notifyDataSetChanged();
        }
        return ans;
    }
}
