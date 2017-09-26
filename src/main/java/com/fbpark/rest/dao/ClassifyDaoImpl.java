package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Classify;
@Repository("classifyDao")
@SuppressWarnings("unchecked")
public class ClassifyDaoImpl extends BaseDaoImpl<Classify, Long> implements ClassifyDao {

	public ClassifyDaoImpl() {
		super(Classify.class);
	}

	@Override
	public List<Classify> getClassifyByOnlineAndHomepage() {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Classify where 1=1 and online=1 and homepage=1 order by sequence";
		Query query = session.createQuery(hql);
		List<Classify> cList = query.list();
		return cList;
	}

	

}
