package com.example.ashu.okhttp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    public Context context;
    public ArrayList<Items> itemsArrayList;

    public UserAdapter(Context context, ArrayList<Items> itemsArrayList) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        return (new UserHolder(view));

    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        final Items items = itemsArrayList.get(position);
        holder.textView.setText(items.getmLogin());
        Picasso
                .get()
                .load(items.getmImageUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(items.getmProfileUrl()));
                context.startActivity(i);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SecondActivity.class);
                intent.putExtra("Url",items.getmProfileUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }


    class UserHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView textView;

        public UserHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.userImage);
            textView = itemView.findViewById(R.id.userName);

        }


    }
}
