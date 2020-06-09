package com.example.hippoplayer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistResponse {

	@SerializedName("name")
	@Expose
	public String name;

	@SerializedName("description")
	@Expose
	public String description;

	@SerializedName("year_of_birth")
	@Expose
	public int year_of_birth;

	@SerializedName("avatar")
	@Expose
	public String avatar;

	@SerializedName("id")
	@Expose
	public String id;

	@SerializedName("songs")
	@Expose
	public List<String> songsList;

}