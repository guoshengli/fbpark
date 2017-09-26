package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.LinkAccounts;

public interface LinkAccountsDao extends BaseDao<LinkAccounts, Long> {

	public Object[] getLinkAccountsByUUID(String uuid);

	public List<LinkAccounts> getLinkAccountsByUserid(Long userid);

	public void deleteLinkAccountsByServiceAndUserid(String service, Long userid);

}
