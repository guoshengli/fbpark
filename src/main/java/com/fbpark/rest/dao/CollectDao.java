package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Collect;

public interface CollectDao extends BaseDao<Collect, Long> {
	public int getWishByUseridAndItemidAndType(Long userid,Long item_id,String type);
	
	public List<Collect> getCollectListByUserid(Long userid);
	
	public List<Collect> getCollectListByUseridAndCount(Long userid,int count);
	
	public List<Collect> getCollectListByUseridAndCountAndId(Long userid,int count,Long id);
	
	public Collect getCollectByUseridAndTypeAndReferenceid(Long userid,String type,Long referenceid);
	
	public void delCollectByUseridAndTypeAndReferenceid(Long userid,String type,Long referenceid);
}
