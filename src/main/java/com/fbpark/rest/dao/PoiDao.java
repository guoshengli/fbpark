package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Poi;

public interface PoiDao extends BaseDao<Poi, Long> {
	public List<Poi> getPoiList(int count,int page);
	
	public int getPoiCount();
	
	public List<Poi> getPoiListByParams(int count, int page,
			String poi_name,String is_online,String classify_id);  
	
	public int getCountByParams(String poi_name, String is_online, String classify_id);
	
	public List<Poi> getPoiListByParams(String is_online,String classify_id);
	
	public List<Poi> getPoiListByClassify_idAndIs_online(int is_online,Long classify_id);
	
	public List<Poi> getPoiListByTitle(String title);
	
	public List<Poi> getPoiListByClassify(Long classify_id,int count);
	
	public List<Poi> getPoiListByClassify(Long classify_id,int count,Long id);
	
	public List<Poi> getPoiListByHot();
	
	public List<Poi> getPoiList();
	
	public List<Poi> getPoiListByTicket(int count,String title);
	
	public List<Poi> getPoiListByTicket(int count,Long poi_id,String title);
	
	public List<Poi> getPoiListByClassify();
}
