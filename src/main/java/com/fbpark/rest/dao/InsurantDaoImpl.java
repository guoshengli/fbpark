package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Insurant;
@Repository("insurantDao")
public class InsurantDaoImpl extends BaseDaoImpl<Insurant, Long> implements InsurantDao {

	public InsurantDaoImpl() {
		super(Insurant.class);
	}

	@Override
	public List<Insurant> getInsurantByIdentityCard(String identity_card) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Insurant where 1=1 and identity_card=?";
		Query query = session.createQuery(hql).setString(0,identity_card);
		return query.list();
	}

//	@Override
//	public List<Insurant> getInsurantByIdentityCards(String identity_cards) {
//		Session session = getSessionFactory().getCurrentSession();
//		String hql = "from Insurant where 1=1 and identity_card in(:idcard)";
//		Query query = session.createQuery(hql).setParameter("idcard",identity_cards);
//		List<Insurant> list = query.list();
//		return list;
//	}

	@Override
	public List<Insurant> getInsurantByIdentityCards(List<String> identity_cards) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Insurant where 1=1 and identity_card in(:idcard)";
		Query query = session.createQuery(hql).setParameterList("idcard",identity_cards);
		List<Insurant> list = query.list();
		return list;
	}
	
	
}
