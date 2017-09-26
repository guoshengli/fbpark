package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.LinkAccounts;

@Repository("linkAccountsDao")
@SuppressWarnings("unchecked")
public class LinkAccountsDaoImpl extends BaseDaoImpl<LinkAccounts, Long>implements LinkAccountsDao {
	public LinkAccountsDaoImpl() {
		super(LinkAccounts.class);
	}
	

	public Object[] getLinkAccountsByUUID(String uuid) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from LinkAccounts la,User u where la.union_id=? and la.user_id=u.id and u.status='enable'";
		Query query = session.createQuery(hql).setString(0, uuid);
		
		List<Object[]> list = query.list();
		Object[] la = null;
		if ((list != null) && (list.size() > 0)) {
			la =  list.get(0);
		}
		return la;
	}

	public List<LinkAccounts> getLinkAccountsByUserid(Long userid) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from LinkAccounts where user_id=?";
		Query query = session.createQuery(hql).setLong(0, userid.longValue());
		List<LinkAccounts> list = query.list();
		return list;
	}

	public void deleteLinkAccountsByServiceAndUserid(String service, Long userid) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from LinkAccounts where user_id=? and service=?";
		Query query = session.createQuery(hql).setLong(0, userid.longValue()).setString(1, service);
		query.executeUpdate();
	}

}
