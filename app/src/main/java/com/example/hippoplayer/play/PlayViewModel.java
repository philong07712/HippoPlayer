package com.example.hippoplayer.play;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.SongResponse;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.utils.SongService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlayViewModel extends ViewModel {


    private SongService songService = new SongService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Song song = new Song(); // nhận song từ fragment
    public static Context mContext;
    List<SongResponse> songList = new ArrayList<>();
    private final String TAG = PlayViewModel.class.getSimpleName();

    private SongService mService = new SongService();
    // media service to play the song
    private MediaPlayerService mMediaService;
    private boolean mServiceBound = false;

    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    private Flowable<List<SongResponse>> mSongResponeFlowable;
    // variables
    private List<Song> mSongs = new ArrayList<>();

    // Todo: Constructor
    public PlayViewModel() {
        mMediaService = new MediaPlayerService();
        mSongResponeFlowable = mService.getSongRespone();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setSong(SongResponse songResponse) { // nhận song từ fragment (Nhận bài hát từ fragment)
        song.setSongResponse(songResponse);
    }

    public Flowable<List<SongResponse>> getmSongResponeFlowable() {
        return mSongResponeFlowable;
    }

    public void setMediaSong(String url) {
        mMediaService.setMediaFile(url);
    }

    public void playMediaSong() {
        mMediaService.loadMediaSource();
        mMediaService.playMedia();
    }

    public String getFullUrl(String endpoint) {
        return RetrofitHandler.SONG_URL + endpoint;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }

    @BindingAdapter("app:load_image")
    public static void setImage(ImageView image, String url) {
        String finalurl = RetrofitHandler.SONG_URL + url;
        Glide.with(mContext)
                .load(finalurl).centerCrop()
                .fitCenter().into(image);
    }
}
