package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="park_map")
public class ParkMap extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 3293499887118574867L;
	
	@Column(name="url")
	private String url;
	
	@Column(name="version")
	private String version;
	
	@Column(name="status")
	private String status;
	
	@Column(name="point_a")
	private String pointA;
	
	@Column(name="point_b")
	private String pointB;
	
	@Column(name="coordinate_a")
	private String coordinateA;
	
	@Column(name="coordinate_b")
	private String coordinateB;
	
	@Column(name="lng_lat_x")
	private String lng_lat_x;
	
	@Column(name="lng_lat_y")
	private String lng_lat_y;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPointA() {
		return pointA;
	}

	public void setPointA(String pointA) {
		this.pointA = pointA;
	}

	public String getPointB() {
		return pointB;
	}

	public void setPointB(String pointB) {
		this.pointB = pointB;
	}

	
	public String getCoordinateA() {
		return coordinateA;
	}

	public void setCoordinateA(String coordinateA) {
		this.coordinateA = coordinateA;
	}

	public String getCoordinateB() {
		return coordinateB;
	}

	public void setCoordinateB(String coordinateB) {
		this.coordinateB = coordinateB;
	}

	public String getLng_lat_x() {
		return lng_lat_x;
	}

	public void setLng_lat_x(String lng_lat_x) {
		this.lng_lat_x = lng_lat_x;
	}

	public String getLng_lat_y() {
		return lng_lat_y;
	}

	public void setLng_lat_y(String lng_lat_y) {
		this.lng_lat_y = lng_lat_y;
	}

	
	

}
