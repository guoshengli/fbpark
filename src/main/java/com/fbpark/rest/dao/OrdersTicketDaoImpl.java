package com.fbpark.rest.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.OrdersTicket;
import com.google.common.base.Strings;
@Repository("ordersTicketDao")
@SuppressWarnings("unchecked")
public class OrdersTicketDaoImpl extends BaseDaoImpl<OrdersTicket, Long> implements OrdersTicketDao {

	public OrdersTicketDaoImpl() {
		super(OrdersTicket.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public List<OrdersTicket> getOrdersTicketByOrdersId(Long ordersId) {
		String hql = "from OrdersTicket where 1=1 and orders.id=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,ordersId);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketByUserid(Long user_id, int count,int status) {
		String hql = "from OrdersTicket where 1=1 and orders.user.id=? and status=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,user_id).setInteger(1, status).setMaxResults(count);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketByUserid(Long user_id, int count, Long orderTicketId,int status) {
		String hql = "from OrdersTicket where 1=1 orders.user.id=? and id < ? and status=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,user_id).setLong(1, orderTicketId)
				.setInteger(2, status).setMaxResults(count);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByParams(int count, String status, String start_date, String end_date,
			String username) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(username)){
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setString("username","%"+username+"%").setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status)).setString("username","%"+username+"%").setMaxResults(count);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and update_time between :startDate and :endDate and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
						.setString("username", "%"+username+"%").setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? and update_time between :startDate and :endDate and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date)
						.setString("username", "%"+username+"%").setMaxResults(count);
			}
		}else{
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket order by update_time desc";
				query = session.createQuery(hql).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status)).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and update_time between :startDate and :endDate order by update_time desc";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
						.setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? and update_time between :startDate and :endDate order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date).setMaxResults(count);
			}
		}
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByParams(int count, int page, String status, String start_date,
			String end_date, String username) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		int start = page * count + 1;
		if(!Strings.isNullOrEmpty(username)){
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setString("username","%"+username+"%").setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status)).setString("username","%"+username+"%").setFirstResult(start).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and create_date between :startDate and :endDate and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
						.setString("username", "%"+username+"%").setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? and create_date between :startDate and :endDate and orders.user.username like :username order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date)
						.setString("username", "%"+username+"%").setFirstResult(start).setMaxResults(count);
			}
		}else{
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket order by update_time desc";
				query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status)).setFirstResult(start).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and create_date between :startDate and :endDate order by update_time desc";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
						.setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from OrdersTicket where 1=1 and status=? and create_date between :startDate and :endDate order by update_time desc";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date)
						.setFirstResult(start).setMaxResults(count);
			}
		}
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public Object[] getOrdersTicketCountAndAmountByParams(String status, String start_date, String end_date,
			String username) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(username)){
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and orders.user.username like :username";
				query = session.createQuery(hql).setString("username","%"+username+"%");
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? and orders.user.username like :username";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status)).setString("username","%"+username+"%");
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and update_time between :startDate and :endDate and orders.user.username like :username";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
						.setString("username", "%"+username+"%");
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? and update_time between :startDate and :endDate and orders.user.username like :username";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date)
						.setString("username", "%"+username+"%");
			}
		}else{
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket";
				query = session.createQuery(hql);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=?";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status));
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and update_time between :startDate and :endDate";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? and update_time between :startDate and :endDate";
				query = session.createQuery(hql).setInteger(0,Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date);
			}
		}
		
		List<Object[]> list = query.list();
		Object[] obj = null;
		if(list != null && list.size() > 0){
			obj = list.get(0);
		}
		return obj;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByStatistics(int count, int page, String poiId, String type,
			String search) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		int start= 0;
		if(page == 0){
			start = page * count;
		}else{
			start = (page-1) * count;
		}
		if(!Strings.isNullOrEmpty(search)){
			if(Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setString("search","%"+search+"%").setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and ticket.poi.id=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setLong(0,Long.parseLong(poiId)).setString("search","%"+search+"%").setFirstResult(start).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type))
						.setString("search", "%"+search+"%").setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? and ticket.poi.id=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type)).setLong(1, Long.parseLong(poiId))
						.setString("search", "%"+search+"%").setFirstResult(start).setMaxResults(count);
			}
		}else{
			if(Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 order by update_time desc";
				query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and ticket.poi.id=? order by update_time desc";
				query = session.createQuery(hql).setLong(0,Long.parseLong(poiId)).setFirstResult(start).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type))
						.setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? and ticket.poi.id=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type)).setLong(1, Long.parseLong(poiId))
						.setFirstResult(start).setMaxResults(count);
			}
		}
		List<OrdersTicket> list = query.list();
		return list;
	}
	
	@Override
	public List<OrdersTicket> getOrdersTicketListByStatistics(String poiId, String type,
			String search) {

		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(search)){
			if(Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setString("search","%"+search+"%");
			}else if(!Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and ticket.poi.id=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setLong(0,Long.parseLong(poiId)).setString("search","%"+search+"%");
			}else if(Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type))
						.setString("search", "%"+search+"%");
			}else if(!Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? and ticket.poi.id=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type)).setLong(1, Long.parseLong(poiId))
						.setString("search", "%"+search+"%");
			}
		}else{
			if(Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 order by update_time desc";
				query = session.createQuery(hql);
			}else if(!Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and ticket.poi.id=? order by update_time desc";
				query = session.createQuery(hql).setLong(0,Long.parseLong(poiId));
			}else if(Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type));
			}else if(!Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "from OrdersTicket where 1=1 and status=? and ticket.poi.id=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type)).setLong(1, Long.parseLong(poiId));
			}
		}
		List<OrdersTicket> list = query.list();
		return list;
	
	}


	@Override
	public Object[] getOrdersTicketCountAndAmountByStatistics(String poiId, String type, String search) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(search)){
			if(Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setString("search","%"+search+"%");
			}else if(!Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and ticket.poi.id=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setLong(0,Long.parseLong(poiId)).setString("search","%"+search+"%");
			}else if(Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type))
						.setString("search", "%"+search+"%");
			}else if(!Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? and ticket.poi.id=? and ticket.ticket_name like :search order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type)).setLong(1, Long.parseLong(poiId))
						.setString("search", "%"+search+"%");
			}
		}else{
			if(Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 order by update_time desc";
				query = session.createQuery(hql);
			}else if(!Strings.isNullOrEmpty(poiId) && Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and ticket.poi.id=? order by update_time desc";
				query = session.createQuery(hql).setLong(0,Long.parseLong(poiId));
			}else if(Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type));
			}else if(!Strings.isNullOrEmpty(poiId) && !Strings.isNullOrEmpty(type)){
				hql = "select count(1),sum(total) from OrdersTicket where 1=1 and status=? and ticket.poi.id=? order by update_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(type)).setLong(1, Long.parseLong(poiId));
			}
		}
		List<Object[]> list = query.list();
		Object[] obj = null;
		if(list != null && list.size() > 0){
			obj = list.get(0);
		}
		return obj;
	}


	@Override
	public OrdersTicket getOrdersTicketByOrdersNoAndRefundId(String orders_no, String refund_id) {
		String hql = "from OrdersTicket where 1=1 and orders.order_no=? and refund_id = ?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,orders_no).setString(1, refund_id);
		List<OrdersTicket> list = query.list();
		OrdersTicket ot = null;
		if(list != null && list.size() > 0){
			ot = list.get(0);
		}
		return ot;
	}


	@Override
	public OrdersTicket getOrdersTicketByOrdersNo(String orders_no) {
		String hql = "from OrdersTicket where 1=1 and ticket_order_no=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,orders_no);
		List<OrdersTicket> list = query.list();
		OrdersTicket ot = null;
		if(list != null && list.size() > 0){
			ot = list.get(0);
		}
		return ot;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketByOrdersId(Long ordersId, int status) {
		String hql = "from OrdersTicket where 1=1 and orders.id=? and status=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,ordersId).setInteger(1, status);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketByStatusAndUserid(Long user_id, int count) {
		String hql = "from OrdersTicket where 1=1 and orders.user.id=? and status in (0,2,4,5) order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,user_id).setMaxResults(count);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketByStatusAndUserid(Long user_id, int count, Long orderTicketId) {
		String hql = "from OrdersTicket where 1=1 and orders.user.id=? and id < ? and status in (0,2,4,5) order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setLong(0,user_id).setLong(1, orderTicketId).setMaxResults(count);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public int getOrdersTicketCountAndAmountByInsurant(String start_date, String end_date, String username) {

		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
			hql = "select count(1) from OrdersTicket ot where 1=1 and transNo != ''";
			query = session.createQuery(hql);
		}else if(!Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(start_date) 
				&& Strings.isNullOrEmpty(end_date)){
			hql = "select count(1) from OrdersTicket ot where 1=1 and transNo != '' and contacter.name like :username";
			query = session.createQuery(hql).setString("username","%"+username+"%");
		}else if(Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "select count(1) from OrdersTicket ot where 1=1 and transNo != '' and update_time between :start_date and :end_date";
			query = session.createQuery(hql).setString("start_date", start_date).setString("end_date", end_date);
		}else if(!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "select count(1) from OrdersTicket ot where 1=1 and contacter.name like :username and transNo != '' and update_time between :start_date and :end_date";
			query = session.createQuery(hql).setString("username", username).setString("start_date", start_date).setString("end_date", end_date);
		}
		List<Object> list = query.list();
		Object obj = null;
		int count = 0;
		if(list != null && list.size() > 0){
			obj = list.get(0);
			count = Integer.parseInt(obj.toString());
		}
		return count;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByInsurant(int count, int page, String start_date, String end_date,
			String username) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		int start= 0;
		if(page == 0){
			start = page * count;
		}else{
			start = (page-1) * count;
		}
		if(Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and transNo != '' order by update_time desc";
			query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
		}else if(!Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(start_date) 
				&& Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and transNo != '' and contacter.name like :username order by update_time desc";
			query = session.createQuery(hql).setString("username","%"+username+"%").setFirstResult(start).setMaxResults(count);
		}else if(Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and transNo != '' and update_time between :start_date and :end_date order by update_time desc";
			query = session.createQuery(hql).setString("start_date", start_date).setString("end_date", end_date)
					.setFirstResult(start).setMaxResults(count);
		}else if(!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and update_time between :start_date and :end_date and contacter.name like :username order by update_time desc";
			query = session.createQuery(hql)
					.setString("start_date", start_date).setString("end_date", end_date)
					.setString("username", "%"+username+"%").setFirstResult(start).setMaxResults(count);
		}
		List<OrdersTicket> otList = query.list();
		return otList;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByInsurant(String start_date, String end_date, String username) {

		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and transNo != '' order by update_time desc";
			query = session.createQuery(hql);
		}else if(!Strings.isNullOrEmpty(username) && Strings.isNullOrEmpty(start_date) 
				&& Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and transNo != '' and contacter.name like :username order by update_time desc";
			query = session.createQuery(hql).setString("username","%"+username+"%");
		}else if(Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and transNo != '' and update_time between :startDate and :endDate order by update_time desc";
			query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date);
		}else if(!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "from OrdersTicket where 1=1 and startDate=? and endDate=? and contacter.name like :username order by update_time desc";
			query = session.createQuery(hql)
					.setString(0, start_date).setString(1, end_date)
					.setString("username", "%"+username+"%");
		}
		
		List<OrdersTicket> otList = query.list();
		return otList;
	
	}


	@Override
	public OrdersTicket getOrdersTicketListByIdcardAndVerify(String identity_card, String verify,Long poi_id) {
		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=? and verification=? and ticket.poi.id=? and status=0";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card).setString(1, verify).setLong(2, poi_id);
		List<OrdersTicket> list = query.list();
		OrdersTicket ot = null;
		if(list != null && list.size() > 0){
			ot = list.get(0);
		}
		return ot;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByIdcardAndTicket(String identity_card, Long ticket_id) {
		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=? and ticket.id=? and status=0";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card).setLong(1, ticket_id);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByIdcardAndVerify(String identity_card, String verify) {
		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=? and verification=? and status=0";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card).setString(1, verify);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByIdcard(String identity_card) {

		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=? and status=0";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card);
		List<OrdersTicket> list = query.list();
		return list;
	
	}
	
	@Override
	public List<OrdersTicket> getOrdersTicketListByIdcardAndStatus(String identity_card) {

		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=? and status=8";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card);
		List<OrdersTicket> list = query.list();
		return list;
	
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByIdcardAll(String identity_card) {
		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card);
		List<OrdersTicket> list = query.list();
		return list;
	}


	@Override
	public List<OrdersTicket> getOrdersTicketListByIdcardAndOnlineAndOffline(String identity_card) {
		String hql = "from OrdersTicket where 1=1 and contacter.identity_card=? and status in (0,8,1)";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0,identity_card);
		List<OrdersTicket> list = query.list();
		return list;
	}




}
