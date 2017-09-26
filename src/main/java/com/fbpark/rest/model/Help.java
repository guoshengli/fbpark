package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="helps")
public class Help extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -8866596515310865044L;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="content_id")
	private Content content;
	
	@Column(name="sequence")
	private int sequence;

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	
	
}
