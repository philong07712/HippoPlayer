package com.example.hippoplayer.detail;

import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.Song;

import java.io.Serializable;
import java.util.List;

public class DetailSerializable implements Serializable {
    private transient String idArtist;
    private transient List<Song> songList;
    private transient List<Artist> artistList;

    public DetailSerializable(String idArtist, List<Song> songList, List<Artist> artistList) {
        this.idArtist = idArtist;
        this.songList = songList;
        this.artistList = artistList;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }
}
