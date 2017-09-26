package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.ParkMap;
@Repository("parkMapDao")
public class ParkMapDaoImpl extends BaseDaoImpl<ParkMap, Long> implements ParkMapDao {

	public ParkMapDaoImpl() {
		super(ParkMap.class);
	}

	@Override
	public List<ParkMap> getParkMapList(String status) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from ParkMap where 1=1 and status=? order by id desc";
		Query query = session.createQuery(hql).setString(0, status);
		return query.list();
	}
	
	

}
