package com.example.hippoplayer.play.utils;

import com.example.hippoplayer.models.SongResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SongAPI {
    @Headers("Content-Type: application/json")
    @GET("api/songs")
    Flowable<List<SongResponse>> getSongList();
}
