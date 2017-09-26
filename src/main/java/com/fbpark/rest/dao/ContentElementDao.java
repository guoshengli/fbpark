package com.fbpark.rest.dao;

import com.fbpark.rest.model.ContentElement;

public interface ContentElementDao extends BaseDao<ContentElement, Long> {
	public void deleteContentElementByContentId(Long content_id);
}
