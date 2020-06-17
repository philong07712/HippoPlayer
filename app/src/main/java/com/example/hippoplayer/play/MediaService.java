package com.example.hippoplayer.play;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.hippoplayer.play.notification.SongNotificationManager;

import java.io.IOException;

public class MediaService implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = MediaService.class.getSimpleName();

    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private String mMediaFile;
    private int resumePoint;
    private int mPosition = 0;

    public MediaService() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    public void loadMediaSource() {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mMediaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
    }

    public void playMedia(int position) {
        if (!mMediaPlayer.isPlaying()) {
            mPosition = position;
            mMediaPlayer.start();
            // if the service play then the notificate will create pause notification
            SongNotificationManager.getInstance().createNotification(mPosition, true);
            mMediaPlayer.setOnCompletionListener(this);
        }
    }

    public void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            // if the service stop then the notificate will create play notification
            SongNotificationManager.getInstance().createNotification(mPosition, false);
        }
    }

    public void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            resumePoint = mMediaPlayer.getCurrentPosition();
            // if the service pause then the notificate will create play notification
            SongNotificationManager.getInstance().createNotification(mPosition, false);
        }
    }

    public void resumeMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(resumePoint);
            mMediaPlayer.start();
            // if the service resume then the notificate will create pause notification
            SongNotificationManager.getInstance().createNotification(mPosition, true);
        }
    }

    public void seekTo(int progress) {
        resumePoint = progress;
        mMediaPlayer.seekTo(progress);
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaFile(String mMediaFile) {
        this.mMediaFile = mMediaFile;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d(TAG, "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(TAG, "MEDIA ERROR SERVER DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(TAG, "MEDIA ERROR UNKNOWN");
                break;
        }
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // the service gained audio focus, so it needs to start playing
                Log.d(TAG, "Audio focus gained");
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // the service lost audio focus, the user probably moved to playing
                // media on other app, so release the media player
                if (mMediaPlayer != null) pauseMedia();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Focus lost for a short time, pause the Media player
                if (mMediaPlayer.isPlaying()) pauseMedia();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short of time, lower the volume of the player
                // maybe there is a notification
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    public boolean requestAudioFocus(Context mContext) {
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        Log.d(TAG, "request Audio focus called" + result);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Focus gained
            this.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
            return true;
        }
        // Could not gained focus
        return false;
    }

    public boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia(mPosition);
        Log.d(TAG, "onPrepared called");
    }
}
