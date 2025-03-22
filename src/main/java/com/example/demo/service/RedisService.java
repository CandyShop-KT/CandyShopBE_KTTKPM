package com.example.demo.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
	
	public void set(String key, Object value);

	public void setWithExpireTime(String key, Object value, long timeout, TimeUnit timeUnit);

	public Object get(String key);

	public void delete(String key);
	
}
