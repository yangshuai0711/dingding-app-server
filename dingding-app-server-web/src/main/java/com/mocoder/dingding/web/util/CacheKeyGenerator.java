package com.mocoder.dingding.web.util;

import com.mocoder.dingding.utils.web.JsonUtil;
import org.springframework.cache.interceptor.DefaultKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by yangshuai on 16/1/30.
 */
public class CacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        try {
            StringBuilder sb = new StringBuilder(target.getClass().getName()).append('.')
                    .append(method.getName()).append('(').append(JsonUtil.toString(params))
                    .append(')');
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("spring cache key 生成失败");
        }
    }
}
