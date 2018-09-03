package com.example.ashu.supersearch.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.util.ArrayList;

public class SearchAppAdaptor extends RecyclerView.Adapter<SearchAppAdaptor.AppHolder> {
    private final Context context;
    private final ArrayList<MediaInfo> appArrayList;
    private String searchText = "";


    public SearchAppAdaptor(Context context, ArrayList<MediaInfo> appArrayList) {
        this.context = context;
        this.appArrayList = appArrayList;
    }
    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_list_item, parent, false);
        return (new SearchAppAdaptor.AppHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final AppHolder holder, int position) {
        final MediaInfo app = appArrayList.get(position);
        holder.textView.setVisibility(View.GONE);
        Drawable drawable = null;
        try {
            drawable = context.getPackageManager().getApplicationIcon(app.getMediaPath());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String appPackageName = app.getMediaPath();
       // holder.imageView.getLayoutParams().height = 32;
        holder.imageView.setImageDrawable(drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (app.getMediaName().equals("Google Play Store") || app.getMediaName().equals("YouTube")){

                    intent = new Intent(Intent.ACTION_SEARCH);
                    intent.setPackage(appPackageName);
                    intent.putExtra("query", searchText);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                } else {
                    Uri uri = Uri.parse("http://www.google.com/#q=" + searchText);
                    intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
                    if (intent != null) {
                        intent.setData(uri);
                    }


                }
                context.startActivity(intent);
            }
        });

    }

    public void filter(String text) {
        searchText = text;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (appArrayList.size() <= 6){
            return appArrayList.size();

        } else {
            return 6;
        }
    }

    class AppHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final ImageView imageView;

        AppHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.appNameTv);

            imageView = itemView.findViewById(R.id.appImageView);
            imageView.getLayoutParams().height = dpToPixel(32);
            imageView.getLayoutParams().width = dpToPixel(32);
        }
    }

    private int dpToPixel(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }


}
