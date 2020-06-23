package com.example.hippoplayer.models;

public class Song {
    private String nameSong;
    private String description;
    private String idSong;
    private String idArtist;
    private String nameArtist;

    private SongResponse songResponse;

    public Song() {
    }

    public Song(String nameSong, String description, String idSong, String idArtist, String nameArtist, SongResponse songResponse) {
        this.nameSong = nameSong;
        this.description = description;
        this.idSong = idSong;
        this.idArtist = idArtist;
        this.nameArtist = nameArtist;
        this.songResponse = songResponse;
    }

    public void setUpSong(SongResponse songResponse) {
        this.nameSong = songResponse.name;
        this.description = songResponse.description;
        this.idSong = songResponse.id;
        this.idArtist = songResponse.artist.id;
        this.nameArtist = songResponse.artist.name;
    }

    public void setSongResponse(SongResponse songResponse) {
        this.songResponse = songResponse;
        setUpSong(songResponse);
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

    public String getIdArtist() {
        return idArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }
}
