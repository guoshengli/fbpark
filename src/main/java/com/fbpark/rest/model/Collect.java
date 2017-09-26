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
@Table(name="collect")
public class Collect extends BaseEntity<Long> implements Serializable {
	


	private static final long serialVersionUID = -8590562262063264858L;
	
	@Column(name="reference_id")
	private Long reference_id;
	
	@Column(name="user_id")
	private Long user_id;
	
	@Column(name="reference_type")
	private String reference_type;
	
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


	public long getCreate_time() {
		return create_time.getTime() / 1000;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Long getReference_id() {
		return reference_id;
	}

	public void setReference_id(Long reference_id) {
		this.reference_id = reference_id;
	}

	public String getReference_type() {
		return reference_type;
	}

	public void setReference_type(String reference_type) {
		this.reference_type = reference_type;
	}
	
	


}
