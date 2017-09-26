package com.fbpark.rest.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.ContentElement;
@Repository("contentElementDao")
public class ContentElementDaoImpl extends BaseDaoImpl<ContentElement, Long> implements ContentElementDao {

	public ContentElementDaoImpl() {
		super(ContentElement.class);
	}

	@Override
	public void deleteContentElementByContentId(Long content_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from ContentElement where 1=1 and content.id=?";
		Query query = session.createQuery(hql).setLong(0,content_id);
		query.executeUpdate();
	}

	

}
