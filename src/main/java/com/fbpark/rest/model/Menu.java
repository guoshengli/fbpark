package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
@Entity
@Table(name="menu")
public class Menu extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 8213409771378549697L;
	
	@Column(name="menu_name")
	private String menu_name;
	
	@ManyToMany(mappedBy="menus", fetch=FetchType.LAZY)
	private Set<Group> groups;
	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	
	
}
