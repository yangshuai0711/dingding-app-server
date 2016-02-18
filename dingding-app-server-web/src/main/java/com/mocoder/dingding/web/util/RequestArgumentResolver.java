package com.mocoder.dingding.web.util;

import com.mocoder.dingding.constants.EncryptionConstant;
import com.mocoder.dingding.constants.RequestAttributeKeyConstant;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.utils.encryp.EncryptUtils;
import com.mocoder.dingding.utils.web.JsonUtil;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.web.annotation.RequiredParam;
import com.mocoder.dingding.web.annotation.ValidateBody;
import com.mocoder.dingding.web.exception.ParameterValidateException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
            BodyAlgorithmEnum[] algorithms = annotation.algorithm();
            String paramName = annotation.value();
            String[] requiredFields = annotation.requiredAttrs();
            if (StringUtils.isNotBlank(paramName) && StringUtils.isNotBlank(webRequest.getParameter(paramName))) {
                String encBody = webRequest.getParameter(paramName);
                String bodyString = getDecodedBody(encBody, algorithms);
                Object obj = null;
                try {
                    obj = JsonUtil.toObject(bodyString, methodParameter.getParameterType());
                } catch (IOException e) {
                    ParameterValidateException exception = new ParameterValidateException();
                    exception.setErrorType(ErrorTypeEnum.INPUT_PARAMETER_PARSE_ERROR,"请求体解析异常");
                    throw exception;
                }
                validParam(obj,requiredFields);
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
     * 解密body
     * @param encBody 加密的body字符串
     * @param algorithms 加密算法列表
     * @return 返回解密后的body字符串
     * @throws ParameterValidateException
     */
    private String getDecodedBody(String encBody, BodyAlgorithmEnum[] algorithms) throws ParameterValidateException {
        if(algorithms.length>0){
            for (BodyAlgorithmEnum alg:algorithms){
                if (BodyAlgorithmEnum.BASE64==alg) {
                    try {
                        encBody = EncryptUtils.base64Decode(encBody);
                    } catch (Exception e1) {
                        ParameterValidateException exception = new ParameterValidateException();
                        exception.setErrorType(ErrorTypeEnum.INPUT_PARAMETER_PARSE_ERROR,"请求体解密失败");
                        throw exception;
                    }
                }else if (BodyAlgorithmEnum.DES==alg) {
                    try {
                        encBody = EncryptUtils.desDecode(EncryptionConstant.DES_ENCRYPT_PRIVATE_KEY,encBody);
                    } catch (Exception e1) {
                        ParameterValidateException exception = new ParameterValidateException();
                        exception.setErrorType(ErrorTypeEnum.INPUT_PARAMETER_PARSE_ERROR,"请求体解密失败");
                        throw exception;
                    }
                }
            }
        }
        return encBody;
    }

    /**
     * 校验必须参数
     * @param obj 需要校验的bean
     * @param requiredField 必须的属性名
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
