package com.example.hippoplayer.play;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongRespone;
import com.example.hippoplayer.play.utils.SongService;
import com.example.hippoplayer.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlayViewModel extends ViewModel {

    private final String TAG = PlayViewModel.class.getSimpleName();

    private SongService service = new SongService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Song>> songsMutableLiveData = new MutableLiveData<>();

    // variables
    private List<Song> songs = new ArrayList<>();

    public PlayViewModel() {

    }

    public void init() {
        compositeDisposable.add(service.getSongRespone()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SongRespone>() {
                    @Override
                    public void accept(SongRespone songRespone) throws Throwable {
                        songsMutableLiveData.setValue(songRespone.getSongList());
                    }
                })
        );
    }

    public void setListSong(List<Song> songs) {
        this.songs = songs;
    }



    public String getFullUrl(String endpoint) {
        return Constants.SONG_BASE_URL + endpoint;
    }

    public LiveData<List<Song>> getSongsLiveData() {
        return songsMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

}
