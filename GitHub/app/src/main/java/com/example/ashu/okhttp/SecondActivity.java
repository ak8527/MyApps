package com.example.ashu.okhttp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {
    TextView nameView,loginView,locationView,blogView,bioView,companyView;
    CircleImageView userImageView;
    Items items;
    TextView hBlogView,hLocationView,hCompanyView,hBioView;
    View drawerLocationView,drawerComapnyView,drawerBioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        nameView = findViewById(R.id.nameView);
        loginView = findViewById(R.id.loginView);
        locationView = findViewById(R.id.locationView);
        blogView = findViewById(R.id.blogView);
        bioView = findViewById(R.id.bioView);
        companyView = findViewById(R.id.companyView);
        userImageView = findViewById(R.id.userImageView);


        hBlogView = findViewById(R.id.helperBlogView);
        hLocationView =findViewById(R.id.helperLocationView);
        hCompanyView = findViewById(R.id.helperCompanyView);
        hBioView = findViewById(R.id.helperBioView);

        drawerLocationView = findViewById(R.id.drawerLocationView);
        drawerComapnyView = findViewById(R.id.helperCompanyDrawerView);
        drawerBioView = findViewById(R.id.helperBioDrawerView);

        Intent i = getIntent();
        String url = i.getStringExtra("Url");
        Log.e("Url", "onCreate: "+url );
        makeHttpRequest(url);
        FloatingActionButton floatingActionButton = findViewById(R.id.fabBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(items.getmHtmlUrl()));
                startActivity(intent);
            }
        });
    }

    void makeHttpRequest(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String apiResult = response.body().string();
                Gson gson = new Gson();
                items = gson.fromJson(apiResult,Items.class);


                (SecondActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        nameView.setText(items.getmName());
                        Picasso.get()
                                .load(items.getmImageUrl())
                                .into(userImageView);
                        loginView.setText(items.getmLogin());
                        if((items.getmBlog()).isEmpty()) {
                            Log.e("Blog", "run: ");
                            blogView.setVisibility(View.GONE);
                            drawerBioView.setVisibility(View.GONE);
                            hBlogView.setVisibility(View.GONE);

                        } else {
                            blogView.setText(items.getmBlog());
                        }

                        if((items.getmBio()) == null){
                            bioView.setVisibility(View.GONE);
                            drawerBioView.setVisibility(View.GONE);
                            hBioView.setVisibility(View.GONE);
                        } else {
                            bioView.setText(items.getmBio());

                        }

                        if ((items.getmLocation()) == null){
                            locationView.setVisibility(View.GONE);
                            hLocationView.setVisibility(View.GONE);
                            drawerLocationView.setVisibility(View.GONE);
                        }else {
                            locationView.setText(items.getmLocation());

                        }

                        if ((items.getmCompany()) == null){
                            companyView.setVisibility(View.GONE);
                            hCompanyView.setVisibility(View.GONE);
                            drawerComapnyView.setVisibility(View.GONE);
                        } else {
                            companyView.setText(items.getmCompany());

                        }

                    }
                });


            }
        });
    }
}
