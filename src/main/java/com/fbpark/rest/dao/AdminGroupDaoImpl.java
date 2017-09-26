package com.fbpark.rest.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.AdminGroup;
@Repository("adminGroupDao")
@SuppressWarnings("unchecked")
public class AdminGroupDaoImpl extends BaseDaoImpl<AdminGroup, Long> implements AdminGroupDao {

	public AdminGroupDaoImpl() {
		super(AdminGroup.class);
	}

	@Override
	public void deleteAdminGroup(Long admin_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from AdminGroup where 1=1 and admin.id=?";
		Query query = session.createQuery(hql).setLong(0, admin_id);
		query.executeUpdate();
	}

	@Override
	public void deleteAdminGroupByGroupId(Long groupId) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from AdminGroup where 1=1 and group.id=?";
		Query query = session.createQuery(hql).setLong(0, groupId);
		query.executeUpdate();
	}

	@Override
	public AdminGroup getAdminGroupByAdminIdAndGroupId(Long admin_id, Long group_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from AdminGroup where 1=1 and admin.id=? and group.id=?";
		Query query = session.createQuery(hql).setLong(0, admin_id).setLong(1, group_id);
		List<AdminGroup> agList = query.list();
		AdminGroup ag = null;
		if(agList != null && agList.size() > 0){
			ag = agList.get(0);
		}
		return ag;
	}

}
