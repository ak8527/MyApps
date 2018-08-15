package com.example.ashu.supersearch.Movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.ashu.supersearch.R;

import java.util.ArrayList;
import java.util.Locale;

public class VideoAdaptor extends RecyclerView.Adapter<VideoAdaptor.VideoHolder> {
    private final Context context;
    private final ArrayList<Video> videoArrayList;
    private final ArrayList<Video> myVideoList = new ArrayList<>();
    private String spannableText;

    public VideoAdaptor(Context context, ArrayList<Video> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item,parent,false);
        return (new VideoHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = myVideoList.get(position);

        String videoName = video.getVideoName();
        SpannableString str = new SpannableString(videoName);


        String testText = videoName.toLowerCase(Locale.US);
        String testTextToBold = spannableText.toLowerCase(Locale.US);
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.nameTv.setText(str);
        Glide.with(context)
                .asBitmap()
                .load(video.getVideoPath())
                .into(holder.imageView);

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
        final ImageView imageView;
        VideoHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.videoNameTv);
            imageView = itemView.findViewById(R.id.videoImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Video video = myVideoList.get(getAdapterPosition());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(video.getVideoPath()),"video/*");
                    context.startActivity(intent);
                }
            });
        }
    }

    public boolean filter(String text) {
        spannableText = text;
        boolean ans = false;
        text = text.toLowerCase();
        myVideoList.clear();
        if (!text.isEmpty()) {
            for (Video wp : videoArrayList) {
                if (wp.getVideoName().toLowerCase(Locale.getDefault()).contains(text)) {
                    myVideoList.add(wp);
                    ans = true;
                }
            }

            notifyDataSetChanged();
        }
        return ans;
    }
}
