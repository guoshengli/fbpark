package com.fbpark.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("/admin")
@Produces({"application/json"})
public interface AdminService {
	 /**
	 * @api {POST} /admin/signin 管理员登录
	 * @apiGroup Admin
	 * @apiVersion 0.0.1
	 * @apiDescription 管理员登录
	 * @apiParam {String} username 用户名
	 * @apiParam {String} password 密码
	 * @apiParamExample {json} 范例
	 *                {
	 *                	username:admin,
	 *                	password:123456
	 *                }
	 * @apiSuccess (200) {long} userid 123
  	 * @apiSuccess (200) {String} token_timestamp 236756123
  	 * @apiSuccess (200) {String} access_token asert123reew
  	 * @apiSuccessExample {json} 返回数据:
  	 * 				{
  	 * 					"userid":1234,
  	 * 					"token_timestamp":236756123,
  	 * 					"access_token":"asert123reew",
  	 * 				}
	 */
	@Path("/signin")
	@POST
	@Consumes({ "application/json" })
	public abstract Response signin(JSONObject admin);
	
	/**
	 * @api {GET} /admin/{admin_id} 得到管理员信息
	 * @apiGroup Admin
	 * @apiVersion 0.0.1
	 * @apiDescription 得到管理员信息
	 * @apiParam {int} admin_id 管理员Id
	 * @apiSuccess (200) {long} userid 123
  	 * @apiSuccess (200) {String} token_timestamp 236756123
  	 * @apiSuccess (200) {String} access_token asert123reew
  	 * @apiSuccessExample {json} 返回数据:
  	 * 				{
  	 * 					"userid":1234,
  	 * 					"token_timestamp":236756123,
  	 * 					"access_token":"asert123reew",
  	 * 				}
	 */
	@Path("/{admin_id}")
	@GET
	public abstract Response getAdmin(@PathParam("admin_id") Long admin_id);
	
	/**
	 * @api {POST} /admin/add_group 添加角色组
	 * @apiGroup Group
	 * @apiVersion 0.0.1
	 * @apiDescription 超级管理员添加角色组
	 * @apiParam {String} group_name 组名
	 * @apiParamExample {json} 范例
	 *                {
	 *                	"group_name":"内容组",
	 *                }
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回范例:
	 *                {
	 *                	"id":12,
	 *                	"group_name":"管理员组",
	 *                	"menu_list":[
	 *                		{
	 *                			"id":1,
	 *                			"menu_name":"管理员管理",
	 *                			"is_exist":0
	 *                		},{
	 *                			"id":1,
	 *                			"menu_name":"管理员管理",
	 *                			"is_exist":0
	 *                		},{
	 *                			"id":1,
	 *                			"menu_name":"管理员管理",
	 *                			"is_exist":0
	 *                		}
	 *                		
	 *                	]
	 *                }
	 */
	@Path("/add_group")
	@POST
	public Response add_group(JSONObject group);
	
	/**
	 * @api {DELETE} /admin/group/{group_id} 删除角色组
	 * @apiGroup Group
	 * @apiVersion 0.0.1
	 * @apiDescription 删除角色组
	 * @apiParam {String} group_id 角色组Id
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回范例:
	 *                {
	 *                	"status":"success",
	 *                }
	 */
	@Path("/group/{group_id}")
	@DELETE
	public Response del_group(@PathParam("group_id") Long group_id);
	
	/**
	 * @api {GET} /admin/group/list 角色组列表
	 * @apiGroup Group
	 * @apiVersion 0.0.1
	 * @apiDescription 角色组列表
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回范例:
	 *               [ {
	 *                	"id":12,
	 *                	"group_name":"组名"
	 *                },{
	 *                	"id":12,
	 *                	"group_name":"组名"
	 *                }]
	 */	
	@Path("/group/list")
	@GET
	public Response groupList();
	
	/**
	 * @api {POST} /admin/group_menu 角色组管理权限
	 * @apiGroup Group
	 * @apiVersion 0.0.1
	 * @apiDescription 角色组管理权限
	 * @apiParam {Long} group_id 角色名
	 * @apiParam {Array} menu_ids 权限组Id
	 * @apiParamExample {json} 范例
	 *                {
	 *                	"group_id":13,
	 *                	"menu_ids":[1,2,3,4]
	 *                }
	 * @apiSuccess (200) {String} status success
	 * @apiSuccessExample {json} 返回范例:
	 *                {
	 *                	"status":"success",
	 *                }
	 */
	
	@Path("/group_menu")
	@POST
	public Response group_menu(JSONObject group_menu);

