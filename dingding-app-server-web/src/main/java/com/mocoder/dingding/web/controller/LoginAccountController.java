package com.mocoder.dingding.web.controller;

import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.service.LoginAccountService;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.LoginAccountRequest;
import com.mocoder.dingding.web.annotation.ValidateBody;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public static final Logger log = LogManager.getLogger(LoginAccountController.class);

    @Resource
    private LoginAccountService loginAccountService;


    @RequestMapping("loginByPass")
    @ResponseBody
    public CommonResponse<LoginAccount> loginByPassword(@ValidateBody(requiredAttrs = {"mobile", "password"}) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) {
        //TODO 调试完成候去掉，保护用户关键信息隐私
        log.info("密码登陆开始：入参：body:{},request:{}", ReflectionToStringBuilder.toString(body, ToStringStyle.SIMPLE_STYLE),ReflectionToStringBuilder.toString(request, ToStringStyle.SIMPLE_STYLE));
        CommonResponse<LoginAccount> response = loginAccountService.loginByPass(body.getMobile(), body.getPassword(), session,request);
        log.info("密码登陆完成：{}", ReflectionToStringBuilder.toString(response, ToStringStyle.SIMPLE_STYLE));
        return response;
    }

    @RequestMapping(value = "loginByCode",method = {RequestMethod.POST})
    @ResponseBody
    public CommonResponse<LoginAccount> loginByCode(@ValidateBody(requiredAttrs = {"mobile", "verifyCode"}) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) {
        log.info("验证码登陆开始：入参：body:{},request:{}", ReflectionToStringBuilder.toString(body, ToStringStyle.SIMPLE_STYLE),ReflectionToStringBuilder.toString(request, ToStringStyle.SIMPLE_STYLE));
        CommonResponse<LoginAccount> response  = loginAccountService.loginByVerifyCode(body.getMobile(), body.getVerifyCode(), session,request);
        log.info("验证码登陆完成：{}", ReflectionToStringBuilder.toString(response, ToStringStyle.SIMPLE_STYLE));
        return response;
    }

    @RequestMapping("getRegVerifyCode")
    @ResponseBody
    public CommonResponse<String> getRegVerifyCode(@ValidateBody(requiredAttrs = "mobile") LoginAccountRequest body, RedisRequestSession session) {
        log.info("获取注册验证码开始：入参：body:{}", ReflectionToStringBuilder.toString(body, ToStringStyle.SIMPLE_STYLE));
        CommonResponse<String> response = loginAccountService.getRegVerifyCode(body.getMobile(), session);
        log.info("获取注册验证码完成：{}", ReflectionToStringBuilder.toString(response, ToStringStyle.SIMPLE_STYLE));
        return response;
    }

    @RequestMapping("getLoginVerifyCode")
    @ResponseBody
    public CommonResponse<String> getLoginVerifyCode(@ValidateBody(requiredAttrs = "mobile") LoginAccountRequest body, RedisRequestSession session) {
        log.info("获取登陆验证码开始：入参：body:{}", ReflectionToStringBuilder.toString(body, ToStringStyle.SIMPLE_STYLE));
        CommonResponse<String> response = loginAccountService.getLoginVerifyCode(body.getMobile(), session);
        log.info("获取登陆验证码完成：{}", ReflectionToStringBuilder.toString(response, ToStringStyle.SIMPLE_STYLE));
        return response;
    }

    @RequestMapping("reg")
    @ResponseBody
    public CommonResponse<LoginAccount> reg(@ValidateBody(requiredAttrs = {"mobile","verifyCode"}) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        log.info("注册开始：入参：body:{},request:{}", ReflectionToStringBuilder.toString(body, ToStringStyle.SIMPLE_STYLE),ReflectionToStringBuilder.toString(request, ToStringStyle.SIMPLE_STYLE));
        CommonResponse<LoginAccount> response = loginAccountService.registerAccount(body, session,request);
        log.info("注册完成：{}", ReflectionToStringBuilder.toString(response, ToStringStyle.SIMPLE_STYLE));
        return response;
    }

    @RequestMapping("logout")
    @ResponseBody
    public CommonResponse<LoginAccount> logout(@ValidateBody(requiredAttrs = {"mobile"}) LoginAccountRequest body, RedisRequestSession session) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CommonResponse<LoginAccount> response = loginAccountService.logoutAccount(body, session);
        return response;
    }

    @RequestMapping("update")
    @ResponseBody
    public CommonResponse<LoginAccount> updateInfo(@ValidateBody(requiredAttrs = {"mobile"}) LoginAccountRequest body, RedisRequestSession session,CommonRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        log.info("更新用户信息开始：入参：body:{},request:{}", ReflectionToStringBuilder.toString(body, ToStringStyle.SIMPLE_STYLE),ReflectionToStringBuilder.toString(request, ToStringStyle.SIMPLE_STYLE));
        CommonResponse<LoginAccount> response = loginAccountService.updateAccount(body, session,request);
        log.info("更新用户信息完成：{}", ReflectionToStringBuilder.toString(response, ToStringStyle.SIMPLE_STYLE));
        return response;
    }
}
