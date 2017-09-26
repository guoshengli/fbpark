package com.fbpark.rest.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "group_privilege")
public class GroupPrivilege extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 3883602443760091516L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	 @JoinColumn(name="group_id")
	private Group group;
	
	@ManyToOne(fetch=FetchType.LAZY)
	 @JoinColumn(name="menu_id")
	private Menu menu;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	
	
	

}
