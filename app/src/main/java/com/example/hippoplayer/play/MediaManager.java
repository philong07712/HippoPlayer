package com.example.hippoplayer.play;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import androidx.lifecycle.MutableLiveData;

import com.example.hippoplayer.ExoPlayerService;
import com.example.hippoplayer.R;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.notification.CreateNotification;
import com.example.hippoplayer.play.notification.NotificationActionService;
import com.example.hippoplayer.play.notification.SongNotificationManager;
import com.example.hippoplayer.utils.PathHelper;
import com.google.android.exoplayer2.ExoPlayer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MediaManager implements Playable {
    Context mContext;
    List<Song> mSongs;
    private ExoPlayerService mService;
    private ExoPlayer mPlayer;
    MutableLiveData<Integer> posLiveData = new MutableLiveData<>();
    int position = 0;

    public MediaManager(Context context) {
        mContext = context;
        mService = new ExoPlayerService(context);
        mPlayer = mService.getPlayer();
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
//                    if (mPlayer.isPlaying()) {
//                        onPause();
//                    } else {
//                        onPlay();
//                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onNext();
                    break;
            }
        }
    };

    public void pauseButtonClicked() {
//        if (mPlayer.isPlaying()) {
//            pauseMedia();
//        }
//        else resumeMedia();
    }

    public void play(int position) {
        this.position = position;
        String fullUrl = PathHelper.getFullUrl(mSongs.get(position).getIdSong(), PathHelper.TYPE_SONG);
        mService.setMediaFile(fullUrl);
        // Change avatar in notification manager

    }

    public void pauseMedia() {
//        mService.pauseMedia();
    }

    public void resumeMedia() {
//        mService.resumeMedia();
    }

    public boolean isSongCompleted() {
        return getDuration() != 0
                && getCurrentPosition() > getDuration() - 1000;
    }

    public void setSongs(List<Song> mSongs) {
        this.mSongs = mSongs;
    }

    public ExoPlayerService getService() {
        return mService;
    }

    public ExoPlayer getPlayer() {
        return mPlayer;
    }

    public long getDuration() {
        return mPlayer.getDuration();
    }

    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return true;
    }

    @Override
    public void onPrevious() {
        position--;
        posLiveData.setValue(position);
    }

    @Override
    public void onPlay() {
//        mService.resumeMedia();
    }

    @Override
    public void onPause() {
//        mService.pauseMedia();
    }

    @Override
    public void onNext() {
        position++;
        posLiveData.setValue(position);
    }
}
