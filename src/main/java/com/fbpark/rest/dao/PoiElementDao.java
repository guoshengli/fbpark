package com.fbpark.rest.dao;

import com.fbpark.rest.model.PoiElement;

public interface PoiElementDao extends BaseDao<PoiElement, Long> {
	public void deletePoiElementByPoiId(Long poiId);
}
