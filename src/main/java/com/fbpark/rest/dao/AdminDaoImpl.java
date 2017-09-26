package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Admin;

@Repository("adminDao")

@SuppressWarnings("unchecked")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

	public AdminDaoImpl() {
		super(Admin.class);
		// TODO Auto-generated constructor stub
	}

	public Admin getUserByUsernameAndPassword(String username, String password) {
		String hql = "from Admin where 1=1 and username=? and password=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, username).setString(1, password);

		List<Admin> adminList = query.list();
		Admin admin = null;
		if (adminList != null && adminList.size() > 0) {
			admin = adminList.get(0);
		}
		return admin;

	}

	public Admin getUserByUsername(String username) {
		String hql = "from Admin where 1=1 and username=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, username);

		List<Admin> adminList = query.list();
		Admin admin = null;
		if (adminList != null && adminList.size() > 0) {
			admin = adminList.get(0);
		}
		return admin;
	}

	@Override
	public List<Admin> getUserByCount(int count) {
		String hql = "from Admin where 1=1 and status='enable' order by id";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setMaxResults(count);
		List<Admin> adminList = query.list();
		return adminList;
	}

	@Override
	public List<Admin> getUserByCount(int count, Long id) {
		String hql = "from Admin where 1=1 and id > ? and status='enable' order by id";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0, id).setMaxResults(count);
		List<Admin> adminList = query.list();
		return adminList;
	}

	@Override
	public Admin getAdminById(Long id) {
		String hql = "from Admin where 1=1 and id = ? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0, id);
		List<Admin> adminList = query.list();
		Admin admin = null;
		if(adminList != null && adminList.size() > 0){
			admin = adminList.get(0);
		}
		return admin;
	}

	@Override
	public List<Admin> getAdminList() {
		String hql = "from Admin where 1=1 and status='enable' order by id";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		List<Admin> adminList = query.list();
		return adminList;
	}

}
