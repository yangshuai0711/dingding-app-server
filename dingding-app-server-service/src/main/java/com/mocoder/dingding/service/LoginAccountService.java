package com.mocoder.dingding.service;

import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.utils.bean.RedisRequestSession;

/**
 * 登录账户服务
 * Created by yangshuai3 on 2016/1/25.
 */

public interface LoginAccountService {

    /**
     * 密码登录
     * @param mobile
     * @param password
     * @param session
     * @param request
     * @return
     */
    public CommonResponse<LoginAccount> loginByPass(String mobile, String password, RedisRequestSession session, CommonRequest request);

    /**
     * 验证码登录
     * @param mobile
     * @param verifyCode
     * @param session
     * @param request
     * @return
     */
    public CommonResponse<LoginAccount> loginByVerifyCode(String mobile, String verifyCode, RedisRequestSession session, CommonRequest request);

    /**
     * 注册
     * @param account
     * @param session
     * @param request
     * @return
     */
    public CommonResponse<LoginAccount> registerAccount(LoginAccount account, RedisRequestSession session, CommonRequest request);

    /**
     * 获取注册验证码
     * @param mobile
     * @param session
     * @return
     */
    public CommonResponse<String> getRegVerifyCode(String mobile, RedisRequestSession session);

    /**
     * 获取登录验证码
     * @param mobile
     * @param session
     * @return
     */
    public CommonResponse<String> getLoginVerifyCode(String mobile, RedisRequestSession session);
}
