package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Poi;
import com.google.common.base.Strings;


@Repository("poiDao")
@SuppressWarnings("unchecked")
public class PoiDaoImpl extends BaseDaoImpl<Poi, Long> implements PoiDao {

	public PoiDaoImpl() {
		super(Poi.class);
	}

	@Override
	public List<Poi> getPoiList(int count, int page) {
		String hql = "from Poi where 1=1 order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		int start = 0;
		if(page >= 1){
			start = (page-1) * count;
		}else{
			start = 0;
		}
		Query query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public int getPoiCount() {
		String hql = "select count(*) from Poi";
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
	public List<Poi> getPoiListByParams(int count, int page,
			String poi_name, String is_online, String classify_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		List<Poi> list = null;
		int start = 0;
		if(page >= 1){
			start =(page-1) * count;
		}
		if(!Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and is_online=? and classify.id=? and title like :title order by id";
			Query query = session.createQuery(hql).setInteger(0, online).setLong(1,classifyId)
					.setString("title","%"+poi_name+"%").setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and is_online=? and classify.id=? order by id";
			Query query = session.createQuery(hql).setInteger(0, online)
					.setLong(1,classifyId).setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and classify.id=? order by id";
			Query query = session.createQuery(hql).setLong(0,classifyId)
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			hql = "from Poi where 1=1 and is_online=? order by id";
			Query query = session.createQuery(hql).setInteger(0, online)
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			hql = "from Poi where 1=1 and is_online=? and title like :title order by id";
			Query query = session.createQuery(hql).setInteger(0, online)
					.setString("title","%"+poi_name+"%").setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			hql = "from Poi where 1=1 and title like :title order by id";
			Query query = session.createQuery(hql).setString("title","%"+poi_name+"%")
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and classify.id=? and title like :title order by id";
			Query query = session.createQuery(hql).setLong(0, classifyId).setString("title","%"+poi_name+"%")
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			hql = "from Poi order by id";
			Query query = session.createQuery(hql)
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}
		return list;
	}

	@Override
	public int getCountByParams(String poi_name, String is_online, String classify_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		List<Poi> list = null;
		if(!Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and is_online=? and classify.id=? and title like :title order by id";
			Query query = session.createQuery(hql).setInteger(0, online).setLong(1,classifyId).setString("title","%"+poi_name+"%");
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and is_online=? and classify.id=? order by id";
			Query query = session.createQuery(hql).setInteger(0, online).setLong(1,classifyId);
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and classify.id=? order by id";
			Query query = session.createQuery(hql).setLong(0,classifyId);
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			hql = "from Poi where 1=1 and is_online=? order by id";
			Query query = session.createQuery(hql).setInteger(0, online);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(poi_name) 
				&& !Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			hql = "from Poi where 1=1 and is_online=? and title like :title order by id";
			Query query = session.createQuery(hql).setInteger(0, online).setString("title","%"+poi_name+"%");
			list = query.list();
		}else if(!Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			hql = "from Poi where 1=1 and title like :title order by id";
			Query query = session.createQuery(hql).setString("title","%"+poi_name+"%");
			list = query.list();
		}else if(!Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& !Strings.isNullOrEmpty(classify_id)){
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and classify.id=? and title like :title order by id";
			Query query = session.createQuery(hql).setLong(0, classifyId).setString("title","%"+poi_name+"%");
			list = query.list();
		}else if(Strings.isNullOrEmpty(poi_name) 
				&& Strings.isNullOrEmpty(is_online) 
				&& Strings.isNullOrEmpty(classify_id)){
			hql = "from Poi order by id";
			Query query = session.createQuery(hql);
			list = query.list();
		}
		
		int count = list.size();
		return count;
	}

	@Override
	public List<Poi> getPoiListByParams(String is_online, String classify_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		List<Poi> list = null;
		if(!Strings.isNullOrEmpty(is_online) && !Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and is_online=? and classify.id=? order by id";
			Query query = session.createQuery(hql).setInteger(0, online).setLong(1,classifyId);
			list = query.list();
		}else if(Strings.isNullOrEmpty(is_online) && !Strings.isNullOrEmpty(classify_id)){
			Long classifyId = Long.parseLong(classify_id);
			hql = "from Poi where 1=1 and classify.id=? order by id";
			Query query = session.createQuery(hql).setLong(0,classifyId);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(is_online) && Strings.isNullOrEmpty(classify_id)){
			int online = Integer.parseInt(is_online);
			hql = "from Poi where 1=1 and is_online=? order by id";
			Query query = session.createQuery(hql).setInteger(0, online);
			list = query.list();
		}
		return list;
	}

	@Override
	public List<Poi> getPoiListByTitle(String title) {
		String hql = "from Poi where 1=1 and title like :title order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString("title","%"+title+"%");
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByClassify(Long classify_id, int count) {
		String hql = "from Poi where 1=1 and classify.id=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,classify_id).setMaxResults(count);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByClassify(Long classify_id, int count, Long id) {
		String hql = "from Poi where 1=1 and classify.id=? and id < ? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,classify_id).setLong(1, id).setMaxResults(count);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByHot() {
		String hql = "from Poi where 1=1 and hot = 1 order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByClassify_idAndIs_online(int is_online, Long classify_id) {
		String hql = "from Poi where 1=1 and classify.id=? and is_online=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,classify_id).setInteger(1, is_online);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiList() {
		String hql = "from Poi where 1=1 and classify.type != 'normal' order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByTicket(int count,String title) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		Query query = null;
		if(!Strings.isNullOrEmpty(title)){
			hql = "select p from Poi p,Ticket t where 1=1 and p.is_online=1 and p.id=t.poi.id and p.title like :title group by p.id";
			query = session.createQuery(hql).setString("title", "%"+title+"%").setMaxResults(count);
		}else{
			hql = "select p from Poi p,Ticket t where 1=1 and p.is_online=1 and p.id=t.poi.id group by p.id";
			query = session.createQuery(hql).setMaxResults(count);
		}
		
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByTicket(int count, Long poi_id,String title) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		Query query = null;
		if(!Strings.isNullOrEmpty(title)){
			hql = "select p from Poi p,Ticket t where 1=1 and p.is_online=1 and p.id=t.poi.id and p.id > ? and p.title like :title group by p.id";
			query = session.createQuery(hql).setLong(0, poi_id).setString("title", "%"+title+"%").setMaxResults(count);
		}else{
			hql = "select p from Poi p,Ticket t where 1=1 and p.is_online=1 and p.id=t.poi.id and p.id > ? group by p.id";
			query = session.createQuery(hql).setLong(0, poi_id).setMaxResults(count);
		}
		List<Poi> list = query.list();
		return list;
	}

	@Override
	public List<Poi> getPoiListByClassify() {
		String hql = "from Poi where 1=1 and classify.type = 'hotel' order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		List<Poi> list = query.list();
		return list;
	}

}
