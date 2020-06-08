package com.example.hippoplayer.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.hippoplayer.R;
import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.play.utils.SongService;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ListViewModel extends ViewModel {


    public Song song = new Song();
    public static Context mContext;
    // User SongService of play fragment
    private SongService mService = new SongService();

    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    private Flowable<List<SongResponse>> mSongResponeFlowable;

    public ListViewModel() {
        mSongResponeFlowable = mService.getListSongResponseList();
    }

    public Flowable<List<SongResponse>> getmSongResponeFlowable() {
        return mSongResponeFlowable;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }

    @BindingAdapter("app:load_image_list")
    public static void setImage(ImageView image, String url){
        String finalurl = RetrofitHandler.SONG_URL + url;
        Glide.with(mContext)
                .load(finalurl)
                .centerCrop()
                .fitCenter()
                .into(image);   
    }
}