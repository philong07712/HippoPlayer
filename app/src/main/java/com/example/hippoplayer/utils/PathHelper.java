package com.example.hippoplayer.utils;

import com.example.hippoplayer.RetrofitHandler;

public class PathHelper {
    private static final String ASSETS_BASE_URL = RetrofitHandler.SONG_URL + "assets/";
    public static final String SONG_BASE_URL = ASSETS_BASE_URL + "song/";
    private static final String THUMBNAIL_BASE_URL = ASSETS_BASE_URL + "image/";
    private static final String ARTIST_BASE_URL = ASSETS_BASE_URL + "artist/";

    public static final int TYPE_SONG = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_ARTIST = 3;

    public static String getFullUrl(String endpoint, int type) {
        switch (type) {
            case TYPE_SONG:
                StringBuilder url = new StringBuilder(SONG_BASE_URL);
                url.append(endpoint);
                return SONG_BASE_URL + endpoint;
            case TYPE_IMAGE:
                return THUMBNAIL_BASE_URL + endpoint;
            case TYPE_ARTIST:
                return ARTIST_BASE_URL + endpoint;
        }
        return null;
    }

}
