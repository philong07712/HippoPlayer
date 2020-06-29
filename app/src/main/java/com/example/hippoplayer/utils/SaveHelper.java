package com.example.hippoplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hippoplayer.models.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveHelper {
    private static final String OFFLINE_LIST_KEY = "Offline Song List";
    public static void saveSong(Context context, Map<String, Song> songs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(songs);
        editor.putString(OFFLINE_LIST_KEY, json);
        editor.apply();
    }

    public static Map<String, Song> loadSong(Context context) {
        Map<String, Song> mapSongs = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(OFFLINE_LIST_KEY, null);
        Type type = new TypeToken<Map<String, Song>>() {}.getType();
        mapSongs = gson.fromJson(json, type);
        if (mapSongs == null) {
            return new HashMap<>();
        }
        return mapSongs;
    }

    public static void deleteData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

    }
}
