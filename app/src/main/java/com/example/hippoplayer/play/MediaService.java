package com.example.hippoplayer.play;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MediaService implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final String TAG = MediaService.class.getSimpleName();

    private MediaPlayer mMediaPlayer;
    private String mMediaFile;
    private int resumePoint;
    public MediaService() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    public void loadMediaSource() {
        try {
            mMediaPlayer.setDataSource(mMediaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
    }

    public void playMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            resumePoint = mMediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(resumePoint);
            mMediaPlayer.start();
        }
    }

    public void pauseButtonClicked() {
        if (mMediaPlayer.isPlaying()) {
            pauseMedia();
        }
        else resumeMedia();
    }

    public void seekTo(int progress) {
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
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }
}
