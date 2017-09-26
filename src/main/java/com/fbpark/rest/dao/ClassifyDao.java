package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Classify;

public interface ClassifyDao extends BaseDao<Classify, Long> {
	public List<Classify> getClassifyByOnlineAndHomepage();
}
