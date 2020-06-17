package com.example.hippoplayer;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;

import com.example.hippoplayer.play.MediaService;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class ExoPlayerService implements AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = ExoPlayerService.class.getSimpleName();

    private AudioManager mAudioManager;
    private Context mContext;
    private SimpleExoPlayer player = null;
    private Uri mMediaFile;
    public ExoPlayerService(Context context) {
        mContext = context;
        initPlayer();
    }

    public void initPlayer() {
        // if the player is not exist then we will create one
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(mContext, new DefaultTrackSelector());
        }
    }

    private void loadMediaSource(Uri uri) {
        player.prepare(buildMediaSource(uri));
        player.setPlayWhenReady(true);
    }

    public void setMediaFile(String url) {
        mMediaFile = Uri.parse(url);
        loadMediaSource(mMediaFile);
    }

    private MediaSource buildMediaSource(Uri uri) {
        // build MediaSource from http data source
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_media");
        MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
        return mediaSource;
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // the service gained audio focus, so it needs to start playing
                Log.d(TAG, "Audio focus gained");
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // the service lost audio focus, the user probably moved to playing
                // media on other app, so release the media player
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Focus lost for a short time, pause the Media player
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short of time, lower the volume of the player
                // maybe there is a notification
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
}
