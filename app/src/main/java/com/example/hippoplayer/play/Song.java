package com.example.hippoplayer.play;

import com.example.hippoplayer.models.Artist;
import com.example.hippoplayer.models.SongResponse;

public class Song {
    private String thumbnail;
    private String nameSong;
    private String description;
    private String idSong;
    private String url;
    private String avatarArtist;
    private String idArtist;
    private String nameArtist;

    public Song() {
    }

    public Song(SongResponse songResponse, Artist artist) {
        this.thumbnail = songResponse.thumbnail;
        this.nameSong = songResponse.name;
        this.description = songResponse.description;
        this.idSong = songResponse.id;
        this.url = songResponse.url;
        this.avatarArtist = artist.avatar;
        this.idArtist = artist.id;
        this.nameArtist = artist.name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getNameSong() {
        return nameSong;
    }

    public String getDescription() {
        return description;
    }

    public String getIdSong() {
        return idSong;
    }

    public String getUrl() {
        return url;
    }

    public String getAvatarArtist() {
        return avatarArtist;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }
}
