package com.fbpark.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

@Path("/notifications")
@Produces({ "application/json" })
public abstract interface NotificationService {
	// @Path("/{notificationId}")
	// @GET
	// public abstract Notification getNotification(@PathParam("notificationId")
	// Long paramLong);
	/**
	 * @api {GET} /notifications/users/{userId} 用户的通知列表
	 * @apiGroup Notifications
	 * @apiVersion 0.0.1
	 * @apiDescription 用户的通知列表
	 * @apiParam {Long} userId 用户Id
	 * @apiSuccessExample {json} 返回数据: 
	 * 	[ { "id":1, "create_time":"123456",
	 *                    "read_already":true, "type":"url/poi/content/normal",
	 *                    "url":"www.baidu.com", "reference_id":12,
	 *                    "notification_type":"0 系统通知 1 退票成功 2 退票申请被驳回",
	 *                    "title":"退票申请被驳回", },{ "id":1, "create_time":"123456",
	 *                    "read_already":true, "type":"url/poi/content/normal",
	 *                    "url":"www.baidu.com", "reference_id":12,
	 *                    "notification_type":"0 系统通知 1 退票成功 2 退票申请被驳回",
	 *                    "title":"退票申请被驳回", },{ "id":1, "create_time":"123456",
	 *                    "read_already":true, "type":"url/poi/content/normal",
	 *                    "url":"www.baidu.com", "reference_id":12,
	 *                    "notification_type":"0 系统通知 1 退票成功 2 退票申请被驳回",
	 *                    "title":"退票申请被驳回", },
	 * 
	 *                    ]
	 */
	@GET
	public abstract Response getNotifications(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@Context HttpServletRequest request);

	/**
	 * @api {POST} /notifications/notifications_info 添加设备信息
	 * @apiGroup Notifications
	 * @apiVersion 0.0.1
	 * @apiDescription 添加设备信息
	 * @apiParam {String} client_id 个推返回的client_id
	 * @apiParam {String} device_type 设备类型（ios android）
	 * @apiParam {String} device_token 设备token
	 * @apiParamExample {json} 范例 { "client_id":"client_id",
	 *                  "device_type":"device_type",
	 *                  "device_token":"device_token" }
	 * @apiSuccessExample {json} 返回数据: 
	 * { "status":"success" }
	 */
	@Path("/notifications_info")
	@POST
	public abstract Response notificationInfo(@HeaderParam("X-Tella-Request-Userid") Long paramLong,
			JSONObject paramJSONObject);

	@Path("/notifications_info/del")
	@DELETE
	public abstract Response deleteNotificationInfo(@HeaderParam("X-Tella-Request-Userid") Long paramLong,
			JSONObject paramJSONObject);

	@Path("/pushnotification")
	@PUT
	public abstract Response del_pushnotification(@HeaderParam("X-Tella-Request-Userid") Long loginUserid
			, JSONObject json);

}
