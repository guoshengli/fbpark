package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Ad;
@Repository("adDao")
public class AdDaoImpl extends BaseDaoImpl<Ad, Long> implements AdDao {

	public AdDaoImpl() {
		super(Ad.class);
	}
	
	@Override
	public List<Ad> getAdByStatus(String status) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ad where 1=1 and status=? order by sequence";
		Query query = session.createQuery(hql).setString(0, status);
		
		return query.list();
	}

	@Override
	public Ad getAdByAdType(int ad_type) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ad order by id";
		Query query = session.createQuery(hql);
		List<Ad> adList = query.list();
		Ad ad = null;
		if(adList != null && adList.size() > 0){
			ad = adList.get(0);
		}
		return ad;
	}
	
}
