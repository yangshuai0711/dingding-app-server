package com.mocoder.dingding.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by yangshuai on 16/1/31.
 */
public class SpringContextHolder implements ApplicationContextAware{
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> claz){
        return context.getBean(claz);
    }

    public static <T> T getBean(String name){
        return (T)context.getBean(name);
    }
}
