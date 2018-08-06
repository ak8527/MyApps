package com.example.ashu.supersearch.Settings;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;

import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.R;

public class MyWidgetService extends Service {
    private WindowManager mWindowManager;


    private View mChatHeadView;


    public MyWidgetService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent stopForeground = new Intent(this, MainActivity.class);
        stopForeground.putExtra("KEY", true);

        PendingIntent pi = PendingIntent.getActivity(this,
                333,
                stopForeground,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // When the onStartCommand was called the second time,
        // i.e. when the user tapped on the stop button
        if (intent.hasExtra("KEY")) {
            startActivity(intent);
        }

        Notification notification = new NotificationCompat.Builder(this, SettingActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Spotlight")
                .setContentText("Spotlight is running.")
                .setAutoCancel(true)
                .setContentIntent(pi)
                .build();

        startForeground(420, notification);

        return super.onStartCommand(intent, flags, startId);

    }

}
