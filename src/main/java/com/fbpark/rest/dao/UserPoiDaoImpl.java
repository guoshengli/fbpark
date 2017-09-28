package com.fbpark.rest.dao;


import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.UserPoi;
@Repository("userPoiDao")
public class UserPoiDaoImpl extends BaseDaoImpl<UserPoi, Long> implements UserPoiDao {

	public UserPoiDaoImpl() {
		super(UserPoi.class);
	}
	
}
