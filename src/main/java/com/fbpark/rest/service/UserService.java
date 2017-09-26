package com.fbpark.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.fbpark.rest.model.LinkAccounts;
import com.fbpark.rest.service.model.PasswordModel;

import net.sf.json.JSONObject;

@Path("/users")
@Produces({ "application/json" })
public abstract interface UserService {
	/**
	 * @api {POST} /users/signup 用户注册
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 用户注册
	 * @apiParam {String} username 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {String} phone 手机号
	 * @apiParam {String} identity_card 身份证号
	 * @apiParam {String} club 所属俱乐部
	 * @apiParam {String} birthday 出生日期
	 * @apiParam {String} trade 行业
	 * @apiParam {Object} link_account 三方用户信息（可选）
	 * @apiParamExample {json} 范例
 	 * {
	 *      "username":"郭胜利",
	 *      "password":"123456",
	 *      "phone":"15201132276",
	 *      "identity_card":"身份证号",
	 *      "club":"所属俱乐部",
	 *      "birthday":"1990-01-12"，
	 *      "trade":"所属行业",
	 *      "link_account":{
	 *      	"unionid":"sdfsfdsdfdsdfse",
	 *      	"auth_token":"sdfsfsdfsdf"
	 *      }
	 * }
	 * @apiSuccess (200) {String} userid 用户id
	 * @apiSuccess (200) {String} token_timestamp 时间戳
	 * @apiSuccess (200) {String} access_token token
	 * @apiSuccessExample {json} 返回数据: 
	 * { 
	 * 	  "userid":12,
	 *    "token_timestamp":"12345",
	 *    "access_token":"fe6386d550bd434b8cd994b58c3f8075" 
	 * }
	 */
	@Path("/signup")
	@POST
	@Consumes({ "application/json" })
	public abstract Response signup(JSONObject user);
	
	/**
	 * @api {POST} /users/register e族用户注册
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 用户注册
	 * @apiParam {String} username 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {String} phone 手机号
	 * @apiParam {String} identity_card 身份证号
	 * @apiParam {String} club 所属俱乐部
	 * @apiParam {String} trade 行业
	 * @apiParamExample {json} 范例
 	 * {
	 *      "username":"郭胜利",
	 *      "password":"123456",
	 *      "phone":"15201132276",
	 *      "identity_card":"身份证号",
	 *      "club":"所属俱乐部",
	 *      "trade":"所属行业",
	 * }
	 * @apiSuccess (200) {String} userid 用户id
	 * @apiSuccess (200) {String} token_timestamp 时间戳
	 * @apiSuccess (200) {String} access_token token
	 * @apiSuccessExample {json} 返回数据: 
	 * { 
	 * 	  "userid":12,
	 *    "token_timestamp":"12345",
	 *    "access_token":"fe6386d550bd434b8cd994b58c3f8075" 
	 * }
	 */
	@Path("/register")
	@POST
	@Consumes({ "application/json" })
	public abstract Response fblife_user(JSONObject user);

	/**
	 * @api {POST} /users/signin 用户登录
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 用户登录
	 * @apiParam {String} phone 手机号
	 * @apiParam {String} password 密码
	 * @apiParam {Long} timestamp 时间戳
	 * 
	 * @apiParamExample {json} 范例 
	 * 							{
								    "phone": "15201132276", 
								    "password": "123456", 
								    "timestamp": 2345678843
								}
	 * @apiSuccess (200) {String} userid 用户id
	 * @apiSuccess (200) {String} token_timestamp 时间戳
	 * @apiSuccess (200) {String} token token
	 * @apiSuccessExample {json} 返回数据:
	 * { 
	 * 	  "userid":12,
	 *    "token_timestamp":"12345",
	 *    "access_token":"fe6386d550bd434b8cd994b58c3f8075" 
	 * }
	 */
	@Path("/signin")
	@POST
	@Consumes({ "application/json" })
	public Response signin(JSONObject user);

	/**
	 * @api {POST} /users/forgot_password 忘记密码
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 忘记密码
	 * @apiParam {String} zone 区号
	 * @apiParam {String} phone 手机号
	 * @apiParam {String} code 手机验证码
	 * @apiParam {String} password 密码
	 * @apiParamExample {json} 范例 :
	 * 					{
	 * 						"phone":"15201132276",
	 * 						"code":"1234",
	 * 						"password":"123456"
	 * 					}
	 * @apiSuccess (200) {String} userid 用户id
	 * @apiSuccess (200) {String} username 用户名
	 * @apiSuccess (200) {String} token token
	 * @apiSuccess (200) {String} token_timestamp 时间戳
	 * @apiSuccessExample {json} 返回数据:
	 * { 
	 * 	  "userid":12,
	 *    "token_timestamp":"12345",
	 *    "access_token":"fe6386d550bd434b8cd994b58c3f8075" 
	 * }
	 */
	@Path("/forgot_password")
	@POST
	public Response forgetPassword(JSONObject data,
			@HeaderParam("X-Tella-Request-AppVersion") String appVersion,
			@HeaderParam("X-Tella-Request-Device") String device) throws Exception;

