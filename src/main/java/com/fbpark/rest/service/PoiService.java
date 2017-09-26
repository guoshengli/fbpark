package com.fbpark.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

@Path("/pois")
@Produces({"application/json"})
public interface PoiService {
	/**
 	 * @api {GET} /pois/{poi_id} POI详情
 	 * @apiGroup Pois
 	 * @apiVersion 0.0.1
 	 * @apiDescription POI详情
 	 * @apiSuccessExample {json} 返回数据:
 	 *                
 	 *                	{
 	 *                		"id":1,
 	 *                		"title":"标题",
 	 *                		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
 	 *                  "address":"北京市昌平区十三陵",
	 *                  "is_collect":true,
	 *                  "location":{
	 *                  	"lng":106,
	 *                  	"lat":40.1
	 *                  },
	 *                  "user_count":234,
	 *                  "status":"end",
	 *                  "start_time":"2017-03-25 15:00", 
	 *                  "end_time":"2017-03-28 15:00", 
	 *                  "description":"描述",
	 *                  "user":{
	 *                  	"id":123,
	 *                  	"username":"guoshengli",
	 *                  	"avatar_image":{
	 *                  	},
	 *                  	"introduction":"简介",
	 *                  	
	 *                  }
 	 *                		
 	 *                	}                
 	 *                	
 	 */
	@Path("/{poi_id}")
	@GET
	public abstract Response get_poi(@HeaderParam("X-Tella-Request-Userid") Long loginUserid,
			@PathParam("poi_id") Long poi_id);
	
	
	/**
	 * @api {GET} /pois/map 地图下的POI列表
	 * @apiGroup Pois
	 * @apiVersion 0.0.1
	 * @apiDescription 分类下的POI列表
	 * @apiParamExample {json} 范例
	 *                classify_id=20
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    [
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览",
	 *                    		"author":"dddd", 
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                    		"is_online":0, 
	 *                    		"create_time":123456,
	 *                    		"update_time":12345
	 *                    	 },
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览", 
	 *                    		"author":"dddd",
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                   		"is_online":0, 
	 *                   		"create_time":123456,
	 *                    		"update_time":12345
	 *                     }
	 *                    ]
	 * 
	 */
	@Path("/map")
	@GET
	public abstract Response map_info(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
	/**
	 * @api {GET} /pois/search 根据标题搜索POI
	 * @apiGroup Pois
	 * @apiVersion 0.0.1
	 * @apiDescription 根据标题搜索POI
	 * @apiParamExample {json} 范例
	 *                ?title=title
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    [
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览",
	 *                    		"author":"dddd", 
	 *                    		"is_online":0, 
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                    		"create_time":123456,
	 *                    		"update_time":12345
	 *                    	 },
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览", 
	 *                    		"author":"dddd",
	 *                   		"is_online":0, 
	 *                   		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                   		"create_time":123456,
	 *                    		"update_time":12345,
	 *                     }
	 *                    ]
	 * 
	 */
	@Path("/search")
	@GET
	public abstract Response search_poi(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
	/**
	 * @api {GET} /pois/list 分类下的POI列表
	 * @apiGroup Pois
	 * @apiVersion 0.0.1
	 * @apiDescription 分类下的POI列表
	 * @apiParamExample {json} 范例
	 *                title=title
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    [
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览",
	 *                    		"author":"dddd", 
	 *                    		"is_online":0, 
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                    		"create_time":123456,
	 *                    		"update_time":12345
	 *                    	 },
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览", 
	 *                    		"author":"dddd",
	 *                   		"is_online":0, 
	 *                   		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                   		"create_time":123456,
	 *                    		"update_time":12345
	 *                     }
	 *                    ]
	 * 
	 */
	@Path("/list")
	@GET
	public abstract Response poi_list(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
	/**
	 * @api {GET} /pois/pos_list POS机POI列表
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription 分类下的POI列表
	 * @apiParamExample {json} 范例
	 *                title=title
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    [
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览",
	 *                    		"author":"dddd", 
	 *                    		"is_online":0, 
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                    		"create_time":123456,
	 *                    		"update_time":12345
	 *                    	 },
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览", 
	 *                    		"author":"dddd",
	 *                   		"is_online":0, 
	 *                   		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                   		"create_time":123456,
	 *                    		"update_time":12345
	 *                     }
	 *                    ]
	 * 
	 */
	@Path("/pos_list")
	@GET
	public abstract JSONObject pos_list(@Context HttpServletRequest request);
	
	/**
	 * @api {POST} /pois/idcard POS机扫描身份证获得订单信息
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription POS机扫描身份证获得订单信息
	 * @apiParamExample {json} 范例
	 *                ?identity_card=identity_card&poi_id=12&ticket_id=12
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    [
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览",
	 *                    		"author":"dddd", 
	 *                    		"is_online":0, 
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                    		"create_time":123456,
	 *                    		"update_time":12345
	 *                    	 },
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览", 
	 *                    		"author":"dddd",
	 *                   		"is_online":0, 
	 *                   		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                   		"create_time":123456,
	 *                    		"update_time":12345
	 *                     }
	 *                    ]
	 * 
	 */
	@Path("/idcard")
	@POST
	public abstract JSONObject idcard_orders(JSONObject param);
	
	/**
	 * @api {POST} /pois/is_collect 收藏/取消收藏POI
	 * @apiGroup Pois
	 * @apiVersion 0.0.1
	 * @apiDescription 收藏/取消收藏POI
	 * @apiParamExample {json} 范例
	 *                {
	 *                	"poi_id":12,
	 *                	"is_collect":0/1
	 *                }
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    	{ 
	 *                    		"status":"success", 
	 *                     }
	 * 
	 */
	@Path("/is_collect")
	@POST
	public abstract Response is_collect_poi(JSONObject collect,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
	/**
	 * @api {POST} /pois/add_device 添加POS机设备
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription 添加POS机设备
	 * @apiParamExample {json} 范例
	 *                {
	 *                	"device_code":"",
	 *                	"poi_id":12
	 *                }
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    	{ 
	 *                    		"status":"success", 
	 *                     }
	 * 
	 */
	@Path("/add_device")
	@POST
	public abstract JSONObject add_device(JSONObject device);
	
	/**
	 * @api {GET} /pois/pos/{poi_id} POS根据poi_id得到酒店房型
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription POS根据poi_id得到酒店房型
	 * 
	 */
	@Path("/pos/{poi_id}")
	@GET
	public abstract JSONObject posHotel(@PathParam("poi_id") Long poi_id);
	
	/**
	 * @api {GET} /pois/search_room POS根据条件得到酒店对应的房间
	 * @apiGroup POS
	 * @apiVersion 0.0.1
	 * @apiDescription POS根据条件得到酒店对应的房间
	 * @apiParamExample {json} 范例
	 * ?type_id=..&start_time=..&end_time=..
	 */
	@Path("/search_room")
	@GET
	public abstract JSONObject search_room(@Context HttpServletRequest request);
	
	/**
	 * @api {GET} /pois/ticket 分类下的POI列表
	 * @apiGroup Pois
	 * @apiVersion 0.0.1
	 * @apiDescription 分类下的POI列表
	 * @apiParamExample {json} 范例
	 *                title=title
	 * @apiSuccessExample {json} 返回数据:
	 * 
	 *                    [
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览",
	 *                    		"author":"dddd", 
	 *                    		"is_online":0, 
	 *                    		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                    		"create_time":123456,
	 *                    		"update_time":12345
	 *                    	 },
	 *                    	{ 
	 *                    		"id":123, 
	 *                    		"title":"title",
	 *                    		"classify_name":"参观游览", 
	 *                    		"author":"dddd",
	 *                   		"is_online":0, 
	 *                   		"cover_image":{"name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508","original_size":"{800,600}"},
	 *                   		"create_time":123456,
	 *                    		"update_time":12345
	 *                     }
	 *                    ]
	 * 
	 */
	@Path("/ticket")
	@GET
	public abstract Response poi_ticket(@Context HttpServletRequest request,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);

}
