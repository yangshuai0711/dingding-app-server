package com.mocoder.dingding.utils.bean;

import org.codehaus.jackson.type.TypeReference;

/**
 * Created by yangshuai on 16/1/31.
 */
public class TypeRef<T> extends TypeReference<T> {

    @Override
    public int compareTo(TypeReference<T> o) {
        return this.getType().equals(o.getType())?0:1;
    }
}
