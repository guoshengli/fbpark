package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Table(name="content")
@Entity
public class Content extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 6432951145098928908L;
	
	@Column(name="title")
	private String title;
	
	@Column(name = "summary",columnDefinition="TEXT")
	private String summary;
	
	@Column(name = "cover_image")
	private String cover_image;
	
	@Column(name = "author")
	private String author;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.ALWAYS)
	@Column(name = "create_time", insertable = false, updatable = false)
	private Date created_time;
	
	@Column(name = "update_time")
	private Date update_time;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "type")
	private int type;
	
	@OneToMany(mappedBy="content", cascade={javax.persistence.CascadeType.ALL}, fetch=FetchType.LAZY)
	 private List<ContentElement> elements = new ArrayList<ContentElement>();
	
	@OneToOne(mappedBy = "content")
	private Help help;
	
	@OneToOne(mappedBy = "content")
	private News news;
	
	@OneToOne(mappedBy = "content")
	private Setting setting;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getCreated_time() {
	     return Long.valueOf(this.created_time.getTime() / 1000L);
	}

	public void setCreated_time(Date created_time) {
		this.created_time = created_time;
	}


	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<ContentElement> getElements() {
		return elements;
	}

	public void setElements(List<ContentElement> elements) {
		this.elements = elements;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUpdate_time() {
		return Long.valueOf(this.update_time.getTime() / 1000L);
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Help getHelp() {
		return help;
	}

	public void setHelp(Help help) {
		this.help = help;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
}
