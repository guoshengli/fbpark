package com.fbpark.rest.dao;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.PoiElement;
@Repository("poiElementDao")
public class PoiElementDaoImpl extends BaseDaoImpl<PoiElement, Long> implements PoiElementDao {

	public PoiElementDaoImpl() {
		super(PoiElement.class);
	}

	@Override
	public void deletePoiElementByPoiId(Long poiId) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from PoiElement where 1=1 and poi.id=?";
		Query query = session.createQuery(hql).setLong(0, poiId);
		query.executeUpdate();
	}

}
