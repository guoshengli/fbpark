package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Notice;
@Repository("noticeDao")
@SuppressWarnings("unchecked")
public class NoticeDaoImpl extends BaseDaoImpl<Notice, Long> implements NoticeDao {

	public NoticeDaoImpl() {
		super(Notice.class);
	}

	@Override
	public List<Notice> getNoticeList(int count, int page) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Notice order by create_time desc";
		int start = (page-1) * count +1;
		Query query = session.createQuery(hql).setFirstResult(start).setMaxResults(page);
		List<Notice> noticeList = query.list();
		return noticeList;
	}

	@Override
	public int getNoticeCount() {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "select count(1) from Notice";
		Query query = session.createQuery(hql);
		List noticeList = query.list();
		int count = 0;
		if(noticeList != null && noticeList.size() > 0){
			count = Integer.parseInt(noticeList.get(0).toString());
		}
		return count;
	}

	@Override
	public List<Notice> getNoticeListByTitle(int count, int page, String title) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Notice where 1=1 and title like :title order by create_time desc";
		int start = (page-1) * count +1;
		Query query = session.createQuery(hql).setString("title", "%"+title+"%")
				.setFirstResult(start).setMaxResults(page);
		List<Notice> noticeList = query.list();
		return noticeList;
	}

	@Override
	public int getNoticeCountByTitle(String title) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "select count(1) from Notice where 1=1 and title like :title";
		Query query = session.createQuery(hql).setString("title", "%"+title+"%");
		List noticeList = query.list();
		int count = 0;
		if(noticeList != null && noticeList.size() > 0){
			count = Integer.parseInt(noticeList.get(0).toString());
		}
		return count;
	}

	@Override
	public Notice getNoticeByStatus(int status) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Notice where 1=1 and status=?";
		Query query = session.createQuery(hql).setInteger(0, status);
		List<Notice> noticeList = query.list();
		Notice notice = null;
		if(noticeList != null && noticeList.size() > 0){
			notice = noticeList.get(0);
		}
		return notice;
	}

}
