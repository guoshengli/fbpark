package com.fbpark.rest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.fbpark.rest.common.ParseFile;
import com.fbpark.rest.dao.AdDao;
import com.fbpark.rest.dao.HelpDao;
import com.fbpark.rest.dao.NewsDao;
import com.fbpark.rest.dao.ParkMapDao;
import com.fbpark.rest.dao.PoiDao;
import com.fbpark.rest.model.Ad;
import com.fbpark.rest.model.Content;
import com.fbpark.rest.model.Help;
import com.fbpark.rest.model.News;
import com.fbpark.rest.model.ParkMap;
import com.fbpark.rest.model.Poi;
import com.fbpark.rest.redis.MyRedisDao;
import com.google.common.base.Strings;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BasicParamServiceImpl implements BasicParamService {

	@Autowired
	private PoiDao poiDao;
	
	@Autowired
	private NewsDao newsDao;
	
	@Autowired
	private HelpDao helpDao;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private ParkMapDao parkMapDao;

	@Autowired
	private MyRedisDao myRedisDao;
	
	public Response getBasicParams() {
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		System.out.println("Basic Param *****"+(end-start));
		
//		String basic_params = jedis.get("basic_param");
		long end1 = System.currentTimeMillis();
		System.out.println("Basic Param get data *****"+(end1-end));
		JSONObject param = new JSONObject();
		JSONObject version = getVersion();
		String minium_version = version.getString("minium_version");
		String newest_version = version.getString("newest_version");
		if(myRedisDao.exists("basic_param")){
			Object basic_params = myRedisDao.get("basic_param");
			param = JSONObject.fromObject(basic_params);
			String redis_minium_version = param.getString("minium_version");
			String redis_newest_version = param.getString("newest_version");
			if(!minium_version.equals(redis_minium_version) 
					&& newest_version.equals(redis_newest_version)){
				param.put("minium_version", minium_version);
			}else if(minium_version.equals(redis_minium_version) 
					&& !newest_version.equals(redis_newest_version)){
				param.put("newest_version", newest_version);
			}else if(!minium_version.equals(redis_minium_version) 
					&& !newest_version.equals(redis_newest_version)){
				param.put("minium_version", version.getString("minium_version"));
				param.put("newest_version", version.getString("newest_version"));
			}
		}else{

			param.put("minium_version", version.getString("minium_version"));
			param.put("newest_version", version.getString("newest_version"));
			List<ParkMap> pmList = parkMapDao.getParkMapList("enable");
			if(pmList != null && pmList.size() > 0){
				ParkMap pm = pmList.get(0);
				if(pm != null){
					JSONObject mapJson = new JSONObject();
					mapJson.put("id", pm.getId());
					mapJson.put("coordinateA", JSONObject.fromObject(pm.getCoordinateA()));
					mapJson.put("coordinateB", JSONObject.fromObject(pm.getCoordinateB()));
					mapJson.put("lng_lat_x", pm.getLng_lat_x());
					mapJson.put("lng_lat_y", pm.getLng_lat_y());
					mapJson.put("pointA", JSONObject.fromObject(pm.getPointA()));
					mapJson.put("pointB", JSONObject.fromObject(pm.getPointB()));
					mapJson.put("url", pm.getUrl());
					mapJson.put("version", pm.getVersion());
					param.put("map", mapJson);
				}
			}
			
			List<Help> helpList = helpDao.getHelpBySequence();
			List<JSONObject> helpJsonList = new ArrayList<JSONObject>();
			JSONObject helpJson = null;
			if(helpList != null && helpList.size() > 0){
				for(Help help:helpList){
					helpJson = new JSONObject();
					Content c = help.getContent();
					helpJson.put("id",c.getId());
					helpJson.put("title", c.getTitle());
					helpJsonList.add(helpJson);
				}
				param.put("help", helpJsonList);
			}
			
			List<News> newsList = newsDao.getNewsBySequence();
			List<JSONObject> newsJsonList = new ArrayList<JSONObject>();
			JSONObject newsJson = null;
			if(newsList != null && newsList.size() > 0){
				for(News n:newsList){
					newsJson = new JSONObject();
					Content c = n.getContent();
					newsJson.put("id",c.getId());
					newsJson.put("title", c.getTitle());
					newsJsonList.add(newsJson);
				}
				param.put("news", newsJsonList);
			}
			
			List<Poi> poiList = poiDao.getPoiListByHot();
			
			List<JSONObject> poiJsonList = new ArrayList<JSONObject>();
			JSONObject poiJson = null;
			if(poiList != null && poiList.size() > 0){
				for(Poi poi:poiList){
					poiJson = new JSONObject();
					poiJson.put("id", poi.getId());
					poiJson.put("title", poi.getTitle());
					poiJson.put("cover_image", JSONObject.fromObject(poi.getElements().get(0).getElement()));
					poiJsonList.add(poiJson);
				}
				param.put("hot", poiJsonList);
			}
			
			Ad slide = adDao.getAdByAdType(0);
			if(slide != null){
				JSONObject slideJson = new JSONObject();
				slideJson.put("id", slide.getId());
				slideJson.put("ad_image", JSONObject.fromObject(slide.getAd_image()));
				String type = slide.getType(); // url content poi
				if (type.equals("url")) {
					slideJson.put("type", slide.getType());
					slideJson.put("url", slide.getUrl());
				} else {
					slideJson.put("type", slide.getType());
					slideJson.put("reference_id", slide.getReference_id());
				}
				
				param.put("ad", slideJson);
			}
			
			myRedisDao.save("basic_param", param.toString(),0);
		}
		
		
		return Response.status(Response.Status.OK).entity(param).build();
	}
	
	public String getToken(String ak, String sk, String bucket) throws Exception {
		Mac mac = new Mac(ak, sk);
		PutPolicy policy = new PutPolicy(bucket);
		String token = policy.token(mac);
		return token;
	}
	
	public JSONObject parseJson(String path) {
		String sets = ReadFile(path);
		JSONObject jo = JSONObject.fromObject(sets);
		return jo;
	}
	
	public String ReadFile(String path) {
		File file = new File(path);
		BufferedReader reader = null;
		String laststr = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			while ((tempString = reader.readLine()) != null) {
				laststr = laststr + tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();

			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException1) {
				}
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException2) {
				}
		}
		return laststr;
	}
	
	public JSONObject getVersion() {
		JSONObject version = new JSONObject();
		String path = getClass().getResource("/../../META-INF/version.json").getPath();
		JSONObject json1 = ParseFile.parseJson(path);
		String minium_version = json1.getString("minium_version");
		String newest_version = json1.getString("newest_version");
		version.put("minium_version", minium_version);
		version.put("newest_version", newest_version);
		return version;

	}
	
//	public Jedis getJedis(){
//		String path = getClass().getResource("/../../META-INF/redis.json").getPath();
//		JSONObject json1 = ParseFile.parseJson(path);
//		String host = json1.getString("host");
//		int port = json1.getInt("port");
//		String password = json1.getString("password");
//		Jedis jedis = null;
//		if(!Strings.isNullOrEmpty(password)){
//			JedisPoolConfig config = new JedisPoolConfig();
//			//最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//			config.setMaxIdle(10000);
//			//最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//			config.setMaxTotal(10000);
//			config.setTestOnBorrow(false);
//			config.setTestOnReturn(false);
//			JedisPool pool = new JedisPool(config, host, port, 1000, password);
//			jedis = pool.getResource();
//		}else{
//			jedis = new Jedis(host, port);
//		}
//		return jedis;
//	}
	
	/**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
//    public void returnResource(Jedis redis) {
//        if (redis != null) {
//            pool.returnResource(redis);
//        }
//    }

}
