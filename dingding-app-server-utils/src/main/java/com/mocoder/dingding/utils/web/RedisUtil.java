package com.mocoder.dingding.utils.web;

import com.mocoder.dingding.utils.bean.TypeRef;
import com.mocoder.dingding.utils.spring.SpringContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangshuai on 16/1/31.
 */
public class RedisUtil {
    private static RedisTemplate redisTemplate;

    static {
        redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
    }


    public static String getString(final String key) {
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] value = connection.get(key.getBytes("utf-8"));
                    if (value == null) {
                        return null;
                    }
                    return new String(value, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("不支持的编码类型utf-8", e);
                }
            }
        }, true, false);
        return result;
    }

    public static void setString(final String key, final String value, final Long seconds) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] byteKey = key.getBytes("utf-8");
                    connection.set(byteKey, value.getBytes("utf-8"));
                    if (seconds != null) {
                        connection.expire(byteKey, seconds);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("不支持的编码类型utf-8", e);
                }
                return null;
            }
        }, true, true);
    }

    public static void del(final String key) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] byteKey = key.getBytes("utf-8");
                    connection.del(byteKey);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("不支持的编码类型utf-8", e);
                }
                return null;
            }
        }, true, true);
    }

    public static <T> T getObj(final String key, final TypeRef<T> type) {
        String result = getString(key);
        if (result == null) {
            return null;
        }
        try {
            return JsonUtil.toObject(result, type);
        } catch (IOException e) {
            throw new RuntimeException("json对象解析失败", e);
        }
    }

    public static <T> void setObj(final String key, T obj, Long seconds) {
        String value = null;
        try {
            value = JsonUtil.toString(obj);
        } catch (IOException e) {
            throw new RuntimeException("json序列化对象失败", e);
        }
        if (value != null) {
            setString(key, value, seconds);
        }
    }

    public static String hGetString(final String key, final String field) {
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] value = connection.hGet(key.getBytes("utf-8"), field.getBytes("utf-8"));
                    if (value == null) {
                        return null;
                    }
                    return new String(value, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("不支持的编码类型utf-8", e);
                }
            }
        }, true, false);
        return result;
    }

    public static void hSetString(final String key, final String field, final String value, final Long seconds) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] byteKey = key.getBytes("utf-8");
                    connection.hSet(byteKey, field.getBytes("utf-8"), value.getBytes("utf-8"));
                    if (seconds != null) {
                        connection.expire(byteKey, seconds);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("不支持的编码类型utf-8", e);
                }
                return null;
            }
        }, true, true);
    }

    public static void hDel(final String key, final String field) {
        Long result = (Long)redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    long value = connection.hDel(key.getBytes("utf-8"), field.getBytes("utf-8"));
                    return Long.valueOf(value);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("不支持的编码类型utf-8", e);
                }
            }
        }, true, false);
    }

    public static <T> T hGetObj(final String key, final String field, final TypeRef<T> type) {
        String result = hGetString(key, field);
        if (result == null) {
            return null;
        }
        try {
            return JsonUtil.toObject(result, type);
        } catch (IOException e) {
            throw new RuntimeException("json对象解析失败", e);
        }
    }

    public static <T> void hSetObj(final String key, final String field, T obj, final Long seconds) {
        String value = null;
        try {
            value = JsonUtil.toString(obj);
        } catch (IOException e) {
            throw new RuntimeException("json序列化对象失败", e);
        }
        if (value != null) {
            hSetString(key, field, value, seconds);
        }
    }

    public static <T> Map<String, T> hGetAllObj(final String key, final TypeRef<T> type) {

        Map<String, T> result = (Map<String, T>) redisTemplate.execute(new RedisCallback<Map<String, T>>() {
            @Override
            public Map<String, T> doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    Map<String, T> resultMap = new LinkedHashMap<String, T>();
                    Map<byte[], byte[]> map = connection.hGetAll(key.getBytes("utf-8"));
                    if (map == null) {
                        return resultMap;
                    }
                    for (Map.Entry<byte[], byte[]> ent : map.entrySet()) {
                        String key = new String(ent.getKey(), "utf-8");
                        T value = JsonUtil.toObject(new String(ent.getValue(), "utf-8"), type);
                        resultMap.put(key, value);
                    }
                    return resultMap;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true, false);
        return result;
    }

}
