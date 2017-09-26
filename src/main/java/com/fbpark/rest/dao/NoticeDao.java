package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Notice;

public interface NoticeDao extends BaseDao<Notice, Long> {
	public List<Notice> getNoticeList(int count, int page);
	
	public int getNoticeCount();
	
	public List<Notice> getNoticeListByTitle(int count, int page,String title);
	
	public int getNoticeCountByTitle(String title);
	
	public Notice getNoticeByStatus(int status);
}
