package com.example.hippoplayer.play;

import android.util.Log;

import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.Song;
import com.example.hippoplayer.models.SongRespone;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SongService {
    private static final String TAG = "SongService";
    private Retrofit retrofit;
    private SongAPI api;

    public SongService() {
        this.retrofit = RetrofitHandler.getInstance().getSongRetrofit();
        api = this.retrofit.create(SongAPI.class);
    }

    public Observable<SongRespone> getSongRespone() {
        return api.getSongList("IwAR07nN-lOIn2Ewd1rrsl2WeHG6jUQ6diZOoPQDpiE8jP7tR7a3tyc67YXmk");
    }

}
