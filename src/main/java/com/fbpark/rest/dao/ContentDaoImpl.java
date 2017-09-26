package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Content;
@Repository("contentDao")
@SuppressWarnings("unchecked")
public class ContentDaoImpl extends BaseDaoImpl<Content, Long> implements ContentDao {

	public ContentDaoImpl() {
		super(Content.class);
	}

	public List<Content> getContentByRegionId(Long region_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Content where region_id=?";
		Query query = session.createQuery(hql).setLong(0, region_id);
		List<Content> contentList = query.setMaxResults(3).list();
		return contentList;
	}

	public List<Content> getAllContentByRegionId(Long region_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Content where region_id=?";
		Query query = session.createQuery(hql).setLong(0, region_id);
		List<Content> contentList = query.list();
		return contentList;
	}

	@Override
	public List<Content> getContentList(int count, int page) {
		String hql = "from Content where 1=1 and type=0 and status='enable' order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		int start= 0;
		if(page == 0){
			start = page * count;
		}else{
			start = (page-1) * count;
		}
		Query query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
		List<Content> list = query.list();
		return list;
	}

	@Override
	public int getContentCount() {

		String hql = "select count(*) from Content where 1=1 and type=0 and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		List list = query.list();
		int count = 0;
		if (list != null && list.size() > 0) {
			count = Integer.parseInt(list.get(0).toString());
		}
		return count;
	
	}

	@Override
	public List<Content> getContentListByTitle(int count, int page, String title) {
		String hql = "from Content where 1=1 and type=0 and status='enable' and title like :title order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		int start= 0;
		if(page == 0){
			start = page * count;
		}else{
			start = (page-1) * count;
		}
		Query query = session.createQuery(hql).setString("title", "%"+title+"%").setFirstResult(start).setMaxResults(count);
		List<Content> list = query.list();
		return list;
	}

	@Override
	public int getContentCount(String title) {
		String hql = "select count(*) from Content where 1=1 and type=0 and status='enable' and title like :title";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString("title", "%"+title+"%");
		int count = 0;
		List list = query.list();
		if (list != null && list.size() > 0) {
			count = Integer.parseInt(list.get(0).toString());
		}
		return count;
	}

	

}
