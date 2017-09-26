 package com.fbpark.rest.service.model;
 
 import java.io.Serializable;
 import javax.xml.bind.annotation.XmlRootElement;
 import net.sf.json.JSONArray;
 import net.sf.json.JSONObject;
 
 @XmlRootElement
 public class StoryModel
   implements Serializable
 {
   private static final long serialVersionUID = -627540663675725689L;
   private Long id;
   private String title;
   private JSONObject cover_media;
   private JSONArray elements;
   private Long update_time;
   private Long created_time;
   private int comment_count;
   private boolean commnents_enables;
   private int like_count;
   private boolean liked_by_current_user;
   private int repost_count;
   private boolean repost_by_current_user;
   private int view_count;
   private Object url;
   private JSONObject author;
   private String summary;
   private int image_count;
 
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
 
   public int getLike_count() {
     return this.like_count;
   }
 
   public void setLike_count(int like_count) {
     this.like_count = like_count;
   }
 
   public boolean isLiked_by_current_user() {
     return this.liked_by_current_user;
   }
 
   public void setLiked_by_current_user(boolean liked_by_current_user) {
     this.liked_by_current_user = liked_by_current_user;
   }
 
   public int getRepost_count() {
     return this.repost_count;
   }
 
   public void setRepost_count(int repost_count) {
     this.repost_count = repost_count;
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
 }

