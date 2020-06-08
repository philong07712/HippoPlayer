package com.example.hippoplayer.utils;

import com.example.hippoplayer.RetrofitHandler;

public class PathHelper {
    public static String getFullUrl(String endpoint) {
        return RetrofitHandler.SONG_URL + endpoint;
    }

}
