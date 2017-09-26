package com.fbpark.rest.dao;

import java.util.List;


import com.fbpark.rest.model.Ad;

public interface AdDao extends BaseDao<Ad, Long> {
	public List<Ad> getAdByStatus(String status);
	
	public Ad getAdByAdType(int ad_type);
}
