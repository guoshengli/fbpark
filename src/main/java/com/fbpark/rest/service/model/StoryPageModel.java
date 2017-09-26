 package com.fbpark.rest.service.model;
 
 import java.io.Serializable;
 import java.util.List;
 import net.sf.json.JSONArray;
 import net.sf.json.JSONObject;
 
 public class StoryPageModel
   implements Serializable
 {
   private static final long serialVersionUID = 5670079676162194305L;
   private Long id;
   private String title;
   private JSONObject cover_media;
   private JSONArray elements;
   private Long update_time;
   private Long created_time;
   private int comment_count;
   private boolean commnents_enables;
   private boolean repost_by_current_user;
   private int view_count;
   private Object url;
   private JSONObject author;
   private List<JSONObject> comments;
   private List<StoryIntro> recommendation;
   private JSONArray collection;
   private int image_count;
   private String summary;
   private String resource;
 
   public Long getId()
   {
     return this.id;
   }
 
   public void setId(Long id) {
     this.id = id;
   }
 
   public String getTitle() {
     return this.title;
   }
 
   public void setTitle(String title) {
     this.title = title;
   }
 
 
   public JSONObject getCover_media() {
     return this.cover_media;
   }
 
   public void setCover_media(JSONObject cover_media) {
     this.cover_media = cover_media;
   }
 
   public JSONArray getElements() {
     return this.elements;
   }
 
   public void setElements(JSONArray elements) {
     this.elements = elements;
   }
 
   public Long getUpdate_time() {
     return this.update_time;
   }
 
   public void setUpdate_time(Long update_time) {
     this.update_time = update_time;
   }
 
   public Long getCreated_time() {
     return this.created_time;
   }
 
   public void setCreated_time(Long created_time) {
     this.created_time = created_time;
   }
 
   public int getComment_count() {
     return this.comment_count;
   }
 
   public void setComment_count(int comment_count) {
     this.comment_count = comment_count;
   }
 
   public boolean isCommnents_enables() {
     return this.commnents_enables;
   }
 
   public void setCommnents_enables(boolean commnents_enables) {
     this.commnents_enables = commnents_enables;
   }
 
 
   public boolean isRepost_by_current_user() {
     return this.repost_by_current_user;
   }
 
   public void setRepost_by_current_user(boolean repost_by_current_user) {
     this.repost_by_current_user = repost_by_current_user;
   }
 
   public int getView_count() {
     return this.view_count;
   }
 
   public void setView_count(int view_count) {
     this.view_count = view_count;
   }
 
   public Object getUrl() {
     return this.url;
   }
 
   public void setUrl(Object url) {
     this.url = url;
   }
 
   public JSONObject getAuthor() {
     return this.author;
   }
 
   public void setAuthor(JSONObject author) {
     this.author = author;
   }
 
   
 
   public List<JSONObject> getComments() {
	return comments;
}

public void setComments(List<JSONObject> comments) {
	this.comments = comments;
}

public List<StoryIntro> getRecommendation() {
     return this.recommendation;
   }
 
   public void setRecommendation(List<StoryIntro> recommendation) {
     this.recommendation = recommendation;
   }
 
 
   public JSONArray getCollection() {
	return collection;
}

public void setCollection(JSONArray collection) {
	this.collection = collection;
}

public int getImage_count() {
     return this.image_count;
   }
 
   public void setImage_count(int image_count) {
     this.image_count = image_count;
   }
 
   public String getSummary() {
     return this.summary;
   }
 
   public void setSummary(String summary) {
     this.summary = summary;
   }

	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
   
   
   
 }

