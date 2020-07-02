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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.hippoplayer.R;
import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.SongResponse;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.PathHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayViewModel extends ViewModel {


    private static final int BLUR_SAMPLING = 2;
    private static final int BLUR_RADIUS = 20;
    private SongService songService = new SongService();

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
    public static void setImage(ImageView image, Song song) {
        if (song.getThumbnail() != null)
        Glide.with(mContext)
                .load(song.getThumbnail()).centerCrop()
                .placeholder(R.drawable.ic_baseline_music_note_orange)
                .fitCenter().into(image);
        else {
            Glide.with(mContext)
                    .load(song.getThumbnailBitmap()).centerCrop()
                    .placeholder(R.drawable.ic_baseline_music_note_orange)
                    .fitCenter().into(image);
        }
    }

    @BindingAdapter("app:load_image_play_bg")
    public static void setImageBackground(ImageView image, Song song) {
        DrawableCrossFadeFactory fadeFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        if (song.getThumbnail() != null)
            Glide.with(mContext)
                    .load(song.getThumbnail())
                    .transition(new DrawableTransitionOptions().crossFade(fadeFactory))
                    .centerCrop()
                    .placeholder(R.drawable.background_list)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(BLUR_RADIUS , BLUR_SAMPLING)))
                    .into(image);
        else {
            Glide.with(mContext)
                    .load(song.getThumbnailBitmap())
                    .transition(new DrawableTransitionOptions().crossFade(fadeFactory))
                    .thumbnail(0.7f)
                    .centerCrop()
                    .placeholder(R.drawable.background_list)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(BLUR_RADIUS , BLUR_SAMPLING)))
                    .into(image);
        }
    }
}
