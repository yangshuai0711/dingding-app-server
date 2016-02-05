package com.mocoder.dingding.web.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验拦截器抽象类，用于参数校验
 * Created by yangshuai3 on 2016/1/26.
 */
public abstract class ValidatorInterceptor implements HandlerInterceptor {

    protected static final Logger logger = LogManager.getLogger(ValidatorInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return validate(request,response);
    }

    /**
     * 实现校验逻辑
     * @param request
     * @param response
     * @return
     */
    protected abstract boolean validate(HttpServletRequest request, HttpServletResponse response);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
