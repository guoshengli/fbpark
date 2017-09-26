package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.ParkMap;

public interface ParkMapDao extends BaseDao<ParkMap, Long> {
	public List<ParkMap> getParkMapList(String status);
}
