package com.mocoder.dingding.web.util;

/**
 * Created by yangshuai3 on 2016/2/4.
 */
public enum BodyAlgorithmEnum {

    BASE64("base64")
    ;
    private String name;

    BodyAlgorithmEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
