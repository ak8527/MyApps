package com.example.ashu.supersearch.FloatingWidget;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ashu.supersearch.MainActivity;
import com.example.ashu.supersearch.R;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;

    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.floating_box, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the chat head position
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;        //Initially view will be added to top-left corner
//        params.x = 0;
//        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (mWindowManager != null) {
            mWindowManager.addView(mChatHeadView, params);
        }


        //Drag and move chat head using user's touch action.
        final ImageView chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.floatingBox);
        chatHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatHeadService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
    }
}
