package com.newbee.smart_album.dao.config;

import com.newbee.smart_album.ApplicationContextHolder;
import org.apache.ibatis.cache.Cache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisConfig implements Cache {

    private final String id;

    private RedisTemplate redisTemplate;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisConfig(String id){
        if(id == null)
            throw new IllegalArgumentException("id error");
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putObject(Object o, Object o1) {
        getRedisTemplate().opsForValue().set(o,o1);
    }

    @Override
    public Object getObject(Object o) {
        return getRedisTemplate().opsForValue().get(o);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object o) {
        getRedisTemplate().delete(o);
        return null;
    }

    @Override
    public void clear() {
        getRedisTemplate().execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.flushDb();
                return null;
            }
        });
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private RedisTemplate getRedisTemplate(){
        if(redisTemplate == null)
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
        return redisTemplate;
    }
}
