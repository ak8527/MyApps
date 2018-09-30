package com.example.ashu.supersearch.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdaptor extends RecyclerView.Adapter<VideoAdaptor.VideoHolder> {
    private final Context context;
    private final ArrayList<MediaInfo> videoArrayList;
    private final ArrayList<MediaInfo> myVideoList = new ArrayList<>();
    private String spannableText;

    public VideoAdaptor(Context context, ArrayList<MediaInfo> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_list_item,parent,false);
        return (new VideoHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        final MediaInfo video = myVideoList.get(position);

        String videoName = video.getMediaName();
        SpannableString str = new SpannableString(videoName);


        String testText = videoName.toLowerCase(Locale.getDefault());
        String testTextToBold = spannableText.toLowerCase(Locale.getDefault());
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.nameTv.setText(str);
        Glide.with(context)
                .asBitmap()
                .load(video.getMediaPath())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                File file = new File(video.getMediaPath());
                Uri uri = FileProvider.getUriForFile(context,"com.example.ashu.supersearch.fileprovider",file);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sharingIntent.setDataAndType(uri,"video/*");
                context.startActivity(sharingIntent);

            }
        });


        }

    @Override
    public int getItemCount() {
        if (myVideoList.size() <= 3){
            return myVideoList.size();
        } else {
            return 3;
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        final TextView nameTv;
        final CircleImageView imageView;
        VideoHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.mediaName);
            imageView = itemView.findViewById(R.id.mediaImage);

        }
    }

    public boolean filter(String text) {
        spannableText = text;
        boolean ans = false;
        text = text.toLowerCase(Locale.getDefault());
        myVideoList.clear();
        if (!text.isEmpty()) {
            for (MediaInfo video : videoArrayList) {
                if (video.getMediaName().toLowerCase(Locale.getDefault()).contains(text)) {
                    myVideoList.add(video);
                    ans = true;
                }
            }

            notifyDataSetChanged();
        }
        return ans;
    }
}
