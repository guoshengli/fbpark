package com.fbpark.rest.dao;

import java.util.List;

import com.fbpark.rest.model.Setting;

public interface SettingDao extends BaseDao<Setting, Long> {
	public List<Setting> getSettingListByType(int type);
}
