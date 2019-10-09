package com.haze.redis.manage;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis资源管理器 通过该类进行redis调用
 */
@Component
public class RedisManager {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Set<String> listRedis() {
        return redisTemplate.keys("*");
    }

    public Object getKey(String key) {
        DataType dataType = redisTemplate.type(key);
        assert dataType != null;
        switch (dataType) {
            case STRING:
                return redisTemplate.opsForValue().get(key);
            case HASH:
                Map<Object, Object> result = new LinkedHashMap<>();
                redisTemplate.opsForHash().keys(key).forEach(k -> {
                    result.put(k, redisTemplate.opsForHash().get(key, k));
                });
                return result;
            case SET:
            case LIST:
            case ZSET:
            default:
                return null;

        }
        //return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void setKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    public boolean expire(String key, long time) {
        if (time > 0) {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return false;
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean hasKey(String key) {
        Objects.requireNonNull(key, "key不能为空");
        return redisTemplate.hasKey(key);
    }
}
