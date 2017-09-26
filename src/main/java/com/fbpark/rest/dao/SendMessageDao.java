package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.SendMessage;

public interface SendMessageDao extends BaseDao<SendMessage, Long> {
	public List<SendMessage> getSendMessageList(int count, int page);
	
	public int getSendMessageCount();
	
	public List<SendMessage> getSendMessageListByTitle(int count, int page,String title);
	
	public int getSendMessageCountByTitle(String title);
}
