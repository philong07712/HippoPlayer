package com.example.hippoplayer.play;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.notification.CreateNotification;
import com.example.hippoplayer.play.notification.NotificationActionService;
import com.example.hippoplayer.play.notification.SongNotificationManager;
import com.example.hippoplayer.utils.PathHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MediaManager implements Playable {
    Context mContext;
    List<Song> mSongs;
    private MediaService mService;
    private MediaPlayer mPlayer;
    private SongNotificationManager notificationManager;
    private int currentPos = 0;

    public MediaManager(Context context) {
        mContext = context;
        mService = new MediaService();
        mPlayer = mService.getMediaPlayer();
        notificationManager =  new SongNotificationManager(mContext);
        notificationManager.init();

    }

    public BroadcastReceiver broadcastReceiver = new NotificationActionService() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString(CreateNotification.ACTION_NAME);

            switch (action) {
                case CreateNotification.ACTION_PREVIOUS:
                    onPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (mPlayer.isPlaying()) {
                        onPause();
                    } else {
                        onPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onNext();
                    break;
            }
        }
    };

    public void pauseButtonClicked() {
        if (mPlayer.isPlaying()) {
            pauseMedia();
        }
        else resumeMedia();
    }

    public void play(int position) {
        String fullUrl = PathHelper.getFullUrl(mSongs.get(position).getUrl());
        mService.setMediaFile(fullUrl);
        mService.loadMediaSource();
        mService.playMedia();
        currentPos = position;
        // Change avatar in notification manager

        notificationManager.createNotification(mSongs.get(position), R.drawable.ic_pause_black_24dp, position, mSongs.size());
    }

    public void pauseMedia() {
        mService.pauseMedia();
    }

    public void resumeMedia() {
        mService.resumeMedia();
    }

    public boolean isSongCompleted() {
        return mPlayer.getDuration() != 0
                && mPlayer.getCurrentPosition() > mPlayer.getDuration() - 1000;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> mSongs) {
        this.mSongs = mSongs;
    }

    public MediaService getService() {
        return mService;
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }


    public int getNextPos() {
        if (currentPos < mSongs.size() - 1) {
            currentPos++;
        }
        return currentPos;
    }

    public int getCurrentPos() {
        return currentPos;
    }


    public SongNotificationManager getNotificationManager() {
        return notificationManager;
    }

    @Override
    public void onPrevious() {
        currentPos--;
        notificationManager.createNotification(mSongs.get(currentPos), R.drawable.ic_pause_black_24dp, currentPos, mSongs.size());
        play(currentPos);
    }

    @Override
    public void onPlay() {
        notificationManager.createNotification(mSongs.get(currentPos), R.drawable.ic_pause_black_24dp, currentPos, mSongs.size());
        mService.resumeMedia();
    }

    @Override
    public void onPause() {
        notificationManager.createNotification(mSongs.get(currentPos), R.drawable.ic_baseline_play_arrow_24, currentPos, mSongs.size());
        mService.pauseMedia();
    }

    @Override
    public void onNext() {
        currentPos++;
        notificationManager.createNotification(mSongs.get(currentPos), R.drawable.ic_pause_black_24dp, currentPos, mSongs.size());
        play(currentPos);
    }
}
