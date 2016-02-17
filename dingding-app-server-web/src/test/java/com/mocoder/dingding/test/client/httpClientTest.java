package com.mocoder.dingding.test.client;

import com.mocoder.dingding.constants.AppVersionConstant;
import com.mocoder.dingding.constants.EncryptionConstant;
import com.mocoder.dingding.constants.PlatformConstant;
import com.mocoder.dingding.utils.web.JsonUtil;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.LoginAccountRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: likun7
 * Date: 14-11-19
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 */
public class httpClientTest {

    private static final String HOST = "https://mocoder.com/dingding/";

    @Test
    public void testGetLoginCode() {
        String sessionId = getSessionId();
        Assert.assertNotNull(sessionId);
        boolean result = getLoginCode(sessionId);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetRegCode() {
        String sessionId = getSessionId();
        Assert.assertNotNull(sessionId);
        boolean result = getRegCode(sessionId);
        Assert.assertTrue(result);
    }

    @Test
    public void testLogout() {
        String sessionId = getSessionId();
        Assert.assertNotNull(sessionId);
        boolean result = logout(sessionId);
        Assert.assertTrue(result);
    }

    public static String getSessionId() {
        String url = HOST+"param/getSessionId";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", "");
        Map<String, String> param = new HashMap<String, String>();
        try {
            String body = "{\"mobile\":\"15652301160\"}";
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------getSessionId()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode() == 0) {
                return (String) commonResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getLoginCode(String sessionId) {
        String url = HOST+"account/getLoginVerifyCode";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        try {
            String body = "{\"mobile\":\"15652301160\"}";
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------getLoginCode()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getRegCode(String sessionId) {
        String url = HOST+"account/getRegVerifyCode";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        try {
            String body = "{\"mobile\":\"15652301160\"}";
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------getRegCode()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean reg(String sessionId, String verifyCode) {
        String url = HOST+"account/reg";
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
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------reg()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode().intValue() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginByCode(String sessionId, String verifyCode) {
        String url = HOST+"account/loginByCode";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        LoginAccountRequest request = new LoginAccountRequest();
        request.setMobile("15652301160");
        request.setVerifyCode(verifyCode);
        try {
            String body = JsonUtil.toString(request);
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;

            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------reg()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode().intValue() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginByPass(String sessionId, String pass) {
        String url = HOST+"account/loginByPass";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        LoginAccountRequest request = new LoginAccountRequest();
        request.setMobile("15652301160");
        request.setPassword(pass);
        try {
            String body = JsonUtil.toString(request);
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;

            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------loginByPass()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode().intValue() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean logout(String sessionId) {
        String url = HOST+"account/logout";
        Map<String, String> header = new HashMap<String, String>();
        header.put("appversion", AppVersionConstant.VERSION_1_0_0);
        header.put("platform", PlatformConstant.ANDROID);
        header.put("timestamp", String.valueOf(new Date().getTime()));
        header.put("deviceid", "123456");
        header.put("sessionid", sessionId);
        Map<String, String> param = new HashMap<String, String>();
        try {
            String body = "{\"mobile\":\"15652301160\"}";
            BASE64Encoder encoder = new BASE64Encoder();
            body = encoder.encode(body.getBytes("utf-8"));
            param.put("body", body);
            String tmpStr = param.get("body") + header.get("timestamp") + header.get("sessionid") + EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
            String targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
            header.put("sign", targetToken);
            String response = HttpClientUtil.doHttpPost(url, header, param);
            System.out.println("--------logout()--------");
            System.out.println(response);
            CommonResponse commonResponse = JsonUtil.toObject(response, CommonResponse.class);
            if (commonResponse.getCode() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        String sessionId = getSessionId();
        String code = "";
        String pass = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n\noptions：1.register; 2.login; 3.get session id; 4.logout; 5.exit");
            String input1 = scanner.nextLine();
            if ("1".equals(input1)) {
                System.out.println("options：1.input verify code; 2.get and input verify code; 3.reg");
                String input2 = scanner.nextLine();
                if ("2".equals(input2)) {
                    getRegCode(sessionId);
                    code = scanner.nextLine();
                }else if("1".equals(input2)){
                    System.out.println("last code:"+code);
                    code = scanner.nextLine();
                }
                System.out.println("use code:"+code);
                reg(sessionId,code);
            } else if ("2".equals(input1)) {//login
                System.out.println("options：1.input verify code; 2.get and input verify code; 3.input pass; 4.login with last code; 5.login with last pass");
                String input2 = scanner.nextLine();
                if ("2".equals(input2)) {
                    getLoginCode(sessionId);
                    code = scanner.nextLine();
                    System.out.println("use code:"+code);
                    loginByCode(sessionId, code);
                }else if("1".equals(input2)){
                    System.out.println("last code:"+code);
                    code = scanner.nextLine();
                    System.out.println("use code:"+code);
                    loginByCode(sessionId, code);
                }else if("3".equals(input2)){
                    System.out.println("last pass:"+pass);
                    pass = scanner.nextLine();
                    System.out.println("use pass:"+pass);
                    loginByPass(sessionId,pass);
                }else if("4".equals(input2)){
                    System.out.println("last code:"+code);
                    System.out.println("use code:"+code);
                    loginByCode(sessionId, code);
                }else if("5".equals(input2)){
                    System.out.println("last pass:"+pass);
                    System.out.println("use pass:"+pass);
                    loginByPass(sessionId, pass);
                }else {
                    System.out.println("无效的选项");
                }
            } else if ("3".equals(input1)) {//get session id
                sessionId = getSessionId();
            } else if ("4".equals(input1)) {//logout
                logout(sessionId);
            }else if ("5".equals(input1)) {//exit
                break;
            }else {
                System.out.println("无效的选项");
            }
        }
    }
}