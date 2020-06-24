package com.example.hippoplayer.play;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.notification.CreateNotification;
import com.example.hippoplayer.play.notification.NotificationActionService;
import com.example.hippoplayer.utils.Constants;

import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MediaManager implements Playable {
    public static final int STATE_NON_REPEAT = 0;
    public static final int STATE_REPEAT_ONE = 1;
    public static final int STATE_SHUFFLE = 2;
    public static final int STATE_REPEAT = 3;
    private static final String TAG = MediaManager.class.getSimpleName();

    Context mContext;
    List<Song> mSongs;
    private ExoPlayerService mService;
    MutableLiveData<Integer> posLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> stateLiveData;
    int position = 0;
    private int stateFlag = 3;
    private Stack<Integer> previousSongPos = new Stack<>();
    public MediaManager(Context context) {
        mContext = context;
        mService = new ExoPlayerService(context);
        stateLiveData = mService.stateLiveData;
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
                case CreateNotification.ACTION_DELETE:
                    onDelete();
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
        String url = mSongs.get(position).getSong();
        mService.setMediaFile(url);
        mService.playMedia(position);
        // Change avatar in notification manager

    }


    public void stopMedia() {
        mService.stopMedia();
    }
    public void pauseMedia() {
        mService.pauseMedia();
    }

    public void resumeMedia() {
        mService.resumeMedia();
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
        Log.d(TAG, "onPrevious: " + previousSongPos.toString());
        if (previousSongPos.isEmpty()) {
            return;
        }
        position = previousSongPos.pop();
        Log.d(TAG, "onPrevious: " + previousSongPos.toString());
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
            case STATE_REPEAT_ONE:
                playNext();
                break;
            case STATE_SHUFFLE:
                playShuffleSong();
                break;
        }
    }

    @Override
    public void onDelete() {
        stopMedia();
        deleteNotification();
    }

    public void deleteNotification() {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_ID);
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
        // push previous position to stack
        previousSongPos.push(position);

        position = generateRandomPos(min, max, position);
        posLiveData.setValue(position);
    }

    private void playNext() {
        // if the song is at the end now
        if (position == mSongs.size() - 1) {
            // if it can repeat then the position turn to 0 again
            if (stateFlag == STATE_REPEAT) {
                // push previous position to stack
                previousSongPos.push(position);

                position = 0;
                posLiveData.setValue(position);
            }
            return;
        }
        // push previous position to stack
        previousSongPos.push(position);

        // else we update the song like normal
        position++;
        posLiveData.setValue(position);
    }

    private int generateRandomPos(int min, int max, int current) {
        if (max == min) return current;
        int randomPos;
        do {
            randomPos = new Random().nextInt((max - min) + 1);
        } while (randomPos == current);
        return randomPos;

    }
}
