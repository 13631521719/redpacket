package com.dy.game.rpcommon.Template;



import com.dy.game.rpcommon.constant.RedPagketConstant;
import com.dy.game.rpcommon.util.SpringBeanUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;


public class RedisTemplate {
    public static org.springframework.data.redis.core.RedisTemplate redisTemplate;

    public static ValueOperations stringOperations;

    public static org.springframework.data.redis.core.RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringBeanUtils.getBean("redisTemplate", org.springframework.data.redis.core.RedisTemplate.class);
        }
        return redisTemplate;
    }

    public static ValueOperations getValueOperations() {
        if (stringOperations == null)
            stringOperations = getRedisTemplate().opsForValue();
        return stringOperations;
    }

    public static void set(String key, Object value) {
        getValueOperations().set(key, value);
    }


    public static Object get(String key) {
        return getValueOperations().get(key);
    }

    /**
     *  获取分布式锁
     *
     * @param lock key值
     * @return 是否获取到
     */
    public static boolean lock(String lock){
        // 利用lambda表达式
        return (Boolean) getRedisTemplate().execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + RedPagketConstant.LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {
                byte[] value = connection.get(lock.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    // 如果锁已经过期
                    if (expireTime < System.currentTimeMillis()) {
                        // 重新加锁，防止死锁
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + RedPagketConstant.LOCK_EXPIRE + 1).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public static void deleteLock(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static Long lRightPushAll(String key, Collection<Object> value) {
        return getRedisTemplate().opsForList().rightPushAll(key, value);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key
     * @param start 开始位置, 0是开始位置
     * @param end   结束位置, -1返回所有
     * @return
     */
    public static List<Object> lRangeWithObj(String key, long start, long end) {
        return getRedisTemplate().opsForList().range(key, start, end);
    }
}
