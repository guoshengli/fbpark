package com.fbpark.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
@Entity
@Table(name = "poi")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Poi extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = -1748958425924210123L;
	
	@Column(name="title")
	private String title;
	
	@Column(name="subtitle")
	private String subtitle;
	
	@Column(name="attention",columnDefinition="TEXT")
	private String attention;
	
//	@Column(name="cover_image")
//	private String cover_image;
	
	//场馆
	@Column(name="place")
	private String place;
	
	@Column(name="admin_id")
	private Long admin_id;
	
	@Column(name="content_id")
	private Long content_id;
	
	@Column(name="location")
	private String location;
	
	@Column(name="time_info")
	private String time_info;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.ALWAYS)
    @Column(name="create_time", insertable=false, updatable=false)
    private Date create_time;
	
    @Column(name="update_time")
    private Date update_time;
	
	@Column(name="is_online")
	private int is_online;
	
	@Column(name="hot")
	private int hot;
	
	@Column(name="direct_sales")
	private String direct_sales;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classify_id")
	private Classify classify;
	
	@OneToMany(mappedBy = "poi", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<PoiElement> elements = new ArrayList<PoiElement>();
	
	@OneToMany(mappedBy = "poi", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Ticket> tickets = new ArrayList<Ticket>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Long getContent_id() {
		return content_id;
	}

	public void setContent_id(Long content_id) {
		this.content_id = content_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime_info() {
		return time_info;
	}

	public void setTime_info(String time_info) {
		this.time_info = time_info;
	}

	public Classify getClassify() {
		return classify;
	}

	public void setClassify(Classify classify) {
		this.classify = classify;
	}

	public List<PoiElement> getElements() {
		return elements;
	}

	public void setElements(List<PoiElement> elements) {
		this.elements = elements;
	}

//	public String getCover_image() {
//		return cover_image;
//	}
//
//	public void setCover_image(String cover_image) {
//		this.cover_image = cover_image;
//	}

	
	 public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public long getCreate_time() {
		 return create_time.getTime() / 1000L;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getIs_online() {
		return is_online;
	}

	public void setIs_online(int is_online) {
		this.is_online = is_online;
	}

	public long getUpdate_time() {
		return update_time.getTime() / 1000L;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public String getDirect_sales() {
		return direct_sales;
	}

	public void setDirect_sales(String direct_sales) {
		this.direct_sales = direct_sales;
	}
	
	

	
}
