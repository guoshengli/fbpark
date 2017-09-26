package com.fbpark.rest.service.model;

import java.io.Serializable;

public class Zyb implements Serializable {

	private static final long serialVersionUID = 6782568447504985226L;
	
	private String corpCode;
	
	private String userName;
	
	private String masterSecret;
	
	private String url;
	
	private String noramlZybCode;
	
	private String vipZybCode;

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNoramlZybCode() {
		return noramlZybCode;
	}

	public void setNoramlZybCode(String noramlZybCode) {
		this.noramlZybCode = noramlZybCode;
	}

	public String getVipZybCode() {
		return vipZybCode;
	}

	public void setVipZybCode(String vipZybCode) {
		this.vipZybCode = vipZybCode;
	}
	
	
	
}
