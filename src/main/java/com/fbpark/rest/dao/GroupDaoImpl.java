package com.fbpark.rest.dao;

import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Group;
@Repository("groupDao")
public class GroupDaoImpl extends BaseDaoImpl<Group, Long> implements GroupDao {

	public GroupDaoImpl() {
		super(Group.class);
	}
	
}
