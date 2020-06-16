package com.example.hippoplayer.play.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.notification.CreateNotification;

import java.util.List;

public class SongNotificationManager {

    private NotificationManager mNotificationManager;
    private CreateNotification mNotificationCreator;
    private Context mContext;
    List<Song> mSongs;

    public static SongNotificationManager INSTANCE;

    private SongNotificationManager() {
    }

    public static SongNotificationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SongNotificationManager();
        }
        return INSTANCE;
    }

    public void init(Context context, List<Song> songs) {
        mContext = context;
        mSongs = songs;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.ACTION_ID,
                    "HippoPlayer", NotificationManager.IMPORTANCE_LOW);

            mNotificationManager = mContext.getSystemService(NotificationManager.class);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void createNotification(int position, boolean isPause) {
        if (isPause) {
            createPauseNotification(position);
        }
        else createPlayNotification(position);
    }

    private void createPauseNotification(int position) {
        mNotificationCreator = new CreateNotification(mContext, mSongs.get(position), R.drawable.ic_pause_black_24dp, position, mSongs.size());
    }

    private void createPlayNotification(int position) {
        mNotificationCreator = new CreateNotification(mContext, mSongs.get(position), R.drawable.ic_baseline_play_arrow_24, position, mSongs.size());
    }

}
