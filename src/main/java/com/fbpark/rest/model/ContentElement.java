package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="content_element")
@Entity
public class ContentElement extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -1566560376748404356L;
	
	@Column(name="element",columnDefinition="TEXT")
	private String element;
	
	@ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	@JoinColumn(name="content_id")
	private Content content;

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	
	

}
