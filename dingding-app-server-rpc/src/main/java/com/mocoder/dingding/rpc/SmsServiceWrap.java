package com.mocoder.dingding.rpc;

/**
 * 短信发送接口包装类
 */
public interface SmsServiceWrap {

    /**
     * 发送登录验证码短信
     * @param validCode
     * @param mobileNum
     * @return
     */
    public boolean sentLoginValidCodeSms(String mobileNum,String validCode);

    /**
     * 发送登录验证码短信
     * @param validCode
     * @param mobileNum
     * @return
     */
    public boolean sentRegValidCodeSms(String mobileNum,String validCode);
    /**
     * 发送重要操作验证码短信
     * @param validCode
     * @param mobileNum
     * @return
     */
    public boolean sentRegValidCodeSms(String mobileNum,String validCode,String operate);
}
