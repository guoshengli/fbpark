package com.fbpark.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


@Path("/basic_params")
@Produces({"application/json"})
public interface BasicParamService {

	/**
	 * @api {GET} /basic_params 基础数据
	 * @apiGroup BasicParams
	 * @apiVersion 0.0.1
	 * @apiDescription 基础数据
	 * @apiSuccessExample {json} 返回数据: 
	 * 	{
    "minium_version":"1.0",
    "newest_version":"1.2",
    "map":{
        "version": 1.0,
        "url":"http://qiniu.com/sdklfsldfjslfjsdlf.jpg"
    },
    "help":[
        {
            "title":"这是一条帮助标题",
            "id":"1212",
        }
    ],
    "news":[
        {
            "title":"这是一条新闻标题",
            "id":"344343",
        }
    ],
    "hot":[
        {
            "id":121,
            "title":"大脚车",
            "cover_image":{
                "org_size":"xxxxxx",
                "name":"xxxxx"
            }
        },
    ],
    "ad":{
        "type":"POI"      //类型：POI，Link，Content，如果为POI类型则content字段中为poi_id,参考轮播图slides
        "cover_image":{
             "name":"http://osx5mq0te.bkt.clouddn.com/client_uploads/images/3/8CE0EC1DFAADD0616CBB7393D3157508",
             "original_size":"{800,600}"
          },
        "url":null,
        "reference_id":12121
    },
}		
	 */
	 @GET
	  public abstract Response getBasicParams();
}
