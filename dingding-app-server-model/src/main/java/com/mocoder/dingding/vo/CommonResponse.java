package com.mocoder.dingding.vo;

import com.mocoder.dingding.enums.ErrorTypeEnum;

import java.io.Serializable;

/**
 * Created by yangshuai3 on 2016/1/26.
 */
public class CommonResponse<T> implements Serializable {
    private String code;
    private String msg;
    private T data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void resolveErrorInfo(ErrorTypeEnum errorType) {
        this.code=String.valueOf(errorType.getCode());
        this.msg =errorType.getUiMsg();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
