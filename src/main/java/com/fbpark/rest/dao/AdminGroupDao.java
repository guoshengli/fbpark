package com.fbpark.rest.dao;

import com.fbpark.rest.model.AdminGroup;

public interface AdminGroupDao extends BaseDao<AdminGroup, Long> {
	public void deleteAdminGroup(Long admin_id);
	
	public void deleteAdminGroupByGroupId(Long groupId);
	
	public AdminGroup getAdminGroupByAdminIdAndGroupId(Long admin_id,Long group_id);
}
