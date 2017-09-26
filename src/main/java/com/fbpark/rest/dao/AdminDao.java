package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Admin;

public interface AdminDao extends BaseDao<Admin, Long> {
	public Admin getUserByUsernameAndPassword(String username,String password);
	
	public Admin getUserByUsername(String username);
	
	public List<Admin> getUserByCount(int count);
	
	public List<Admin> getUserByCount(int count,Long id);
	
	public Admin getAdminById(Long id);
	
	public List<Admin> getAdminList();
}
