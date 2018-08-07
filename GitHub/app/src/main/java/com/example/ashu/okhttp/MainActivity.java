package com.example.ashu.okhttp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Items> itemsArrayList;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView =findViewById(R.id.tapImage);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view,null,true);
        final AlertDialog customDialog = new AlertDialog.Builder(this)
                .setTitle("Enter Name : ")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText dialogEt = dialogView.findViewById(R.id.dialogEditText);
                        String userSearch = dialogEt.getText().toString();
                        makeNetworkCall("https://api.github.com/search/users?q="+userSearch);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        FloatingActionButton fabBtn = findViewById(R.id.searchBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
            }
        });


    }

    private void makeNetworkCall(String url) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final Result result1 = gson.fromJson(result, Result.class);
                itemsArrayList = result1.getArrayList();

                (MainActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (itemsArrayList != null){
                            mTextView.setVisibility(View.GONE);
                        }
                      recyclerView = findViewById(R.id.rvView);
                      recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
                      recyclerView.setAdapter(new UserAdapter(getBaseContext(),itemsArrayList));

                    }
                });

            }
        });

    }
}
