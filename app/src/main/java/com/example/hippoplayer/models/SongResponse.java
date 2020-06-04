package com.example.hippoplayer.models;

import com.google.gson.annotations.SerializedName;

public class SongResponse {
    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("artist")
    public Artist artist;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("id")
    public String id;

    @SerializedName("url")
    public String url;
}