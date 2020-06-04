package com.example.hippoplayer.play;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.ViewModel;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongRespone;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlayViewModel extends ViewModel {

    // Todo: Constant
    private final String TAG = PlayViewModel.class.getSimpleName();

    // Todo: Fields
    // Song service to get data
    private SongService mService = new SongService();
    // media service to play the song
    private MediaPlayerService mMediaService;
    private boolean mServiceBound = false;

    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    private Flowable<SongRespone> mSongResponeFlowable;
    // variables
    private List<Song> mSongs = new ArrayList<>();
    // Todo: Constructor
    public PlayViewModel() {

    }

    // Todo: Override method
    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }

    // Todo: public method
    public void init() {
        mMediaService = new MediaPlayerService();
        mSongResponeFlowable = mService.getSongRespone();
    }

    // Todo: getter and setter
    public void setMediaSong(String url) {
        mMediaService.setMediaFile(url);
    }

    public void playMediaSong() {
        mMediaService.loadMediaSource();
        mMediaService.playMedia();
    }

    public ServiceConnection getMusicConnection() {
        return musicConnection;
    }

    public String getFullUrl(String endpoint) {
        return Constants.SONG_BASE_URL + endpoint;
    }

    public Flowable<SongRespone> getSongs() {
        return mSongResponeFlowable;
    }

    // Todo: private method

    // Todo: inner classes + interface

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            // get service
            mMediaService = binder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
}
