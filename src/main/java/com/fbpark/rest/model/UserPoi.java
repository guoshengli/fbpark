package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
@Entity
@Table(name="user_poi")
public class UserPoi extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 7215747710183451120L;
	
	@Column(name="user_id")
	private Long user_id;
	
	@Column(name="poi_id")
	private Long poi_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.ALWAYS)
	@Column(name="create_time", insertable=false, updatable=false)
	private Date create_time;

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getPoi_id() {
		return poi_id;
	}

	public void setPoi_id(Long poi_id) {
		this.poi_id = poi_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	

}
