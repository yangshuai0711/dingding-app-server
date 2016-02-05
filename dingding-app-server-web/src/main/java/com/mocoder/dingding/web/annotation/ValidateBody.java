package com.mocoder.dingding.web.annotation;

import com.mocoder.dingding.web.util.BodyAlgorithmEnum;

import java.lang.annotation.*;

/**
 * Created by yangshuai3 on 2016/2/1.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateBody {
    /**
     * @return 参数字段名，从request.getParameter获取
     */
    String value() default "body";

    /**
     * @return 对称加密算法
     * @see com.mocoder.dingding.web.util.BodyAlgorithmEnum
     */
    BodyAlgorithmEnum[] algorithm() default {};

    /**
     * 必需字段名
     * @return
     */
    String[] requiredAttrs() default {};
}
