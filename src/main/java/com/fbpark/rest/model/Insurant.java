package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="insurant")
public class Insurant extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -5297493239065879921L;
	
	@Column(name="identity_card")
	private String identity_card;

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}
	
	
	

}
