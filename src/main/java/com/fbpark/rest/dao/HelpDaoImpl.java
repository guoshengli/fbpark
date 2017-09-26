package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Help;
@Repository("helpDao")
public class HelpDaoImpl extends BaseDaoImpl<Help, Long> implements HelpDao {

	public HelpDaoImpl() {
		super(Help.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Help> getHelpBySequence() {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Help where 1=1 and content.status='enable' order by sequence";
		Query query = session.createQuery(hql);
		List<Help> helpList = query.list();
		return helpList;
	}

	

}
