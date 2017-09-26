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

@Table(name = "ad")
@Entity
public class Ad extends BaseEntity<Long> implements Serializable {
	private static final long serialVersionUID = 4812181417354871477L;

	// pio content url
	@Column(name = "type")
	private String type;

	@Column(name = "ad_image")
	private String ad_image;

	@Column(name = "reference_id")
	private Long reference_id;

	@Column(name = "reference_url")
	private String url;

	@Column(name = "author_id")
	private Long authorId;

	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.ALWAYS)
	@Column(name = "create_time", insertable = false)
	private Date create_time;

	@Column(name = "sequence")
	private int sequence;

	@Column(name = "status")
	private String status;
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAd_image() {
		return ad_image;
	}

	public void setAd_image(String ad_image) {
		this.ad_image = ad_image;
	}

	public Long getReference_id() {
		return this.reference_id;
	}

	public void setReference_id(Long reference_id) {
		this.reference_id = reference_id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Date getCreate_time() {
		return this.create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	
	

}