	/**
	 * @api {PUT} /users/perfect/{userid} 完善用户信息
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 更新用户信息
	 * @apiParam {String} zone 区号
	 * @apiParam {String} phone 手机号
	 * @apiParam {String} code 短信验证码
	 * @apiParamExample {json} 范例
	 *  {
	 *      "zone":"区号",
	 *      "phone":"123456",
	 *      "code":"短信验证码",
	 * }
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回数据: 
	 * 					{"status":"success"}
	 * @apiError 10010 invalid_request
	 * @apiErrorExample {json} Error-Response: HTTP/1.1 400 Not Found 
	 * {
	 *    "status":"invalid request",
	 *    "code":10010,
	 *    "error_message":"invalid request" 
	 * }
	 */
	@Path("/perfect/{userId}")
	@PUT
	@Consumes({ "application/json" })
	public abstract Response perfectUser(@PathParam("userId") Long userId, JSONObject paramUpdateUserModel,
			@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
	/**
	 * @api {PUT} /users/{userid} 更新用户信息
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 更新用户信息
	 * @apiParam {String} club_name 俱乐部名 可选
	 * @apiParam {String} username 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {String} identity_card 身份证号
	 * @apiParam {String} birthday 出生日期
	 * @apiParam {String} trade 行业
	 * @apiParamExample {json} 范例
	 *  {
	 *      "username":"郭胜利",
	 *      "password":"123456",
	 *      "identity_card":"身份证号",
	 *      "club_name":"所属俱乐部",
	 *      "birthday":"1990-01-12"，
	 *      "trade":"所属行业"
	 * }
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回数据: 
	 * 					{"status":"success"}
	 * @apiError 10010 invalid_request
	 * @apiErrorExample {json} Error-Response: HTTP/1.1 400 Not Found 
	 * {
	 *    "status":"invalid request",
	 *    "code":10010,
	 *    "error_message":"invalid request" 
	 * }
	 */
	@Path("/{userId}")
	@PUT
	@Consumes({ "application/json" })
	public abstract Response updateUser(@PathParam("userId") Long paramLong1, JSONObject paramUpdateUserModel,
			@HeaderParam("X-Tella-Request-Userid") Long paramLong2);
	
	/**
	 * @api {PUT} /users/{userid} 更新用户信息
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 更新用户信息
	 * @apiParam {String} club_name 俱乐部名 可选
	 * @apiParam {String} username 用户名
	 * @apiParam {String} password 密码
	 * @apiParam {String} identity_card 身份证号
	 * @apiParam {String} birthday 出生日期
	 * @apiParam {String} trade 行业
	 * @apiParamExample {json} 范例
	 *  {
	 *      "username":"郭胜利",
	 *      "password":"123456",
	 *      "identity_card":"身份证号",
	 *      "club_name":"所属俱乐部",
	 *      "birthday":"1990-01-12"，
	 *      "trade":"所属行业"
	 * }
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回数据: 
	 * 					{"status":"success"}
	 * @apiError 10010 invalid_request
	 * @apiErrorExample {json} Error-Response: HTTP/1.1 400 Not Found 
	 * {
	 *    "status":"invalid request",
	 *    "code":10010,
	 *    "error_message":"invalid request" 
	 * }
	 */
	@Path("/{userId}")
	@GET
	public abstract Response getUser(@PathParam("userId") Long userId);
	

	/**
	 * @api {POST} /users/login/linked_account 用户三方登录
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 用户三方登录（微信）
	 * @apiParam {String} nickname 三方的昵称
	 * @apiParam {String} service 三方的类型（wechat、weibo等）
	 * @apiParam {String} union_id 三方唯一的标示
	 * @apiParam {String} avatar_url 三方头像的路径
	 * 
	 * @apiParamExample {json} 范例 
	 * 		{ 
	 * 			"nickname":"shengli", 
	 * 			"service":"wechat",
	 *      	"union_id":"sfdelklol124",
	 *      	"avatar_url":"http://www.baidu.com" 
	 *      }
	 * @apiSuccess (200) {long} userid 123
	 * @apiSuccess (200) {String} token_timestamp 236756123
	 * @apiSuccess (200) {String} access_token asert123reew
	 * @apiSuccessExample {json} 返回数据: 
	 * 			{ "user_id":1234,
	 *            "token_timestamp":236756123,
	 *            "access_token":"asert123reew"
	 *           }
	 * @apiError 10010 invalid_request
	 * @apiError 10080 该用户不存在
	 * @apiErrorExample {json} Error-Response: HTTP/1.1 400 Not Found 
	 * {
	 * 	"status":"该用户不存在",
	 *  "code":10080, 
	 *  "error_message":"该用户不存在"
	 *}
	 */
	@Path("/login/linked_account")
	@POST
	public abstract Response loginLinkAccounts(LinkAccounts linkAccount, @Context HttpServletRequest request);

	/**
	 * @api {PUT} /users/{userid}/pwd 修改密码
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 修改密码
	 * @apiParam {String} current_password 原密码
	 * @apiParam {String} password_new 新密码
	 * @apiParamExample {json} 范例
	 *                  {current_password:123456,new_password:123456789}
	 * @apiSuccess (200) {String} userid 用户id
	 * @apiSuccess (200) {String} token_timestamp 时间戳
	 * @apiSuccess (200) {String} access_token token
	 * @apiSuccessExample {json} 返回数据: 
	 * { 
	 *   "userid":"user_id",
	 *   "token_timestamp":123456,
	 *   "access_token":"fe6386d550bd434b8cd994b58c3f8075" 
	 * }
	 */
	@Path("/{userId}/pwd")
	@PUT
	@Consumes({ "application/json" })
	public abstract Response updatePassword(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@PathParam("userId") Long userid, PasswordModel passwordModel);
	
	/**
	 * @api {POST} /users/{poi_id}/collect 收藏热点
	 * @apiGroup Users
	 * @apiVersion 0.0.1
	 * @apiDescription 修改密码
	 * @apiParam {Long} poi_id poiId
	 * @apiSuccessExample {json} 返回数据: 
	 * { 
	 *   "status":"success",
	 * }
	 */
	@Path("/{poi_id}/collect")
	@POST
	public abstract Response add_collect(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@PathParam("poi_id") Long poi_id);
	
	
	/**
 	 * @api {GET} /users/my_collect 我的收藏
 	 * @apiGroup Users
 	 * @apiVersion 0.0.1
 	 * @apiDescription 我的收藏
 	 * @apiSuccessExample {json} 返回数据:
 	 *                
 	 *                
 	 *                
 	 * [
		 * 		{
		 * 			"id":11,
		 * 			"reference_type":"poi",
		 * 			"create_time":1234567,
		 * 			"content":{
		 * 				"id":12,
		 * 				"title":"title",
		 * 				"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
		 * 				"is_collect":true
		 * 			},
		 * 		},{
		 * 			"id":11,
		 * 			"reference_type":"poi",
		 * 			"create_time":1234567,
		 * 			"content":{
		 * 				"id":12,
		 * 				"title":"title",
		 * 				"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
		 * 				"is_collect":true
		 * 			},
		 * 		}
		 * ]
 	 */
	@Path("/my_collect")
	@GET
	public abstract Response my_collect(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@Context HttpServletRequest request);
	
	/**
	 * @api {POST} /users/cart/add 添加购物车
	 * @apiGroup Cart
	 * @apiVersion 0.0.1
	 * @apiDescription 添加购物车
	 * @apiParam {Long} ticket_id 票Id
	 * @apiParamExample {json} 范例
	 *                  {"ticket_id":12}
	 * @apiSuccessExample {json} 返回数据: 
	 * { 
	 *   "status":"success",
	 * }
	 */
	@Path("/cart/add")
	@POST
	public abstract Response add_cart(JSONObject cart, @HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
	/**
 	 * @api {GET} /users/cart 购物车所有商品
 	 * @apiGroup Cart
 	 * @apiVersion 0.0.1
 	 * @apiDescription 购物车所有商品
 	 * @apiParam {int} count 每页数量（可选）
	 * @apiParam {String} max_id 分页的id
 	 * @apiSuccessExample {json} 返回数据:
 	 *                [
 	 *                	{
 	 *                		"id":1,
 	 *                		"num":2,
 	 *                		"create_time":1234565,
 	 *                		"total":234.56,
 	 *                		"ticket":{
 	 *                						"ticket_id":12,
 	 *                						"title":"Monster Jam大脚车表演",
 	 *                						"cover_image":{
     *       									"original_size":"{750, 1334}",
     *                							"name":"6/4FB02A9D81D2930B4AD15E9796697477"
	 	 *                					},
	 	 *                					"price":234,
	 	 *                					
 	 *                		},
 	 *                		
 	 *                	},{
 	 *                		"id":1,
 	 *                		"num":2,
 	 *                		"create_time":1234565,
 	 *                		"total":234.56,
 	 *                		"ticket":{
 	 *                			"ticket_id":12,
 *                			    "title":"Monster Jam大脚车表演",
 *                				"cover_image":{
 *       						"original_size":"{750, 1334}",
 *                				"name":"6/4FB02A9D81D2930B4AD15E9796697477"
 	 *                			},
 	 *                			"price":234,		
	 	 *                					
 	 *                		},
 	 *                		
 	 *                	}
 	 *                
 	 *                	
 	 *                ]
 	 */
	@Path("/cart")
	@GET
	public abstract Response cart_list(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@Context HttpServletRequest request);

	/**
	 * @api {DELETE} /users/cart/{cart_id} 删除购物车中的其中的一个商品
	 * @apiGroup Cart
	 * @apiVersion 0.0.1
	 * @apiDescription 删除购物车中的其中的一个商品
	 * @apiSuccessExample {json} 返回数据: 
	 * 				{"status":"success"}
	 */
	@Path("/cart/{cart_id}")
	@DELETE
	public abstract Response delete_cart(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@PathParam("cart_id") Long cart_id);

	/**
	 * @api {POST} /users/add_order 提交订单
	 * @apiGroup Client Orders
	 * @apiVersion 0.0.1
	 * @apiDescription 提交订单
	 * @apiParam {Double} amount 订单金额
	 * @apiParam {JSONObject} ticket 票信息
	 * @apiParamExample {json} 范例 
	 * {
	    "amount": 2543.23,
	    "room_type_id":12,
	    "room_type":"大床房",
	    "start_date":"起订日期",
	    "end_date":"结束日期"
	     
	    "ticket":{
	            "id": 12, 
	            "ticket_name": "大脚怪",
	            "contacters":[
	            	1,2,3,4,5
	            ]
	        }
	}
	 * @apiSuccessExample {json} 返回数据: 
	 * 			{"orders_id":12}
	 */
	@Path("/add_order")
	@POST
	public abstract Response add_order(@HeaderParam("X-Tella-Request-Userid") Long loginUserid, JSONObject orders);
	
	/**
	 * @api {POST} /users/pos/add_order POS机提交订单
	 * @apiGroup Client Orders
	 * @apiVersion 0.0.1
	 * @apiDescription 提交订单
	 * @apiParam {Double} amount 订单金额
	 * @apiParam {JSONObject} ticket 票信息
	 * @apiParamExample {json} 范例 
	 * {
	    "amount": 2543.23, 
	    "ticket":{
	            "id": 12, 
	            "ticket_name": "大脚怪",
	            "contacters":[
	            	{
	            		"identity_card":"",
	            		"username":""
	            	},{
	            		"identity_card":"",
	            		"username":""
	            	},{
	            		"identity_card":"",
	            		"username":""
	            	}
	            ]
	        }
	}
	 * @apiSuccessExample {json} 返回数据: 
	 * 			{"orders_id":12}
	 */
	@Path("/pos/add_order")
	@POST
	public abstract JSONObject add_pos_order(JSONObject orders);
	
	/**
	 * @api {POST} /users/charge 获得支付charge
	 * @apiGroup Ping ++
	 * @apiVersion 0.0.1
	 * @apiDescription 获得支付charge
	 * @apiParam {Long} id 订单Id
	 * @apiParam {String} amount 订单金额
	 * @apiParam {String} pay_type 支付类型
	 * @apiParamExample {json} 范例 
	 * {
	 *  "id": 12, 
		}
	 * @apiSuccessExample {json} 返回数据: 
	 * 	{
		  "id": "ch_L8qn10mLmr1GS8e5OODmHaL4",
		  "object": "charge",
		  "created": 1410834527,
		  "livemode": true,
		  "paid": false,
		  "refunded": false,
		  "app": "app_1Gqj58ynP0mHeX1q",
		  "channel": "upacp",
		  "order_no": "123456789",
		  "client_ip": "127.0.0.1",
		  "amount": 100,
		  "amount_settle": 100,
		  "currency": "cny",
		  "subject": "Your Subject",
		  "body": "Your Body",
		  "extra":{},
		  "time_paid": null,
		  "time_expire": 1410838127,
		  "time_settle": null,
		  "transaction_no": null,
		  "refunds": {
		    "object": "list",
		    "url": "/v1/charges/ch_L8qn10mLmr1GS8e5OODmHaL4/refunds",
		    "has_more": false,
		    "data": [ ]
		  },
		  "amount_refunded": 0,
		  "failure_code": null,
		  "failure_msg": null,
		  "metadata": {},
		  "credential": {
		    "object": "credential",
		    "upacp": {
		      "tn": "201409161028470000000",
		      "mode": "01"
		    }
		  },
		  "description": null
		}		
	 */
	@Path("/charge")
	@POST
	public abstract Response add_charge(@HeaderParam("X-Tella-Request-Userid") Long loginUserid, JSONObject orders,@Context HttpServletRequest request);
	
	/**
	 * @api {POST} /users/free 获得免费票
	 * @apiGroup Ping ++
	 * @apiVersion 0.0.1
	 * @apiDescription 获得支付charge
	 * @apiParam {Long} id 订单Id
	 * @apiParam {String} amount 订单金额
	 * @apiParam {String} pay_type 支付类型
	 * @apiParamExample {json} 范例 
	 * {
	 *  "id": 12, 
		}
	 */
	@Path("/free")
	@POST
	public abstract Response free_ticket(@HeaderParam("X-Tella-Request-Userid") Long loginUserid, JSONObject orders,@Context HttpServletRequest request)throws Exception;
	
	/**
	 * @api {POST} /users/orders_refund 订单退款
	 * @apiGroup Ping ++
	 * @apiVersion 0.0.1
	 * @apiDescription 订单退款
	 * @apiParam {Long} id 订单Id
	 * @apiParam {String} amount 订单金额
	 * @apiParam {String} pay_type 支付类型
	 * @apiParamExample {json} 范例 
	 * {
	 *  "id": 12, 
	    "amount": 2543.23, 
	    "pay_type": "wechat/alipay", 
		}
	 * @apiSuccessExample {json} 返回数据: 
	 * 	{
		  "status":"success",
		  "message":"退款申请已提交，请耐心等待"
		}		
	 */
	@Path("/orders_refund")
	@POST
	public abstract Response orders_refund(@HeaderParam("X-Tella-Request-Userid") Long loginUserid, JSONObject orders,@Context HttpServletRequest request);
	
	/**
	 * @api {POST} /users/return_ticket 退票申请
	 * @apiGroup Ping ++
	 * @apiVersion 0.0.1
	 * @apiDescription 退票申请
	 * @apiParam {Long} id 票订单Id
	 * @apiParamExample {json} 范例 
	 * {
	 *  "id": 12, 
		}
	 * @apiSuccessExample {json} 返回数据: 
	 * 	{
		  "status":"success",
		  "message":"退款申请已提交，请耐心等待"
		}		
	 */
	@Path("/return_ticket")
	@POST
	public abstract Response return_ticket(@HeaderParam("X-Tella-Request-Userid") Long loginUserid, JSONObject orders_ticket,@Context HttpServletRequest request)throws Exception;
	
	/**
	 * @api {POST} /users/refund_response 退票申请是否成功回调API
	 * @apiGroup Ping ++
	 * @apiVersion 0.0.1
	 * @apiDescription 退票申请是否成功回调API
	 * @apiSuccessExample {json} 返回数据: 
	 * url?retreatBatchNo={retreatBatchNo}&order_Code={orderCode}&subOrderCode={subOrderCode}&auditStatus=${auditStatus}&returnNum=1&sign=md5({orderCode}{privateKey})
		
	 */
	@Path("/refund_response")
	@GET
	public abstract String refund_response(@Context HttpServletRequest request)throws Exception;
	
	/**
	 * @api {GET} /users/check_ticket POS机检票
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription 检票是否成功回调API
	 * @apiSuccessExample {json} 返回数据: 
	 * url?order_code={orderCode}&status=success&checkNum=1&returnNum=5&total=10&sign=md5(order_code={orderCode}{privateKey})
	 */
	@Path("/check_ticket")
	@GET
	public abstract JSONObject check_ticket(@Context HttpServletRequest request)throws Exception;
	
	/**
	 * @api {GET} /users/hotel POS机根据订单号查出对应的房间号
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription POS机根据订单号查出对应的房间号
	 * @apiParam {String} ticket_order_no 订单号
	 * @apiParamExample {json} 范例 
	 * {
	 *  "orders_ticket_no": 12, 
		}
	 * @apiSuccessExample {json} 返回数据: 
	 	{
		  "status":"success",
		}
	 */
	@Path("/hotel")
	@GET
	public abstract JSONObject hotel(@Context HttpServletRequest request)throws Exception; 
	
	/**
	 * @api {POST} /users/checkin POS机分配入住
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription POS机分配入住
	 * @apiParam {Long} id 票订单Id
	 * @apiParamExample {json} 范例 
	 * {
	 *  "orders_ticket_no": 12, 
	 *  "room_id":[23,32]
		}
	 * @apiSuccessExample {json} 返回数据: 
	 * 	{
		  "status":"success",
		}
	 */
	@Path("/checkin")
	@POST
	public abstract JSONObject checkin(JSONObject checkin)throws Exception; 
	
	/**
	 * @api {GET} /users/verification POS核销
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription 检票是否成功回调API
	 * @apiSuccessExample {json} 返回数据: 
	 * url?order_code={orderCode}&status=success&checkNum=1&returnNum=5&total=10&sign=md5(order_code={orderCode}{privateKey})
	 */
	@Path("/verification")
	@GET
	public abstract JSONObject verification_ticket(@Context HttpServletRequest request)throws Exception;
	
	/**
	 * @api {POST} /users/record POS机录入用户
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription 检票是否成功回调API
	 * @apiSuccessExample {json} 返回数据: 
	 * url?order_code={orderCode}&status=success&checkNum=1&returnNum=5&total=10&sign=md5(order_code={orderCode}{privateKey})
	 */
	@Path("/record")
	@POST
	public abstract JSONObject record_user(JSONObject user)throws Exception;
	
	/**
	 * @api {POST} /users/check_charge 支付完成后客户端调用
	 * @apiGroup Ping ++
	 * @apiVersion 0.0.1
	 * @apiDescription 支付完成后客户端调用
	 * @apiParam {String} charge_id charge_id
	 * @apiParam {String} order_no 票订单号
	 * @apiParamExample {json} 范例 
	 * {
	 *  "charge_id": "ch_L8qn10mLmr1GS8e5OODmHaL4", 
	 *  "order_no":"012434354545"
		}
	 * @apiSuccessExample {json} 返回数据: 
	 * 	{
		  "status":"success",
		  "message":"退款申请已提交，请耐心等待"
		}		
	 */
	@Path("/check_charge")
	@POST
	public abstract Response check_charge(@HeaderParam("X-Tella-Request-Userid") Long loginUserid, JSONObject orders,@Context HttpServletRequest request)throws Exception;
	
	 @Path("/error/noData")
	  @GET
	  public abstract Response noData();

	  @Path("/error/noUser")
	  @GET
	  public abstract Response noUser();

	  @Path("/error/invalid_token")
	  @GET
	  public abstract Response invalidToken();
	  
	  @Path("/error/invalid_version")
	  @GET
	  public abstract Response invalidVersion();

	  @Path("/error/noAuthority")
	  @GET
	  public abstract Response noAuthority();
	  
	  /**
	 	 * @api {GET} /users/profile 个人页
	 	 * @apiGroup Users
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 个人页
	 	 * @apiSuccessExample {json} 返回数据:
	 	 *                
	 	 *                	{
	 	 *                		"user_id":1,
	 	 *                		"username":"标题",
	 	 *                		"avatar_image":{
		 *                						"media":{
		 *                									"zoom":1,
		 *                									"original_size":"{750, 1334}",
		 *                									"focalpoint":"{0, 0}",
		 *                									"name":"6/4FB02A9D81D2930B4AD15E9796697477"
		 *                								},
		 *                						"type":"image"
		 *                					},
	 	 *                  "introduction":"北京市昌平区十三陵",
		 *                  "join_event":[
		 *                  	{
		                   			"id":1,
		 *                			"title":"标题",
		 *                			"cover_image":{
		 *                						"media":{
		 *                									"zoom":1,
		 *                									"original_size":"{750, 1334}",
		 *                									"focalpoint":"{0, 0}",
		 *                									"name":"6/4FB02A9D81D2930B4AD15E9796697477"
		 *                								},
		 *                						"type":"image"
		 *                					},
		 *                 	 		"address":"北京市昌平区十三陵",
		 *                  		"is_joined":true,
		 *                  	}
		 *                  ],
		 *                  "orders":[
		 *                  	{
		 *                  		"order_id":12,
		 *                  		"order_no":234567890,
		 *                  		"acount":1234.98,
		 *                  		"status":"unpaid",
		 *                  		"address":"",
		 *                  		"delivery_point":"",
		 *                  		"goods":[
		 *                  			{
		 *                  				"goods_id":12,
		 *                  				"goods_name":"iphone 7",
		 *                  				"color":"#123455",
		 *                  				"category":"特大",
		 *                  				"number":12,
		 *                  				"price":123.45,
		 *                  				"cover_image":{
		 *                						"media":{
		 *                									"zoom":1,
		 *                									"original_size":"{750, 1334}",
		 *                									"focalpoint":"{0, 0}",
		 *                									"name":"6/4FB02A9D81D2930B4AD15E9796697477"
		 *                								},
		 *                						"type":"image"
		 *                					}
		 *                  			}
		 *                  		]
		 *              
		 *                  	}
		 *                  ]
	 	 *                		
	 	 *                	}                
	 	 *                	
	 	 */
	  @Path("/profile")
	  @GET
	  public Response profile(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
	  


		 /**
		 * @api {GET} /users/unuse_ticket 未使用的门票
		 * @apiGroup Client_Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 未使用的门票
		 * @apiSuccessExample {json} 返回数据: 
		 * 		{ 
		 * 			"user":{
		 * 				"id":12,
		 * 				"username":"username",
		 * 				"level":"vip",
		 * 				
		 * 			},
		 * 			"orders":[
		 * 				{
		 * 					"orderTicket_id":11,
		 * 					"ticket_order_no":"sfsfsfd",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":1,
		 * 					"start_time":"1234234343",
		 * 					"end_time":"1234234343",
		 * 					"status":0
		 * 				},{
		 * 					"orderTicket_id":11,
		 * 					"ticket_order_no":"sfsfsfd",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":1,
		 * 					"start_time":"1234234343",
		 * 					"end_time":"1234234343",
		 * 					"status":0
		 * 				},{
		 * 					"orderTicket_id":11,
		 * 					"ticket_order_no":"sfsfsfd",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":1,
		 * 					"start_time":"1234234343",
		 * 					"end_time":"1234234343",
		 * 					"status":0
		 * 				}
		 * 			]
		 *     }
		 */
		@Path("/unuse_ticket")
		@GET
		public abstract Response unuse_ticket(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
		
		 /**
		 * @api {GET} /users/used_ticket 已使用的门票
		 * @apiGroup Client_Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 已使用的门票
		 * @apiSuccessExample {json} 返回数据: 
		 * 		[
		 * 				{
		 * 					"orderTicket_id":11,
		 * 					"ticket_order_no":"sfsfsfd",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":1,
		 * 					"start_time":"1234234343",
		 * 					"end_time":"1234234343",
		 * 					"status":0	
		 * 				},{
		 * 					"orderTicket_id":12,
		 * 					"ticket_order_no":"sfsfsfd",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":1,
		 * 					"start_time":"1234234343",
		 * 					"end_time":"1234234343",
		 * 					"status":0
		 * 				},{
		 * 					"orderTicket_id":13,
		 * 					"ticket_order_no":"sfsfsfd",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":1,
		 * 					"start_time":"1234234343",
		 * 					"end_time":"2424234234",
		 * 					"status":0
		 * 				}
		 * 			]
		 */
		@Path("/used_ticket")
		@GET
		public abstract Response used_ticket(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
		
		 /**
		 * @api {GET} /users/unpaid_orders 未支付订单
		 * @apiGroup Client_Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 未支付订单
		 * @apiSuccessExample {json} 返回数据: 
		 * 		[
		 * 				{	"id":12,
		 * 					"orders_no":"1235667",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":3,
		 * 					"start_time":"sfdsfsddsf",
		 * 					"end_time":"dsfsfdsfsf",
		 * 					"status":"1",
		 * 					"count":2,
		 * 					"type":0 //未付款的里面有未支付、已过期、已售完、退票中、余票不足 使用数据中使用type对应：0、1、2、3、4
		 * 				},{
		 * 					"id":12,
		 * 					"orders_no":"1235667",
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":3,
		 * 					"start_time":"sfdsfsddsf",
		 * 					"end_time":"dsfsfdsfsf",
		 * 					"status":"1",
		 * 					"count":4,
		 * 					"type":1 //未付款的里面有未支付、已过期、已售完、退票中 使用数据中使用type对应：0、1、2、3、4
		 * 				}
		 * 			]
		 */
		@Path("/unpaid_orders")
		@GET
		public abstract Response unpaid_orders(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
		
		 /**
		 * @api {GET} /users/orders_info/{orders_no} 未支付订单信息
		 * @apiGroup Client_Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 未支付订单信息
		 * @apiSuccessExample {json} 返回数据: 
		 * 				{	"orders_id":12,
		 * 					"ticket_id":12,
		 * 					"ticket_name":"腾格里国际音乐节",
		 * 					"number":3,
		 * 					"start_time":"sfdsfsddsf",
		 * 					"end_time":"dsfsfdsfsf",
		 * 					"status":"1",
		 * 					"count":2,
		 * 					"amount":234.23,
		 * 					"contacters":[
		 * 						{
		 * 							"id":1,
		 * 							"name":"dsfsf",
		 * 							"identity_card":"12345677"
		 * 						},{
		 * 							"id":1,
		 * 							"name":"dsfsf",
		 * 							"identity_card":"12345677"
		 * 						}
		 * 					]
		 * 				}
		 */
		@Path("/orders_info/{orders_no}")
		@GET
		public abstract Response orders_info(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@PathParam("orders_no")String orders_no);
		
		/**
		 * @api {PUT} /users/cancel_orders 取消订单
		 * @apiGroup Client_Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 取消订单
		 * @apiParam {Long} id 订单Id
		 * @apiSuccessExample {json} 返回数据: 
		 * 		{
		 * 			"id":23
		 * 		}
		 */
		@Path("/cancel_orders")
		@PUT
		public abstract Response cancel_orders(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,JSONObject orders);
		
		 /**
		 * @api {GET} /users/content/{content_id} 获得内容详情
		 * @apiGroup Users
		 * @apiVersion 0.0.1
		 * @apiDescription 获得内容详情
		 * @apiSuccessExample {json} 返回数据: 
		 * 				{
		 * 					"id":11,
		 * 					"title":"腾格里国际音乐节",
		 * 					"summary":"腾格里国际音乐节",
		 * 					"author":"郭胜利",
		 * 					"cover_image":{
		 * 						"media":{
		 * 							"original_size":"{600, 399}",
		 * 							"name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"
		 * 						},
		 * 						"type":"image"
		 * 					},
		 * 					"create_time":"123456",
		 * 					"elements":[
		 * 						{
						            "content": {
						                "type": "text", 
						                "media": {
						                    "alignment": "left", 
						                    "style": "body", 
						                    "plain_text": "导语：熊本县政府以虚拟明星带活地方经济的概念，利用当地农业、景点、美食、特产等旅游资源，打造一系列连锁品牌度假目的地及旅游周边产品，成为依靠主题形象打造旅游区域品牌知名度的典范。熊本熊给原本默默无闻的熊本县带来了数以亿计的经济效益。"
						                }
						            }
						        },
						        {
						        	"content": {
							                "type": "image", 
							                "media": {
							                    "name": "29/7D8AC810AC1A99AEF8BE77A2E766A2A0", 
							                    "original_size": "{550,312}"
							                }
							            }
						        }
		 * 					]
		 * 				}
		 */
		@Path("/content/{content_id}")
		@GET
		public abstract Response get_content(@PathParam("content_id")Long content_id,@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
		
		/**
		 * @api {GET} /users/homepage 首页
		 * @apiGroup Users
		 * @apiVersion 0.0.1
		 * @apiDescription 首页
		 * @apiSuccessExample {json} 返回数据: 
		 * 				{
		 * 					"slides":[
		 * 						{
						            "id":12,
						            "title":"轮播图标题",
						            "cover_image":{},
						            "reference_type":"url",
						            "url":"www.baidu.com"
						        },
						        {
						            "id":16,
						            "title":"轮播图标题",
						            "cover_image":{},
						            "reference_type":"content",
						            "reference_id":123
						        },{
						            "id":15,
						            "title":"轮播图标题",
						            "cover_image":{},
						            "reference_type":"poi",
						            "reference_id":14
						        }
		 * 					],
		 * 					"classifies":[
		 * 						{
		 * 							"id":12,
		 * 							"classify_name":"商店"
		 * 						},{
		 * 							"id":12,
		 * 							"classify_name":"餐饮"
		 * 						}
		 * 					]
		 * 				}
		 */
		@Path("/homepage")
		@GET
		public abstract Response homepage();
		
		
		/**
		 * @api {GET} /users/setting/{type} 根据类型获得内容
		 * @apiGroup Users
		 * @apiVersion 0.0.1
		 * @apiDescription 1用户协议，2隐私声明，3入园须知，4救援信息
		 * @apiParam {Integer} type 1用户协议，2隐私声明，3入园须知，4救援信息
		 * @apiSuccessExample {json} 返回数据: 
		 * {
		 * 					"id":11,
		 * 					"title":"腾格里国际音乐节",
		 * 					"summary":"腾格里国际音乐节",
		 * 					"author":"郭胜利",
		 * 					"cover_image":{
		 * 						"media":{
		 * 							"original_size":"{600, 399}",
		 * 							"name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"
		 * 						},
		 * 						"type":"image"
		 * 					},
		 * 					"create_time":"123456",
		 * 					"elements":[
		 * 						{
						            "content": {
						                "type": "text", 
						                "media": {
						                    "alignment": "left", 
						                    "style": "body", 
						                    "plain_text": "导语：熊本县政府以虚拟明星带活地方经济的概念，利用当地农业、景点、美食、特产等旅游资源，打造一系列连锁品牌度假目的地及旅游周边产品，成为依靠主题形象打造旅游区域品牌知名度的典范。熊本熊给原本默默无闻的熊本县带来了数以亿计的经济效益。"
						                }
						            }
						        },
						        {
						        	"content": {
							                "type": "image", 
							                "media": {
							                    "name": "29/7D8AC810AC1A99AEF8BE77A2E766A2A0", 
							                    "original_size": "{550,312}"
							                }
							            }
						        }
		 * 					]
		 * 				}
		 */
		@Path("/setting/{type}")
		@GET
		public abstract Response setting(@PathParam("type") int type);
		
		@Path("/webhooks")
		@POST
		public abstract Response webhooks(JSONObject data)throws Exception;
		
		/**
		 * @api {POST} /users/contacter 添加常用联系人
		 * @apiGroup Contacter
		 * @apiVersion 0.0.1
		 * @apiDescription 添加常用联系人
		 * @apiParam {String} name 姓名
		 * @apiParam {String} phone 手机号(选填)
		 * @apiParam {String} identity_card 身份证号
		 * @apiParam {String} gender 性别
		 * @apiParam {String} birthday 生日
		 * @apiParamExample {json} 范例
	 	 * {
		 *      "name":"郭胜利",
		 *      "phone":"15201132276",
		 *      "identity_card":"身份证号",
		 *      "gender":"性别",
		 *      "birthday":"生日",
		 *      
		 * }
		 * @apiSuccess (200) {Long} id 常用联系人Id
		 * @apiSuccess (200) {String} name 姓名
		 * @apiSuccess (200) {String} phone 手机号
		 * @apiSuccess (200) {String} identity_card 身份证号
		 * @apiParam {String} gender 性别
		 * @apiParam {String} birthday 生日
		 * @apiSuccessExample {json} 返回数据: 
		 *	{
		 *		"id":12,
		 *      "name":"郭胜利",
		 *      "phone":"15201132276",
		 *      "identity_card":"身份证号",
		 *      "gender":"性别",
		 *      "birthday":"生日",
		 * }
		 */
		@Path("/contacter")
		@POST
		public abstract Response add_contacter(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,JSONObject data);
		
		/**
		 * @api {PUT} /users/contacter 修改常用联系人
		 * @apiGroup Contacter
		 * @apiVersion 0.0.1
		 * @apiDescription 修改常用联系人
		 * @apiParam {Long} id 常用联系人Id
		 * @apiParam {String} name 姓名
		 * @apiParam {String} phone 手机号(选填)
		 * @apiParam {String} identity_card 身份证号
		 * @apiParamExample {json} 范例
	 	 * {
	 	 * 		"id":12,
		 *      "name":"郭胜利",
		 *      "phone":"15201132276",
		 *      "identity_card":"身份证号",
		 *      "gender":"性别",
		 *      "birthday":"生日",
		 * }
		 * @apiSuccess (200) {Long} id 常用联系人Id
		 * @apiSuccess (200) {String} name 姓名
		 * @apiSuccess (200) {String} phone 手机号
		 * @apiSuccess (200) {String} identity_card 身份证号
		 * @apiSuccessExample {json} 返回数据: 
		 *	{
		 *		"id":12,
		 *      "name":"郭胜利",
		 *      "phone":"15201132276",
		 *      "identity_card":"身份证号",
		 *      "gender":"性别",
		 *      "birthday":"生日",
		 * }
		 */
		@Path("/contacter")
		@PUT
		public abstract Response update_contacter(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,JSONObject data);
		
		/**
		 * @api {DELETE} /users/contacter/{id} 删除常用联系人
		 * @apiGroup Contacter
		 * @apiVersion 0.0.1
		 * @apiDescription 删除常用联系人
		 * @apiParam {Long} id 常用联系人Id
		 * @apiSuccessExample {json} 返回数据: 
		 *	{
		 *		"status":"success"
		 * }
		 */
		@Path("/contacter/{id}")
		@DELETE
		public abstract Response delete_contacter(@PathParam("id")Long id,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /users/contacter/list 常用联系人列表
		 * @apiGroup Contacter
		 * @apiVersion 0.0.1
		 * @apiDescription	常用联系人列表
		 * @apiSuccessExample {json} 返回数据: 
		 * [
		 * 		{
		 * 			"id":11,
		 * 			"name":"姓名",
		 * 			"identity_code":"身份证号",
		 * 			"phone":"手机号",
		 * 		},{
		 * 			"id":11,
		 * 			"name":"姓名",
		 * 			"identity_code":"身份证号",
		 * 			"phone":"手机号",
		 * 		}
		 * ]
		 */
		@Path("/contacter/list")
		@GET
		public abstract Response contacter_list(@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /users/collect/list 收藏列表
		 * @apiGroup Users
		 * @apiVersion 0.0.1
		 * @apiDescription	收藏列表
		 * @apiParam {int} count 每页显示的数目
		 * @apiParam {Long} max_id  收藏Id
		 * @apiSuccessExample {json} 返回数据: 
		 * [
		 * 		{
		 * 			"id":11,
		 * 			"reference_type":"poi",
		 * 			"create_time":1234567,
		 * 			"content":{
		 * 				"id":12,
		 * 				"title":"title",
		 * 				"cover_image":{},
		 * 				"is_collect":true
		 * 			},
		 * 		},{
		 * 			"id":11,
		 * 			"reference_type":"poi",
		 * 			"create_time":1234567,
		 * 			"content":{
		 * 				"id":12,
		 * 				"title":"title",
		 * 				"cover_image":{},
		 * 				"is_collect":true
		 * 			},
		 * 		}
		 * ]
		 */
		@Path("/collect/list")
		@GET
		public abstract Response collect_list(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
		
		@Path("/vip")
		@GET
		public abstract Response vip_info(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,@Context HttpServletRequest request);
		
		/**
		 * @api {POST} /users/record_vip POS机购买VIP用户
		 * @apiGroup POS
		 * @apiVersion 0.0.1
		 * @apiDescription POS机购买VIP用户
		 * @apiParam {String} identity_card 身份证号
		 * @apiParam {String} username 姓名
		 * @apiParam {String} password 密码
		 * @apiParamExample {json} 范例
	 	 * {
		 *      "username":"郭胜利",
		 *      "identity_card":"身份证号",
		 *      "password":"密码"
		 * }
		 * @apiSuccessExample {json} 返回数据: 
		 *	{
		 *		"id":12,
		 *      "name":"郭胜利",
		 *      "phone":"15201132276",
		 *      "identity_card":"身份证号",
		 *      "gender":"性别",
		 *      "birthday":"生日",
		 * }
		 */
		
		@Path("/record_vip")
		@POST
		public abstract JSONObject record_vip(JSONObject user);
		
		/**
		 * @api {POST} /users/material POS机刷身份证获取物料列表
		 * @apiGroup POS
		 * @apiVersion 0.0.1
		 * @apiDescription POS机刷身份证获取物料列表
		 * @apiParam {String} identity_card 身份证号
		 * @apiParam {String} username 姓名
		 * @apiParamExample {json} 范例
	 	 * {
		 *      "username":"郭胜利",
		 *      "identity_card":"身份证号",
		 * }
		 */
		
		@Path("/material")
		@POST
		public abstract JSONObject material(JSONObject user);
		
		/**
		 * @api {POST} /users/barcode 根据身份证号得到用户的二维码
		 * @apiGroup Users
		 * @apiVersion 0.0.1
		 * @apiDescription 根据身份证号得到用户的二维码
		 * @apiParamExample {json} 范例
	 	 * {
		 *      "identity_card":"身份证号",
		 * }
		 * @apiSuccessExample {json} 返回数据: 
		 *	{
		 *		"barcode":12,
		 * }
		 */
		@Path("/barcode")
		@POST
		public abstract Response barcode(JSONObject param);
		
		/**
		 * @api {GET} /users/verification_user POS机扫描用户二维码
		 * @apiGroup POS
		 * @apiVersion 0.0.1
		 * @apiDescription POS机扫描用户二维码
		 * @apiParamExample {json} 范例
		 * url?barcode=..
		 */
		@Path("/verification_user")
		@GET
		public abstract JSONObject verification_user(@Context HttpServletRequest request)throws Exception;
		
		/**
		 * @api {POST} /users/verification_material POS机核销物料
		 * @apiGroup POS
		 * @apiVersion 0.0.1
		 * @apiDescription POS机核销物料
		 * @apiParam {String} order_ticket_no 订单号
		 * @apiParamExample {json} 范例
	 	 * {
		 *      "order_ticket_no":"订单号",
		 * }
		 */
		
		@Path("/verification_material")
		@POST
		public abstract JSONObject verification_material(JSONObject orders_ticket);
		
		/**
		 * @api {POST} /users/bind_wechat 微信公众号绑定用户
		 * @apiGroup Users
		 * @apiVersion 0.0.1
		 * @apiDescription 微信公众号绑定用户
		 * @apiParam {String} order_ticket_no 订单号
		 * @apiParamExample {json} 范例
	 	*  {
	 	*      	"union_id":"sdfsfdsdfdsdfse",
	 	*      	"auth_token":"sdfsfsdfsdf"	
	 	*  }
		 */
		
		@Path("/bind_wechat")
		@POST
		public abstract Response bind_wechat(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,JSONObject link_account);
		
		@Path("/synchronousUser")
		@GET
		public abstract String synchronousUser(@Context HttpServletRequest request);
		
		@Path("/synchronousOrder")
		@GET
		public abstract String synchronousOrder(@Context HttpServletRequest request);
}
