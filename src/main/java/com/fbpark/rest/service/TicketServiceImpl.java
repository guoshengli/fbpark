package com.fbpark.rest.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.fbpark.rest.common.ParseFile;
import com.fbpark.rest.dao.ContacterDao;
import com.fbpark.rest.dao.InsurantDao;
import com.fbpark.rest.dao.TicketDao;
import com.fbpark.rest.model.Contacter;
import com.fbpark.rest.model.Insurant;
import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.PoiElement;
import com.fbpark.rest.model.Ticket;
import com.google.common.base.Strings;

import net.sf.json.JSONObject;

public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketDao ticketDao;
	
	@Autowired
	private ContacterDao contacterDao;
	
	@Autowired
	private InsurantDao insurantDao;
	
	@Override
	public Response getTicketsList(HttpServletRequest request) {
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		String ticket_name = request.getParameter("ticket_name");
		List<Ticket> ticketList = null;
		int count = 20;
		if(!Strings.isNullOrEmpty(ticket_name)){
			if(Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
				ticketList = ticketDao.getTicketListByTicket(count, ticket_name);
				
			}else if(!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
				count = Integer.parseInt(countStr);
				ticketList = ticketDao.getTicketListByTicket(count, ticket_name);
			}else if(Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
				Long max_id = Long.parseLong(maxIdStr);
				ticketList = ticketDao.getTicketListByTicket(count,max_id, ticket_name);
			}else if(!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				ticketList = ticketDao.getTicketListByTicket(count,max_id, ticket_name);
			}
		}else{
			if(Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
				ticketList = ticketDao.getTicketList(count);
				
			}else if(!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
				count = Integer.parseInt(countStr);
				ticketList = ticketDao.getTicketList(count);
			}else if(Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
				Long max_id = Long.parseLong(maxIdStr);
				ticketList = ticketDao.getTicketList(count,max_id);
			}else if(!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				ticketList = ticketDao.getTicketList(count,max_id);
			}
		}
		List<JSONObject> ticketJsonList = new ArrayList<JSONObject>();
		JSONObject ticketJson = null;
		if(ticketList != null && ticketList.size() > 0){
			for(Ticket t:ticketList){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Poi poi = t.getPoi();
				PoiElement pe = poi.getElements().get(0);
				ticketJson = new JSONObject();
				ticketJson.put("id", t.getId());
				ticketJson.put("poi_id", poi.getId());
				ticketJson.put("ticket_name", t.getTicket_name());
				ticketJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
				ticketJson.put("place", poi.getPlace());
				String startTimeStr = t.getStart_date()+" "+t.getStart_time();
				String endTimeStr = t.getStart_date()+" "+t.getEnd_time();
				long start_time = 0l;
				long end_time = 0l;
				try {
					Date startTime = sdf.parse(startTimeStr);
					Date endTime = sdf.parse(endTimeStr);
					start_time = startTime.getTime() / 1000;
					end_time = endTime.getTime() / 1000;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ticketJson.put("start_time",start_time);
				ticketJson.put("end_time",end_time);
				ticketJson.put("price", t.getPrice());
				ticketJsonList.add(ticketJson);
			}
		}
		return Response.status(Response.Status.OK).entity(ticketJsonList).build();
	}
	@Override
	public Response ticket_order_info(Long ticket_id, Long loginUserid) {
		JSONObject resp = new JSONObject();
		if(loginUserid != null && loginUserid > 0){
			Ticket t = ticketDao.get(ticket_id);
			JSONObject ticketJson = new JSONObject();
			ticketJson.put("id", t.getId());
			ticketJson.put("ticket_name", t.getTicket_name());
			String startTimeStr = t.getStart_date()+" "+t.getStart_time();
			String endTimeStr = t.getStart_date()+" "+t.getEnd_time();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			long start_time = 0l;
			long end_time = 0l;
			long current_time = 0l;
			try {
				Date startTime = sdf.parse(startTimeStr);
				Date endTime = sdf.parse(endTimeStr);
				start_time = startTime.getTime() / 1000;
				end_time = endTime.getTime() / 1000;
				String current_date = sdf.format(new Date());
				current_time = sdf.parse(current_date).getTime() / 1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ticketJson.put("start_time",start_time);
			ticketJson.put("end_time",end_time);
			ticketJson.put("price", t.getPrice());
//			ticketJson.put("is_insurance", t.getIs_insurance());
//			ticketJson.put("insurance_price", t.getInsurance_price());
			JSONObject insurant = getInsurantInfo();
			ticketJson.put("insurance_link", insurant.getString("insurance_link"));
			resp.put("ticket",ticketJson);
			List<Contacter> cList = contacterDao.getContacterList(loginUserid);
			List<JSONObject> contacterJsonList = new ArrayList<JSONObject>();
			JSONObject contacterJson = null;
			if(cList != null && cList.size() > 0){
				for(Contacter c:cList){
					contacterJson = new JSONObject();
					contacterJson.put("id", c.getId());
					contacterJson.put("name", c.getName());
					contacterJson.put("identity_card", c.getIdentity_card());
					List<Insurant> iList = insurantDao.getInsurantByIdentityCard(c.getIdentity_card());
					if(iList != null && iList.size() > 0){
						contacterJson.put("insurant", "yes");
					}else{
						contacterJson.put("insurant", "no");
					}
					contacterJsonList.add(contacterJson);
				}
				resp.put("contacters", contacterJsonList);
			}
			
			
			int number = t.getNumber();
			
			
			if(current_time > end_time){
				resp.put("type", 1);
			}else if(number == 0){
				resp.put("type", 2);
			}
			
			return Response.status(Response.Status.OK).entity(resp).build();
		} else {
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}
	
	public JSONObject getInsurantInfo(){
		String path = getClass().getResource("/../../META-INF/insurant.json").getPath();
		JSONObject insurant = ParseFile.parseJson(path);
		return insurant;
	}
	
}
