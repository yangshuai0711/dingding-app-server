package com.mocoder.dingding.rpc.impl;

import com.mocoder.dingding.rpc.SmsServiceWrap;
import com.taobao.api.ApiException;
import com.taobao.api.AutoRetryTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 短信发送服务实现
 */
@Service
public class SmsServiceWrapImpl implements SmsServiceWrap {

    private static final Logger log = LogManager.getLogger(SmsServiceWrapImpl.class);

    public static final String SMS_SIGH_NAME = "注册验证";
    public static final String SMS_PRODUCT_NAME = "叮叮";
    public static final String SMS_TAMPLATE_REG_VALIDATE_CODE = "SMS_4790018";
    public static final String SMS_TAMPLATE_LOGIN_VALIDATE_CODE = "SMS_4790018";
    public static final String SMS_TAMPLATE_CHANGE_PASS = "SMS_4790016";

    @Value("${taobao.api.sms.url}")
    private String sms_url;
    @Value("${taobao.api.sms.appkey}")
    private String sms_appkey;
    @Value("${taobao.api.sms.secret}")
    private String sms_secret;

    private TaobaoClient smsClient = null;

    public TaobaoClient getSmsClient() {
        if(smsClient==null) {
            smsClient = new AutoRetryTaobaoClient(sms_url, sms_appkey, sms_secret);
        }
        return smsClient;
    }

    public boolean sentLoginValidCodeSms(String mobileNum, String validCode) {
        return sentValidCodeSms(mobileNum,validCode,SMS_SIGH_NAME,SMS_TAMPLATE_LOGIN_VALIDATE_CODE,"登录验证");
    }

    @Override
    public boolean sentRegValidCodeSms(String mobileNum, String validCode) {
        return sentValidCodeSms(mobileNum,validCode,SMS_SIGH_NAME,SMS_TAMPLATE_REG_VALIDATE_CODE,"注册验证");
    }

    @Override
    public boolean sentRegValidCodeSms(String mobileNum, String validCode, String operate) {
        return sentValidCodeSms(mobileNum,validCode,SMS_SIGH_NAME,SMS_TAMPLATE_REG_VALIDATE_CODE,"重要操作");
    }

    /**
     * 发送验证码
     * @param mobileNum 手机号
     * @param validCode 验证码内容
     * @param signName 短信签名
     * @param templateNum 模板号
     * @param opeateName 操作名称（日志用）
     * @return 发送是否成功
     */
    private boolean sentValidCodeSms(String mobileNum, String validCode,String signName,String templateNum,String opeateName){
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName(signName);
        req.setSmsParamString("{\"code\":\"" + validCode + "\",\"product\":\" "+SMS_PRODUCT_NAME+" \"}");
        req.setRecNum(mobileNum);
        req.setSmsTemplateCode(templateNum);
        AlibabaAliqinFcSmsNumSendResponse rsp ;
        try {
            rsp = getSmsClient().execute(req);
            if (rsp==null){
                log.error("验证码-"+opeateName+"-失败:返回结果为null，mobile:{} validCode{}",mobileNum,validCode);
                return false;
            }else{
                if(!rsp.isSuccess()){
                    log.error("验证码-"+opeateName+"-失败:返回结果失败:,mobile:{} code:{} response:{}",mobileNum,validCode, ReflectionToStringBuilder.toString(rsp, ToStringStyle.SIMPLE_STYLE));
                    return false;
                }
            }
        } catch (ApiException e) {
            log.error("验证码-"+opeateName+"-失败:短信发送异常,mobile:"+mobileNum+" code:"+validCode, e);
        }

        log.info("验证码-"+opeateName+":发送成功，mobile:{} validCode:{}",mobileNum,validCode);
        return true;
    }
}
