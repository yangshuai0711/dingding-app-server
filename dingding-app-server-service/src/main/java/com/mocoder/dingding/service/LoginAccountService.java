package com.mocoder.dingding.service;

import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.vo.LoginAccountRequest;

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
    public CommonResponse<LoginAccount> registerAccount(LoginAccountRequest account, RedisRequestSession session, CommonRequest request);

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

    /**
     * 注销当前设备的当前账户
     * @param body
     * @param session
     * @return
     */
    CommonResponse<LoginAccount> logoutAccount(LoginAccountRequest body, RedisRequestSession session);
    /**
     * 更新账户信息
     * @param account
     * @param session
     * @param request
     * @return
     */
    public CommonResponse<LoginAccount> updateAccount(LoginAccountRequest account, RedisRequestSession session, CommonRequest request);
    /**
     * 获取重要操作验证码
     * @param mobile
     * @param session
     * @return
     */
    public CommonResponse<String> getImportantOperationVerifyCode(String mobile, RedisRequestSession session);
}
