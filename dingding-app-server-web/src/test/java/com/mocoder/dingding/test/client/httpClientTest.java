package com.mocoder.dingding.test.client;

import com.mocoder.dingding.constants.AppVersionConstant;
import com.mocoder.dingding.constants.EncryptionConstant;
import com.mocoder.dingding.constants.PlatformConstant;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.utils.web.JsonUtil;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.LoginAccountRequest;
import org.junit.Test;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: likun7
 * Date: 14-11-19
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 */
public class httpClientTest {

    @Test
    public void getLoginCode() {
        String sessionId = "";
        String url = "http://localhost:8080/account/getLoginVerifyCode";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        try {
            String response = HttpClientUtil.doPost(url, header, param);
            System.out.println("--------getLoginCode()--------");
            System.out.println(response);
            CommonResponse<String> result = JsonUtil.toObject(response, CommonResponse.class);
            if (ErrorTypeEnum.INPUT_PARAMETER_SESSION_ABSENT.getCode().equals(result.getCode())) {
                header.put("sessionid", result.getData());
                String body = "{\"mobile\":\"15652301160\"}";
                BASE64Encoder encoder = new BASE64Encoder();
                body = encoder.encode(body.getBytes("utf-8"));
                param.put("body", body);
                String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
                String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
                header.put("sign", targetToken);
                response = HttpClientUtil.doPost(url, header, param);
                System.out.println("--------getLoginCode()--------");
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRegCode() {
        String sessionId = "";
        String url = "http://localhost:8080/account/getRegVerifyCode";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        try {
            String response = HttpClientUtil.doPost(url, header, param);
            System.out.println("--------getRegCode()--------");
            System.out.println(response);
            CommonResponse<String> result = JsonUtil.toObject(response, CommonResponse.class);
            if (ErrorTypeEnum.INPUT_PARAMETER_SESSION_ABSENT.getCode().equals(result.getCode())) {
                header.put("sessionid", result.getData());
                String body = "{\"mobile\":\"15652301160\"}";
                BASE64Encoder encoder = new BASE64Encoder();
                body = encoder.encode(body.getBytes("utf-8"));
                param.put("body", body);
                String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
                String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
                header.put("sign", targetToken);
                response = HttpClientUtil.doPost(url, header, param);
                System.out.println("--------getRegCode()--------");
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reg() {
        String sessionId = "9fcb0913-382f-4377-b132-55e84ee777fb";
        String verifyCode = "5889";
        String url = "http://localhost:8080/account/reg";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        LoginAccountRequest request = new LoginAccountRequest();
        request.setNickName("杨帅");
        request.setMobile("15652301160");
        request.setPassword("123456");
        request.setVerifyCode(verifyCode);
        try {
            String body = JsonUtil.toString(request);
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;

            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doPost(url, header, param);
            System.out.println("--------reg()--------");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginAccount a = new LoginAccount();
        try {
            String b = JsonUtil.toString(a);
            System.out.println(b==null?"empty":b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}