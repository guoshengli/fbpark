package com.fbpark.rest.common;

import redis.clients.jedis.Jedis;

public class RedisUtil {
	public static Jedis initJedis(String host,int port){
		Jedis jedis = new Jedis(host,port);
		return jedis;
	}
}
