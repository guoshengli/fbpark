package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Poi;
import com.fbpark.rest.model.Recommandation;

public interface RecommandationDao extends BaseDao<Recommandation, Long> {
	public List<Poi> getRecommandationListByPoiId(Long poiId);
	
	public void deleteRecommandationByPoiReference(Long poiId,Long referenceId);
	
	public void deleteRecommandationByPoi(Long poiId);
}
