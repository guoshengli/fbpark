package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="device")
public class Device extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -9000817429475738748L;
	@Column(name="device_code")
	private String device_code;
	
	@Column(name="poi_id")
	private Long poi_id;
	
	@Column(name="status")
	private String status;
	
	@Column(name="random_char")
	private String random_char;

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public Long getPoi_id() {
		return poi_id;
	}

	public void setPoi_id(Long poi_id) {
		this.poi_id = poi_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRandom_char() {
		return random_char;
	}

	public void setRandom_char(String random_char) {
		this.random_char = random_char;
	}
	
	
	
}
