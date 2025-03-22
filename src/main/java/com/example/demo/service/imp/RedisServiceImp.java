package com.example.demo.service.imp;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.service.RedisService;

@Service
public class RedisServiceImp implements RedisService {
	
	private RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImp(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    @Override
    public void setWithExpireTime(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
    
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
}
