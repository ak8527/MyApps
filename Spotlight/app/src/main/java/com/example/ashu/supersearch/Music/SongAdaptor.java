package com.example.ashu.supersearch.Music;

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
import android.widget.TextView;

import com.example.ashu.supersearch.R;

import java.util.ArrayList;
import java.util.Locale;

public class SongAdaptor extends RecyclerView.Adapter<SongAdaptor.SongHolder> {

    Context context;
    ArrayList<Song> songArrayList;
    ArrayList<Song> mySongList = new ArrayList<>();
    String spannableText;

    public SongAdaptor(Context context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_item, parent, false);
        return (new SongHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        final Song song = mySongList.get(position);

        String songName = song.getSongName();
        SpannableString str = new SpannableString(songName);


        String testText = songName.toLowerCase(Locale.US);
        String testTextToBold = spannableText.toLowerCase(Locale.US);
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.songTv.setText(str);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(song.getSongPath()), "audio/*");
                context.startActivity(intent);


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
        TextView songTv;

        public SongHolder(View itemView) {
            super(itemView);
            songTv = itemView.findViewById(R.id.songNameTv);
        }
    }

    public boolean filter(String text) {
        boolean ans = false;
        spannableText = text;
        text = text.toLowerCase();
        mySongList.clear();
        if (!text.isEmpty()) {
            for (Song wp : songArrayList) {
                if (wp.getSongName().toLowerCase(Locale.getDefault()).contains(text)) {
                    mySongList.add(wp);
                    ans = true;
                }
            }

            notifyDataSetChanged();
        }
        return ans;
    }
}
