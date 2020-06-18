package com.example.hippoplayer.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.hippoplayer.ExoPlayerService;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.notification.CreateNotification;
import com.example.hippoplayer.play.notification.NotificationActionService;
import com.example.hippoplayer.utils.PathHelper;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.List;
import java.util.Random;

public class MediaManager implements Playable {
    public static final int STATE_NON_REPEAT = 0;
    public static final int STATE_REPEAT_ONE = 1;
    public static final int STATE_SHUFFLE = 2;
    public static final int STATE_REPEAT = 3;

    Context mContext;
    List<Song> mSongs;
    private ExoPlayerService mService;
    MutableLiveData<Integer> posLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> stateLiveData = new MutableLiveData<>();
    int position = 0;
    private int stateFlag = 3;

    public MediaManager(Context context) {
        mContext = context;
        mService = new ExoPlayerService(context);
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
                    if (mService.isPlaying()) {
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
        if (mService.isPlaying()) {
            pauseMedia();
        }
        else resumeMedia();
    }

    public void play(int position) {
        this.position = position;
        String fullUrl = PathHelper.getFullUrl(mSongs.get(position).getIdSong(), PathHelper.TYPE_SONG);
        mService.setMediaFile(fullUrl);
        mService.playMedia(position);
        stateLiveData.setValue(isPlaying());
        // Change avatar in notification manager

    }

    public void pauseMedia() {
        mService.pauseMedia();
        stateLiveData.setValue(isPlaying());
    }

    public void resumeMedia() {
        mService.resumeMedia();
        stateLiveData.setValue(isPlaying());
    }

    public void seekTo(int progress) {
        mService.seekTo(progress);
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

    public long getDuration() {
        if (mService.getPlayer() == null) return 0;
        return mService.getPlayer().getDuration();
    }

    public long getCurrentPosition() {
        if (mService.getPlayer() == null) return 0;
        return mService.getPlayer().getCurrentPosition();
    }

    public boolean isPlaying() {
        return mService.isPlaying();
    }

    public int getStateFlag() {
        return stateFlag;
    }

    public void updateStateFlag() {
        stateFlag++;
        stateFlag %= 4;
    }

    @Override
    public void onPrevious() {
        if (position == 0) {
            return;
        }
        position--;
        posLiveData.setValue(position);
    }

    @Override
    public void onPlay() {
        resumeMedia();
    }

    @Override
    public void onPause() {
        pauseMedia();
    }

    @Override
    public void onNext() {
        // when we press next
        switch (stateFlag) {
            case STATE_NON_REPEAT:
            case STATE_REPEAT:
                playNext();
                break;
            case STATE_REPEAT_ONE:
                playNext();
                break;
            case STATE_SHUFFLE:
                playShuffleSong();
                break;
        }
    }

    public void playNextSong() {
        // when finish the song
        switch (stateFlag) {
            case STATE_NON_REPEAT:
            case STATE_REPEAT:
                playNext();
                break;
            case STATE_REPEAT_ONE:
                mService.seekTo(0);
                break;
            case STATE_SHUFFLE:
                playShuffleSong();
                break;

        }
    }

    private void playShuffleSong() {
        int max = mSongs.size() - 1;
        int min = 0;
        position = new Random().nextInt((max - min) + 1);
        posLiveData.setValue(position);
    }

    private void playNext() {
        // if the song is at the end now
        if (position == mSongs.size() - 1) {
            // if it can repeat then the position turn to 0 again
            if (stateFlag == STATE_REPEAT) {
                position = 0;
                posLiveData.setValue(position);
            }
            return;
        }
        // else we update the song like normal
        position++;
        posLiveData.setValue(position);
    }
}
