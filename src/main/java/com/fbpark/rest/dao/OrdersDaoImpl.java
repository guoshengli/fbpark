package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Orders;
import com.google.common.base.Strings;

@Repository("ordersDao")
@SuppressWarnings("unchecked")
public class OrdersDaoImpl extends BaseDaoImpl<Orders, Long> implements OrdersDao {

	public OrdersDaoImpl() {
		super(Orders.class);
	}

	
	@Override
	public List<Orders> getOrderListByType(int count, int page, String type) {
		String hql = "";
		if(type.equals("unexpress")){//未邮递
			hql = "from Orders o where 1=1 and o.address.id > 0 and o.status='completed' order by create_time desc";
		}else if(type.equals("express")){//已邮递
			hql = "from Orders o where 1=1 and o.address.id > 0 and o.status='completed' and o.express_name != '' and o.express_no != '' order by create_time desc";
		}else if(type.equals("undelivery")){//待自提
			hql = "from Orders o where 1=1 and o.deliveryPoint.id > 0 and o.status='completed' order by create_time desc";
		}else if(type.equals("delivery")){//已自提
			hql = "from Orders o where 1=1 and o.deliveryPoint.id > 0 and o.status='delivery' order by create_time desc";
		}
		
		Session session = getSessionFactory().getCurrentSession();
	    int start = page * count + 1;
	    Query query = session.createQuery(hql)
	    		.setFirstResult(start).setMaxResults(count);
	    List<Orders> orderList = query.list();
		return orderList;
	}

	@Override
	public int getOrderListByType(String type) {
		String hql = "";
		if(type.equals("unexpress")){//未邮递
			hql = "from Orders o where 1=1 and o.address.id > 0 and o.status='completed' order by create_time desc";
		}else if(type.equals("express")){//已邮递
			hql = "from Orders o where 1=1 and o.address.id > 0 and o.status='completed' and o.express_name != '' and o.express_no != '' order by create_time desc";
		}else if(type.equals("undelivery")){//待自提
			hql = "from Orders o where 1=1 and o.deliveryPoint.id > 0 and o.status='completed' order by create_time desc";
		}else if(type.equals("delivery")){//已自提
			hql = "from Orders o where 1=1 and o.deliveryPoint.id > 0 and o.status='delivery' order by create_time desc";
		}
		
		Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    List<Orders> orderList = query.list();
		return orderList.size();
	}

