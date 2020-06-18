package com.example.hippoplayer.search;

import androidx.lifecycle.ViewModel;

import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.play.utils.SongService;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchViewModel extends ViewModel {
    private Flowable<List<ArtistResponse>> mSongListArtist;
    private SongService mService = new SongService();
    private CompositeDisposable mCompositeDisposal = new CompositeDisposable();

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
}