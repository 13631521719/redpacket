package com.dy.game.rpcommon.Template;



import com.dy.game.rpcommon.util.SpringBeanUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedisTemplate {
    public static org.springframework.data.redis.core.RedisTemplate redisTemplate;

    public static ValueOperations stringOperations;

    public static HashOperations hashOperations;

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

    private static HashOperations getHashOperations() {
        if (hashOperations == null)
            hashOperations = getRedisTemplate().opsForHash();
        return hashOperations;
    }

    /**
     * 默认过期时长，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 不设置过期时长
     */
    public static final long NOT_EXPIRE = -1;


    public static void set(String key, String value) {
        getValueOperations().set(key, value);
    }

    public static void set(String key, String value, long l, TimeUnit timeUnit) {
        getValueOperations().set(key, value);
        expireKey(key, l, timeUnit);
    }

    public static void set(String key, Object value) {
        getValueOperations().set(key, value);
    }

    public static void set(String key, Object value, long l, TimeUnit timeUnit) {
        getValueOperations().set(key, value);
        expireKey(key, l, timeUnit);
    }
    /**
     * 自动加一
     * @param key
     */
    public static void incr(String key) {
        getRedisTemplate().getConnectionFactory().getConnection().incr(getRedisTemplate().getKeySerializer().serialize(key));
    }
    /**
     * 给key的值加N
     * @param key
     */
    public static void incrBy(String key, int value) {
        getRedisTemplate().getConnectionFactory().getConnection().incrBy(getRedisTemplate().getKeySerializer().serialize(key),value);
    }



    /**
     * 自动减1
     * @param key
     */
    public static void decr(String key) {
        getRedisTemplate().getConnectionFactory().getConnection().decr(getRedisTemplate().getKeySerializer().serialize(key));
    }
    /**
     * 给key的值减N
     * @param key
     */
    public static void decrBy(String key, int value) {
        getRedisTemplate().getConnectionFactory().getConnection().decrBy(getRedisTemplate().getKeySerializer().serialize(key),value);
    }



    public static Object get(String key) {
        return getValueOperations().get(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static boolean existsKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
        getRedisTemplate().rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return getRedisTemplate().renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public static void deleteKey(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
        getRedisTemplate().delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
        getRedisTemplate().delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public static void expireKey(String key, long time, TimeUnit timeUnit) {
        getRedisTemplate().expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key) {
        getRedisTemplate().persist(key);
    }

}
