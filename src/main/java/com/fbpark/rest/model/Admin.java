package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
@Table(name="admin")
@Entity
public class Admin extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -2327249592573306210L;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.ALWAYS)
    @Column(name="create_time", insertable=false, updatable=false)
    private Date create_time;
	
	@Column(name="status")
	private String status;
	
	@ManyToMany(mappedBy = "admins", fetch = FetchType.LAZY)
	private Set<Group> groups;
	
	@OneToMany(mappedBy = "admin", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Ticket> tickets = new ArrayList<Ticket>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreate_time() {
		return Long.valueOf(create_time.getTime() / 1000L);
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	

}
