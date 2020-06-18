package com.example.hippoplayer.models;

import java.util.List;

public class Artist {
    private String name;
    private String description;
    private int year_of_birth;
    private String id;
    private List<String> songsList;

    private ArtistResponse artistResponse;

    public Artist() {
    }

    public void setUpArtist(ArtistResponse artistResponse){
        this.name = artistResponse.name;
        this.description = artistResponse.description;
        this.year_of_birth = artistResponse.year_of_birth;
        this.id = artistResponse.id;
        this.songsList = artistResponse.songsList;
    }

    public void setSongResponse(ArtistResponse artistResponse) {
        this.artistResponse = artistResponse;
        setUpArtist(artistResponse);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    public String getId() {
        return id;
    }

    public List<String> getSongsList() {
        return songsList;
    }

    public ArtistResponse getArtistResponse() {
        return artistResponse;
    }
}
