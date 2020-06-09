package com.example.hippoplayer.models;

public class Song {
    private String thumbnail;
    private String nameSong;
    private String description;
    private String idSong;
    private String url;
    private String avatarArtist;
    private String idArtist;
    private String nameArtist;

    private SongResponse songResponse;

    public Song() {
    }

    public void setUpSong(SongResponse songResponse) {
        this.thumbnail = songResponse.thumbnail;
        this.nameSong = songResponse.name;
        this.description = songResponse.description;
        this.idSong = songResponse.id;
        this.url = songResponse.url;
        this.avatarArtist = songResponse.artist.avatar;
        this.idArtist = songResponse.artist.id;
        this.nameArtist = songResponse.artist.name;
    }

    public void setSongResponse(SongResponse songResponse) {
        this.songResponse = songResponse;
        setUpSong(songResponse);
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
