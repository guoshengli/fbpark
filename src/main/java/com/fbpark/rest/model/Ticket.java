package com.fbpark.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="ticket")
public class Ticket extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 4610370302190173890L;
	
	@Column(name = "ticket_name")
	private String ticket_name;

	@Column(name = "price")
	private BigDecimal price;
	
	//库存
	@Column(name = "number")
	private int number;
	
	//已售票数
	@Column(name = "sold")
	private int sold;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "start_date")
	private String start_date;
	
	@Column(name = "start_time")
	private String start_time;
	
	@Column(name = "end_time")
	private String end_time;
	
	@Column(name = "end_date")
	private String end_date;
	
	@Column(name = "ticket_type")
	private String type;//票的类型 normal 普通 vip 
	
//	//是否需要保险
//	@Column(name = "is_insurance")
//	private int is_insurance;
//	
	@Column(name = "checking_rule")
	private String checking_rule;
	
	@Column(name = "insurance_link" ,columnDefinition="TEXT")
	private String insurance_link;
	
	@ManyToMany(mappedBy="ticket", fetch=FetchType.LAZY)
	private Set<Cart> carts;
	
	 @ManyToMany(fetch=FetchType.LAZY)
	 @JoinTable(name="orders_ticket", joinColumns={@JoinColumn(name="ticket_id")}, inverseJoinColumns={@JoinColumn(name="order_id")})
	 private Set<Orders> orders;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "poi_id", updatable = false)
	 private Poi poi;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "admin_id", updatable = false)
	 private Admin admin;

	public String getTicket_name() {
		return ticket_name;
	}

	public void setTicket_name(String ticket_name) {
		this.ticket_name = ticket_name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public Set<Cart> getCarts() {
		return carts;
	}

	public void setCarts(Set<Cart> carts) {
		this.carts = carts;
	}

	public Set<Orders> getOrders() {
		return orders;
	}

	public void setOrders(Set<Orders> orders) {
		this.orders = orders;
	}

	public Poi getPoi() {
		return poi;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

//	public int getIs_insurance() {
//		return is_insurance;
//	}
//
//	public void setIs_insurance(int is_insurance) {
//		this.is_insurance = is_insurance;
//	}
//
	
	public String getInsurance_link() {
		return insurance_link;
	}

	public String getChecking_rule() {
		return checking_rule;
	}

	public void setChecking_rule(String checking_rule) {
		this.checking_rule = checking_rule;
	}

	public void setInsurance_link(String insurance_link) {
		this.insurance_link = insurance_link;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	
	

}
