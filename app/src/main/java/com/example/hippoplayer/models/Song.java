package com.example.hippoplayer.models;

import android.graphics.Bitmap;

import com.example.hippoplayer.utils.PathHelper;

import java.nio.file.Path;

public class Song {
    private String nameSong;
    private String description;
    private String idSong;
    private String idArtist;
    private String nameArtist;
    private String thumbnail;
    private String imgArtist;
    private String song;
    private Bitmap thumbnailBitmap;
    private SongResponse songResponse;

    public Song() {
    }

    public Song(String song, String nameSong, String idSong, String idArtist, String nameArtist, Bitmap thumbnailBitmap) {
        this.song = song;
        this.nameSong = nameSong;
        this.idSong = idSong;
        this.idArtist = idArtist;
        this.nameArtist = nameArtist;
        this.thumbnailBitmap = thumbnailBitmap;
    }

    public void setUpSong(SongResponse songResponse) {
        this.nameSong = songResponse.name;
        this.description = songResponse.description;
        this.idSong = songResponse.id;
        this.idArtist = songResponse.artist.id;
        this.nameArtist = songResponse.artist.name;
        this.imgArtist = PathHelper.getFullUrl(idArtist, PathHelper.TYPE_ARTIST);
        this.thumbnail = PathHelper.getFullUrl(idSong, PathHelper.TYPE_IMAGE);
        this.song = PathHelper.getFullUrl(idSong, PathHelper.TYPE_SONG);
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

    public String getThumbnail() {
        return thumbnail;
    }

    public String getImgArtist() {
        return imgArtist;
    }

    public String getSong() {
        return song;
    }

    public Bitmap getThumbnailBitmap() {
        return thumbnailBitmap;
    }
}
