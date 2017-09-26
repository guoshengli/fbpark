package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.News;
@Repository("newsDao")
public class NewsDaoImpl extends BaseDaoImpl<News, Long> implements NewsDao {

	public NewsDaoImpl() {
		super(News.class);
	}

	@Override
	public List<News> getNewsBySequence() {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from News order by sequence";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	

}
