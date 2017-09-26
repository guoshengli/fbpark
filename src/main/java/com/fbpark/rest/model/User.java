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
 
 @Entity
 @Table(name="user")
 public class User extends BaseEntity<Long>
   implements Serializable
 {
   private static final long serialVersionUID = 7042495344488712849L;
   
   @Column(name="username")
   private String username;
 
   @Column(name="password")
   private String password;
   
   @Column(name="birthday")
   private String birthday;
   
   //身份证号
   @Column(name="identify_card")
   private String identity_card;
   
   //是否同意‘用户协议’及‘隐私声明
   @Column(name="is_agree")
   private int is_agree;
   
   //所属行业
   @Column(name="trade")
   private String trade;
   
   //所属俱乐部
   @Column(name="club_name")
   private String club_name;
   
   //会员等级
   @Column(name="level")
   private String level;
   
   @Column(name="resource")
   private String resource;
   
   @Temporal(TemporalType.TIMESTAMP)
   @Generated(GenerationTime.ALWAYS)
   @Column(name="create_time", insertable=false, updatable=false)
   private Date created_time;
 
   @Column(name="status")
   private String status;
 
//   @Column(name="introduction")
//   private String introduction;
 
   @Column(name="avatar_image")
   private String avatar_image;
 
   @Column(name="zone")
   private String zone;
 
   @Column(name="phone")
   private String phone;
 
   @Column(name="address")
   private String address;
   
   
   @Column(name="gender")
   private String gender;
   
   @Column(name="tmp_pass")
   private String tmp_pass;
   
   @Column(name="normal_barcode",columnDefinition="TEXT")
   private String normal_barcode;
   
   @Column(name="vip_barcode",columnDefinition="TEXT")
   private String vip_barcode;
 
   @OneToMany(mappedBy = "user", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Orders> orders = new ArrayList<Orders>();
   
   @OneToMany(mappedBy = "user", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Contacter> Contacters = new ArrayList<Contacter>();
 
   public String getUsername()
   {
     return this.username;
   }
 
   public void setUsername(String username) {
     this.username = username;
   }
 
   public String getPassword() {
     return this.password;
   }
 
   public void setPassword(String password) {
     this.password = password;
   }
 
 
   public Long getCreated_time() {
     return Long.valueOf(this.created_time.getTime() / 1000L);
   }
 
   public void setCreated_time(Date created_time) {
     this.created_time = created_time;
   }
 
 
   public String getStatus() {
     return this.status;
   }
 
   public void setStatus(String status) {
     this.status = status;
   }
 
   public String getZone() {
     return this.zone;
   }
 
   public void setZone(String zone) {
     this.zone = zone;
   }
 

	public String getAvatar_image() {
		return avatar_image;
	}

	public void setAvatar_image(String avatar_image) {
		this.avatar_image = avatar_image;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}

	public int getIs_agree() {
		return is_agree;
	}

	public void setIs_agree(int is_agree) {
		this.is_agree = is_agree;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getClub_name() {
		return club_name;
	}

	public void setClub_name(String club_name) {
		this.club_name = club_name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public List<Contacter> getContacters() {
		return Contacters;
	}

	public void setContacters(List<Contacter> contacters) {
		Contacters = contacters;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNormal_barcode() {
		return normal_barcode;
	}

	public void setNormal_barcode(String normal_barcode) {
		this.normal_barcode = normal_barcode;
	}

	public String getVip_barcode() {
		return vip_barcode;
	}

	public void setVip_barcode(String vip_barcode) {
		this.vip_barcode = vip_barcode;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getTmp_pass() {
		return tmp_pass;
	}

	public void setTmp_pass(String tmp_pass) {
		this.tmp_pass = tmp_pass;
	}

	
	   
 }
