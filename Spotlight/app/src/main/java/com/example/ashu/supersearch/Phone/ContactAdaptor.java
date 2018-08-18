package com.example.ashu.supersearch.Phone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashu.supersearch.Media.MediaInfo;
import com.example.ashu.supersearch.R;

import java.util.ArrayList;
import java.util.Locale;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ContactHolder> {
    private final Context context;
    private final ArrayList<MediaInfo> contactLists;
    private final ArrayList<MediaInfo> myContactList = new ArrayList<>();
    private String spannableText;
    private String phoneNumber;
    public static final int MY_TELEPHONE_REQUEST_CODE = 111;



    public ContactAdaptor(Context context, ArrayList<MediaInfo> contactLists) {
        this.context = context;
        this.contactLists = contactLists;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item,parent,false);
        return (new ContactHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        MediaInfo contactList = myContactList.get(position);

        String contactName = contactList.getMediaName();
        SpannableString str = new SpannableString(contactName);


        String testText = contactName.toLowerCase(Locale.US);
        String testTextToBold = spannableText.toLowerCase(Locale.US);
        int startingIndex = testText.indexOf(testTextToBold);
        int endingIndex = startingIndex + testTextToBold.length();


        str.setSpan(new StyleSpan(Typeface.BOLD),startingIndex,endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.nameTv.setText(str);
        phoneNumber = contactList.getMediaPath();

        holder.chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                //noinspection SpellCheckingInspection
                intent.setData(Uri.parse("smsto:"+Uri.encode(phoneNumber)));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
        });
        holder.callImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        Log.e("If", "onClick: " );
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        context.startActivity(intent);

                    } else {
                        Log.e("Else", "onClick: " );
                        requestPermissions(
                                (Activity) context, new String[]{Manifest.permission.CALL_PHONE},MY_TELEPHONE_REQUEST_CODE);


                }


            }
        });




    }




    @SuppressLint("MissingPermission")
    public void calling(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (myContactList.size() <= 3)
           return myContactList.size();
        else
            return 3;
    }

    public boolean filter(String text) {
        Log.e("Filter", "filter: "+ text );
        boolean ans = false;
        spannableText = text;
        text = text.toLowerCase();
        myContactList.clear();
        if (!text.isEmpty()) {
            for (MediaInfo contact : contactLists) {
                if (contact.getMediaName().toLowerCase(Locale.getDefault()).contains(text)) {
                    myContactList.add(contact);
                    ans = true;
                }
            }

            notifyDataSetChanged();

        }
        return ans;
    }


    class ContactHolder extends RecyclerView.ViewHolder{
        final TextView nameTv;
        final ImageView callImageView;
        final ImageView chatImageView;

        ContactHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.contactName);
            callImageView = itemView.findViewById(R.id.callIcon);
            chatImageView = itemView.findViewById(R.id.chatIcon);


        }
    }

}
