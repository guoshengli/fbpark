package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.Ticket;
import com.google.common.base.Strings;

@Repository("ticketDao")
@SuppressWarnings("unchecked")
public class TicketDaoImpl extends BaseDaoImpl<Ticket, Long> implements TicketDao {

	public TicketDaoImpl() {
		super(Ticket.class);
	}

	@Override
	public List<Ticket> getTicketList(int count) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and status='enable' and poi.id > 0 order by id desc";
		Query query = session.createQuery(hql).setMaxResults(count);
		List<Ticket> list = query.list();
		return list;
	}

	@Override
	public List<Ticket> getTicketList(int count, Long ticket_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and status='enable' and id < ? and poi.id > 0  order by id desc";
		Query query = session.createQuery(hql).setLong(0,ticket_id).setMaxResults(count);
		List<Ticket> list = query.list();
		return list;
	}

	@Override
	public List<Ticket> getTicketListByTicket(int count, String ticket_name) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and status='enable' and ticket_name like :ticket_name and poi.id > 0 order by id desc";
		Query query = session.createQuery(hql)
				.setString("ticket_name","%"+ticket_name+"%").setMaxResults(count);
		List<Ticket> list = query.list();
		return list;
	}

	@Override
	public List<Ticket> getTicketListByTicket(int count, Long ticket_id, String ticket_name) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and status='enable' and id< ? and ticket_name like :ticket_name and poi.id > 0  order by id desc";
		Query query = session.createQuery(hql).setLong(0,ticket_id)
				.setString("ticket_name","%"+ticket_name+"%").setMaxResults(count);
		List<Ticket> list = query.list();
		return list;
	}

	@Override
	public int getTicketByParamsCount(int count, int page, String ticket_name, String zyb_code, String poi_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		List<Ticket> list = null;
		if(!Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? and ticket_name like :ticket_name order by id";
			Query query = session.createQuery(hql).setLong(0,poiId)
					.setString("ticket_name","%"+ticket_name+"%");
			list = query.list();
		}else if(!Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? and ticket_name like :ticket_name and zyb_code like :zyb_code order by id";
			Query query = session.createQuery(hql).setLong(0,poiId).setString("ticket_name", "%"+ticket_name+"%")
					.setString("zyb_code","%"+zyb_code+"%");
			list = query.list();
		}else if(!Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' and ticket_name like :ticket_name order by id";
			Query query = session.createQuery(hql)
					.setString("ticket_name","%"+ticket_name+"%");
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? and zyb_code like :zyb_code order by id";
			Query query = session.createQuery(hql).setLong(0,poiId)
					.setString("zyb_code","%"+zyb_code+"%");
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? order by id";
			Query query = session.createQuery(hql).setLong(0,poiId);
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' and zyb_code like :zyb_code  order by id";
			Query query = session.createQuery(hql).setString("zyb_code","%"+zyb_code+"%");
			list = query.list();
		}else if(!Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' and zyb_code like :zyb_code and ticket_name like :ticket_name  order by id";
			Query query = session.createQuery(hql).setString("zyb_code","%"+zyb_code+"%").setString("ticket_name", "%"+ticket_name+"%");
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' order by id";
			Query query = session.createQuery(hql);
			list = query.list();
		}
		int c = 0;
		if(list != null){
			c = list.size();
		}
		return c;
	}

	@Override
	public List<Ticket> getTicketByParams(int count, int page, String ticket_name, String zyb_code, String poi_id) {

		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		List<Ticket> list = null;
		int start = 0;
		if(page >= 1){
			start =(page-1) * count;
		}
		if(!Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? and ticket_name like :ticket_name order by id";
			Query query = session.createQuery(hql).setLong(0,poiId)
					.setString("ticket_name","%"+ticket_name+"%").setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? and zyb_code like :zyb_code order by id";
			Query query = session.createQuery(hql).setLong(0,poiId)
					.setString("zyb_code","%"+zyb_code+"%").setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? and ticket_name like :ticket_name and zyb_code like :zyb_code order by id";
			Query query = session.createQuery(hql).setLong(0,poiId).setString("ticket_name", "%"+ticket_name+"%")
					.setString("zyb_code","%"+zyb_code+"%").setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& !Strings.isNullOrEmpty(poi_id)){
			Long poiId = Long.parseLong(poi_id);
			hql = "from Ticket where 1=1 and status='enable' and poi.id=? order by id";
			Query query = session.createQuery(hql).setLong(0,poiId)
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' and zyb_code like :zyb_code  order by id";
			Query query = session.createQuery(hql).setString("zyb_code","%"+zyb_code+"%")
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' and ticket_name like :ticket_name order by id";
			Query query = session.createQuery(hql).setString("ticket_name","%"+ticket_name+"%")
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(!Strings.isNullOrEmpty(ticket_name) 
				&& !Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' and ticket_name like :ticket_name and zyb_code like :zyb_code  order by id";
			Query query = session.createQuery(hql).setString("ticket_name","%"+ticket_name+"%").setString("zyb_code","%"+zyb_code+"%")
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}else if(Strings.isNullOrEmpty(ticket_name) 
				&& Strings.isNullOrEmpty(zyb_code) 
				&& Strings.isNullOrEmpty(poi_id)){
			hql = "from Ticket where 1=1 and status='enable' order by id";
			Query query = session.createQuery(hql)
					.setFirstResult(start).setMaxResults(count);
			list = query.list();
		}
		return list;
	
	}

	@Override
	public List<Ticket> getTicketByZybCode(String zybcode) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and zyb_code=? and status='enable' order by id desc";
		Query query = session.createQuery(hql).setString(0,zybcode);
		List<Ticket> list = query.list();
		return list;
	}

	@Override
	public void updateTicket(Long id, Long poi_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "update Ticket set poi.id=? where 1=1 and id=?";
		Query query = session.createQuery(hql).setLong(0,poi_id).setLong(1,id);
		query.executeUpdate();
	}

	@Override
	public int getTicketByNopoiCount() {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and status='enable' and poi.id is null";
		int c = 0;
		Query query = session.createQuery(hql);
		List<Ticket> list = query.list();
		if(list != null){
			c = list.size();
		}
		return c;
	}

	@Override
	public List<Ticket> getTicketByNopoi(int count, int page) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		List<Ticket> list = null;
		int start = 0;
		if(page >= 1){
			start =(page-1) * count;
		}
		hql = "from Ticket where 1=1 and status='enable' and poi.id is null order by id desc";
		Query query = session.createQuery(hql)
				.setFirstResult(start).setMaxResults(count);
		list = query.list();
		return list;
	}

	@Override
	public void updateTicket(Long id, String status) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		if(status.equals("disable")){
			hql = "update Ticket set poi.id=0,status=? where 1=1 and id=?";
		}else{
			hql = "update Ticket set status=? where 1=1 and id=?";
		}
		Query query = session.createQuery(hql).setString(0,status).setLong(1,id);
		query.executeUpdate();
	}

	@Override
	public List<Ticket> getTicketByTypeAndStatus(String type) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Ticket where 1=1 and status='enable' and type=?";
		Query query = session.createQuery(hql).setString(0, type);
		List<Ticket> list = query.list();
		return list;
	}
	
}
