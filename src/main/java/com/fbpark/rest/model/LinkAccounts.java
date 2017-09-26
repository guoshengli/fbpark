package com.fbpark.rest.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "linked_acounts")
public class LinkAccounts extends BaseEntity<Long> implements Serializable {
	private static final long serialVersionUID = 4551831834861252688L;


	@Column(name = "union_id")
	private String union_id;

	@Column(name = "user_id")
	private Long user_id;
	
	@Column(name = "auth_token")
	private String auth_token;


	public Long getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	
	
}
