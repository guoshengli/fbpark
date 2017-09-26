package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Contacter;
@Repository("contacterDao")
@SuppressWarnings("unchecked")
public class ContacterDaoImpl extends BaseDaoImpl<Contacter, Long> implements ContacterDao {

	public ContacterDaoImpl() {
		super(Contacter.class);
	}

	@Override
	public List<Contacter> getContacterList(Long userid) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Contacter where 1=1 and user.id=?";
		Query query = session.createQuery(hql).setLong(0, userid);
		List<Contacter> list = query.list();
		return list;
	}

	@Override
	public List<Contacter> getContacterByIdentityCard(String identify_card,Long user_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Contacter where 1=1 and identity_card=? and user.id=?";
		Query query = session.createQuery(hql).setString(0, identify_card).setLong(1, user_id);
		List<Contacter> list = query.list();
		return list;
	}

	

}
