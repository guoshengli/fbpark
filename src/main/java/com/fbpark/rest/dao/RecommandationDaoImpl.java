package com.fbpark.rest.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.Recommandation;
@Repository("recommandationDao")
@SuppressWarnings("unchecked")
public class RecommandationDaoImpl extends BaseDaoImpl<Recommandation, Long> implements RecommandationDao {

	public RecommandationDaoImpl() {
		super(Recommandation.class);
	}

	@Override
	public List<Poi> getRecommandationListByPoiId(Long poiId) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "select r.pk.reference from Recommandation r where 1=1 and r.pk.poi.id=?";
		Query query = session.createQuery(hql).setLong(0, poiId);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public void deleteRecommandationByPoiReference(Long poiId, Long referenceId) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from Recommandation r where 1=1 and r.pk.poi.id=? and r.pk.reference.id=?";
		Query query = session.createQuery(hql).setLong(0, poiId).setLong(1, referenceId);
		query.executeUpdate();
	}

	@Override
	public void deleteRecommandationByPoi(Long poiId) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from Recommandation r where 1=1 and r.pk.poi.id=?";
		Query query = session.createQuery(hql).setLong(0, poiId);
		query.executeUpdate();
	}

	

}
