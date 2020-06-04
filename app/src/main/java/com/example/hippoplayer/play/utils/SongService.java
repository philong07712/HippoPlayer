package com.example.hippoplayer.play.utils;

import com.example.hippoplayer.RetrofitHandler;
import com.example.hippoplayer.models.SongRespone;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;

public class SongService {
    private static final String TAG = "SongService";
    private Retrofit retrofit;
    private SongAPI api;

    public SongService() {
        this.retrofit = RetrofitHandler.getInstance().getSongRetrofit();
        api = this.retrofit.create(SongAPI.class);
    }

    public Flowable<SongRespone> getSongRespone() {
        return api.getSongList("IwAR07nN-lOIn2Ewd1rrsl2WeHG6jUQ6diZOoPQDpiE8jP7tR7a3tyc67YXmk");
    }

}
