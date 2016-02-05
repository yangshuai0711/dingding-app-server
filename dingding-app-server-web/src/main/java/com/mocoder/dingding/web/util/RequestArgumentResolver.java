package com.mocoder.dingding.web.util;

import com.mocoder.dingding.constants.RequestAttributeKeyConstant;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.utils.web.JsonUtil;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.web.annotation.RequiredParam;
import com.mocoder.dingding.web.annotation.ValidateBody;
import com.mocoder.dingding.web.exception.ParameterValidateException;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by yangshuai3 on 2016/2/1.
 */
public class RequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        ValidateBody annotation = methodParameter.getParameterAnnotation(ValidateBody.class);
        if (annotation != null) {
            return true;
        } else if (RedisRequestSession.class == methodParameter.getParameterType()) {
            return true;
        } else if (CommonRequest.class == methodParameter.getParameterType()) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ValidateBody annotation = methodParameter.getParameterAnnotation(ValidateBody.class);
        if (annotation != null) {
            BodyAlgorithmEnum[] algorithm = annotation.algorithm();
            String paramName = annotation.value();
            String[] requiredField = annotation.requiredAttrs();
            if (StringUtils.isNotBlank(paramName) && StringUtils.isNotBlank(webRequest.getParameter(paramName))) {
                String bodyString = webRequest.getParameter(paramName);
                if(algorithm.length>0){
                    for (BodyAlgorithmEnum e:algorithm){
                        if (BodyAlgorithmEnum.BASE64==e) {
                            try {
                                BASE64Decoder decoder = new BASE64Decoder();
                                bodyString = new String(decoder.decodeBuffer(bodyString), "utf-8");
                            } catch (IOException e1) {
                                ParameterValidateException exception = new ParameterValidateException();
                                exception.setErrorType(ErrorTypeEnum.INPUT_PARAMETER_PARSE_ERROR,"请求体解密失败");
                                throw exception;
                            }
                        }
                    }
                }
                Object obj = null;
                try {
                    obj = JsonUtil.toObject(bodyString, methodParameter.getParameterType());
                } catch (IOException e) {
                    ParameterValidateException exception = new ParameterValidateException();
                    exception.setErrorType(ErrorTypeEnum.INPUT_PARAMETER_PARSE_ERROR,"请求体解析异常");
                    throw exception;
                }
                validParam(obj,requiredField);
                return obj;
            }else{
                ParameterValidateException exception = new ParameterValidateException("请求体"+paramName+"不能为空");
                throw exception;
            }
        } else if (RedisRequestSession.class == methodParameter.getParameterType()) {
            return webRequest.getAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_REQUEST_SESSION, RequestAttributes.SCOPE_REQUEST);
        } else if (CommonRequest.class == methodParameter.getParameterType()) {
            return webRequest.getAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_COMMON_REQUEST, RequestAttributes.SCOPE_REQUEST);
        }
        return null;
    }

    /**
     * 校验必须参数
     * @param obj
     * @param requiredField
     * @throws Exception
     */
    private void validParam(Object obj, String[] requiredField) throws Exception{
        Map<String,Object> map = PropertyUtils.describe(obj);
        if(requiredField!=null&&requiredField.length>0){
            for (String key:requiredField){
                Object object = map.get(key);
                if (object == null || (object instanceof String && StringUtils.isBlank((String) object))) {
                    ParameterValidateException exception = new ParameterValidateException(key + "不能为空");
                    throw exception;
                }
            }
        }else {
            for (Map.Entry<String, Object> ent : map.entrySet()) {
                Field field = obj.getClass().getField(ent.getKey());
                if (field != null && field.getAnnotation(RequiredParam.class) != null) {
                    if (ent.getValue() == null || (ent.getValue() instanceof String && StringUtils.isBlank((String) ent.getValue()))) {
                        ParameterValidateException exception = new ParameterValidateException(ent.getKey() + "不能为空");
                        throw exception;
                    }
                }
            }
        }
    }
}
