package com.fbpark.rest.service.model;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class CollectionDiscover implements Serializable {

	private static final long serialVersionUID = 6405500693597937799L;

	private Long id;
	private String collection_name;
	private JSONObject cover_image;
	private String info;
	private boolean is_followed_by_current_user;
	private JSONObject author;
	
	private int view_count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollection_name() {
		return collection_name;
	}

	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}

	public JSONObject getCover_image() {
		return cover_image;
	}

	public void setCover_image(JSONObject cover_image) {
		this.cover_image = cover_image;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}


	public boolean isIs_followed_by_current_user() {
		return is_followed_by_current_user;
	}

	public void setIs_followed_by_current_user(boolean is_followed_by_current_user) {
		this.is_followed_by_current_user = is_followed_by_current_user;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public JSONObject getAuthor() {
		return author;
	}

	public void setAuthor(JSONObject author) {
		this.author = author;
	}

	
	

}
