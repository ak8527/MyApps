package com.example.ashu.supersearch.Settings;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.R;

public class MyWidgetService extends Service {
    private WindowManager mWindowManager;


    private View floatingWidgetView;


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




    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created
        floatingWidgetView = LayoutInflater.from(this).inflate(R.layout.floating_box, null);

        //Add the view to the window.
        WindowManager.LayoutParams params ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Specify the chat head position
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;        //Initially view will be added to top-left corner
//        params.x = 0;
//        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (mWindowManager != null) {
            mWindowManager.addView(floatingWidgetView, params);
        }


        //Drag and move chat head using user's touch action.
        final ImageView chatHeadImage = floatingWidgetView.findViewById(R.id.floatingBox);
        chatHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWidgetService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingWidgetView != null) mWindowManager.removeView(floatingWidgetView);
    }

}
