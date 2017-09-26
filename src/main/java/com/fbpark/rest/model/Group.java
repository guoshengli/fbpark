package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "groups")
public class Group extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -761345669599861313L;

	@Column(name = "group_name")
	private String group_name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "admin_group", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = {
			@JoinColumn(name = "admin_id") })
	private Set<Admin> admins;

	 @ManyToMany(fetch=FetchType.LAZY)
	 @JoinTable(name="group_privilege", joinColumns={@JoinColumn(name="group_id")}, inverseJoinColumns={@JoinColumn(name="menu_id")})
	 private Set<Menu> menus;
	 
	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
	
	

}
