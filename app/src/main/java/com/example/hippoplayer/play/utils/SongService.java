package com.example.hippoplayer.play.utils;

import android.util.Log;

import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.SongResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SongService {

    // This class look like SongRepo
    private static final String TAG = "SongService";
    private Retrofit retrofit;
    private SongAPI api;
    private List<SongResponse> listSong = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SongService() {
        this.retrofit = RetrofitHandler.getInstance().getSongRetrofit();
        api = this.retrofit.create(SongAPI.class);
    }

    // Lấy data từ api
    public Flowable<List<SongResponse>> getSongRespone() {
        return api.getSongList();
    }
}
