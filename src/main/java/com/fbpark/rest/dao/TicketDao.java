package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Ticket;

public interface TicketDao extends BaseDao<Ticket, Long> {
	public List<Ticket> getTicketList(int count);
	
	public List<Ticket> getTicketList(int count,Long ticket_id);
	
	public List<Ticket> getTicketListByTicket(int count,String ticket_name); 
	
	public List<Ticket> getTicketListByTicket(int count,Long ticket_id,String ticket_name);
	
	public int getTicketByParamsCount(int count, int page,
			String ticket_name, String zyb_code, String poi_id);
	
	public List<Ticket> getTicketByParams(int count, int page,
			String ticket_name, String zyb_code, String poi_id);
	
	public List<Ticket> getTicketByZybCode(String zybcode);
	
	public void updateTicket(Long id,Long poi_id);
	
	public int getTicketByNopoiCount();
	
	public List<Ticket> getTicketByNopoi(int count, int page);
	
	public void updateTicket(Long id,String status);
	
	public List<Ticket> getTicketByTypeAndStatus(String type);
}
