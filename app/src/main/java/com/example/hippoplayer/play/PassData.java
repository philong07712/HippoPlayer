package com.example.hippoplayer.play;

import com.example.hippoplayer.models.Song;

import java.util.List;

public interface PassData {
    public void onChange(List<Song> songs, int position);
}