	 /**
		 * @api {POST} /admin/add_admin 添加管理员
		 * @apiGroup Admin
		 * @apiVersion 0.0.1
		 * @apiDescription 超级管理员分配用户
		 * @apiParam {String} username 用户名
		 * @apiParam {String} password 密码
		 * @apiParam {Long} group_id 密码
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"username":"admin",
		 *                	"password":"123456",
		 *                  "group_id":13
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/add_admin")
		@POST
		public Response add_admin(JSONObject user);
		
		 /**
		 * @api {PUT} /admin/update_admin 更新管理员信息
		 * @apiGroup Admin
		 * @apiVersion 0.0.1
		 * @apiDescription 超级管理员分配用户
		 * @apiParam {Long} id 用户Id
		 * @apiParam {String} username 用户名
		 * @apiParam {String} password 密码
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":123,
		 *                	"username":"admin",
		 *                	"password":"123456",
		 *                  "group_id":13
		 *                }
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":123,
		 *                	"status":"disable",
		 *                	
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/update_admin")
		@PUT
		public Response update_admin(JSONObject user);
		
		/**
	 	 * @api {GET} /admin/admin_list 所有管理员
	 	 * @apiGroup Admin
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 所有管理员
	 	 * @apiSuccessExample {json} 返回数据:
	 	 *                [
	 	 *                	{
	 	 *                		"id":1,
	 	 *                		"username":"阿拉善景区",
	 	 *                		"mobile":"15201137678",
	 	 *                		"email":"23456@qq.com",
	 	 *                		"user_type":"用户类型",
	 	 *                		"role_name":"角色名"
	 	 *                	},
	 	 *                
	 	 *                	{
	 	 *                		"id":1,
	 	 *                		"username":"阿拉善景区",
	 	 *                		"mobile":"15201137678",
	 	 *                		"email":"23456@qq.com",
	 	 *                		"user_type":"用户类型"，
	 	 *                		"role_name":"角色名"
	 	 *                	}
	 	 *                ]
	 	 */
		@Path("/admin_list")
		@GET
		public Response adminList(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
	 	 * @api {DELETE} /admin/{admin_id} 删除管理员
	 	 * @apiGroup Admin
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 删除管理员
	 	 * @apiSuccessExample {json} 返回数据:
	 	 *                	{
	 	 *                		"status":"success",
	 	 *                	},
	 	 *                
	 	 */
		@Path("/{admin_id}")
		@DELETE
		public Response deleteAdmin(@PathParam("admin_id") Long admin_id,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		
		/**
		 * @api {GET} /admin/user_list 会员列表
		 * @apiGroup Member
		 * @apiVersion 0.0.1
		 * @apiDescription 会员列表
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例 :
		 * 					?count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   [
		 *                   	{
		 *                   		"id":123,
		 *                   		"username":"郭胜利",
		 *                   		"phone":"1520112345",
		 *                   		"identity_card":"140826199401232341",
		 *                   		"status":"enable",
		 *                   		"level":"会员等级",
		 *                   		"club":"俱乐部",
		 *                   		"birthday":"出生年月",
		 *                   		"create_time":"",
		 *                   	},{
		 *                   		"id":123,
		 *                   		"username":"郭胜利",
		 *                   		"phone":"1520112345",
		 *                   		"identity_card":"140826199401232341",
		 *                   		"status":"enable",
		 *                   		"level":"会员等级",
		 *                   		"club":"俱乐部",
		 *                   		"birthday":"出生年月",
		 *                   		"create_time":"",
		 *                   	}
		 *                   ]
		 */
		@Path("/user_list")
		@GET
		public Response user_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/search_user 搜索用户
		 * @apiGroup Member
		 * @apiVersion 0.0.1
		 * @apiDescription 通过用户名或手机号搜索普通用户信息
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例 :
		 * 					?username=guoshengli&count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   [
		 *                   	{
		 *                   		"id":123,
		 *                   		"username":"20170323123456123456",
		 *                   		"phone":"1520112345",
		 *                   		"wechat":true,
		 *                   		"weibo":true
		 *                   	},{
		 *                   		"id":123,
		 *                   		"username":"20170323123456123456",
		 *                   		"phone":"1520112345",
		 *                   		"wechat":true,
		 *                   	}
		 *                   ]
		 */
		@Path("/search_user")
		@GET
		public Response search_user(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/export_user 导出会员信息
		 * @apiGroup Member
		 * @apiVersion 0.0.1
		 * @apiDescription 导出会员信息
		 */
		@Path("/export_user")
		@GET
		public Response export_user(@Context HttpServletRequest request,@Context HttpServletResponse response,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/export_order 导出订单信息
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 导出订单信息
		 */
		@Path("/export_order")
		@GET
		public Response export_order(@Context HttpServletRequest request,@Context HttpServletResponse response,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/export_insure 导出保单信息
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 导出会员信息
		 */
		@Path("/export_insure")
		@GET
		public Response export_insure(@Context HttpServletRequest request,@Context HttpServletResponse response,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		
		/**
		 * @api {POST} /admin/poi/add 添加poi
		 * @apiGroup POI
		 * @apiVersion 0.0.1
		 * @apiDescription 添加poi
		 * @apiParam {String} title 景点标题
		 * @apiParam {String} subtitle 景点副标题
		 * @apiParam {String} place 场馆
		 * @apiParam {String} attention 注意事项
		 * @apiParam {String} location 地图位置
		 * @apiParam {Long} price 最低参考价
		 * @apiParam {String} status 状态
		 * @apiParam {String} time_info 活动时间
		 * @apiParam {Long} content_id 关联内容
		 * @apiParam {JSONArray} elements 景点图片元素
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"title":"主题公园",
		 *                	"subtitle":"主题公园",
		 *                	"cover_image":{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},,
		 *                	"status":"enable",
		 *                	"address":"english name",
		 *                  "location":"[234,567]"，
		 *                  "attention":"注意事项",
		 *                  "price":1000,
		 *                  "content_id":12,
		 *                  "time_info":"",
		 *                  "elements":[
		 *                  	{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},
		 *                  	{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},
		 *                  ],
		 *                  "recommandations":[1,2,3]
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/poi/add")
		@POST
		public Response add_poi(JSONObject poi,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/poi/update 更新POI
		 * @apiGroup POI
		 * @apiVersion 0.0.1
		 * @apiDescription 更新POI
		 * @apiParam {Long} id poiId
		 * @apiParam {int} is_online 是否上线
		 * @apiParam {String} title 景点标题
		 * @apiParam {String} subtitle 景点副标题
		 * @apiParam {String} place 场馆
		 * @apiParam {String} attention 注意事项
		 * @apiParam {String} location 地图位置
		 * @apiParam {Long} price 最低参考价
		 * @apiParam {String} status 状态
		 * @apiParam {String} time_info 活动时间
		 * @apiParam {String} cover_image 题图
		 * @apiParam {Long} content_id 关联内容
		 * @apiParam {JSONArray} elements 景点图片元素
		 * @apiParam {JSONArray} recommandations 景点图片元素
		 * @apiParamExample {json} 更新POI内容
		 *              {
		 *              	"id":12,
		 *                	"title":"主题公园",
		 *                	"subtitle":"主题公园",
		 *                	"cover_image":{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},,
		 *                	"status":"enable",
		 *                	"address":"english name",
		 *                  "location":{"lat":259.5,"lng":306}，
		 *                  "attention":"注意事项",
		 *                  "price":1000,
		 *                  "content_id":12,
		 *                  "time_info":"",
		 *                  "elements":[
		 *                  	{"original_size":"{600, 399}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},
		 *                  	{"original_size":"{600, 399}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},
		 *                  ],
		 *                  "recommandations":[1,2,3,4]
		 *                }
		 *                
		 * @apiParamExample {json} 上线/下线
		 *              {
		 *              	"id":12,
		 *                	"is_online":1,
		 *                	
		 *                }
		 * @apiParamExample {json} 修改位置信息
		 *              {
		 *              	"id":12,
		 *                	"location":{"lat":259.5,"lng":306},
		 *                	
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *              {
		 *              	"id":12,
		 *                	"title":"主题公园",
		 *                	"subtitle":"主题公园",
		 *                	"cover_image":{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},,
		 *                	"status":"enable",
		 *                	"address":"english name",
		 *                  "location":"[234,567]"，
		 *                  "attention":"注意事项",
		 *                  "price":1000,
		 *                  "content_id":12,
		 *                  "time_info":"",
		 *                  "elements":[
		 *                  	{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},
		 *                  	{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},
		 *                  ],
		 *                  
		 *                }
		 */
		@Path("/poi/update")
		@PUT
		public Response update_poi(JSONObject content);
		
		/**
		 * @api {PUT} /admin/poi/map/update 更新地图下POI
		 * @apiGroup POI
		 * @apiVersion 0.0.1
		 * @apiDescription 更新POI
		 * @apiParam {Long} id poiId
		 * @apiParam {String} location 地图位置
		 * @apiParamExample {json} 修改位置信息
		 *              {
		 *              	"id":12,
		 *                	"location":{"lat":259.5,"lng":306},
		 *                	
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *              {
		 *              	"id":12,
		 *                  "location":{"lat":259.5,"lng":306}，
		 *                  
		 *                }
		 */
		@Path("/poi/map/update")
		@PUT
		public Response update_map_poi(JSONObject content);
		
		/**
		 * @api {GET} /admin/poi/map 地图下POI
		 * @apiGroup POI
		 * @apiVersion 0.0.1
		 * @apiDescription 地图下POI
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *           {
		 *              "pois":[
		 *              	{
		 *              		"id":12,
		 *                		"title":"主题公园",
		 *                		"subtitle":"主题公园",
		 *                		"cover_image":{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},,
		 *                		"status":"enable",
		 *                		"address":"english name",
		 *                  	"location":"[234,567]"，
		 *                  	"attention":"注意事项",
		 *                  	"price":1000,
		 *                  	"content_id":12,
		 *                  	"time_info":"",
		 *                  	"elements":[
		 *                  		{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},
		 *                  		{"media":{"zoom":0,"original_size":"{600, 399}","focalpoint":"{0, 0}","name":"11/60F755DF7DAB9CE7B90A6AB24E52E9FC"},"type":"image"},
		 *                  	],
		 *                	}
		 *                ],
		 *                "classifies":[
		 *                	{
		 *                		"id":1,
		 *                		"classify_name":"classify_name"
		 *                	},
		 *                	{
		 *                		"id":1,
		 *                		"classify_name":"classify_name"
		 *                	}
		 *                ]
		 *             }
		 */
		@Path("/poi/map")
		@GET
		public Response map_poi_list(@Context HttpServletRequest request);
		
		/**
	 	 * @api {GET} /admin/poi/classify 不同分类下POI列表
	 	 * @apiGroup POI
	 	 * @apiVersion 0.0.1
	 	 * @apiParam {int} count 每页的条数
		 * @apiParam {int} page 页数
		 * @apiParam {Long} classify_id 分类Id
		 * @apiParam {is_online} is_online 是否上线
		 *  @apiParam {String} poi_name 搜索框的值
		 *  @apiParamExample {json} 范例
		 *                count=20&page=1&classify_id=2&is_online=0&poi_name=qqq
	 	 * @apiDescription POI列表
	 	 * @apiSuccessExample {json} 返回数据:
	 	 * 					{
	 	 * 						"total":234,
		 * 						"pois":[{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"classify_name":"参观游览",
	 	 *                			"author":"dddd",
	 	 *                			"is_online":0,
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                		},{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"classify_name":"参观游览",
	 	 *                			"author":"dddd",
	 	 *                			"is_online":0,
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                			}]
	 	 *                }
	 	 *  @apiError 10002 POI不存在
	     *  @apiErrorExample {json} Error-Response: 
	     *  HTTP/1.1 400 Not Found 
	     *  { 
	     *   status:'POI不存在'
	     *   code:10002, 
	     *   error_message:'POI不存在', 
	     *   }
	 	 */
		@Path("/poi/classify")
		@GET
		public Response classify_poi(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
	 	 * @api {GET} /admin/poi/list POI列表
	 	 * @apiGroup POI
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription POI列表
	 	 * @apiSuccessExample {json} 返回数据:
	 	 * {
	 	 * 						"total":234,
		 * 						"pois":[{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"classify_name":"参观游览",
	 	 *                			"author":"dddd",
	 	 *                			"is_online":0,
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                		},{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"classify_name":"参观游览",
	 	 *                			"author":"dddd",
	 	 *                			"is_online":0,
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                			}]
	 	 *                }
	 	 *  @apiError 10002 POI不存在
	     *  @apiErrorExample {json} Error-Response: 
	     *  HTTP/1.1 400 Not Found 
	     *  { 
	     *   status:'POI不存在'
	     *   code:10002, 
	     *   error_message:'POI不存在', 
	     *   }
	 	 */
		@Path("/poi/list")
		@GET
		public Response poi_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/poi/hot 热门推荐POI列表
		 * @apiGroup POI
		 * @apiVersion 0.0.1
		 * @apiDescription 热门推荐POI列表
		 * @apiSuccessExample {json} 返回范例:
		 * 				[
		 * 					{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"classify_name":"参观游览",
	 	 *                			"author":"dddd",
	 	 *                			"is_online":0,
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                	},
	 	 *                {
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"classify_name":"参观游览",
	 	 *                			"author":"dddd",
	 	 *                			"is_online":0,
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                			
	 	 *                }
	 	 *                
	 	 *             ]
		 */
		
		@Path("/poi/hot")
		@GET
		public Response poi_hot(@Context HttpServletRequest request);
		/**
		 * @api {PUT} /admin/poi/is_hot 推荐/取消推荐POI
		 * @apiGroup POI
		 * @apiVersion 0.0.1
		 * @apiDescription 推荐/取消推荐POI
		 * @apiParam {Long} poi_id poiId
		 * @apiParam {int} hot 0 取消推荐 1推荐
		 * @apiParamExample {json} 范例
		 *              {
		 *                	"poi_id":13,
		 *                	"hot":0/1,
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/poi/is_hot")
		@PUT
		public Response update_poi_hot(JSONObject hot_poi);
		/**
		 * @api {GET} /admin/order/list 不同状态的订单
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 不同状态的订单
		 * @apiParam {String} pay 订单状态（unpaid:未付款 paid:已付款 ）
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例:
		 * 					?status=unpaid&start_date=2017-06-02&end_date=2017-06-04&count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   [
		 *                   	{
		 *                   		"id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"create_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":"paid",
		 *                   		"amount":278.2
		 *                   	},{
		 *                   		"id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"create_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":"paid",
		 *                   		"amount":278.2
		 *                   	},{
		 *                   		"id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"create_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":"paid",
		 *                   		"amount":278.2
		 *                   	}
		 *                   ]
		 */
		
		@Path("/order/list")
		@GET
		public Response order_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/orders_ticket/list 票的状态列表
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 票的状态列表
		 * @apiParam {String} username 根据用户名搜索
		 * @apiParam {int} status 票的状态
		 * @apiParam {String} start_date 开始日期
		 * @apiParam {String} end_date 结束日期
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例:
		 * 					?username=胜利&status=1&start_date=2017-06-02&end_date=2017-06-04&count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   [
		 *                   	{
		 *                   		"id":12,
		 *                   		"ticket_id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"update_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":3,
		 *                   		"ticket_name":"停车券",
		 *                   		"total":2345.3
		 *                   	},{
		 *                   		"id":12,
		 *                   		"ticket_id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"update_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":3,
		 *                   		"ticket_name":"停车券",
		 *                   		"total":2345.3
		 *                   	}
		 *                   ]
		 */
		
		@Path("/order_ticket/list")
		@GET
		public Response orders_ticket_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/orders_ticket/statistics 票务统计
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 票务统计
		 * @apiParam {String} search 搜索票券名称
		 * @apiParam {String} ticket_name 票券名称
		 * @apiParam {int} status 票的状态
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例:
		 * 					?search=大脚车表演&ticket_name=大脚车表演&status=1&count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   [
		 *                   	{
		 *                   		"id":12,
		 *                   		"ticket_id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"update_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":3,
		 *                   		"ticket_name":"停车券",
		 *                   		"total":2345.3
		 *                   	},{
		 *                   		"id":12,
		 *                   		"ticket_id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"update_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":3,
		 *                   		"ticket_name":"停车券",
		 *                   		"total":2345.3
		 *                   	}
		 *                   ]
		 */
		@Path("/orders_ticket/statistics")
		@GET
		public Response orders_ticket_statistics(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/orders_ticket/export_statistics 票务统计导出
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 票务统计导出
		 * @apiParam {String} search 搜索票券名称
		 * @apiParam {String} ticket_name 票券名称
		 * @apiParam {int} status 票的状态
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例:
		 * 					?search=大脚车表演&ticket_name=大脚车表演&status=1&count=20&page=1
		 */
		@Path("/orders_ticket/export_statistics")
		@GET
		public Response export_orders_ticket_statistics(@Context HttpServletRequest request,@Context HttpServletResponse response,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/insurant/statistics 保险统计
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 保险统计
		 * @apiParam {String} name 被保人名字
		 * @apiParam {int} status 票的状态
		 * @apiParam {String} count 每页显示条数
		 * @apiParam {String} page 页码
		 * @apiParamExample {json} 范例:
		 * 					?search=大脚车表演&ticket_name=大脚车表演&status=1&count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   [
		 *                   	{
		 *                   		"orders_ticket_no":"20170323123456123456",
		 *                   		"transNo":"1234567",
		 *                   		"create_time":1234567,
		 *                   		"insurant":{
		 *                   			"name":"cuixiaochuan",
		 *                   			"gender":"男",
		 *                   			"identity_card":"dfsdsfd"
		 *                   		},
		 *                   		"insureNum":2017293434,
		 *                   		"startDate":"2017-10-01",
		 *                   		"endDate":"2017-10-08"
		 *                   	},{
		 *                   		"orders_ticket_no":"20170323123456123456",
		 *                   		"transNo":"1234567",
		 *                   		"create_time":1234567,
		 *                   		"insurant":{
		 *                   			"name":"cuixiaochuan",
		 *                   			"gender":"男",
		 *                   			"identity_card":"dfsdsfd"
		 *                   		},
		 *                   		"insureNum":2017293434,
		 *                   		"startDate":"2017-10-01",
		 *                   		"endDate":"2017-10-08"
		 *                   	}
		 *                   ]
		 */
		@Path("/insurant/statistics")
		@GET
		public Response insurant_statistics(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/order/search 根据订单号查询订单
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 根据订单号查询订单
		 * @apiParamExample {json} 范例:
		 * 					?order_no=2017020340
		 * @apiSuccessExample {json} 返回数据:
		 *                   {
		 *                   		"id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"create_time":1234567,
		 *                   		"username":"郭胜利",
		 *                   		"userid":123,
		 *                   		"status":"paid",
		 *                   		"amount":278.2
		 *                   	}
		 */
		@Path("/order/search")
		@GET
		public Response order_search(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		/**
		 * @api {GET} /admin/order/{order_id} 订单详情
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 订单详情
		 * @apiParam {Long} order_id 订单Id
		 * @apiParamExample {json} 范例:
		 * 					?type=express&count=20&page=1
		 * @apiSuccessExample {json} 返回数据:
		 *                   
		 *                   	{
		 *                   		"id":123,
		 *                   		"order_no":"20170323123456123456",
		 *                   		"amount":3456,
		 *                   		"username":"郭胜利",
		 *                   		"tickets":[
		 *                   			{
		 *                   				"ticket_id":12,
		 *                   				"ticket_name":"券名称",
		 *                   				"status":"used/unuse/",
		 *                   				"num":2,
		 *                   				"total":321
		 *                   			},{
		 *                   				"ticket_id":12,
		 *                   				"ticket_name":"券名称",
		 *                   				"status":"used/unuse/",
		 *                   				"num":2,
		 *                   				"total":321
		 *                   			}
		 *                   		]
		 *                   
		 *                   	}
		 *                   
		 */
		
		@Path("/order/{order_id}")
		@GET
		public Response order_detail(@PathParam("order_id")Long order_id);
		
		
		/**
		 * @api {POST} /admin/add_member 添加会员
		 * @apiGroup Member
		 * @apiVersion 0.0.1
		 * @apiDescription 超级管理员添加会员
		 * @apiParam {String} username 用户名
		 * @apiParam {String} password 密码
		 * @apiParam {String} phone 手机号
		 * @apiParam {String} identity_card 身份证号
		 * @apiParam {String} level 用户类型
		 * @apiParam {String} club 所属俱乐部
		 * @apiParam {String} birthday 出生日期
		 * @apiParam {String} car_number 车牌号
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"username":"郭胜利",
		 *                	"password":"123456",
		 *                	"phone":"15201132276",
		 *                	"identity_card":"身份证号",
		 *                  "level":"会员等级",
		 *                  "club":"所属俱乐部",
		 *                  "birthday":"1990-01-12"，
		 *                  "car_number":"车牌号"
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/add_member")
		@POST
		@Consumes({"application/json"})
		public Response add_member(JSONObject member);
		
		@Path("/add_member")
		@POST
		public Response import_member(JSONObject member);
		
		/**
		 * @api {PUT} /admin/setting_member_level 更新用户信息
		 * @apiGroup Member
		 * @apiVersion 0.0.1
		 * @apiDescription 超级管理员分配用户
		 * @apiParam {Long} id 用户Id
		 * @apiParam {String} level 会员等级
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":123,
		 *                	"level":"vip",
		 *                	
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/setting_member_level")
		@PUT
		public Response setting_member_level(JSONObject member);
		
		/**
		 * @api {POST} /admin/add_message_template 添加消息模板
		 * @apiGroup Template
		 * @apiVersion 0.0.1
		 * @apiDescription 超级管理员添加消息模板
		 * @apiParam {String} content 消息模板内容
		 * @apiParam {String} type 消息模板类型
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"content":"消息模板",
		 *                	"type":"模板类型",
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/add_message_template")
		@POST
		@Consumes({"application/json"})
		public Response add_message_template(JSONObject message_template);
		
		/**
		 * @api {PUT} /admin/update_message_template 修改消息模板
		 * @apiGroup Template
		 * @apiVersion 0.0.1
		 * @apiDescription 修改消息模板
		 * @apiParam {Long} id 消息模板Id
		 * @apiParam {String} content 消息模板内容
		 * @apiParam {String} type 消息模板类型
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":12,
		 *                	"content":"消息模板",
		 *                	"type":"模板类型",
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/update_message_template")
		@PUT
		public Response update_message_template(JSONObject message_template);
		
		/**
		 * @api {POST} /admin/add_content_help 添加内容到帮助中心
		 * @apiGroup Help
		 * @apiVersion 0.0.1
		 * @apiDescription 添加内容到帮助中心
		 * @apiParam {Long} content_id 内容Id
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"content_id":23
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/add_content_help")
		@POST
		@Consumes({"application/json"})
		public Response add_content_help(JSONObject help);
		
		/**
		 * @api {PUT} /admin/content_help_sequence 帮助中心排序
		 * @apiGroup Help
		 * @apiVersion 0.0.1
		 * @apiDescription 帮助中心置顶
		 * @apiParam {Long} id 帮助Id
		 * @apiParam {int} top 是否置顶 0 否 1 是
		 * @apiParamExample {json} 范例
		 *              [
		 *                {
		 *                	"id":23,
		 *                	"sequence":1
		 *                },{
		 *                	"id":24,
		 *                	"sequence":4
		 *                }
		 *               ]
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/content_help_sequence")
		@PUT
		public Response content_help_sequence(JSONArray help);
		
		/**
		 * @api {GET} /admin/helps/list 帮助列表
		 * @apiGroup Help
		 * @apiVersion 0.0.1
		 * @apiDescription 帮助列表
		 * @apiSuccessExample {json} 返回范例:
		 *               [ {
		 *                	"id":12,
		 *                	"content_id":13,
		 *                	"title":"",
		 *                	"type":1
		 *                },{
		 *                	"id":12,
		 *                	"content_id":13,
		 *                	"title":"",
		 *                  "type":2
		 *                }]
		 */	
		@Path("/helps/list")
		@GET
		public Response helps_list(@Context HttpServletRequest request);
		
		/**
		 * @api {POST} /admin/add_content_news 添加内容到新闻中心
		 * @apiGroup News
		 * @apiVersion 0.0.1
		 * @apiDescription 添加内容到新闻中心
		 * @apiParam {Long} content_id 内容Id
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"content_id":23
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/add_content_news")
		@POST
		@Consumes({"application/json"})
		public Response add_content_news(JSONObject news);
		
		/**
		 * @api {PUT} /admin/content_news_sequence 新闻中心内容排序
		 * @apiGroup News
		 * @apiVersion 0.0.1
		 * @apiDescription 新闻中心内容置顶
		 * @apiParam {Long} id 新闻Id
		 * @apiParam {int} top 是否置顶 0 否 1 是
		 * @apiParamExample {json} 范例
		 *               [ 
		 *               {
		 *                	"id":23,
		 *                	"sequence":1
		 *                },{
		 *                	"id":23,
		 *                	"sequence":2
		 *                }
		 *                ]
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/content_news_sequence")
		@PUT
		public Response content_news_sequence(JSONArray news);
		
		/**
		 * @api {GET} /admin/news/list 新闻列表
		 * @apiGroup News
		 * @apiVersion 0.0.1
		 * @apiDescription 新闻列表
		 * @apiSuccessExample {json} 返回范例:
		 *               [ {
		 *                	"id":12,
		 *                	"content_id":13,
		 *                	"title":"",
		 *                	"type":1
		 *                },{
		 *                	"id":12,
		 *                	"content_id":13,
		 *                	"title":"",
		 *                  "type":2
		 *                }]
		 */	
		@Path("/news/list")
		@GET
		public Response news_list(@Context HttpServletRequest request);
		
		/**
		 * @api {POST} /admin/add_content_setting 添加内容到设置
		 * @apiGroup Setting
		 * @apiVersion 0.0.1
		 * @apiDescription 添加内容到设置
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} cover_image 题图
		 * @apiParam {String} author 作者名字
		 * @apiParam {int} type 协议类型 0:用户协议 1:隐私声明 2:入园须知
		 * @apiParam {array} elements 内容元素
		 * @apiParamExample {json} 范例
		 *	   {
		 *                	"title":"标题",
		 *                	"summary":"简介",
		 *                  "cover_image":{},
		 *                  "author":"作者名字",
		 *                  "elements":[
		 *                  	{
		 *                  		"content":{}
		 *                  	},{
		 *                  		"content":{}
		 *                  	}
		 *                  ],
		 *                  "type":1
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/add_content_setting")
		@POST
		public Response add_content_setting(JSONObject settting);
		
		/**
		 * @api {GET} /admin/setting/list 设置列表
		 * @apiGroup Setting
		 * @apiVersion 0.0.1
		 * @apiDescription 设置列表
		 * @apiSuccessExample {json} 返回范例:
		 *               [ {
		 *                	"id":12,
		 *                	"content_id":13,
		 *                	"title":"",
		 *                	"type":1
		 *                },{
		 *                	"id":12,
		 *                	"content_id":13,
		 *                	"title":"",
		 *                  "type":2
		 *                }]
		 */	
		@Path("/setting/list")
		@GET
		public Response setting_list(@Context HttpServletRequest request);
		
		/**
		 * @api {POST} /admin/content/add 添加内容
		 * @apiGroup Content
		 * @apiVersion 0.0.1
		 * @apiDescription 添加内容
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} cover_image 题图
		 * @apiParam {String} author 作者名字
		 * @apiParam {array} elements 内容元素
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"title":"标题",
		 *                	"summary":"简介",
		 *                  "cover_image":{},
		 *                  "author":"作者名字",
		 *                  "elements":[
		 *                  	{
		 *                  		"content":{}
		 *                  	},{
		 *                  		"content":{}
		 *                  	}
		 *                  ]
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/content/add")
		@POST
		@Consumes({"application/json"})
		public Response add_content(JSONObject content);
		
		
		/**
		 * @api {DELETE} /admin/content/{content_id} 删除内容
		 * @apiGroup Content
		 * @apiVersion 0.0.1
		 * @apiDescription 删除内容
		 *                
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 * {
         *    "status":"success",
         * }
		 */
		@Path("/content/{content_id}")
		@DELETE
		public Response del_content(@PathParam("content_id")Long content_id);
		
		/**
		 * @api {PUT} /admin/content/update 更新内容
		 * @apiGroup Content
		 * @apiVersion 0.0.1
		 * @apiDescription 更新内容
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} cover_image 题图
		 * @apiParam {String} author 作者名字
		 * @apiParam {array} elements 内容元素
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":12,
		 *                	"title":"标题",
		 *                	"summary":"简介",
		 *                  "cover_image":{},
		 *                  "author":"作者名字",
		 *                  "elements":[
		 *                  	{
		 *                  		"content":{}
		 *                  	},{
		 *                  		"content":{}
		 *                  	}
		 *                  ]
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/content/update")
		@PUT
		public Response update_content(JSONObject content);
		
		/**
		 * @api {POST} /admin/slide/add 添加轮播图
		 * @apiGroup Slide
		 * @apiVersion 0.0.1
		 * @apiDescription 添加轮播图
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} slide_image 显示图片
		 * @apiParam {String} type 轮播图类型
		 * @apiParam {Long} reference_id 平台内内容Id、POI id
		 * @apiParam {String} reference_url 外包链接
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"title":"标题",
		 *                  "slide_image":{},
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "reference_url":"www.baidu.co"
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/slide/add")
		@POST
		@Consumes({"application/json"})
		public Response add_slide(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/slide/update 修改轮播图
		 * @apiGroup Slide
		 * @apiVersion 0.0.1
		 * @apiDescription 修改轮播图
		 * @apiParam {Long} id 轮播图Id
		 * @apiParam {String} title 轮播图标题
		 * @apiParam {String} slide_image 显示图片
		 * @apiParam {String} type 轮播图类型
		 * @apiParam {Long} reference_id 平台内内容Id、POI id
		 * @apiParam {String} reference_url 外包链接
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":12,
		 *                	"title":"标题",
		 *                  "slide_image":{},
		 *                  "type":"content/poi/url",
		 *                  "reference_id":21,
		 *                  "reference_url":"www.baidu.co"
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/slide/update")
		@PUT
		public Response update_slide(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/slide/sequence 轮播图排序
		 * @apiGroup Slide
		 * @apiVersion 0.0.1
		 * @apiDescription 修改轮播图
		 * @apiParam {Long} id 轮播图Id
		 * @apiParam {int} sequence 序列号
		 * @apiParamExample {json} 范例
		 *               [ {
		 *                	"id":12,
		 *                	"sequence":1,
		 *                },{
		 *                	"id":13,
		 *                	"sequence":2,
		 *                }]
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/slide/sequence")
		@PUT
		public Response slide_sequence(JSONArray content);
		
		/**
		 * @api {GET} /admin/slide/list 轮播图列表
		 * @apiGroup Slide
		 * @apiVersion 0.0.1
		 * @apiDescription 轮播图列表
		 * @apiSuccessExample {json} 返回范例:
		 *               [ {
		 *                	"id":12,
		 *                	"title":"title",
		 *                	"type":"content",
		 *                	"slide_image":{},
		 *                	"sequence":1,
		 *                },{
		 *                	"id":12,
		 *                	"title":"title",
		 *                	"type":"poi",
		 *                	"slide_image":{},
		 *                	"sequence":1,
		 *                }]
		 */
		@Path("/slide/list")
		@GET
		public Response slide_list();
		/**
		 * @api {DELETE} /admin/slide/{slide_id} 删除轮播图
		 * @apiGroup Slide
		 * @apiVersion 0.0.1
		 * @apiDescription 删除轮播图
		 * @apiParam {Long} id 轮播图Id
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/slide/{slide_id}")
		@DELETE
		public Response delete_slide(@PathParam("slide_id")Long slide_id);
		
		/**
		 * @api {PUT} /admin/order_ticket/audit 退票审核处理
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 退票审核处理
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 * 				{
		 *                	"id":12,
		 *                	"status":3, //3 通过 4 拒绝
		 *                }
		 */
		@Path("/order_ticket/audit")
		@PUT
		public Response audit_order_ticket(JSONObject content);
		
		/**
	 	 * @api {GET} /admin/content/list 内容列表
	 	 * @apiGroup Content
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 内容列表
	 	 * @apiSuccessExample {json} 返回数据:
	 	 * 					{
	 	 * 						"total":234,
		 * 						"contents":[{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"cover_image":{},
	 	 *                			"author":"dddd",
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                		},{
	 	 *                			"id":123,
	 	 *                			"title":"title"
	 	 *                			"cover_image":{},
	 	 *                			"author":"dddd",
	 	 *                			"create_time":123456,
	 	 *                			"update_time":12345,
	 	 *                			}]
	 	 *                }
	     *  @apiErrorExample {json} Error-Response: 
	     *  HTTP/1.1 400 Not Found 
	     *  { 
	     *   status:'内容不存在'
	     *   code:10003, 
	     *   error_message:'内容不存在', 
	     *   }
	 	 */
		@Path("/content/list")
		@GET
		public Response content_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
	 	 * @api {GET} /admin/classify/list 分类列表
	 	 * @apiGroup Classify
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 分类列表
	 	 * @apiSuccessExample {json} 返回数据:
		 * 					[{
	 	 *                			"id":123,
	 	 *                			"classify_name":"classify_name"
	 	 *                		},{
	 	 *                			"id":123,
	 	 *                			"classify_name":"classify_name"
	 	 *                			}]
	     *  @apiErrorExample {json} Error-Response: 
	     *  HTTP/1.1 400 Not Found 
	     *  { 
	     *   status:'内容不存在'
	     *   code:10003, 
	     *   error_message:'内容不存在', 
	     *   }
	 	 */
		@Path("/classify/list")
		@GET
		public Response classify_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {POST} /admin/notice/add 添加通知
		 * @apiGroup Notice
		 * @apiVersion 0.0.1
		 * @apiDescription 添加通知
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} type 通知类型类型 poi content url
		 * @apiParam {Long} reference_id 平台内内容Id、POI id
		 * @apiParam {String} url 链接
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"title":"标题",
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "url":"www.baidu.co"
		 *                  "status":0
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/notice/add")
		@POST
		@Consumes({"application/json"})
		public Response add_notice(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
	 	 * @api {GET} /admin/notice/list 通知列表
	 	 * @apiGroup Notice
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 通知列表
	 	 * @apiParam {String} title 通知标题
	 	 * @apiSuccessExample {json} 返回数据:
		 * 			[
		 * 				{
		 *                	"title":"标题",
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "url":"www.baidu.co"
		 *                },
		 *                {
		 *                	"title":"标题",
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "url":"www.baidu.co"
		 *                }
	 	 *           ]
	     *  @apiErrorExample {json} Error-Response: 
	     *  HTTP/1.1 400 Not Found 
	     *  { 
	     *   status:'内容不存在'
	     *   code:10003, 
	     *   error_message:'内容不存在', 
	     *   }
	 	 */
		@Path("/notice/list")
		@GET
		public Response notice_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/notice/update 修改通知
		 * @apiGroup Notice
		 * @apiVersion 0.0.1
		 * @apiDescription 修改通知
		 * @apiParam {Long} id	通知Id
		 * @apiParam {int} status 是否显示
		 * @apiParamExample {json} 范例
		 *               {
		 *                	"id":12,
		 *                	"status":1,
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/notice/update")
		@PUT
		public Response update_notice(JSONObject notice,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {POST} /admin/message/add 添加推送通知
		 * @apiGroup SendMessage
		 * @apiVersion 0.0.1
		 * @apiDescription 添加推送通知
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} type 通知类型类型 poi content url
		 * @apiParam {Long} reference_id 平台内内容Id、POI id
		 * @apiParam {String} url 链接
		 * @apiParam {int} send_type 推送类型 0 即时没有send_time 1 定时传send_time
		 * @apiParam {String} send_time 推送时间
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"title":"标题",
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "url":"www.baidu.co",
		 *                  "send_time":"2017-12-03 12:00",
		 *                  "send_type":0
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/message/add")
		@POST
		@Consumes({"application/json"})
		public Response add_message(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid)throws Exception;
		
		/**
	 	 * @api {GET} /admin/message/list 推送通知列表
	 	 * @apiGroup SendMessage
	 	 * @apiVersion 0.0.1
	 	 * @apiDescription 推送通知列表
	 	 * @apiParam {String} title 通知标题
	 	 * @apiSuccessExample {json} 返回数据:
		 * 			[
		 * 				{
		 *                	"title":"标题",
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "url":"www.baidu.co",
		 *                  "send_time":"2017-02-24 12:00"
		 *                },
		 *                {
		 *                	"title":"标题",
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "url":"www.baidu.co",
		 *                  "send_time":"2017-02-24 12:00"
		 *                }
	 	 *           ]
	     *  @apiErrorExample {json} Error-Response: 
	     *  HTTP/1.1 400 Not Found 
	     *  { 
	     *   status:'内容不存在'
	     *   code:10003, 
	     *   error_message:'内容不存在', 
	     *   }
	 	 */
		@Path("/message/list")
		@GET
		public Response message_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {POST} /admin/ticket/add 添加门票
		 * @apiGroup Ticket
		 * @apiVersion 0.0.1
		 * @apiDescription 添加门票
		 * @apiParam {String} zyb_code 智游宝票编码
		 * @apiParam {String} ticket_name 门票名
		 * @apiParam {JSONObject} cover_image 题图
		 * @apiParam {INT} number 库存
		 * @apiParam {String} start_date 开始日期"2017-07-23"
		 * @apiParam {String} start_time 开始时间"08:00"
		 * @apiParam {String} end_time 结束时间"18:00"
		 * @apiParam {String} price 票价
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00
		 *                }
		 * @apiSuccessExample {json} 返回范例:
		 *               {
		 *               	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00
		 *                }
		 */
		@Path("/ticket/add")
		@POST
		@Consumes({"application/json"})
		public Response add_ticket(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/ticket/update 修改门票信息
		 * @apiGroup Ticket
		 * @apiVersion 0.0.1
		 * @apiDescription 修改门票信息
		 * @apiParam {Long} id 票Id
		 * @apiParam {String} zyb_code 智游宝票编码
		 * @apiParam {String} ticket_name 门票名
		 * @apiParam {JSONObject} cover_image 题图
		 * @apiParam {INT} number 库存
		 * @apiParam {String} start_date 开始日期"2017-07-23"
		 * @apiParam {String} start_time 开始时间"08:00"
		 * @apiParam {String} end_time 结束时间"18:00"
		 * @apiParam {String} price 票价
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00
		 *                }
		 *  
		 *  @apiParamExample {json} 范例
		 *                {
		 *                	"id":12,
		 *                	"status":"disable"
		 *                }
		 * @apiSuccessExample {json} 返回范例:
		 *               {
		 *               	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00,
		 *                  "slod":123,
		 *                }
		 */
		@Path("/ticket/update")
		@PUT
		public Response update_ticket(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {GET} /admin/ticket/list 获取门票列表
		 * @apiGroup Ticket
		 * @apiVersion 0.0.1
		 * @apiDescription 获取门票列表
		 * @apiParamExample 范例
		 *  ?ticket_name=..&zyb_code..&poi_id=12
		 * @apiSuccessExample {json} 返回范例:
		 *          {
		 *               "total":230,
		 *               "pois":[
		 *               	{
		 *               		"id":12,
		 *               		"title":"title"
		 *               	},{
		 *               		"id":12,
		 *               		"title":"title"
		 *               	}
		 *               ],
		 *              "tickets":[
		 *               {
		 *               	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00,
		 *                  "slod":123,
		 *                },{
		 *               	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00,
		 *                  "slod":123,
		 *                  "poi":{
		 *                  	"id":12,
		 *                  	"title":"title"
		 *                  }
		 *                },
		 *                
		 *                ]
		 *                
		 *          }
		 */
		@Path("/ticket/list")
		@GET
		public Response ticket_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid)throws Exception;
		
		/**
		 * @api {GET} /admin/ticket/poi 获取没有关联POI门票列表
		 * @apiGroup Ticket
		 * @apiVersion 0.0.1
		 * @apiDescription 获取没有关联POI门票列表
		 * @apiParamExample 范例
		 *  ?ticket_name=..&zyb_code..&poi_id=12
		 * @apiSuccessExample {json} 返回范例:
		 *          {
		 *               "total":230,
		 *               "pois":[
		 *               	{
		 *               		"id":12,
		 *               		"title":"title"
		 *               	},{
		 *               		"id":12,
		 *               		"title":"title"
		 *               	}
		 *               ],
		 *              "tickets":[
		 *               {
		 *               	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00,
		 *                  "slod":123,
		 *                },{
		 *               	"id":12,
		 *                	"zyb_code":"zyb_code",
		 *                  "cover_image":{},
		 *                  "ticket_name":"轮播图类型",
		 *                  "number":21,
		 *                  "start_date":"2017-07-23",
		 *                  "start_time":"08:00",
		 *                  "end_time":"18:00",
		 *                  "price":235.00,
		 *                  "slod":123,
		 *                  "poi":{
		 *                  	"id":12,
		 *                  	"title":"title"
		 *                  }
		 *                },
		 *                
		 *                ]
		 *                
		 *          }
		 */
		@Path("/ticket/poi")
		@GET
		public Response ticket_poi_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid)throws Exception;
		
		/**
		 * @api {POST} /admin/ad/add 添加广告
		 * @apiGroup Advertising
		 * @apiVersion 0.0.1
		 * @apiDescription 添加广告
		 * @apiParam {String} title 内容标题
		 * @apiParam {String} ad_image 显示图片
		 * @apiParam {String} type 轮播图类型
		 * @apiParam {Long} reference_id 平台内内容Id、POI id
		 * @apiParam {String} reference_url 外包链接
		 * @apiParam {int} ad_type 广告类型 0 APP启动
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"title":"标题",
		 *                  "ad_image":{},
		 *                  "type":"轮播图类型",
		 *                  "reference_id":21,
		 *                  "reference_url":"www.baidu.co",
		 *                  "ad_type":0
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/ad/add")
		@POST
		@Consumes({"application/json"})
		public Response add_ad(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/ad/update 修改广告
		 * @apiGroup Advertising
		 * @apiVersion 0.0.1
		 * @apiDescription 修改广告
		 * @apiParam {Long} id ad Id
		 * @apiParam {String} title ad标题
		 * @apiParam {String} slide_image 显示图片
		 * @apiParam {String} type ad类型
		 * @apiParam {Long} reference_id 平台内内容Id、POI id
		 * @apiParam {String} reference_url 外包链接
		 * @apiParam {int} ad_type 广告类型 0 APP启动
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"id":12,
		 *                	"title":"标题",
		 *                  "slide_image":{},
		 *                  "type":"content/poi/url",
		 *                  "reference_id":21,
		 *                  "reference_url":"www.baidu.co"，
		 *                  "ad_type":0
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/ad/update")
		@PUT
		public Response update_ad(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		
		/**
		 * @api {GET} /admin/ad/list 广告列表
		 * @apiGroup Advertising
		 * @apiVersion 0.0.1
		 * @apiDescription 广告列表
		 * @apiSuccessExample {json} 返回范例:
		 *               [ {
		 *                	"id":12,
		 *                	"title":"title",
		 *                	"type":"content",
		 *                	"slide_image":{},
		 *                	"sequence":1,
		 *                	"ad_type":0
		 *                },{
		 *                	"id":12,
		 *                	"title":"title",
		 *                	"type":"poi",
		 *                	"slide_image":{},
		 *                	"sequence":1,
		 *                	"ad_type":0
		 *                }]
		 */
		@Path("/ad/list")
		@GET
		public Response ad_list();
		/**
		 * @api {DELETE} /admin/ad/{ad_id} 删除广告
		 * @apiGroup Advertising
		 * @apiVersion 0.0.1
		 * @apiDescription 删除广告
		 * @apiParam {Long} id 轮播图Id
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/ad/{ad_id}")
		@DELETE
		public Response delete_ad(@PathParam("ad_id")Long ad_id);
		
		
		/**
		 * @api {POST} /admin/map/add 添加地图信息
		 * @apiGroup Map
		 * @apiVersion 0.0.1
		 * @apiDescription 添加地图信息
		 * @apiParam {String} url 内容标题
		 * @apiParam {String} version 地图版本
		 * @apiParam {String} pointA A经纬度
		 * @apiParam {String} pointB B经纬度
		 * @apiParam {String} coordinateX x坐标
		 * @apiParam {String} coordinateY y坐标
		 * @apiParam {String} lng_lat_x 单位经纬度x
		 * @apiParam {String} lng_lat_y 单位经纬度y
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"url":"url",
		 *                  "version":"1.0",
		 *                  "pointA":"",
		 *                  "pointB":"",
		 *                  "coordinateX":"",
		 *                  "coordinateY":"",
		 *                  "lng_lat_x":"",
		 *                  "lng_lat_y":""
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/map/add")
		@POST
		@Consumes({"application/json"})
		public Response add_map(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {PUT} /admin/map/update 修改地图信息
		 * @apiGroup Map
		 * @apiVersion 0.0.1
		 * @apiDescription 修改地图信息
		 * @apiParam {String} url 内容标题
		 * @apiParam {String} version 地图版本
		 * @apiParamExample {json} 范例
		 *              {
		 *                	"url":"url",
		 *                  "version":"1.0",
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/map/update")
		@PUT
		public Response update_map(JSONObject content,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		
		/**
		 * @api {GET} /admin/map/list 列表
		 * @apiGroup Map
		 * @apiVersion 0.0.1
		 * @apiDescription 广告列表
		 * @apiSuccessExample {json} 返回范例:
		 *               [ 
		*               {
		*                	"url":"url",
		*                  "version":"1.0",
		*                },{
		*                	"url":"url",
		*                  "version":"1.0",
		*                }
		 *                ]
		 */
		@Path("/map/list")
		@GET
		public Response map_list();
		
		/**
		 * @api {GET} /admin/map/detail 地图详情
		 * @apiGroup Map
		 * @apiVersion 0.0.1
		 * @apiDescription 地图详情
		 * @apiSuccessExample {json} 返回范例:
		*               {
		*                	"url":"url",
		*                  "version":"1.0",
		*                }
		 */
		@Path("/map/detail")
		@GET
		public Response map_detail();
		/**
		 * @api {DELETE} /admin/map/{map_id} 删除地图
		 * @apiGroup Map
		 * @apiVersion 0.0.1
		 * @apiDescription 删除广告
		 * @apiParam {Long} map_id 轮播图Id
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/map/{map_id}")
		@DELETE
		public Response delete_map(@PathParam("map_id")Long ad_id);
		
		@Path("/hotel")
		@GET
		public Response poi_hotel(@Context HttpServletRequest request);
		
		/**
		 * @api {POST} /admin/refund 退款
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 退款
		 * @apiParam {String} orders_ticket_no 子订单号
		 * @apiParamExample {json} 范例
		 *                {
		 *                	"orders_ticket_no":"订单号"
		 *                }
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/refund")
		@POST
		@Consumes({"application/json"})
		public Response refund(JSONObject orders,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
		
		/**
		 * @api {POST} /admin/import_insurant 导入用户购买的保险单号
		 * @apiGroup Orders
		 * @apiVersion 0.0.1
		 * @apiDescription 导入用户购买的保险单号
		 * @apiParam {String} identity_card 身份证号
		 * @apiParam {String} insurant_no 保险单号
		 * @apiParamExample {json} 范例
		 *            [
		 *                {
		 *                	"identity_card":"身份证号",
		 *                	"insurant_no":"保险单号"
		 *                },{
		 *                	"identity_card":"身份证号",
		 *                	"insurant_no":"保险单号"
		 *                }
		 *            ]
		 * @apiSuccess (200) {String} status success
		 * @apiSuccessExample {json} 返回范例:
		 *                {
		 *                	"status":"success",
		 *                }
		 */
		@Path("/import_insurant")
		@POST
		@Consumes({"application/json"})
		public Response import_insurant(JSONArray arr,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
}
