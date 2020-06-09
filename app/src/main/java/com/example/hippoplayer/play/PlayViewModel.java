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

    public Song song = new Song();
    public static Context mContext;
    private final String TAG = PlayViewModel.class.getSimpleName();

    // Todo: Fields
    // Song service to get data
    private SongService mService = new SongService();

    // media service to play the song
    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    private Flowable<List<SongResponse>> mSongResponeFlowable;
    // Todo: Constructor
    public PlayViewModel() {
        mSongResponeFlowable = mService.getListSongResponsePlay();
    }

    // Todo: public method
    public void init() {
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Flowable<List<SongResponse>> getmSongResponeFlowable() {
        return mSongResponeFlowable;
    }

    public String getFullUrl(String endpoint) {
        return RetrofitHandler.SONG_URL + endpoint;
    }

    @BindingAdapter("app:load_image_play")
    public static void setImage(ImageView image, String url) {
        String finalurl = RetrofitHandler.SONG_URL + url;
        Glide.with(mContext)
                .load(finalurl).centerCrop()
                .fitCenter().into(image);
    }
}
