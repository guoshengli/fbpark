package com.fbpark.rest.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
@Repository("myRedisDao")
public class MyRedisDao extends AbstractRedisDao<String, Object> {
	// 保存键值对， 并设置过期时间
    public void save(String key,String  value,int timeout){
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value);//, timeout, TimeUnit.SECONDS
    }
    
    // 删除键
    public void delete(String key){
        redisTemplate.delete(key);
    }
    
    // 获取键值
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    
    // 更新过期时间
    public boolean updateExperiTime(String key,int timeout){
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
    
    // 判断 键 是否已存在
    public boolean exists(final String key){
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
            throws DataAccessException {
                return connection.exists(key.getBytes());
            }
            
        });
    }
}
