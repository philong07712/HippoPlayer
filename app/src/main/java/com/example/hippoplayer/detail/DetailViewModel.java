package com.example.hippoplayer.detail;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.play.utils.SongService;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DetailViewModel extends ViewModel {

    private SongService mService = new SongService();
    public static Context mContext;
    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();
    private Flowable<ArtistResponse> mArtistResponse;

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setIdArtist(String idArtist){
        mArtistResponse = mService.getArtistResponse(idArtist);
    }

    public Flowable<ArtistResponse> getmArtistResponse() {
        return mArtistResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposal.clear();
    }
}