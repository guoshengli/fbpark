package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="template")
public class Template extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -8297275163068695012L;
	
	@Column(name="content",columnDefinition="TEXT")
	private String content;
	
	@Column(name="type")
	private int type;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	

}
