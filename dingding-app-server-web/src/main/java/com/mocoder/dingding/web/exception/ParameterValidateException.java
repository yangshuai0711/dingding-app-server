package com.mocoder.dingding.web.exception;

import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.vo.CommonResponse;

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
    }

    public ParameterValidateException(String message) {
        super(message);
        response.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
    }

    public ParameterValidateException() {
        super();
        response.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
    }

    public CommonResponse getResponse() {
        return response;
    }

    public void setErrorType(ErrorTypeEnum errorType){
        response.resolveErrorInfo(errorType);
    }
}

