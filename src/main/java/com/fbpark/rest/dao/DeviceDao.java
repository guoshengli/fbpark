package com.fbpark.rest.dao;

import com.fbpark.rest.model.Device;

public interface DeviceDao extends BaseDao<Device, Long> {
	public Device getDeviceByDeviceCode(String device_code);
}
