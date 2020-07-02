package com.example.hippoplayer.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.hippoplayer.R;
import com.example.hippoplayer.detail.DetailFragment;
import com.example.hippoplayer.detail.DetailSerializable;
import com.example.hippoplayer.models.ArtistResponse;
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
    public static void setImageItemSearch(ImageView image, String idArtist, String idSong) {
        Log.d("TAG", idArtist != null ? idArtist : idSong);
        if (idArtist != null) {
            String finalurl = PathHelper.getFullUrl(idArtist, PathHelper.TYPE_ARTIST);
            Glide.with(mContext)
                    .load(finalurl)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.ic_baseline_music_note_orange)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(image);
        } else if (idSong != null) {
            String finalurl = PathHelper.getFullUrl(idSong, PathHelper.TYPE_IMAGE);
            Glide.with(mContext)
                    .load(finalurl)
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
                if (idSong != null) {
                    Log.e("Button", "Click" + idSong);
                }
                if (idArtist != null) {
                    DetailSerializable songSerializable = new DetailSerializable(idArtist, SearchFragment.songs, SearchFragment.artists);
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