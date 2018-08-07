package com.example.ashu.pokedex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView imageView,searchImageView;
    TextView nameTextView,rankTextView,nextView,prevView;
    LinearLayout linearLayout;
    static int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        button = findViewById(R.id.idBtn);
//        editText = findViewById(R.id.etText);
        imageView = findViewById(R.id.imageView);
        linearLayout = findViewById(R.id.linearLayout);
        nameTextView = findViewById(R.id.nameTv);
        rankTextView = findViewById(R.id.rankView);
        nextView = findViewById(R.id.nextBtn);
        prevView = findViewById(R.id.prevBtn);
        searchImageView = findViewById(R.id.searchBtn);


        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_main,null,true);

        final AlertDialog customDialog = new AlertDialog.Builder(this)
                .setTitle("Enter Pokemon Id : ")
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editText = view.findViewById(R.id.editIdView);
                        String dId = editText.getText().toString();

                        if(dId.isEmpty()){
                            Toast.makeText(getBaseContext(),"Try Again!!!",Toast.LENGTH_SHORT).show();
                        } else {
                            id = Integer.parseInt(dId);
                            if(id>=1 && id<= 802) {
                                makeNetworkCall("http://pokeapi.co/api/v2/pokemon/" + id + "/");
                                dialog.dismiss();
                            } else{
                                Toast.makeText(getBaseContext(),"Wrong Input, Try Again!!!",Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();


        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewDialog alert = new ViewDialog();
//                alert.showDialog(   MainActivity.this);
                customDialog.show();

            }
        });

        makeNetworkCall("http://pokeapi.co/api/v2/pokemon/"+id+"/");
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Please Wait...",Toast.LENGTH_SHORT).show();

                if(id >= 802){
                    String s = "You have Reach the End.";
                    Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                    id = 1;
                }else {
                    id++;
                    makeNetworkCall("http://pokeapi.co/api/v2/pokemon/"+id+"/");

                }
            }
        });

        prevView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Please Wait...",Toast.LENGTH_SHORT).show();


                if(id <= 1){
                    String s = "You have Reach the Start.";
                    Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                    id = 1;
                }else {
                    id--;
                    makeNetworkCall("http://pokeapi.co/api/v2/pokemon/"+id+"/");

                }


            }
        });

    }


    public void makeNetworkCall(String url){

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {



                final String apiResponse = response.body().string();
                Gson gson = new Gson();
                final Result result = gson.fromJson(apiResponse,Result.class);
                Log.e("ONRESPONSE", "onResponse: "+result.getName() );



                (MainActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Result ", "onResponse: "+result.getTypes().get(0).getType().getName());
                        nameTextView.setText(result.getName());
                        Picasso.get()
                                .load(result.getSprites().getImageUrl())
                                .resize(800,600)
                                .centerCrop()
                                .into(imageView);
                        int stringId = Integer.parseInt(result.getId());
                        String hash;
                       if (stringId >=1 && stringId <=9)
                           hash = "#00";
                       else if(stringId >=10 && stringId <=99)
                           hash = "#0";
                       else
                           hash = "#";



                        rankTextView.setText(hash+result.getId());
                        nameTextView.setText(result.getName());
                        nameTextView.setAllCaps(true);
                        id = Integer.parseInt(result.getId());

                        for(int i = 0;i<result.getMoves().size();i++){
                            Log.e("Move "+i, "run: "+result.getMoves().get(i).getMove().getName());
                        }

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(),SecondActivity.class);
                                intent.putExtra("Response",apiResponse);
                                startActivity(intent);
                            }
                        });

                    }
                });

            }
        });

    }
}
