package com.example.hippoplayer;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {
    public static RetrofitHandler INSTANCE = null;
    private static final String SONG_URL = "http://vn.dubulator.com/testHT/api/";
    private Retrofit mSongRetrofit;

    private RetrofitHandler()
    {
        mSongRetrofit = new Retrofit.Builder()
                .baseUrl(SONG_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitHandler getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RetrofitHandler();
        }
        return INSTANCE;
    }

    public Retrofit getSongRetrofit() {
        return mSongRetrofit;
    }


}