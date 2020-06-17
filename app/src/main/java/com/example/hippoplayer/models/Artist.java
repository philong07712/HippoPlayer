package com.example.hippoplayer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist{

	@SerializedName("name")
	@Expose
	public String name;

	@SerializedName("id")
	@Expose
	public String id;

}