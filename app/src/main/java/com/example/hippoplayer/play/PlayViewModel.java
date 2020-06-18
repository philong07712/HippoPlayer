package com.example.hippoplayer.play;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.SongResponse;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.PathHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlayViewModel extends ViewModel {


    private SongService songService = new SongService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Song song = new Song();
    public static Context mContext;
    private final String TAG = PlayViewModel.class.getSimpleName();

    // Todo: Fields
    // Song service to get data
    private SongService mService = new SongService();
    // media service to play the song

    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    private Flowable<List<SongResponse>> mSongResponeFlowable;
    // variables
    private List<Song> mSongs = new ArrayList<>();

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

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }

    @BindingAdapter("app:load_image_play")
    public static void setImageSongPlay(ImageView image, String url) {
        String finalurl = PathHelper.getFullUrl(url, PathHelper.TYPE_IMAGE);
        Glide.with(mContext)
                .load(finalurl).centerCrop()
                .fitCenter().into(image);
    }

//    @BindingAdapter("app:load_image_background_play")


}
