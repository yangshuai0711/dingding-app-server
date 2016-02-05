package com.mocoder.dingding.utils.web;

import com.mocoder.dingding.utils.bean.TypeRef;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;

/**
 * Created by yangshuai3 on 2016/1/26.
 */
public class JsonUtil {

    /**
     * 将对象按照json字符串格式输出
     *
     * @param obj javabean对象实例
     * @return json字符串
     * @throws IOException
     */
    public static String toString(Object obj) throws IOException {
        if(obj==null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper.writeValueAsString(obj);
    }

    /**
     * 解析json字符串格式到javabean对象
     *
     * @param json json字符串
     * @param type 类型包装类，new新实力请带上泛型
     * @param <T>  目标对象类型
     * @return 目标对象实例
     * @throws IOException
     */
    public static <T> T toObject(String json, TypeRef<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, type);
    }

    /**
     * 解析json字符串格式到javabean对象
     *
     * @param json json字符串
     * @param claz 目标类型，不支持泛型
     * @param <T>  目标对象类型
     * @return 目标对象实例
     * @throws IOException
     */
    public static <T> T toObject(String json, Class<T> claz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, claz);
    }
}
