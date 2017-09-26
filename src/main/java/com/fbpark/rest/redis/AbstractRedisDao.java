package com.fbpark.rest.redis;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public abstract class AbstractRedisDao<K, V> {
	@Resource(name="stringRedisTemplate")
    protected RedisTemplate<K, V> redisTemplate;
    protected RedisSerializer<String> getRedisSerializer() {  
        return redisTemplate.getStringSerializer();  
    }  
}
