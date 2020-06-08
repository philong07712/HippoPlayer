package com.example.hippoplayer.play.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.hippoplayer.models.Song;

public class SongNotificationManager {

    private NotificationManager mNotificationManager;
    private CreateNotification mNotificationCreator;
    private Context mContext;


    public SongNotificationManager(Context context) {
        mContext = context;
    }

    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.ACTION_ID,
                    "HippoPlayer", NotificationManager.IMPORTANCE_LOW);

            mNotificationManager = mContext.getSystemService(NotificationManager.class);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void createNotification(Song song, int pos, int size) {
        mNotificationCreator = new CreateNotification(mContext, song, pos, size);
    }
}
