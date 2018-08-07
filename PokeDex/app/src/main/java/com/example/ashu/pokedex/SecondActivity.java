package com.example.ashu.pokedex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondActivity extends AppCompatActivity {
    Result iResult;
    TextView nameTv, rankTv,weightTv,heightTv,typeTv,ability1view,ability2view,baseView;
    TextView hpTv,sdTv,saTv,dTv,aTv,speedTv;
    CircleImageView sImageView;
    String apiString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        nameTv = findViewById(R.id.sNameView);
        rankTv = findViewById(R.id.rView);
        sImageView = findViewById(R.id.sImageView);
        weightTv = findViewById(R.id.weightView);
        heightTv = findViewById(R.id.heightView);
        typeTv = findViewById(R.id.typeView);
        baseView = findViewById(R.id.baseView);
        ability1view = findViewById(R.id.abilityView1);
        ability2view = findViewById(R.id.abilityView2);
        hpTv = findViewById(R.id.hpView);
        sdTv = findViewById(R.id.spDefenceView);
        saTv = findViewById(R.id.spAttackView);
        dTv = findViewById(R.id.defenceView);
        aTv = findViewById(R.id.attackView);
        speedTv = findViewById(R.id.speedView);

        Toast.makeText(getBaseContext(),"Please Wait...",Toast.LENGTH_SHORT).show();



        Intent i  = getIntent();
        if (i != null) {
            apiString = i.getStringExtra("Response");
        }
        Gson gson = new Gson();
        iResult = gson.fromJson(apiString,Result.class);

        Log.e("Result ", "onCreate: "+ iResult.getName());
        int stringId = Integer.parseInt(iResult.getId());
        String hash;
        if (stringId >=1 && stringId <=9)
            hash = "#00";
        else if(stringId >=10 && stringId <=99)
            hash = "#0";
        else
            hash = "#";


        FloatingActionButton fb = findViewById(R.id.fabBtn);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String s = "https://bulbapedia.bulbagarden.net/wiki/"+iResult.getName()+"_(Pok%C3%A9mon)";
                i.setData(Uri.parse(s));
                startActivity(i);
            }
        });





        nameTv.setText(iResult.getName());
        String rank = hash + iResult.getId();
        rankTv.setText(rank);
        Picasso.get()
                .load(iResult.getSprites().getImageUrl())
                .resize(300,400)
                .centerCrop()
                .into(sImageView);

        String type = "";
        for (int j =0;j<iResult.getTypes().size();j++){
            type = type +  iResult.getTypes().get(j).getType().getName() + " ";
        }
        typeTv.setText(type);

        double convertWeight = (Double.parseDouble(iResult.getWeight()))/10;
        double lbsWeight1 = convertWeight*2.20;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        String lbsWeight = decimalFormat.format(lbsWeight1);
        String setWeight = lbsWeight + " lbs  ("+convertWeight+" kg)";


        weightTv.setText(setWeight);

        double height = (Double.parseDouble(iResult.getHeight()))/10;
        String setHeight = height + " m";

        heightTv.setText(setHeight);

        baseView.setText(iResult.getBaseExperience());

        ability1view.setText(iResult.getAbilities().get(0).getAbility().getName());


        if(!(iResult.getAbilities().get(1).getAbility().getName()).isEmpty()){
            ability2view.setText(iResult.getAbilities().get(1).getAbility().getName());

        } else {
            ability2view.setVisibility(View.GONE);
        }

        speedTv.setText(iResult.getStats().get(0).getBaseStat());
        sdTv.setText(iResult.getStats().get(1).getBaseStat());
        saTv.setText(iResult.getStats().get(2).getBaseStat());
        dTv.setText(iResult.getStats().get(3).getBaseStat());
        aTv.setText(iResult.getStats().get(4).getBaseStat());
        hpTv.setText(iResult.getStats().get(5).getBaseStat());









        }




}
