package com.example.hippoplayer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongResponse {
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    @SerializedName("artist")
    @Expose
    public Artist artist;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("url")
    @Expose
    public String url;
}