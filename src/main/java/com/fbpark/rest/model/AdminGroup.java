package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="admin_group")
public class AdminGroup extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 4611373892601343233L;
	
	 @ManyToOne(fetch=FetchType.LAZY)
	   @JoinColumn(name="admin_id")
	   private Admin admin;
	 
	   @ManyToOne(fetch=FetchType.LAZY)
	   @JoinColumn(name="group_id")
	   private Group group;

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	   
	   

}
