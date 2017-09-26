package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Slide;

public interface SlideDao extends BaseDao<Slide, Long> {
	public List<Slide> getSlideByStatus(String status);
}
