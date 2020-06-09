package com.example.hippoplayer.play;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import androidx.lifecycle.MutableLiveData;

import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.notification.CreateNotification;
import com.example.hippoplayer.play.notification.NotificationActionService;
import com.example.hippoplayer.play.notification.SongNotificationManager;
import com.example.hippoplayer.utils.PathHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MediaManager implements Playable {
    Context mContext;
    List<Song> mSongs;
    private MediaService mService;
    private MediaPlayer mPlayer;
    MutableLiveData<Integer> posLiveData = new MutableLiveData<>();
    int position = 0;

    public MediaManager(Context context) {
        mContext = context;
        mService = new MediaService();
        mPlayer = mService.getMediaPlayer();
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
        this.position = position;
        String fullUrl = PathHelper.getFullUrl(mSongs.get(position).getUrl());
        mService.setMediaFile(fullUrl);
        mService.loadMediaSource();
        mService.playMedia(position);
        // Change avatar in notification manager

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

    public void setSongs(List<Song> mSongs) {
        this.mSongs = mSongs;
    }

    public MediaService getService() {
        return mService;
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    @Override
    public void onPrevious() {
        position--;
        posLiveData.setValue(position);
    }

    @Override
    public void onPlay() {
        mService.resumeMedia();
    }

    @Override
    public void onPause() {
        mService.pauseMedia();
    }

    @Override
    public void onNext() {
        position++;
        posLiveData.setValue(position);
    }
}
