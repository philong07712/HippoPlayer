package com.example.hippoplayer.offline;

import com.example.hippoplayer.models.Song;

import java.util.List;

public interface OfflineItemListener {
    public void onClick(List<Song> songs, int position);
}
