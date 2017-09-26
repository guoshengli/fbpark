package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Orders;

public interface OrdersDao extends BaseDao<Orders, Long> {
	public List<Orders> getOrderListByType(int count,int page,String type);
	
	public int getOrderListByType(String type);
	
	public List<Orders> getOrderListByTypeAndCount(String type,int count,Long userid);
	
	public List<Orders> getOrderListByTypeAndCountAndId(String type,int count,Long id,Long userid);
	
	public List<Orders> getOrdersListByParams(int count,String type,String start_date,String end_date);
	
	public List<Orders> getOrdersListByParams(int count,int page,String type,String start_date,String end_date,String order_no);
	
	public List<Orders> getOrdersListByParams(String status, String start_date, String end_date,String order_no);
	
	public Object[] getOrdersCountAndAmountByParams(String type,String start_date,String end_date,String order_no);
	
	public Orders getOrdersByOrderno(String order_no);
	
	public int updateOrderStatus(String order_no,String channel);
	
	public Orders getOrdersByOrderNoAndChargeId(String order_no,String charge_id);
	
	public Orders getOrdersByOrderNoAndRefundId(String order_no,String refund_id);
	
	public List<Orders> getOrdersByStatusAndCount(int status,int count,Long user_id);
	
	public List<Orders> getOrdersByStatusAndCount(int status,int count,Long id,Long user_id);
	
	public List<Orders> getOrdersByUserid(Long user_id);
	
	public List<Orders> getOrdersByCreateTime(String create_time);
}
