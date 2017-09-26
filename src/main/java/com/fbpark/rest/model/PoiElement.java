package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Table(name="poi_element")
@Entity
public class PoiElement extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 2971521063494439512L;
	
	@Column(name="element")
	private String element;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poi_id", updatable = false)
	private Poi poi;

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public Poi getPoi() {
		return poi;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}
	
	
}
