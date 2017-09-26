package com.fbpark.rest.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.GroupPrivilege;
@Repository("groupPrivilegeDao")
public class GroupPrivilegeDaoImpl extends BaseDaoImpl<GroupPrivilege, Long> implements GroupPrivilegeDao {

	public GroupPrivilegeDaoImpl() {
		super(GroupPrivilege.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void deleteGroupPrivilegeByGroupId(Long group_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from GroupPrivilege where 1=1 and group.id=?";
		Query query = session.createQuery(hql).setLong(0, group_id);
		query.executeUpdate();
	}

}
