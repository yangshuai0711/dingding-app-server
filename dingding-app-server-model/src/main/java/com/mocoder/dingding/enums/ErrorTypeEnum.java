package com.mocoder.dingding.enums;

/**
 * Created by yangshuai3 on 2016/1/28.
 */
public enum ErrorTypeEnum {

    SYSTEM_EXCEPTION(10,"后台程序异常","系统繁忙，请重试"),
    SYSTEM_ENV_EXCEPTION(11,"系统环境异常","系统繁忙，请重试"),

    INPUT_PARAMETER_VALIDATE_ERROR(20,"参数校验错误","系统繁忙，请重试"),
    INPUT_PARAMETER_SESSION_ABSENT(-20,"参数sessionId为空","系统繁忙，请重试"),
    INPUT_PARAMETER_PARSE_ERROR(21,"入参解析错误","系统繁忙，请重试"),

    VALIDATE_CODE_ERROR(30,"验证码不正确","验证码输入有误，请重试"),
    NOT_LOGIN(-31,"未登录或者会话过期","您的登录信息已过期，请重新登录"),
    LOGIN_DUPLICATE_ERROR(31,"重复登录,session未过期,无需登录" ,"您已经在线,无需再次登录" ),
    REG_DUPLICATE_ERROR(32,"重复注册，手机号码已注册" ,"您的手机号码已注册" ),
    PASS_LOGIN_ERROR(33,"密码错误","账号或密码错误"),
    ACCOUNT_NOT_EXISTS(35,"手机号不存在","该手机号还没有注册"),
    NOT_LOG_OUT_ERROR(36,"需要退出登录状态","请先退出登录"),

    SMS_SEND_ERROR(40,"短信发送失败","短信发送失败，请稍后再试"),
    ;

    private Integer code;
    private String msg;
    private String uiMsg;

    ErrorTypeEnum(Integer code, String msg, String uiMsg) {
        this.code = code;
        this.msg = msg;
        this.uiMsg = uiMsg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getUiMsg() {
        return uiMsg;
    }
}
