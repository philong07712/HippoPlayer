package com.example.hippoplayer.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.hippoplayer.R;
import com.example.hippoplayer.detail.DetailFragment;
import com.example.hippoplayer.detail.DetailSerializable;
import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongResponse;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.PathHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchViewModel extends ViewModel {
    private Flowable<List<ArtistResponse>> mSongListArtist;
    private Flowable<List<SongResponse>> mSongResponeFlowable;

    private SongService mService = new SongService();
    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    public static Context mContext;
    public static Activity mActivity;

    private String idSong = null;

    public SearchViewModel() {
        mSongListArtist = mService.getListArtistResponse();
        mSongResponeFlowable = mService.getListSongResponseList();
    }

    public Flowable<List<ArtistResponse>> getmSongListArtist() {
        return mSongListArtist;
    }

    public Flowable<List<SongResponse>> getmSongResponeFlowable() {
        return mSongResponeFlowable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    @BindingAdapter({"app:load_image_item_artist_search", "app:load_image_item_song_search"})
    public static void setImageItemSearch(ImageView image, Artist artist, Song song) {
        if (artist != null) {
            String finalurl = PathHelper.getFullUrl(artist.getId(), PathHelper.TYPE_ARTIST);
            Glide.with(mContext)
                    .load(finalurl)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.ic_baseline_music_note_orange)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(image);
        } else if (song != null) {
            String finalurl = PathHelper.getFullUrl(song.getIdSong(), PathHelper.TYPE_IMAGE);
            Glide.with(mContext)
                    .load(finalurl)
                    .error(Glide.with(mContext).load(song.getThumbnail()).placeholder(R.drawable.ic_baseline_music_note_orange))
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.ic_baseline_music_note_orange)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(image);
        }

        //Event for item
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (artist != null) {
                    DetailSerializable songSerializable = new DetailSerializable(artist.getId(), SearchFragment.songs, SearchFragment.artists);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("serializable", songSerializable);
                    DetailFragment detailArtistFragment = new DetailFragment();
                    detailArtistFragment.setArguments(bundle);
                    ((AppCompatActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom)
                            .add(R.id.fragment_search, detailArtistFragment, "Detail")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
}