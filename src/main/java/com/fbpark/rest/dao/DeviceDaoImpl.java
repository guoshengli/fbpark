package com.fbpark.rest.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.Device;
@Repository("deviceDao")
public class DeviceDaoImpl extends BaseDaoImpl<Device, Long> implements DeviceDao {

	public DeviceDaoImpl() {
		super(Device.class);
	}

	@Override
	public Device getDeviceByDeviceCode(String device_code) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from Device where 1=1 and device_code=? and status='enable'";
		Query query = session.createQuery(hql).setString(0, device_code);
		List<Device> dList = query.list();
		Device device = null;
		if(dList != null && dList.size() > 0){
			device = dList.get(0);
		}
		return device;
	}
	
}
