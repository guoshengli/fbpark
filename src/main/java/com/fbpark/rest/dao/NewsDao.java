package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.News;

public interface NewsDao extends BaseDao<News, Long> {
	public List<News> getNewsBySequence();
}
