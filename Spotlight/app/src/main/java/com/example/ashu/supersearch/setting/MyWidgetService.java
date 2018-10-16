package com.example.ashu.supersearch.setting;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.R;

import butterknife.ButterKnife;

import static com.example.ashu.supersearch.setting.SettingActivity.CHANNEL_ID;

public class MyWidgetService extends Service implements View.OnTouchListener {

    private View myWidgetView;
    private WindowManager.LayoutParams params;
    private WindowManager myWidgetWindow;
    private GestureDetector gestureDetector;



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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("Super Search")
                .setContentText("Super Search is running.")
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .build();

        startForeground(420, notification);

        return super.onStartCommand(intent, flags, startId);

    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

    }



    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        myWidgetView = LayoutInflater.from(this).inflate(R.layout.floating_box,null);
        ButterKnife.bind(this,myWidgetView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;



        //Add the view to the window
        myWidgetWindow = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (myWidgetWindow != null) {
            myWidgetWindow.addView(myWidgetView, params);
        }



        myWidgetView.findViewById(R.id.floatingBox).setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());




    }


    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;



    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();

                    return true;

                case MotionEvent.ACTION_UP:

                    view.performClick();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);

                    myWidgetWindow.updateViewLayout(myWidgetView, params);
                    return true;


            }
        }


        return false;
    }




    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myWidgetView != null){
            myWidgetWindow.removeView(myWidgetView);
        }
    }
}
