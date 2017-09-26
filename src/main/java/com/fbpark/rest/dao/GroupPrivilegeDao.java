package com.fbpark.rest.dao;

import com.fbpark.rest.model.GroupPrivilege;

public interface GroupPrivilegeDao extends BaseDao<GroupPrivilege, Long> {
	public void deleteGroupPrivilegeByGroupId(Long group_id);
}
