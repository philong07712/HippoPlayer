package com.example.hippoplayer.play.utils;

import com.example.hippoplayer.models.SongRespone;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SongAPI {
    @GET("data.json")
    Flowable<SongRespone> getSongList(
            @Query("fbclid") String endpoint
    );
}
