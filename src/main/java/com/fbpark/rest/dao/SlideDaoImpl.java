package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Slide;
@Repository("slideDao")
public class SlideDaoImpl extends BaseDaoImpl<Slide, Long> implements SlideDao {

	public SlideDaoImpl() {
		super(Slide.class);
	}

	@Override
	public List<Slide> getSlideByStatus(String status) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Slide where 1=1 and status=? order by sequence";
		Query query = session.createQuery(hql).setString(0, status);
		
		return query.list();
	}

	

}
