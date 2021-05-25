package com.bjfu.notation_jh.service.impl;

import com.bjfu.notation_jh.common.Constants;
import com.bjfu.notation_jh.model.History;
import com.bjfu.notation_jh.service.intf.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author john
 * @create 2020/9/6 17:06
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void put(String key, String value) {

        redisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        String messageCode = (String) redisTemplate.opsForValue().get(key);
        return messageCode;
    }


    @Override
    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER,1);
    }

    public Long lrem(String key, Long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    public long lpush(String key, Object value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void lTrim(String key, Long start, Long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    public boolean expire(String key, Long seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public List<Object> lrange(String key, Long start, Long end) {
        List<Object> range = redisTemplate.opsForList().range(key, start, end);
//        List<History> historyList = range.stream().map(object->{
//            History history = (History) object;
//            return history;
//        }).collect(Collectors.toList());
        return range;
    }

    public Long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Long del(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.delete(keySet);
    }
}