	@Override
	public List<Orders> getOrderListByTypeAndCount(String type, int count,Long userid) {
		String hql = "from Orders where 1=1 and status=? and user_id=? order by create_time desc";
		Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql).setString(0, type).setLong(1,userid).setMaxResults(count);
	    List<Orders> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Orders> getOrderListByTypeAndCountAndId(String type, int count, Long id,Long userid) {
		String hql = "from Orders where 1=1 and status=? and id != ? and user_id=? order by create_time desc";
		Session session = getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql).setString(0, type)
	    		.setLong(1,id).setLong(2,userid).setMaxResults(count);
	    List<Orders> orderList = query.list();
		return orderList;
	}

	@Override
	public List<Orders> getOrdersListByParams(int count, String type, String start_date, String end_date) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(Strings.isNullOrEmpty(type) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
			hql = "from Orders order by create_time desc";
			query = session.createQuery(hql).setMaxResults(count);
		}else if(!Strings.isNullOrEmpty(type) && Strings.isNullOrEmpty(start_date) 
				&& Strings.isNullOrEmpty(end_date)){
			hql = "from Orders where 1=1 and status=? order by create_time desc";
			query = session.createQuery(hql).setString(0, type).setMaxResults(count);
		}else if(Strings.isNullOrEmpty(type) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "from Orders where 1=1 and create_date between :startDate and :endDate order by create_time desc";
			query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
					.setMaxResults(count);
		}else if(!Strings.isNullOrEmpty(type) && !Strings.isNullOrEmpty(start_date) 
				&& !Strings.isNullOrEmpty(end_date)){
			hql = "from Orders where 1=1 and status=? and create_date between :startDate and :endDate order by create_time desc";
			query = session.createQuery(hql).setString(0, type)
					.setString("startDate", start_date).setString("endDate", end_date).setMaxResults(count);
		}
		List<Orders> list = query.list();
		return list;
	}

	@Override
	public List<Orders> getOrdersListByParams(int count, int page, String status, String start_date, String end_date,String order_no) {
		int start= 0;
		if(page == 0){
			start = page * count;
		}else{
			start = (page-1) * count;
		}
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(order_no)){
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and order_no=? order by create_time desc";
				query = session.createQuery(hql).setString(0, order_no).setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? and order_no=? order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setString(1, order_no).setFirstResult(start).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and order_no=? and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setString(0,order_no).setString("startDate", start_date).setString("endDate", end_date)
												.setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? and order_no=? and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setString(1, order_no)
						.setString("startDate", start_date).setString("endDate", end_date)
						.setFirstResult(start).setMaxResults(count);
			}
		}else{
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from Orders order by create_time desc";
				query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setFirstResult(start).setMaxResults(count);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date)
												.setFirstResult(start).setMaxResults(count);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date)
						.setFirstResult(start).setMaxResults(count);
			}
		}
		
		List<Orders> list = query.list();
		return list;
	}
	
	@Override
	public List<Orders> getOrdersListByParams(String status, String start_date, String end_date,String order_no) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(order_no)){
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and order_no=? order by create_time desc";
				query = session.createQuery(hql).setString(0, order_no);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? and order_no=? order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setString(1, order_no);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and order_no=? and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setString(0,order_no).setString("startDate", start_date).setString("endDate", end_date);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? and order_no=? and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setString(1, order_no)
						.setString("startDate", start_date).setString("endDate", end_date);
			}
		}else{
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "from Orders order by create_time desc";
				query = session.createQuery(hql);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status));
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date);
			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "from Orders where 1=1 and status=? and create_time between :startDate and :endDate order by create_time desc";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date);
			}
		}
		
		List<Orders> list = query.list();
		return list;
	}

	@Override
	public Object[] getOrdersCountAndAmountByParams(String status, String start_date, String end_date,String order_no) {
		Session session = getSessionFactory().getCurrentSession();
		String hql =  "";
		Query query = null;
		if(!Strings.isNullOrEmpty(order_no)){
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and order_no=?";
				query = session.createQuery(hql).setString(0, order_no);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and status=? and order_no=?";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setString(1,order_no);
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and order_no=? and create_time between :startDate and :endDate";
				query = session.createQuery(hql).setString(0,order_no).setString("startDate", start_date).setString("endDate", end_date);

			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and status=? and order_no=? and create_time between :startDate and :endDate";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status)).setString(1,order_no)
						.setString("startDate", start_date).setString("endDate", end_date);
			}
		}else{
			if(Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) && Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders";
				query = session.createQuery(hql);
			}else if(!Strings.isNullOrEmpty(status) && Strings.isNullOrEmpty(start_date) 
					&& Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and status=?";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status));
			}else if(Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and create_time between :startDate and :endDate";
				query = session.createQuery(hql).setString("startDate", start_date).setString("endDate", end_date);

			}else if(!Strings.isNullOrEmpty(status) && !Strings.isNullOrEmpty(start_date) 
					&& !Strings.isNullOrEmpty(end_date)){
				hql = "select count(1),sum(amount) from Orders where 1=1 and status=? and create_time between :startDate and :endDate";
				query = session.createQuery(hql).setInteger(0, Integer.parseInt(status))
						.setString("startDate", start_date).setString("endDate", end_date);
			}
		}
		
		List list = query.list();
		Object[] obj = null;
		if(list != null && list.size() > 0){
			obj = (Object[])list.get(0);
		}
		return obj;
	}

	@Override
	public Orders getOrdersByOrderno(String order_no) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and order_no=?";
		Query query = session.createQuery(hql).setString(0, order_no);
		List<Orders> list = query.list();
		Orders o = null;
		if(list != null && list.size() > 0){
			o = list.get(0);
		}
		return o;
	}

	@Override
	public int updateOrderStatus(String order_no,String channel) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "update Orders set status=1 and pay_type=? where 1=1 and order_no=?";
		Query query = session.createQuery(hql).setString(0, channel).setString(1,order_no);
		int result = query.executeUpdate();
		return result;
	}

	@Override
	public Orders getOrdersByOrderNoAndChargeId(String order_no, String charge_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and order_no=? and charge_id=?";
		Query query = session.createQuery(hql).setString(0, order_no).setString(1,charge_id);
		List<Orders> list = query.list();
		Orders o = null;
		if(list != null && list.size() > 0){
			o = list.get(0);
		}
		return o;
	}

	@Override
	public Orders getOrdersByOrderNoAndRefundId(String order_no, String refund_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and order_no=? and refund_id=?";
		Query query = session.createQuery(hql).setString(0, order_no).setString(1,refund_id);
		List<Orders> list = query.list();
		Orders o = null;
		if(list != null && list.size() > 0){
			o = list.get(0);
		}
		return o;
	}

	@Override
	public List<Orders> getOrdersByStatusAndCount(int status, int count,Long user_id) {
		
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and user.id=? and status=? order by create_time desc";
		Query query = session.createQuery(hql).setLong(0, user_id)
				.setInteger(1,status).setMaxResults(count);
		List<Orders> list = query.list();
		return list;
	}

	@Override
	public List<Orders> getOrdersByStatusAndCount(int status, int count, Long id,Long user_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and user.id=? and status=? and id < ? order by create_time desc";
		Query query = session.createQuery(hql).setLong(0, user_id)
				.setInteger(1,status).setLong(2,id).setMaxResults(count);
		List<Orders> list = query.list();
		return list;
	}


	@Override
	public List<Orders> getOrdersByUserid(Long user_id) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and user.id=? order by create_time desc";
		Query query = session.createQuery(hql).setLong(0, user_id);
		List<Orders> list = query.list();
		return list;
	}


	@Override
	public List<Orders> getOrdersByCreateTime(String create_time) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Orders where 1=1 and create_time <= ?";
		Query query = session.createQuery(hql).setString(0, create_time);
		List<Orders> list = query.list();
		return list;
	}

}
