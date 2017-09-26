package com.fbpark.rest.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fbpark.rest.dao.NotificationDao;
import com.fbpark.rest.dao.OrdersTicketDao;
import com.fbpark.rest.dao.PushNotificationDao;
import com.fbpark.rest.dao.UserDao;
import com.fbpark.rest.model.Notification;
import com.fbpark.rest.model.OrdersTicket;
import com.fbpark.rest.model.PushNotification;
import com.fbpark.rest.model.Ticket;
import com.google.common.base.Strings;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
public class NotificationServiceImpl implements NotificationService {
	private static final Log log = LogFactory.getLog(NotificationServiceImpl.class);

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrdersTicketDao ordersTicketDao;

	@Autowired
	private PushNotificationDao pushNotificationDao;


	public Response getNotifications(Long userId, HttpServletRequest request) {
		log.debug("*** start get notifications ***");
		String countStr = request.getParameter("count");
		String maxIdStr = request.getParameter("max_id");
		int count = 20;
		List<Notification> nList = null;
		if (Strings.isNullOrEmpty(countStr) &&  Strings.isNullOrEmpty(maxIdStr)) {
			nList = this.notificationDao.getNotifications(userId, count);
		} else if (!Strings.isNullOrEmpty(countStr) 
				&& Strings.isNullOrEmpty(maxIdStr)) {
			count = Integer.parseInt(countStr);
			nList = this.notificationDao.getNotifications(userId, count);
		} else if (Strings.isNullOrEmpty(countStr) 
				&& !Strings.isNullOrEmpty(maxIdStr)) {
			nList = this.notificationDao.getNotificationsByPage(userId, count, Long.parseLong(maxIdStr));
		} else if (!Strings.isNullOrEmpty(countStr)
				&& !Strings.isNullOrEmpty(maxIdStr)) {
			count = Integer.parseInt(countStr);
			nList = this.notificationDao.getNotificationsByPage(userId, count, Long.parseLong(maxIdStr));
		}
		List<JSONObject> notificationModelList = new ArrayList<JSONObject>();
		JSONObject notificationJson = null;
		if ((nList != null) && (nList.size() > 0)) {
			for (Notification n : nList) {
				notificationJson = new JSONObject();
				notificationJson.put("id", n.getId());
				notificationJson.put("create_time", n.getCreate_time());
//				notificationJson.put("recipient_id", n.getRecipientId());
				JSONObject contentJson = new JSONObject();
				notificationJson.put("notification_type", n.getNotificationType());
				if (n.getNotificationType() == 0) {
					
					int object_type = n.getObjectType();
					if(object_type == 2){
						contentJson.put("type", "url");
						contentJson.put("url", n.getReference_url());
					}else if(object_type == 3){
						contentJson.put("type", "poi");
						contentJson.put("reference_id", n.getObjectId());
					}else if(object_type == 4){
						contentJson.put("type", "content");
						contentJson.put("reference_id", n.getObjectId());
					}else if(object_type == 5){
						contentJson.put("type", "normal");
					}
					notificationJson.put("content", contentJson);
					notificationJson.put("title", n.getTitle());
				}else if(n.getNotificationType() == 1){//退票成功
					int objectType = n.getObjectType();
					if(objectType == 1){
						OrdersTicket ot = ordersTicketDao.get(n.getObjectId());
						Ticket ticket = ot.getTicket();
						contentJson.put("ticket_id", ticket.getId());
						contentJson.put("ordersTicket_id", ot.getId());
						contentJson.put("ticket_name", ticket.getTicket_name());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
						String endTimeStr = ticket.getStart_date()+" "+ticket.getEnd_time();
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
						contentJson.put("start_time", start_time);
						contentJson.put("end_time", end_time);
						contentJson.put("status", ot.getStatus());
						contentJson.put("num", ot.getNum());
					}
					notificationJson.put("content", contentJson);
					notificationJson.put("title", n.getTitle());
				}else if(n.getNotificationType() == 2){//退票申请被驳回
					int objectType = n.getObjectType();
					if(objectType == 1){
						OrdersTicket ot = ordersTicketDao.get(n.getObjectId());
						Ticket ticket = ot.getTicket();
						contentJson.put("ticket_id", ticket.getId());
						contentJson.put("ordersTicket_id", ot.getId());
						contentJson.put("ticket_name", ticket.getTicket_name());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String startTimeStr = ticket.getStart_date()+" "+ticket.getStart_time();
						String endTimeStr = ticket.getStart_date()+" "+ticket.getEnd_time();
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
						contentJson.put("start_time", start_time);
						contentJson.put("end_time", end_time);
						contentJson.put("status", ot.getStatus());
						contentJson.put("num", ot.getNum());
					}
					notificationJson.put("content", contentJson);
					notificationJson.put("title", n.getTitle());
				}else if(n.getNotificationType() == 5){//退款成功
					int objectType = n.getObjectType();
					if(objectType == 1 || objectType == 6){
						contentJson.put("info",n.getRemark());
					}
					notificationJson.put("content", contentJson);
					notificationJson.put("title", n.getTitle());
				}else if(n.getNotificationType() == 4){//退款失败
					int objectType = n.getObjectType();
					if(objectType == 1){
						contentJson.put("info",n.getRemark());
					}
					notificationJson.put("content", contentJson);
					notificationJson.put("title", n.getTitle());
				}
				notificationModelList.add(notificationJson);
			}
		}
		log.debug("*** notification list ***" + JSONArray.fromObject(notificationModelList));
		return Response.status(Response.Status.OK).entity(notificationModelList).build();
	}


