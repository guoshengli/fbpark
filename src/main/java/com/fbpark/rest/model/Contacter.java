package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "contacter")
public class Contacter extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -7245082314109640296L;

	@Column(name = "name")
	private String name;

	// 身份证号
	@Column(name = "identity_card")
	private String identity_card;

	@Column(name = "phone")
	private String phone;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "gender")
	private String gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "contacter", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<OrdersTicket> Contacters = new ArrayList<OrdersTicket>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrdersTicket> getContacters() {
		return Contacters;
	}

	public void setContacters(List<OrdersTicket> contacters) {
		Contacters = contacters;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
}
