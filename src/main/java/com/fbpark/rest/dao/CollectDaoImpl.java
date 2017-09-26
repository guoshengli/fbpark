package com.fbpark.rest.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Collect;
@Repository("collectDao")
@SuppressWarnings("unchecked")
public class CollectDaoImpl extends BaseDaoImpl<Collect, Long> implements CollectDao {

	public CollectDaoImpl() {
		super(Collect.class);
	}

	@Override
	public int getWishByUseridAndItemidAndType(Long userid, Long item_id, String type) {
		String hql = "select count(id) from Collect where 1=1 and user_id=? and reference_id=? and reference_type=?";
	    Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql).setLong(0,userid)
	    		.setLong(1,item_id).setString(2,type);
	    List list = query.list();
	    int count = 0;
	    if(list != null && list.size() > 0){
	    	count = (Integer)list.get(0);
	    }
		return count;
	}

	@Override
	public List<Collect> getCollectListByUserid(Long userid) {
		String hql = "from Collect where 1=1 and user_id=? order by create_time desc";
	    Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql).setLong(0,userid);
	    List<Collect> list = query.list();
		return list;
	}

	@Override
	public List<Collect> getCollectListByUseridAndCount(Long userid, int count) {
		String hql = "from Collect where 1=1 and user_id=? order by create_time desc";
	    Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql)
	    		.setLong(0,userid).setMaxResults(count);
	    List<Collect> list = query.list();
		return list;
	}

	@Override
	public List<Collect> getCollectListByUseridAndCountAndId(Long userid, int count, Long id) {
		String hql = "from Collect where 1=1 and user_id=? and id != ? order by create_time desc";
	    Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql)
	    		.setLong(0,userid)
	    		.setLong(1, id).setMaxResults(count);
	    List<Collect> list = query.list();
		return list;
	}

	@Override
	public Collect getCollectByUseridAndTypeAndReferenceid(Long userid, String type, Long referenceid) {
		String hql = "from Collect where 1=1 and user_id=? and reference_type = ? and reference_id=?";
	    Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql)
	    		.setLong(0,userid).setString(1, type)
	    		.setLong(2, referenceid);
	    List<Collect> list = query.list();
	    Collect collect = null;
	    if(list != null && list.size() > 0){
	    	collect = list.get(0);
	    }
		return collect;
	}

	@Override
	public void delCollectByUseridAndTypeAndReferenceid(Long userid, String type, Long referenceid) {
		String hql = "delete from Collect where 1=1 and user_id=? and reference_type = ? and reference_id=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql)
		    		.setLong(0,userid).setString(1, type)
		    		.setLong(2, referenceid);
		query.executeUpdate();
	}

}
