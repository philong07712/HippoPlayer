package com.example.hippoplayer.play;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener {

    private final IBinder iBinder = new LocalBinder();

    // MediaPlayer init
    private MediaPlayer mediaPlayer;
    private String mediaFile;

    @Override
    public void onCreate() {
        super.onCreate();
        // init mediaPlayer
        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        // still loading the media source
        mediaPlayer.setOnBufferingUpdateListener(this);
        // when load the media source complete
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        // if it has some error
        mediaPlayer.setOnErrorListener(this);
    }

    public void loadMediaSource() {
        try {
            mediaPlayer.setDataSource(mediaFile);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mediaPlayer.prepareAsync();
    }

    public void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }
}
