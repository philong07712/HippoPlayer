package com.example.hippoplayer.search;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.PathHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchViewModel extends ViewModel {
    private Flowable<List<ArtistResponse>> mSongListArtist;
    private SongService mService = new SongService();
    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    public static Context mContext;

    public SearchViewModel() {
        mSongListArtist = mService.getListArtistResponse();
    }

    public Flowable<List<ArtistResponse>> getmSongListArtist() {
        return mSongListArtist;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @BindingAdapter("app:load_image_item_search")
    public static void setImageItemSearch(ImageView image, String url) {
        String finalurl = PathHelper.getFullUrl(url, PathHelper.TYPE_ARTIST);
        Glide.with(mContext)
                .load(finalurl)
                .centerCrop()
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(image);
    }
}