package com.example.hippoplayer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("name") @Expose
    private String name;

    @SerializedName("description") @Expose
    private String description;

    @SerializedName("url") @Expose
    private String url;

    @SerializedName("thumbnail") @Expose
    private String thumbnail;

    @SerializedName("artist") @Expose
    private String artist;

    @SerializedName("id") @Expose
    private String id;

    public Song(String name, String description, String url, String thumbnail, String artist, String id) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.thumbnail = thumbnail;
        this.artist = artist;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
