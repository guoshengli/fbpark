package com.fbpark.rest.service.model;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class RegionModel implements Serializable {

	private static final long serialVersionUID = -5392421539709200730L;
	
	private Long id;
	
	private String english;
	
	private String region_name;
	
	private JSONObject cover_image;
	
	private String location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public JSONObject getCover_image() {
		return cover_image;
	}

	public void setCover_image(JSONObject cover_image) {
		this.cover_image = cover_image;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	

}
