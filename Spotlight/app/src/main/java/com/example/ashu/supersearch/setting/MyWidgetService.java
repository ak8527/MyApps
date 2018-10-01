package com.example.ashu.supersearch.setting;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.R;

import static com.example.ashu.supersearch.setting.SettingActivity.CHANNEL_ID;

public class MyWidgetService extends Service implements View.OnTouchListener {

    View myWidgetView;
    WindowManager.LayoutParams params;
    WindowManager myWidgetWindow;

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

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Spotlight")
                .setContentText("Spotlight is running.")
                .setAutoCancel(true)
                .setContentIntent(pi)
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        //Specify the chat head position
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;


        //Add the view to the window
        myWidgetWindow = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (myWidgetWindow != null) {
            myWidgetWindow.addView(myWidgetView, params);
        }

        myWidgetView.findViewById(R.id.floatingBox).setOnTouchListener(this);




    }

    private int lastAction;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    float dX,dY;
    int startX,startY,endX,endY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN :
                startX =  params.x;
                startY = params.y;
                dX = event.getRawX();
                dY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
//                view.animate().x(dX + event.getRawX()).y(dY + event.getRawY()).setDuration(0).start();
                params.x = startX + (int) (event.getRawX() - dX );
                params.y = startY + (int) (event.getRawY() - dY);
                myWidgetWindow.updateViewLayout(myWidgetView,params);
                break;
            default:
                return false;

        }
        return true;
    }
}
