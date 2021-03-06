package com.example.hippoplayer.play.utils;

import com.example.hippoplayer.models.ArtistResponse;
import com.example.hippoplayer.models.SongResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface SongAPI {
    @Headers("Content-Type: application/json")
    @GET("api/songs")
    Flowable<List<SongResponse>> getListSongPlay();

    @GET("api/songs")
    Flowable<List<SongResponse>> getListSongList();

    @GET("api/artists")
    Flowable<List<ArtistResponse>> getListArtist();

    @GET("api/artists/{id}")
    Flowable<ArtistResponse> getArtist(@Path("id") String id);
}
