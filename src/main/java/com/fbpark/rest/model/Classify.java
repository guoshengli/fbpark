package com.fbpark.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table(name="classify")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Classify extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 840851953967770798L;
	
	@Column(name="classify_name")
	private String classify_name;
	
	//是否上线 0 没上线 1 上线
	@Column(name="online")
	private int online;
	
	//是否显示在首页 0 不显示 1显示
	@Column(name="homepage")
	private int homepage;
	
	//type 对应parking（停车券） tickets（票） hotel（住宿券） normal（普通不会产生票）
	@Column(name="type")
	private String type;
	
	@OneToMany(mappedBy = "classify", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Poi> pois = new ArrayList<Poi>();

	public String getClassify_name() {
		return classify_name;
	}

	public void setClassify_name(String classify_name) {
		this.classify_name = classify_name;
	}

	public List<Poi> getPois() {
		return pois;
	}

	public void setPois(List<Poi> pois) {
		this.pois = pois;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getHomepage() {
		return homepage;
	}

	public void setHomepage(int homepage) {
		this.homepage = homepage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
