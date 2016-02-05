package com.mocoder.dingding.utils.web;

import com.mocoder.dingding.vo.CommonResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by yangshuai3 on 2016/1/26.
 */
public class WebUtil {

    /**
     * 转换请求中的参数为简单对象（只有对象中的简单类型会被赋值）,参数值为数组的只取第一个
     *
     * @param request 请求对象
     * @param claz    目标对象类型类
     * @param <T>     目标对象类型
     * @return 目标对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T paramsToSimpleBean(HttpServletRequest request, Class<T> claz) throws IllegalAccessException, InstantiationException {
        Map<String, String[]> paramMap = request.getParameterMap();
        T object = claz.newInstance();
        for (Map.Entry<String, String[]> ent : paramMap.entrySet()) {
            if (StringUtils.isNotBlank(ent.getKey()) && ent.getValue() != null && ent.getValue().length > 0 && StringUtils.isNotBlank(ent.getValue()[0])) {
                try {
                    BeanUtils.setProperty(object, ent.getKey(), ent.getValue()[0]);
                } catch (Exception e) {
                }
            }
        }
        return object;
    }

    /**
     * 转换请求中的header中的参数为简单对象（只有对象中的简单类型会被赋值）,参数值为数组的只取第一个
     *
     * @param request 请求对象
     * @param claz    目标对象类型类
     * @param <T>     目标对象类型
     * @return 目标对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T HeaderToSimpleBean(HttpServletRequest request, Class<T> claz) throws IllegalAccessException, InstantiationException {
        Enumeration it = request.getHeaderNames();
        T object = claz.newInstance();
        while (it.hasMoreElements()) {
            String hKey = (String) it.nextElement();
            String hValue = request.getHeader(hKey);
            if (StringUtils.isNotBlank(hKey) && StringUtils.isNotBlank(hValue)) {
                try {
                    BeanUtils.setProperty(object, hKey, hValue);
                } catch (Exception e) {
                }
            }
        }
        return object;
    }


    /**
     * 手否简单类型（字符串、基本类型以及其包装类型）
     *
     * @param obj 要判断的对象实例
     * @return 是否简单类型
     */
    public static boolean isSimpleType(Object obj) {
        Class claz = obj.getClass();
        if (claz.isPrimitive()) {//基本类型
            return true;
        }
        try {
            if (((Class) claz.getField("TYPE").get(null)).isPrimitive()) {//包装基本类型
                return true;
            }
        } catch (Exception e) {
        }
        if (claz == String.class) {
            return true;
        }
        return false;
    }

    /**
     * 写出返回结果到response
     * @param response
     * @param result
     * @throws IOException
     */
    public static void writeResponse(HttpServletResponse response, CommonResponse result) throws IOException {
        PrintWriter out = response.getWriter();
        out.write(JsonUtil.toString(result));
        out.flush();
    }
}
