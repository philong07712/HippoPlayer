package com.example.hippoplayer.detail;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.PathHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DetailViewModel extends ViewModel {

    public static Context mContext;

    public DetailViewModel() {
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @BindingAdapter("app:load_image_detail_song")
    public static void setImageSong(ImageView image, String idSong) {
        String finalurl = PathHelper.getFullUrl(idSong, PathHelper.TYPE_IMAGE);
        Glide.with(mContext)
                .load(finalurl)
                .centerCrop()
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(image);
    }
}