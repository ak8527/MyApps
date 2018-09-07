package com.example.ashu.supersearch.Adaptor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.StorageHolder> {
    private final Context context;
    private final ArrayList<MediaInfo> storageArrayList;
    private final ArrayList<MediaInfo> mySearchStorageList = new ArrayList<>();
    private String spannableText;
    private boolean moreFiles = false;


    public StorageAdapter(Context context, ArrayList<MediaInfo> storageArrayList) {
        this.context = context;
        this.storageArrayList = storageArrayList;
    }

    @NonNull
    @Override
    public StorageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_list_item,parent,false);
        return (new StorageHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final StorageHolder holder, int position) {
        final MediaInfo storage = mySearchStorageList.get(position);
        String fileName = storage.getMediaName();
        SpannableString str = new SpannableString(fileName);


        String testText = fileName.toLowerCase(Locale.getDefault());
        String testTextToBold = spannableText.toLowerCase(Locale.getDefault());
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.storageNameTv.setText(str);
        final File file = new File(storage.getMediaPath());

        String extension = extensionFind(storage.getMediaName());
        switch (extension) {
            case "image":
                holder.imageView.setImageResource(R.drawable.ic_image_action);
                holder.imageView.setCircleBackgroundColor(context.getResources().getColor(R.color.imageColor));
                break;
            case "text":
                holder.imageView.setImageResource(R.drawable.ic_action_file);
                holder.imageView.setCircleBackgroundColor(context.getResources().getColor(R.color.fileColor));
                break;
            case "video":
                holder.imageView.setImageResource(R.drawable.ic_action_movie);
                holder.imageView.setCircleBackgroundColor(context.getResources().getColor(R.color.movieColor));
                break;
            case "audio":
                holder.imageView.setImageResource(R.drawable.ic_action_song);
                holder.imageView.setCircleBackgroundColor(context.getResources().getColor(R.color.songColor));
                break;
            default:
                holder.imageView.setImageResource(R.drawable.ic_folder_action);
                holder.imageView.setCircleBackgroundColor(context.getResources().getColor(R.color.folderColor));

                break;
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.isFile()){
                    resultDialog(storage.getMediaPath());

                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(storage.getMediaPath());
                    intent.setDataAndType(uri,"*/*");
                    context.startActivity(intent);

                }


            }
        });



    }

    public void moreFiles(boolean moreFiles){
        this.moreFiles = moreFiles;
        notifyDataSetChanged();
        }


        public boolean isMoreFiles(){
            return mySearchStorageList.size() > 4;
        }

    @Override
    public int getItemCount() {
        if (mySearchStorageList.size() < 4){
            return mySearchStorageList.size();
        } else {
           if (moreFiles){
               return mySearchStorageList.size();
           } else {
               return 4;
           }

        }

    }


    public boolean filter(String text) {
        spannableText = text;
        boolean ans = false;
        text = text.toLowerCase(Locale.getDefault());
        mySearchStorageList.clear();
        if (!text.isEmpty()) {

            for (MediaInfo storage : storageArrayList) {
                if (storage.getMediaName().toLowerCase(Locale.getDefault()).contains(text)) {
                    mySearchStorageList.add(storage);
                    ans = true;
                }
            }

            notifyDataSetChanged();
        }
        return ans;
    }

    private void resultDialog(final String file){

        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_view, null,true);
        new AlertDialog.Builder(context)
                .setTitle("Open As")
                .setView(dialogView)
                .show();
        TextView imageView , audioView, videoView, textView;
        imageView = dialogView.findViewById(R.id.dialogImageView);
        audioView = dialogView.findViewById(R.id.dialogAudioView);
        videoView = dialogView.findViewById(R.id.dialogVideoView);
        textView = dialogView.findViewById(R.id.dialogTextView);

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        final Uri uri = Uri.parse(file);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setDataAndType(uri,"image/*");
                context.startActivity(intent);
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setDataAndType(uri,"video/*");
                context.startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setDataAndType(uri,"text/*");
                context.startActivity(intent);
            }
        });

        audioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setDataAndType(uri,"audio/mp3");
                context.startActivity(intent);


            }
        });
    }

        class StorageHolder extends RecyclerView.ViewHolder {
            final TextView storageNameTv;
            final CircleImageView imageView;

            StorageHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.mediaImage);
                storageNameTv = itemView.findViewById(R.id.mediaName);
            }
        }

     private String extensionFind(String text){
         String[] image = new String[]{".jpg",".jpeg",".png",".gif"};
        String[] video = new String[]{".mkv",".mp4",".3gp",".avi"};
        String[] audio = new String[]{".mp3",".aac",".wav",".wma",".ogg"};
        String[] doc = new String[]{".txt",".doc",".pdf",".docs",".xls"};

        for (String extension : image){
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "image";
        }

        for (String extension : video){
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "video";
        }

        for (String extension : doc){
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "text";
        }

        for (String extension : audio){
            if (text.toLowerCase(Locale.getDefault()).endsWith(extension))
                return "audio";
        }

        return "empty";
     }


}
