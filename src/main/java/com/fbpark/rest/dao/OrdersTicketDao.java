package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.OrdersTicket;

public interface OrdersTicketDao extends BaseDao<OrdersTicket, Long> {
	public List<OrdersTicket> getOrdersTicketByOrdersId(Long ordersId);
	
	public List<OrdersTicket> getOrdersTicketByUserid(Long user_id,int count,int status);

	public List<OrdersTicket> getOrdersTicketByUserid(Long user_id,int count,Long orderTicketId,int status);
	
	public List<OrdersTicket> getOrdersTicketByStatusAndUserid(Long user_id,int count);

	public List<OrdersTicket> getOrdersTicketByStatusAndUserid(Long user_id,int count,Long orderTicketId);
	
	public List<OrdersTicket> getOrdersTicketListByParams(int count,String type,String start_date,String end_date,String username);
	
	public List<OrdersTicket> getOrdersTicketListByParams(int count,int page,String type,String start_date,String end_date,String username);
	
	public Object[] getOrdersTicketCountAndAmountByParams(String type,String start_date,String end_date,String username);
	
	public List<OrdersTicket> getOrdersTicketListByStatistics(int count,int page,String ticket_name,String type,String search);
	
	public List<OrdersTicket> getOrdersTicketListByStatistics(String poiId, String type,
			String search);
	
	public Object[] getOrdersTicketCountAndAmountByStatistics(String ticket_name,String type,String search);
	
	public OrdersTicket getOrdersTicketByOrdersNoAndRefundId(String orders_no,String refund_id);
	
	public OrdersTicket getOrdersTicketByOrdersNo(String orders_no);
	
	public List<OrdersTicket> getOrdersTicketByOrdersId(Long ordersId,int status);
	
	public int getOrdersTicketCountAndAmountByInsurant(String start_date,String end_date,String username);
	
	public List<OrdersTicket> getOrdersTicketListByInsurant(int count,int page,String start_date,String end_date,String username);
	
	public List<OrdersTicket> getOrdersTicketListByInsurant(String start_date,String end_date,String username);
	
	public List<OrdersTicket> getOrdersTicketListByIdcardAndVerify(String identity_card,String verify);
	
	public OrdersTicket getOrdersTicketListByIdcardAndVerify(String identity_card,String verify,Long poi_id);
	
	public List<OrdersTicket> getOrdersTicketListByIdcardAndTicket(String identity_card,Long ticket_id);
	
	public List<OrdersTicket> getOrdersTicketListByIdcard(String identity_card);
	
	public List<OrdersTicket> getOrdersTicketListByIdcardAndOnlineAndOffline(String identity_card);
	
	public List<OrdersTicket> getOrdersTicketListByIdcardAll(String identity_card);
	
	public List<OrdersTicket> getOrdersTicketListByIdcardAndStatus(String identity_card);
	
}