	public Response notificationInfo(Long loginUserid, JSONObject json) {
		String client_id = json.getString("client_id");
		String device_type = json.getString("device_type");
		String device_token = null;
		if (json.containsKey("device_token")) {
			device_token = json.getString("device_token");
		}
		JSONObject returnJson = new JSONObject();

		if ((!Strings.isNullOrEmpty(client_id)) && (!Strings.isNullOrEmpty(device_type))
				&& (!Strings.isNullOrEmpty(device_token))) {
			PushNotification pushNotification = new PushNotification();
			PushNotification pn = pushNotificationDao.getPushNotificationByClientid(client_id);
			if (loginUserid != null && loginUserid > 0) {
				
				if (pn != null) {
					pn.setClientId(client_id);
					pn.setDeviceToken(device_token);
					pn.setUserId(loginUserid);
					pn.setDeviceType(device_type);
					pushNotificationDao.update(pn);
				} else {
					pushNotification.setUserId(loginUserid);
					pushNotification.setClientId(client_id);
					pushNotification.setDeviceType(device_type);
					if (json.containsKey("device_token")) {
						pushNotification.setDeviceToken(device_token);
					}

					this.pushNotificationDao.save(pushNotification);
				}

			} else {
				if(pn == null){
					pushNotification.setUserId(0l);
					pushNotification.setClientId(client_id);
					pushNotification.setDeviceType(device_type);
					if (json.containsKey("device_token")) {
						pushNotification.setDeviceToken(device_token);
					}

					this.pushNotificationDao.save(pushNotification);
				}
				
			}
			/*
			 * if (pn != null) { //this.pushNotificationDao.delete((Long)
			 * pn.getId()); if(loginUserid != null && loginUserid > 0){
			 * pn.setUserId(loginUserid); }else{ pn.setUserId(0l); }
			 * 
			 * //pushNotification.setClientId(client_id);
			 * if(json.containsKey("device_token")){
			 * pn.setDeviceToken(device_token); }
			 * 
			 * this.pushNotificationDao.update(pn); } else { if(loginUserid !=
			 * null && loginUserid > 0){
			 * pushNotification.setUserId(loginUserid); }else{
			 * pushNotification.setUserId(0l); }
			 * pushNotification.setClientId(client_id);
			 * pushNotification.setDeviceType(device_type);
			 * if(json.containsKey("device_token")){
			 * pushNotification.setDeviceToken(device_token); }
			 * 
			 * this.pushNotificationDao.save(pushNotification); }
			 */
			returnJson.put("status", "success");
			return Response.status(Response.Status.CREATED).entity(returnJson).build();
		}
		returnJson.put("status", "invalid_request");
		returnJson.put("code", Integer.valueOf(10010));
		returnJson.put("error_message", "Invalid payload parameters");
		return Response.status(Response.Status.BAD_REQUEST).entity(returnJson).build();
	}

	public Response deleteNotificationInfo(Long loginUserid, JSONObject json) {
		String client_id = json.getString("client_id");
		String device_type = json.getString("device_type");
		JSONObject returnJson = new JSONObject();
		PushNotification pn = this.pushNotificationDao.getPushNotificationByClientidAndDevice(client_id, device_type);
		if (pn != null) {
			this.pushNotificationDao.delete((Long) pn.getId());
			returnJson.put("status", "success");
			return Response.status(Response.Status.CREATED).entity(returnJson).build();
		}
		returnJson.put("status", "invalid_request");
		returnJson.put("code", Integer.valueOf(10010));
		returnJson.put("error_message", "Invalid payload parameters");
		return Response.status(Response.Status.BAD_REQUEST).entity(returnJson).build();
	}


	@Override
	public Response del_pushnotification(Long loginUserid, JSONObject json) {

		String client_id = json.getString("client_id");
		String device_type = json.getString("device_type");
		String device_token = null;
		if (json.containsKey("device_token")) {
			device_token = json.getString("device_token");
		}
		JSONObject returnJson = new JSONObject();

		if ((!Strings.isNullOrEmpty(client_id)) && (!Strings.isNullOrEmpty(device_type))
				&& (!Strings.isNullOrEmpty(device_token))) {
			PushNotification pn = pushNotificationDao.getPushNotificationByClientid(client_id);
			if (loginUserid != null && loginUserid > 0) {
				
				if (pn != null) {
					pn.setClientId(client_id);
					pn.setDeviceToken(device_token);
					pn.setUserId(0l);
					pn.setDeviceType(device_type);
					pushNotificationDao.update(pn);
				} 

			}
			returnJson.put("status", "success");
			return Response.status(Response.Status.CREATED).entity(returnJson).build();
		}
		returnJson.put("status", "invalid_request");
		returnJson.put("code", Integer.valueOf(10010));
		returnJson.put("error_message", "Invalid payload parameters");
		return Response.status(Response.Status.BAD_REQUEST).entity(returnJson).build();
	
	}


}
