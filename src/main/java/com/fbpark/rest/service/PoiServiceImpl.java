package com.fbpark.rest.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.fbpark.rest.common.CodeUtil;
import com.fbpark.rest.common.HttpUtil;
import com.fbpark.rest.common.ParseFile;
import com.fbpark.rest.dao.CollectDao;
import com.fbpark.rest.dao.ContentDao;
import com.fbpark.rest.dao.DeviceDao;
import com.fbpark.rest.dao.OrdersTicketDao;
import com.fbpark.rest.dao.PoiDao;
import com.fbpark.rest.dao.RecommandationDao;
import com.fbpark.rest.model.Classify;
import com.fbpark.rest.model.Collect;
import com.fbpark.rest.model.Content;
import com.fbpark.rest.model.ContentElement;
import com.fbpark.rest.model.Device;
import com.fbpark.rest.model.OrdersTicket;
import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.PoiElement;
import com.fbpark.rest.model.Ticket;
import com.google.common.base.Strings;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PoiServiceImpl implements PoiService {
	
	@Autowired
	private PoiDao poiDao;
	
	@Autowired
	private ContentDao contentDao;
	
	@Autowired
	private RecommandationDao recommandationDao;
	
	@Autowired
	private CollectDao collectDao;
	
	@Autowired
	private OrdersTicketDao ordersTicketDao;
	
	@Autowired
	private DeviceDao deviceDao;

	@Override
	public Response get_poi(Long loginUserid, Long poi_id) {
		JSONObject resp = new JSONObject();
		if(poi_id != null && poi_id > 0){
			Poi poi = poiDao.get(poi_id);
			if(poi != null){
				JSONObject poiJson = new JSONObject();
				poiJson.put("id", poi.getId());
				
				
				if(loginUserid != null && loginUserid > 0){
					Collect c = collectDao.getCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", poi_id);
					if(c != null){
						poiJson.put("is_collect", true);
					}else{
						poiJson.put("is_collect", false);
					}
				}else{
					poiJson.put("is_collect", false);
				}
				if(!Strings.isNullOrEmpty(poi.getTitle())){
					poiJson.put("title", poi.getTitle());
				}
				Long content_id = poi.getContent_id();
				if(content_id != null && content_id > 0){
					Content content = contentDao.get(content_id);
					JSONObject contentJson = new JSONObject();
					contentJson.put("id",content.getId());
					if(!Strings.isNullOrEmpty(content.getTitle())){
						contentJson.put("title", content.getTitle());
					}
					
					if(!Strings.isNullOrEmpty(content.getSummary())){
						contentJson.put("summary", content.getSummary());
					}
					List<ContentElement> contentElementList = content.getElements();
					List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
					JSONObject elementJson = null;
					if(contentElementList != null && contentElementList.size() > 0){
						for(ContentElement ce:contentElementList){
							elementJson = new JSONObject();
							elementJson.put("id",ce.getId());
							elementJson.put("content", JSONObject.fromObject(ce.getElement()));
							elementJsonList.add(elementJson);
						}
					}
					contentJson.put("elements", elementJsonList);
					poiJson.put("content", contentJson);
				}
				if(!Strings.isNullOrEmpty(poi.getDirect_sales())){
					poiJson.put("direct_sales", poi.getDirect_sales());
				}
				
				
				if(!Strings.isNullOrEmpty(poi.getAttention())){
					poiJson.put("attention", poi.getAttention());
				}
				
				if(!Strings.isNullOrEmpty(poi.getPlace())){
					poiJson.put("place", poi.getPlace());
				}
				
				if(poi.getPrice() != null && poi.getPrice().floatValue() > 0){
					poiJson.put("price", poi.getPrice());
				}
				if(!Strings.isNullOrEmpty(poi.getLocation())){
					poiJson.put("location", JSONObject.fromObject(poi.getLocation()));
				}
				
				if(!Strings.isNullOrEmpty(poi.getSubtitle())){
					poiJson.put("subtitle", poi.getSubtitle());
				}
				
				if(!Strings.isNullOrEmpty(poi.getTime_info())){
					poiJson.put("time_info", poi.getTime_info());
				}
				List<PoiElement> elementList = poi.getElements();
				List<JSONObject> eleJsonList = new ArrayList<JSONObject>();
				JSONObject eleJson = null;
				if(elementList != null && elementList.size() > 0){
					for(PoiElement pe:elementList){
						eleJson = new JSONObject();
						eleJson.put("id", pe.getId());
						eleJson.put("content", JSONObject.fromObject(pe.getElement()));
						eleJsonList.add(eleJson);
					}
					poiJson.put("slides", eleJsonList);
				}
				
				Classify c = poi.getClassify();
				poiJson.put("classify_type", c.getType());
				poiJson.put("classify_name", c.getClassify_name());
				if(c.getType().equals("hotel")){
					
					String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
					
					JSONObject json1 = ParseFile.parseJson(path);
					List<Ticket> tList = poi.getTickets();
					List<JSONObject> tJsonList = new ArrayList<JSONObject>();
					if(tList != null && tList.size() > 0){
						Ticket t = tList.get(0);
						JSONObject tJson = new JSONObject();
						tJson.put("id", t.getId());
						tJson.put("ticket_name", t.getTicket_name());
						tJson.put("price", t.getPrice());
						tJsonList.add(tJson);
						poiJson.put("tickets", tJsonList);
						String start_date = t.getStart_date()+" 12:00";
						String end_date = t.getEnd_date()+" 12:00";
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						Date startDate = null;
						Date endDate = null;
						try {
							startDate = sdf.parse(start_date);
							endDate = sdf.parse(end_date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						poiJson.put("start_date", (startDate.getTime()/1000));
						poiJson.put("end_date", (endDate.getTime()/1000));
					}
					
					String url = json1.getString("url");
					String result = HttpUtil.sendPostStr(url+"/hotel/room-type/"+poi.getId(), "");
					double minPrice = 0;
					if(!Strings.isNullOrEmpty(result)){
						JSONObject resJson = JSONObject.fromObject(result);
						int code = resJson.getInt("code");
						if(code == 1000){
							JSONArray ja = resJson.getJSONArray("data");
							if(ja != null && ja.size() > 0){
								poiJson.put("room_type", ja);
								JSONObject first = ja.getJSONObject(0);
								minPrice = Double.valueOf(first.getString("price"));
								for(Object obj:ja){
									JSONObject j =JSONObject.fromObject(obj);
									double price = Double.valueOf(j.getString("price"));
									if(minPrice > price){
										minPrice = price;
									}
								}
							}
						}
					}
					poiJson.put("price",minPrice);
					
				}else{
					List<Ticket> ticketList = poi.getTickets();
					List<JSONObject> tJsonList = new ArrayList<JSONObject>();
					if(!Strings.isNullOrEmpty(poi.getDirect_sales()) && poi.getDirect_sales().equals("yes")){
						JSONObject tJson = null;
						if(ticketList != null && ticketList.size() > 0){
							BigDecimal min = ticketList.get(0).getPrice();
							for(Ticket t:ticketList){
								tJson = new JSONObject();
								tJson.put("id", t.getId());
								tJson.put("ticket_name", t.getTicket_name());
								tJson.put("price", t.getPrice());
								BigDecimal price = t.getPrice();
								int compare = min.compareTo(price);
								if(compare == 1){
									min = price;
								}
								
								tJsonList.add(tJson);
								
							}
							poiJson.put("tickets", tJsonList);
							poiJson.put("price", min);
							
						}
					}else{
						JSONObject tJson = null;
						int ticket_count = 0;
						if(ticketList != null && ticketList.size() > 0){
							BigDecimal min = ticketList.get(0).getPrice();
							for(Ticket t:ticketList){
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
									Date d = sdf.parse(sdf.format(new Date()));
									current_time = d.getTime() / 1000;
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(current_time < end_time){
									tJson = new JSONObject();
									tJson.put("id", t.getId());
									tJson.put("ticket_name", t.getTicket_name());
									tJson.put("start_time",start_time);
									tJson.put("end_time",end_time);
									tJson.put("price", t.getPrice());
									ticket_count += t.getNumber();
									BigDecimal price = t.getPrice();
									int compare = min.compareTo(price);
									if(compare == 1){
										min = price;
									}
									
									tJsonList.add(tJson);
								}
								
							}
							if(ticket_count == 0){
								poiJson.put("no_tickets", "yes"); //没有门票
							}else{
								poiJson.put("tickets", tJsonList);
								poiJson.put("price", min);
							}
							
						}
					}
					
				}
				
				List<Poi> poiList = recommandationDao.getRecommandationListByPoiId(poi_id);
				List<JSONObject> recommandJsonList = new ArrayList<JSONObject>();
				JSONObject recommandJson = null;
				if(poiList != null && poiList.size() > 0){
					for(Poi p:poiList){
						recommandJson = new JSONObject();
						recommandJson.put("id", p.getId());
						PoiElement pe = p.getElements().get(0);
						recommandJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
						recommandJson.put("title", p.getTitle());
						recommandJsonList.add(recommandJson);
					}
					poiJson.put("recommandation", recommandJsonList);
				}
				
				return Response.status(Response.Status.OK).entity(poiJson).build();
			}else{
				resp.put("status", "该POI不存在");
				resp.put("code", Integer.valueOf(10805));
				resp.put("error_message", "该POI不存在");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
			
			
		}else{
			resp.put("status", "缺少必要的请求参数");
			resp.put("code", Integer.valueOf(10009));
			resp.put("error_message", "缺少必要的请求参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response map_info(HttpServletRequest request,Long loginUserid) {
		String classify_id = request.getParameter("classify_id");
		List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
		JSONObject poiJson = null;
		if(!Strings.isNullOrEmpty(classify_id)){
			List<Poi> poiList = poiDao.getPoiListByParams("1", classify_id);
			if(poiList != null && poiList.size() > 0){
				for(Poi poi:poiList){
					poiJson = new JSONObject();
					poiJson.put("id", poi.getId());
					poiJson.put("title", poi.getTitle());
					PoiElement pe = poi.getElements().get(0);
					poiJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
					Collect collect = collectDao.getCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", poi.getId());
					if(collect != null){
						poiJson.put("is_collect", true);
					}else{
						poiJson.put("is_collect", false);
					}
					poiJson.put("location", JSONObject.fromObject(poi.getLocation()));
					poiJsonList.add(poiJson);
				}
			}
		}else{
			List<Poi> poiList = poiDao.getPoiListByParams("1", classify_id);
			if(poiList != null && poiList.size() > 0){
				for(Poi poi:poiList){
					poiJson = new JSONObject();
					poiJson.put("id", poi.getId());
					poiJson.put("title", poi.getTitle());
					PoiElement pe = poi.getElements().get(0);
					poiJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
					Collect collect = collectDao.getCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", poi.getId());
					if(collect != null){
						poiJson.put("is_collect", true);
					}else{
						poiJson.put("is_collect", false);
					}
					poiJsonList.add(poiJson);
				}
			}
		}
		return Response.status(Response.Status.OK).entity(poiJsonList).build();
	}

	@Override
	public Response search_poi(HttpServletRequest request, Long loginUserid) {
		
		JSONObject resp = new JSONObject();
		String title = request.getParameter("title");
		List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
		JSONObject poiJson = null;
		if(!Strings.isNullOrEmpty(title)){
			List<Poi> poiList = poiDao.getPoiListByTitle(title);
			if(poiList != null && poiList.size() > 0){
				for(Poi poi:poiList){
					poiJson = new JSONObject();
					poiJson.put("id", poi.getId());
					poiJson.put("title", poi.getTitle());
					List<PoiElement> peList = poi.getElements();
					if(peList != null && peList.size() > 0){
						PoiElement pe = peList.get(0);
						poiJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
					}
					
//					if(loginUserid != null && loginUserid > 0){
//						Collect collect = collectDao.getCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", poi.getId());
//						if(collect != null){
//							poiJson.put("is_collect", true);
//						}else{
//							poiJson.put("is_collect", false);
//						}
//					}else{
//						poiJson.put("is_collect", false);
//					}
					
					poiJsonList.add(poiJson);
				}
				
			}
			return Response.status(Response.Status.OK).entity(poiJsonList).build();
		}else{
			resp.put("status", "缺少必要的请求参数");
			resp.put("code", Integer.valueOf(10009));
			resp.put("error_message", "缺少必要的请求参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		
	
	}

	@Override
	public Response poi_list(HttpServletRequest request, Long loginUserid) {
		
		String classify_id = request.getParameter("classify_id");
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		JSONObject resp = new JSONObject();
		int count = 20;
		
		List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
		JSONObject poiJson = null;
		if(!Strings.isNullOrEmpty(classify_id)){
			List<Poi> poiList = null;
			if(Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
				poiList = poiDao.getPoiListByClassify(Long.parseLong(classify_id), count);
			}else if(!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
				count = Integer.parseInt(countStr);
				poiList = poiDao.getPoiListByClassify(Long.parseLong(classify_id), count);
			}else if(Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
				Long max_id = Long.parseLong(maxIdStr);
				poiList = poiDao.getPoiListByClassify(Long.parseLong(classify_id), count,max_id);
			}else if(!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
				Long max_id = Long.parseLong(maxIdStr);
				count = Integer.parseInt(countStr);
				poiList = poiDao.getPoiListByClassify(Long.parseLong(classify_id), count,max_id);
			}
			if(poiList != null && poiList.size() > 0){
				for(Poi poi:poiList){
					poiJson = new JSONObject();
					poiJson.put("id", poi.getId());
					poiJson.put("title", poi.getTitle());
					PoiElement pe = poi.getElements().get(0);
					poiJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
					if(loginUserid != null && loginUserid > 0){
						Collect collect = collectDao.getCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", poi.getId());
						if(collect != null){
							poiJson.put("is_collect", true);
						}else{
							poiJson.put("is_collect", false);
						}
					}else{
						poiJson.put("is_collect", false);
					}
					
					poiJson.put("location", JSONObject.fromObject(poi.getLocation()));
					poiJsonList.add(poiJson);
				}
			}
			return Response.status(Response.Status.OK).entity(poiJsonList).build();
		}else{
			resp.put("status", "缺少必要的请求参数");
			resp.put("code", Integer.valueOf(10009));
			resp.put("error_message", "缺少必要的请求参数");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		
	
	}

	@Override
	public Response is_collect_poi(JSONObject collect, Long loginUserid) {
		JSONObject resp = new JSONObject();
		if(loginUserid != null && loginUserid > 0){
			if(collect != null){
				int is_collect = collect.getInt("is_collect");
				if(is_collect == 1){
					collectDao.delCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", collect.getLong("poi_id"));
					Collect c = new Collect();
					c.setReference_id(collect.getLong("poi_id"));
					c.setReference_type("poi");
					c.setUser_id(loginUserid);
					collectDao.save(c);
				}else{
					collectDao.delCollectByUseridAndTypeAndReferenceid(loginUserid, "poi", collect.getLong("poi_id"));
				}
				resp.put("status", "success");
				return Response.status(Response.Status.OK).entity(resp).build();
			}else{
				resp.put("status", "缺少必要的请求参数");
				resp.put("code", Integer.valueOf(10009));
				resp.put("error_message", "缺少必要的请求参数");
				return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
			}
		}else{
			resp.put("status", "用户未登录");
			resp.put("code", Integer.valueOf(10010));
			resp.put("error_message", "用户未登录");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
	}

	@Override
	public Response poi_ticket(HttpServletRequest request, Long loginUserid) {
		String title = request.getParameter("title");
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		int count = 20;
		
		List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
		JSONObject poiJson = null;

		List<Poi> poiList = null;
		if(Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
			poiList = poiDao.getPoiListByTicket(count, title);
		}else if(!Strings.isNullOrEmpty(countStr) && Strings.isNullOrEmpty(maxIdStr)){
			count = Integer.parseInt(countStr);
			poiList = poiDao.getPoiListByTicket(count, title);
		}else if(Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
			Long max_id = Long.parseLong(maxIdStr);
			poiList = poiDao.getPoiListByTicket(count, max_id, title);
		}else if(!Strings.isNullOrEmpty(countStr) && !Strings.isNullOrEmpty(maxIdStr)){
			Long max_id = Long.parseLong(maxIdStr);
			count = Integer.parseInt(countStr);
			poiList = poiDao.getPoiListByTicket(count, max_id, title);
		}
		if(poiList != null && poiList.size() > 0){
			for(Poi poi:poiList){
				poiJson = new JSONObject();
				poiJson.put("poi_id", poi.getId());
				poiJson.put("ticket_name", poi.getTitle());
				PoiElement pe = poi.getElements().get(0);
				poiJson.put("cover_image", JSONObject.fromObject(pe.getElement()));
				poiJson.put("place", poi.getPlace());
				List<Ticket> tList = poi.getTickets();
				if(tList != null && tList.size() > 0){
					BigDecimal min = tList.get(0).getPrice();
					for(Ticket t:tList){
						BigDecimal price = t.getPrice();
						int compare = min.compareTo(price);
						if(compare == 1){
							min = price;
						}
					}
					poiJson.put("price", min);
				}
				poiJsonList.add(poiJson);
			}
		}
		return Response.status(Response.Status.OK).entity(poiJsonList).build();
	
	}

	@Override
	public JSONObject pos_list(HttpServletRequest request) {
		
		List<Poi> poiList = poiDao.getPoiList();
		JSONObject poiJson = null;
		List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
		if(poiList != null && poiList.size() > 0){
			for(Poi p:poiList){
				poiJson = new JSONObject();
				if(p.getClassify().getType().equals("hotel")){
					poiJson.put("poi_id", p.getId());
					poiJson.put("poi_name", p.getTitle());
					poiJson.put("poi_type", "hotel");
					poiJson.put("direct_sales", p.getDirect_sales());
					List<Ticket> tickets = p.getTickets();
					JSONObject ticketJson = null;
					List<JSONObject> ticketJsonList = new ArrayList<JSONObject>();
					if(tickets != null && tickets.size() > 0){
						for(Ticket t:tickets){
							ticketJson = new JSONObject();
							ticketJson.put("type_id", t.getId());
							ticketJson.put("type_name", t.getTicket_name());
							ticketJson.put("start_price", t.getPrice());
							ticketJson.put("checking_rule", t.getChecking_rule());
							ticketJsonList.add(ticketJson);
						}
					}
					poiJson.put("types", ticketJsonList);
				}else{
					poiJson.put("poi_id", p.getId());
					poiJson.put("poi_name", p.getTitle());
					poiJson.put("poi_type", "event");
					poiJson.put("direct_sales", p.getDirect_sales());
					List<Ticket> tickets = p.getTickets();
					JSONObject ticketJson = null;
					List<JSONObject> ticketJsonList = new ArrayList<JSONObject>();
					if(tickets != null && tickets.size() > 0){
						for(Ticket t:tickets){
							ticketJson = new JSONObject();
							ticketJson.put("type_id", t.getId());
							ticketJson.put("type_name", t.getTicket_name());
							ticketJson.put("start_price", t.getPrice());
							ticketJson.put("checking_rule", t.getChecking_rule());
							ticketJsonList.add(ticketJson);
						}
					}
					poiJson.put("types", ticketJsonList);
				}
				poiJsonList.add(poiJson);
			}
		}
		JSONObject poi_list = new JSONObject();
		poi_list.put("poi_list", poiJsonList);
		JSONObject resp = CodeUtil.returnData(1000, "查询成功", poi_list);
		return resp;
	}

	@Override
	public JSONObject idcard_orders(JSONObject param) {

		JSONObject returnJson = null;
		
		if(param != null){
			String identity_card = "";
			String poi_id = "";
			String ticket_id = "";
			String verify =  "";
			if(param.containsKey("identity_card")){
				identity_card = param.getString("identity_card");
			}
			
			if(param.containsKey("poi_id")){
				poi_id = param.getString("poi_id");
			}
			
			if(param.containsKey("ticket_id")){
				ticket_id = param.getString("ticket_id");
			}
			
			if(param.containsKey("verify")){
				verify = param.getString("verify");
			}
			if(!Strings.isNullOrEmpty(identity_card) && !Strings.isNullOrEmpty(verify) && !Strings.isNullOrEmpty(poi_id)){
				OrdersTicket ot = ordersTicketDao.getOrdersTicketListByIdcardAndVerify(identity_card, verify,Long.parseLong(poi_id));
				if(ot != null){
					Ticket t = ot.getTicket();
					JSONObject resp = new JSONObject();
					JSONObject oJson = new JSONObject();
					List<JSONObject> oList = new ArrayList<JSONObject>();
					if(t.getType().equals("hotel")){
						String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
						JSONObject hotel = ParseFile.parseJson(path);
						String url = hotel.getString("url");//根据订单号找订单
						JSONArray paramArr = new JSONArray();
						paramArr.add(ot.getTicket_order_no());
						String result = HttpUtil.sendPostStr(url+"/order/getsub", paramArr.toString());
						if(!Strings.isNullOrEmpty(result)){
							JSONObject resJson = JSONObject.fromObject(result);
							int code = resJson.getInt("code");
							if(code == 1000){
								JSONArray ja = resJson.getJSONArray("data");
								JSONObject json = new JSONObject();
								json.put("hotel_room",ja);
								returnJson = CodeUtil.returnData(1000, "查询成功", json);
							}else{
								returnJson = CodeUtil.returnData(1000, "没有可用的房型", "");
							}
						}else{
							returnJson = CodeUtil.returnData(1040, "房源不足，无法预订", "");
						}
						
					}else{
						resp.put("orders_ticket_id", ot.getId());
						resp.put("poi_name", t.getPoi().getTitle());
						resp.put("ticket_name", t.getTicket_name());
						resp.put("verification", ot.getVerification());
						JSONObject user = new JSONObject();
						user.put("identity_card", ot.getContacter().getIdentity_card());
						user.put("username", ot.getContacter().getName());
						resp.put("user",user);
						oList.add(resp);
						oJson.put("orders_ticket", oList);
						returnJson = CodeUtil.returnData(1000, "查询成功", oJson);
					}
				}else{
					returnJson = CodeUtil.returnData(1015, "该订单不可用", "");
				}
				
				
			}else if(!Strings.isNullOrEmpty(identity_card) 
					&& !Strings.isNullOrEmpty(poi_id) 
					&& !Strings.isNullOrEmpty(ticket_id)){
				List<OrdersTicket> otList = ordersTicketDao.getOrdersTicketListByIdcardAndTicket(identity_card, Long.parseLong(ticket_id));
				List<JSONObject> otJSONList = new ArrayList<JSONObject>();
				JSONObject oJson = new JSONObject();
				if(otList != null && otList.size() > 0){
					OrdersTicket ot = otList.get(0);
					Ticket t = ot.getTicket();
					Poi p = t.getPoi();
					if(t.getType().equals("hotel")){
						String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
						JSONObject hotel = ParseFile.parseJson(path);
						String url = hotel.getString("url");//根据订单号找订单
						JSONArray paramArr = new JSONArray();
						for(OrdersTicket o:otList){
							paramArr.add(o.getTicket_order_no());
						}
						String result = HttpUtil.sendPostStr(url+"/order/getsub", paramArr.toString());
						if(!Strings.isNullOrEmpty(result)){
							JSONObject resJson = JSONObject.fromObject(result);
							int code = resJson.getInt("code");
							if(code == 1000){
								JSONArray ja = resJson.getJSONArray("data");
								JSONObject json = new JSONObject();
								json.put("hotel_room",ja);
								returnJson = CodeUtil.returnData(1000, "查询成功", json);
							}else{
								returnJson = CodeUtil.returnData(1000, "没有可用的房型", "");
							}
						}else{
							returnJson = CodeUtil.returnData(1040, "房源不足，无法预订", "");
						}
					}else if(p.getDirect_sales().equals("yes")){
						ot.setStatus(1);
						ordersTicketDao.update(ot);
						returnJson = CodeUtil.returnData(1000, "检票成功", "");
					}else{
						for(OrdersTicket ots:otList){
							JSONObject resp = new JSONObject();
							String startTimeStr = t.getStart_date()+" "+t.getStart_time();
							String endTimeStr = t.getStart_date()+" "+t.getEnd_time();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
							resp.put("orders_ticket_id", ots.getId());
							resp.put("poi_name", t.getPoi().getTitle());
							resp.put("ticket_name", t.getTicket_name());
							resp.put("verification", ots.getVerification());
							JSONObject user = new JSONObject();
							user.put("identity_card", ots.getContacter().getIdentity_card());
							user.put("username", ots.getContacter().getName());
							resp.put("user",user);
							resp.put("start_time",start_time);
							resp.put("end_time",end_time);
							otJSONList.add(resp);
						}
						oJson.put("orders_ticket", otJSONList);
						returnJson = CodeUtil.returnData(1000, "查询成功", oJson);
						
					}
				}else{
//					oJson.put("orders_ticket", otJSONList);
					returnJson = CodeUtil.returnData(1048, "该身份证号没有购买票", "");
				}
			}else{
				returnJson = CodeUtil.returnData(1039, "参数异常", "");
			}
		}else{
			returnJson = CodeUtil.returnData(1039, "参数异常", "");
		}
		return returnJson;
	
	
	}

	@Override
	public JSONObject add_device(JSONObject device) {
		JSONObject json = null;
		if(device != null){
			Device d = new Device();
			String device_code = device.getString("device_code");

			d.setDevice_code(device_code);
			d.setPoi_id(device.getLong("poi_id"));
			d.setStatus("enable");
			d.setRandom_char(device.getString("random_char"));
			deviceDao.save(d);
			JSONObject idJson = new JSONObject();
			idJson.put("id", d.getId());
			json = CodeUtil.returnData(1000, "初始化成功",idJson);
		}else{
			json = CodeUtil.returnData(1017, "初始化失败","");
		}
		Object o = json.get("rspData");
		if(o == null){
			json.discard("rspData");
		}
		return json;
	}

	@Override
	public JSONObject posHotel(Long poi_id) {
		JSONObject returnJson = new JSONObject();
		if(poi_id != null && poi_id > 0){
			Poi poi = poiDao.get(poi_id);
			String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
			JSONObject json1 = ParseFile.parseJson(path);
			String url = json1.getString("url");
			String result = HttpUtil.sendPostStr(url+"/hotel/room-type/"+poi.getId(), "");
			JSONObject poiJson = new JSONObject();
			if(!Strings.isNullOrEmpty(result)){
				JSONObject resJson = JSONObject.fromObject(result);
				List<Ticket> tList = poi.getTickets();
				if(tList != null && tList.size() > 0){
					Ticket t = tList.get(0);
					String start_date = t.getStart_date()+" 12:00";
					String end_date = t.getEnd_date()+" 12:00";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date startDate = null;
					Date endDate = null;
					try {
						startDate = sdf.parse(start_date);
						endDate = sdf.parse(end_date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					poiJson.put("start_date", (startDate.getTime()/1000));
					poiJson.put("end_date", (endDate.getTime()/1000));
					poiJson.put("checking_rule",t.getChecking_rule());
				}
				int code = resJson.getInt("code");
				if(code == 1000){
					JSONArray ja = resJson.getJSONArray("data");
					if(ja != null && ja.size() > 0){
						poiJson.put("room_type", ja);
					}
					returnJson = CodeUtil.returnData(1000, "酒店查询成功", poiJson);
				}else{
					returnJson = CodeUtil.returnData(1043, "酒店查询失败", "");
				}
			}else{
				returnJson = CodeUtil.returnData(1040, "房源不足，无法预订", "");
			}
		}else{
			returnJson = CodeUtil.returnData(1041, "缺少参数", "");
		}
		
		return returnJson;
	}

	@Override
	public JSONObject search_room(HttpServletRequest request) {
		String type_id = request.getParameter("type_id");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		JSONObject returnJson = null;
		if(!Strings.isNullOrEmpty(type_id) 
				&& !Strings.isNullOrEmpty(start_time)
				&& !Strings.isNullOrEmpty(end_time)){
			JSONObject params = new JSONObject();
			long st = Long.parseLong(start_time);
			long et = Long.parseLong(end_time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = new Date(st * 1000);
			Date d2 = new Date(et * 1000);
			String start_date = sdf.format(d1);
			String end_date = sdf.format(d2);
			params.put("type_id", type_id);
			params.put("start_time", start_date);
			params.put("end_time", end_date);
			String path = getClass().getResource("/../../META-INF/hotel.json").getPath();
			JSONObject json1 = ParseFile.parseJson(path);
			String url = json1.getString("url");
			String result = HttpUtil.sendPostStr(url+"/inventory/usable", params.toString());
			if(!Strings.isNullOrEmpty(result)){
				JSONObject resJson = JSONObject.fromObject(result);
				int code = resJson.getInt("code");
				if(code == 1000){
					JSONArray ja = resJson.getJSONArray("data");
					if(ja != null && ja.size() > 0){
						JSONObject json = new JSONObject();
						json.put("rooms", ja);
						returnJson = CodeUtil.returnData(1000, "查询成功", json);
					}else{
						returnJson = CodeUtil.returnData(1036, "房源不足，无法预订", "");
					}
					
				}else{
					returnJson = CodeUtil.returnData(1035, "查询失败", "");
				}
			}else{
				returnJson = CodeUtil.returnData(1036, "房源不足，无法预订", "");
			}
		}
		return returnJson;
	}

}
