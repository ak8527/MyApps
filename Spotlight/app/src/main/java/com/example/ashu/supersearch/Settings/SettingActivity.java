package com.example.ashu.supersearch.Settings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ashu.supersearch.R;

public class SettingActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    public static final String CHANNEL_ID = "420YOLO";
    NotificationManager notificationManager;
    LinearLayout assistTv, widgetSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        assistTv = findViewById(R.id.settingAssistView);
        widgetSv = findViewById(R.id.settingWidgetServiceView);

        assistTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS), 0);

            }
        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID,
                            "Default Channel",
                            NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(notificationChannel);


            widgetSv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myForegroundService = new Intent(getBaseContext(), MyWidgetService.class);

//                startService(myService);

                    Log.e("Setting", "onClick: ");
                    ContextCompat.startForegroundService(getBaseContext(), myForegroundService);


                }
            });


        }

        findViewById(R.id.floatingWidgetService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getBaseContext())) {

                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                } else {
                    initializeView();

                }


            }
        });



    }

    private void initializeView() {
//        findViewById(R.id.floatingWidgetService).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                startService(new Intent(SettingActivity.this, MyWidgetService.class));
                finish();
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}



