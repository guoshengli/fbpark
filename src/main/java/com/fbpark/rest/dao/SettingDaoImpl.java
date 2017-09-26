package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Setting;
@Repository("settingDao")
@SuppressWarnings("unchecked")
public class SettingDaoImpl extends BaseDaoImpl<Setting, Long> implements SettingDao {

	public SettingDaoImpl() {
		super(Setting.class);
	}

	@Override
	public List<Setting> getSettingListByType(int type) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Setting where 1=1 and type=?";
		Query query = session.createQuery(hql).setInteger(0,type);
		List<Setting> settingList = query.list();
		return settingList;
	}

}
