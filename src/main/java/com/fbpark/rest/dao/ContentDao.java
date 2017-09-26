package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Content;

public interface ContentDao extends BaseDao<Content, Long> {
	public List<Content> getContentByRegionId(Long region_id);
	
	public List<Content> getAllContentByRegionId(Long region_id);
	
	public List<Content> getContentList(int count,int page);
	
	public int getContentCount();
	
	public List<Content> getContentListByTitle(int count,int page,String title);
	
	public int getContentCount(String title);
}
