package com.mocoder.dingding.utils.bean;

import com.mocoder.dingding.constants.RedisKeyConstant;
import com.mocoder.dingding.utils.bean.TypeRef;
import com.mocoder.dingding.utils.spring.SpringContextHolder;
import com.mocoder.dingding.utils.web.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangshuai on 16/1/31.
 */
public class RedisRequestSession {
    private String sessionId;
    private Map<String,Object> attributes = new ConcurrentHashMap<String,Object>();
    private RedisTemplate redisTemplate;
    private long expireDelay;

    /**
     * 创建一个session
     * @param sessionId
     * @param minutes n秒后过期
     */
    public RedisRequestSession(String sessionId,long minutes) {
        this();
        this.sessionId = sessionId;
        expireDelay = minutes;
    }

    public RedisRequestSession() {
        redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Map<String, Object> getAttributes() {
        return RedisUtil.hGetAllObj(RedisKeyConstant.REQUEST_SESSION_KEY_PREFIX, new TypeRef<Object>());
    }

    public <T> T getAttribute(String key,Class<T> tClass){
        return RedisUtil.hGetObj(RedisKeyConstant.REQUEST_SESSION_KEY_PREFIX + sessionId,key, new TypeRef<T>());
    }

    public void setAttribute(String key,Object obj){
        RedisUtil.hSetObj(RedisKeyConstant.REQUEST_SESSION_KEY_PREFIX+sessionId,key, obj,expireDelay*60);
    }

    public void removeAttribute(String key){
        RedisUtil.hDel(RedisKeyConstant.REQUEST_SESSION_KEY_PREFIX+sessionId,key);
    }
}
