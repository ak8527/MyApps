package com.example.ashu.supersearch.setting;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ashu.supersearch.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Intent intent;


    public static final String CHANNEL_ID = "420";

    public static final String MY_SETTING_PREF = "setting_pref";

    @BindView(R.id.searchAnyWhereSwitch) Switch searchAnyWhereSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences(MY_SETTING_PREF,MODE_PRIVATE);
        String switchValue = sharedPreferences.getString("background_switch",null);
        if (switchValue != null && switchValue.equals("true")) {
            searchAnyWhereSwitch.setChecked(true);
        }
//        SharedPreferences.Editor
//        LinearLayout assistTv = findViewById(R.id.settingAssistView);
//        LinearLayout widgetSv = findViewById(R.id.settingWidgetServiceView);

//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel =
//                    new NotificationChannel(CHANNEL_ID,
//                            "Default Channel",
//                            NotificationManager.IMPORTANCE_DEFAULT);
//
//            Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);
//
//
//            widgetSv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent myForegroundService = new Intent(getBaseContext(), MyWidgetService.class);
//                    ContextCompat.startForegroundService(getBaseContext(), myForegroundService);
//
//
//                }
//            });
//
//
//        }

//        findViewById(R.id.tapAnySetting).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getBaseContext())) {
//
//                    //If the draw over permission is not available open the settings screen
//                    //to grant the permission.
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
//                } else {
//                    initializeView();
//
//                }
//
//
//            }
//        });



    }

    @OnClick(R.id.searchAnyWhereSwitch)
    public void switchCheck(){
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_SETTING_PREF,MODE_PRIVATE).edit();
            if (searchAnyWhereSwitch.isChecked() ){
                editor.putString("background_switch", String.valueOf(searchAnyWhereSwitch.isChecked()));
                drawOverAppPermission();
            } else {
                editor.putString("background_switch", String.valueOf(searchAnyWhereSwitch.isChecked()));
                Log.e("SettingActivity", "switchCheck: " +  String.valueOf(searchAnyWhereSwitch.isChecked()));
                stopService(new Intent(SettingActivity.this,MyWidgetService.class));
            }
            editor.apply();

    }

    public void drawOverAppPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getBaseContext())) {

            intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();

        }

    }
    private void initializeView() {
                startService(new Intent(SettingActivity.this, MyWidgetService.class));
                finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == Activity.RESULT_OK) {
                Log.e("SettingActivity", "onActivityResult: " + "Success");
                initializeView();
            } else { //Permission is not available

                Log.e("SettingActivity", "onActivityResult: " + "Failure " + resultCode );

                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {

        }
    }

    @OnClick(R.id.settingAssistView)
     public void setAssistApp(){
        startActivityForResult(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS), 0);
    }



}



