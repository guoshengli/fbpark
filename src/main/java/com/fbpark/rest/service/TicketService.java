package com.fbpark.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@Path("/tickets")
@Produces({"application/json"})
public interface TicketService {
	
	/**
 	 * @api {GET} /tickets/list 门票列表
 	 * @apiGroup Tickets
 	 * @apiVersion 0.0.1
 	 * @apiDescription 门票列表
 	 * @apiParam {String} count 每页显示条数
	 * @apiParam {Long} max_id 每页最后的内容Id
	 * @apiParam {String} ticket_name 门票名
 	 * @apiSuccessExample {json} 返回数据:
 	 *                [
 	 *                		{
 	 *                			"id":123,
 	 *                			"poi_id":12,
 	 *                			"ticket_name":"ticket_name"
 	 *                			"start_time":"start_time",
 	 *                			"end_time":"end_time",
 	 *                			"price":765.9,
 	 *                			"place":"place",
 	 *                		},
 	 *                
 	 *                	{
 	 *                			"id":123,
 	 *                			"poi_id":12,
 	 *                			"ticket_name":"ticket_name"
 	 *                			"start_time":"start_time",
 	 *                			"end_time":"end_time",
 	 *                			"price":765.9,
 	 *                			"place":"place",
 	 *                		}
 	 *                ]
 	 */
	@Path("/list")
	@GET
	public Response getTicketsList(@Context HttpServletRequest request);
	
	/**
 	 * @api {GET} /tickets/ticket_order_info/{ticket_id} 门票对应联系人
 	 * @apiGroup Tickets
 	 * @apiVersion 0.0.1
 	 * @apiDescription 门票对应联系人
 	 * @apiParam {Long} ticket_id 票的Id
 	 * @apiSuccessExample {json} 返回数据:
 	 * 					{
 	 *                		"ticket":{
 	 *                			"id":123,
 	 *                			"ticket_name":"ticket_name"
 	 *                			"start_time":"start_time",
 	 *                			"end_time":"end_time",
 	 *                			"price":765.9,
 	 *                			"place":"place",
 	 *                			"is_insurance":0/1,	// 0 没有保险 1有保险
 	 *                			"insurance_price":45.00//保险价格
 	 *                		},
 	 *                	"contacters":[
 	 *                		{
 	 *                			"id":1,
 	 *                			"name":"guo",
 	 *                			"identity_card":"dfsdfs"
 	 *                		},{
 	 *                			"id":1,
 	 *                			"name":"guo",
 	 *                			"identity_card":"dfsdfs"
 	 *                		}
 	 *                	]
 	 *                }
 	 *                
 	 */
	@Path("/ticket_order_info/{ticket_id}")
	@GET
	public Response ticket_order_info(@PathParam("ticket_id")Long ticket_id,@HeaderParam("X-Tella-Request-Userid") Long loginUserid);
	
}
