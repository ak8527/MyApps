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

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.MyDialog.PopUpWindow;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongAdaptor extends RecyclerView.Adapter<SongAdaptor.SongHolder> {

    private final Context context;
    private final ArrayList<MediaInfo> songArrayList;
    private final ArrayList<MediaInfo> mySongList = new ArrayList<>();
    private String spannableText;

    public SongAdaptor(Context context, ArrayList<MediaInfo> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_list_item, parent, false);
        return (new SongHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder holder, int position) {
        final MediaInfo song = mySongList.get(position);

        String songName = song.getMediaName();
        SpannableString str = new SpannableString(songName);


        String testText = songName.toLowerCase(Locale.getDefault());
        String testTextToBold = spannableText.toLowerCase(Locale.getDefault());
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.songTv.setText(str);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                File file = new File(song.getMediaPath());
                Uri uri = FileProvider.getUriForFile(context,"com.example.ashu.supersearch.fileprovider",file);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sharingIntent.setDataAndType(uri,"audio/*");
                context.startActivity(sharingIntent);


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopUpWindow popUpWindow = new PopUpWindow("Media",v,context,song);
                popUpWindow.showPopUpWindow();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mySongList.size() > 3){
            return 3;
            }
        else{
            return mySongList.size();
        }
    }

    class SongHolder extends RecyclerView.ViewHolder {
        final TextView songTv;
        final CircleImageView songImageView;

        SongHolder(View itemView) {
            super(itemView);
            songTv = itemView.findViewById(R.id.mediaName);
            songImageView = itemView.findViewById(R.id.mediaImage);
            songImageView.setImageResource(R.drawable.ic_action_song);
            songImageView.setCircleBackgroundColor(context.getResources().getColor(R.color.audioColor));

        }
    }

    public boolean filter(String text) {
        boolean ans = false;
        spannableText = text;
        text = text.toLowerCase(Locale.getDefault());
        mySongList.clear();
        if (!text.isEmpty()) {
            for (MediaInfo song : songArrayList) {
                if (song.getMediaName().toLowerCase(Locale.getDefault()).contains(text)) {
                    mySongList.add(song);
                    ans = true;
                }
            }

            notifyDataSetChanged();
        }
        return ans;
    }


}
