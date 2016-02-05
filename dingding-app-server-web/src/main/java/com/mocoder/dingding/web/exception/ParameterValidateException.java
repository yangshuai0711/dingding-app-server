package com.mocoder.dingding.web.exception;

import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.vo.CommonResponse;
import org.apache.velocity.util.StringUtils;

/**
 * Created by yangshuai3 on 2016/2/4.
 */
public class ParameterValidateException extends Exception{
    private CommonResponse response = new CommonResponse();

    public ParameterValidateException(Throwable cause) {
        super(cause);
        response.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
    }

    public ParameterValidateException(String message, Throwable cause) {
        super(message, cause);
        response.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
        response.setMsg(message);
    }

    public ParameterValidateException(String message) {
        super(message);
        response.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
        response.setMsg(message);
    }

    public ParameterValidateException() {
        super();
        response.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
    }

    public CommonResponse getResponse() {
        return response;
    }

    public void setErrorType(ErrorTypeEnum errorType,String msg){
        response.resolveErrorInfo(errorType);
        if(org.apache.commons.lang.StringUtils.isNotBlank(msg)){
            response.setMsg(msg);
        }
    }
}

