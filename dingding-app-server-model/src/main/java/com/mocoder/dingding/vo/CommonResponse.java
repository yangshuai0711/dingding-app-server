package com.mocoder.dingding.vo;

import com.mocoder.dingding.enums.ErrorTypeEnum;

import java.io.Serializable;

/**
 * Created by yangshuai3 on 2016/1/26.
 */
public class CommonResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private String uiMsg;
    private ErrorTypeEnum errorType;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUiMsg() {
        return uiMsg;
    }

    public void setUiMsg(String uiMsg) {
        this.uiMsg = uiMsg;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorTypeEnum errorType) {
        this.errorType = errorType;
    }

    public void resolveErrorInfo(ErrorTypeEnum errorType) {
        this.code=errorType.getCode();
        this.msg=errorType.getMsg();
        this.uiMsg=errorType.getUiMsg();
        this.errorType = errorType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
