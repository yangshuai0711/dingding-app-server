package com.mocoder.dingding.web.controller;

import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.service.LoginAccountService;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.LoginAccountRequest;
import com.mocoder.dingding.web.annotation.ValidateBody;
import com.mocoder.dingding.web.util.BodyAlgorithmEnum;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * 登录账户相关功能的控制器，包括登录、注册、获取验证码等，其中获取验证码也对应不同的入口，这样是为了区分开是哪个功能调用的获取验证码，从来增加一些条件判断
 */
@Controller
@RequestMapping("/account/")
public class LoginAccountController {

    @Resource
    private LoginAccountService loginAccountService;


    @RequestMapping("loginByPass")
    @ResponseBody
    public CommonResponse<LoginAccount> loginByPassword(@ValidateBody(requiredAttrs = {"mobile", "password"},algorithm = BodyAlgorithmEnum.BASE64) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) {
        CommonResponse<LoginAccount> response = loginAccountService.loginByPass(body.getMobile(), body.getPassword(), session,request);
        return response;
    }

    @RequestMapping(value = "loginByCode",method = {RequestMethod.POST})
    @ResponseBody
    public CommonResponse<LoginAccount> loginByCode(@ValidateBody(requiredAttrs = {"mobile", "verifyCode"},algorithm = BodyAlgorithmEnum.BASE64) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) {
        CommonResponse<LoginAccount> response  = loginAccountService.loginByVerifyCode(body.getMobile(), body.getVerifyCode(), session,request);
        return response;
    }

    @RequestMapping("getRegVerifyCode")
    @ResponseBody
    public CommonResponse<String> getRegVerifyCode(@ValidateBody(requiredAttrs = "mobile",algorithm = BodyAlgorithmEnum.BASE64) LoginAccountRequest body, RedisRequestSession session) {
        CommonResponse<String> response = loginAccountService.getRegVerifyCode(body.getMobile(), session);
        return response;
    }

    @RequestMapping("getLoginVerifyCode")
    @ResponseBody
    public CommonResponse<String> getLoginVerifyCode(@ValidateBody(requiredAttrs = "mobile",algorithm = BodyAlgorithmEnum.BASE64) LoginAccountRequest body, RedisRequestSession session) {
        CommonResponse<String> response = loginAccountService.getLoginVerifyCode(body.getMobile(), session);
        return response;
    }

    @RequestMapping("reg")
    @ResponseBody
    public CommonResponse<LoginAccount> reg(@ValidateBody(requiredAttrs = {"mobile", "nickName", "password","verifyCode"},algorithm = BodyAlgorithmEnum.BASE64) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CommonResponse<LoginAccount> response = loginAccountService.registerAccount(body, session,request);
        return response;
    }

    @RequestMapping("logout")
    @ResponseBody
    public CommonResponse<LoginAccount> logout(@ValidateBody(requiredAttrs = {"mobile"},algorithm = BodyAlgorithmEnum.BASE64) LoginAccountRequest body, RedisRequestSession session) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CommonResponse<LoginAccount> response = loginAccountService.logoutAccount(body, session);
        return response;
    }
}
