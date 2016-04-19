package com.mocoder.dingding.service.impl;

import com.mocoder.dingding.constants.SessionKeyConstant;
import com.mocoder.dingding.dao.LoginAccountMapper;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.model.LoginAccountCriteria;
import com.mocoder.dingding.rpc.SmsServiceWrap;
import com.mocoder.dingding.service.LoginAccountService;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.utils.encryp.EncryptUtils;
import com.mocoder.dingding.utils.web.JsonUtil;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.LoginAccountRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by yangshuai3 on 2016/1/26.
 */
@Service
public class LoginAccountServiceImpl implements LoginAccountService {

    private static final Logger log = LogManager.getLogger(LoginAccountServiceImpl.class);

    @Resource
    private SmsServiceWrap smsServiceWrap;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private LoginAccountMapper loginAccountMapper;

    @Override
    public CommonResponse<LoginAccount> loginByPass(String mobile, String password, RedisRequestSession session, CommonRequest request) {

        CommonResponse<LoginAccount> response = new CommonResponse<LoginAccount>();
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.LOGIN_DUPLICATE_ERROR);
            return response;
        }
        LoginAccount record = queryLoginAccounts(mobile);
        if (record != null) {
            if (EncryptUtils.md5(password).equals(record.getPassword())) {
                if(record.getPassword()!=null){
                    record.setPassword("Y");
                }else{
                    record.setPassword("N");
                }
                try {
                    session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, JsonUtil.toString(record));
                    session.setAttribute(SessionKeyConstant.LOGIN_TYPE_KEY, "pass");
                } catch (IOException e) {
                    log.error("用户验证码登陆-失败：设置session异常", e);
                    response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
                    return  response;
                }
                session.removeAttribute(SessionKeyConstant.VERIFY_CODE_KEY);
                session.saveUserSessionId(request, record.getMobile());
                response.setData(record);
                response.setCode("0");
                response.setMsg("登录成功");
                return response;
            } else {
                response.resolveErrorInfo(ErrorTypeEnum.PASS_LOGIN_ERROR);
                return response;
            }
        } else {
            response.resolveErrorInfo(ErrorTypeEnum.ACCOUNT_NOT_EXISTS);
            return response;
        }
    }

    private LoginAccount queryLoginAccounts(String mobile) {
        LoginAccountCriteria example = new LoginAccountCriteria();
        example.createCriteria().andMobileEqualTo(mobile);
        List<LoginAccount> list = loginAccountMapper.selectByExample(example);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public CommonResponse<LoginAccount> loginByVerifyCode(String mobile, String verifyCode, RedisRequestSession session, CommonRequest request) {
        CommonResponse<LoginAccount> response = new CommonResponse<LoginAccount>();
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.LOGIN_DUPLICATE_ERROR);
            return response;
        }
        String code = session.getAttribute(SessionKeyConstant.VERIFY_CODE_KEY);
        if (verifyCode != null && verifyCode.equals(code)) {
            LoginAccount record = queryLoginAccounts(mobile);
            if (record != null) {
                if(record.getPassword()!=null){
                    record.setPassword("Y");
                }else{
                    record.setPassword("N");
                }
                response.setData(record);
                response.setCode("0");
                response.setMsg("登录成功");
                try {
                    session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, JsonUtil.toString(record));
                    session.setAttribute(SessionKeyConstant.LOGIN_TYPE_KEY, "code");
                } catch (IOException e) {
                    log.error("用户验证码登陆-失败：设置session异常", e);
                    response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
                    return  response;
                }
                session.removeAttribute(SessionKeyConstant.VERIFY_CODE_KEY);
                session.saveUserSessionId(request, record.getMobile());
                return response;
            } else {
                response.resolveErrorInfo(ErrorTypeEnum.ACCOUNT_NOT_EXISTS);
                return response;
            }
        } else {
            response.resolveErrorInfo(ErrorTypeEnum.VALIDATE_CODE_ERROR);
            return response;
        }
    }

    @Override
    public CommonResponse<LoginAccount> registerAccount(LoginAccountRequest body, RedisRequestSession session, CommonRequest request) {
        CommonResponse<LoginAccount> response = new CommonResponse<LoginAccount>();
        String code = session.getAttribute(SessionKeyConstant.VERIFY_CODE_KEY);
        if (!body.getVerifyCode().equals(code)) {
            response.resolveErrorInfo(ErrorTypeEnum.VALIDATE_CODE_ERROR);
            return response;
        }
        LoginAccount account = new LoginAccount();
        try {
            PropertyUtils.copyProperties(account, body);
        } catch (Exception e) {
            response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
        }
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.NOT_LOG_OUT_ERROR);
            return response;
        }
        LoginAccount record = queryLoginAccounts(account.getMobile());
        if (record != null) {
            response.resolveErrorInfo(ErrorTypeEnum.REG_DUPLICATE_ERROR);
            return response;
        }
        if(StringUtils.isNotBlank(body.getPassword())) {
            account.setPassword(EncryptUtils.md5(body.getPassword()));
        }
        loginAccountMapper.insertSelective(account);
        if(account.getPassword()!=null){
            account.setPassword("Y");
        }else{
            account.setPassword("N");
        }
        try {
            session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, JsonUtil.toString(account));
            session.setAttribute(SessionKeyConstant.LOGIN_TYPE_KEY, "pass");
        } catch (IOException e) {
            session.removeAttribute(SessionKeyConstant.USER_LOGIN_KEY);
            log.error("用户注册-成功：设置session异常",e);
        }
        session.removeAttribute(SessionKeyConstant.VERIFY_CODE_KEY);
        session.saveUserSessionId(request, account.getMobile());
        response.setData(account);
        response.setCode("0");
        response.setMsg("注册成功");
        return response;
    }

    @Override
    public CommonResponse<String> getRegVerifyCode(String mobile, RedisRequestSession session) {
        CommonResponse<String> response = new CommonResponse<String>();
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.NOT_LOG_OUT_ERROR);
            return response;
        }
        LoginAccount record = queryLoginAccounts(mobile);
        if (record != null) {
            response.resolveErrorInfo(ErrorTypeEnum.REG_DUPLICATE_ERROR);
            return response;
        }
        Random random = new Random();
        String code = String.valueOf(1000 + random.nextInt(8999));
        session.setAttribute(SessionKeyConstant.VERIFY_CODE_KEY, code);
        if (smsServiceWrap.sentRegValidCodeSms(mobile, code)) {
            response.setCode("0");
            return response;
        } else {
            response.resolveErrorInfo(ErrorTypeEnum.SMS_SEND_ERROR);
            return response;
        }
    }

    @Override
    public CommonResponse<String> getLoginVerifyCode(String mobile, RedisRequestSession session) {
        CommonResponse<String> response = new CommonResponse<String>();
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.LOGIN_DUPLICATE_ERROR);
            return response;
        }
        LoginAccount record = queryLoginAccounts(mobile);
        if (record != null) {
            Random random = new Random();
            String code = String.valueOf(1000 + random.nextInt(8999));
            session.setAttribute(SessionKeyConstant.VERIFY_CODE_KEY, code);
            if (smsServiceWrap.sentRegValidCodeSms(mobile, code)) {
                response.setCode("0");
                return response;
            } else {
                response.resolveErrorInfo(ErrorTypeEnum.SMS_SEND_ERROR);
                return response;
            }
        } else {
            response.resolveErrorInfo(ErrorTypeEnum.ACCOUNT_NOT_EXISTS);
            return response;
        }
    }

    @Override
    public CommonResponse<LoginAccount> logoutAccount(LoginAccountRequest body, RedisRequestSession session) {
        CommonResponse response = new CommonResponse();
        try {
            session.destroy();
        } catch (Exception e) {
            response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
            log.error("账户注销-异常：销毁session失败，redis操作异常", e);
            return response;
        }
        response.setCode("0");
        response.setMsg("注销成功");
        return response;
    }

    @Override
    public CommonResponse<LoginAccount> updateAccount(LoginAccountRequest body, RedisRequestSession session, CommonRequest request) {
        CommonResponse<LoginAccount> response = new CommonResponse<LoginAccount>();
//        String code = session.getAttribute(SessionKeyConstant.VERIFY_CODE_KEY, String.class);
//        if (!body.getVerifyCode().equals(code)) {
//            response.resolveErrorInfo(ErrorTypeEnum.VALIDATE_CODE_ERROR);
//            return response;
//        }
        //用户权限验证
        LoginAccount login = null;
        try {
            login = JsonUtil.toObject(session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY), LoginAccount.class);
        } catch (IOException e) {
            log.error("更新登陆账户-异常：反序列化redis值失败",e);
            response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
            return response;
        }
        if (login == null||!login.getMobile().equals(body.getMobile())) {
            response.resolveErrorInfo(ErrorTypeEnum.USER_OPERATE_NOT_PERMIT);
            return response;
        }
        LoginAccountCriteria example = new LoginAccountCriteria();
        example.createCriteria().andMobileEqualTo(body.getMobile());
        LoginAccount record = loginAccountMapper.selectByExample(example).get(0);
        LoginAccount account = new LoginAccount();
        try {
            PropertyUtils.copyProperties(account, body);
        } catch (Exception e) {
            log.error("更新登陆账户-异常：属性拷贝异常",e);
            response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
            return response;
        }
        if(StringUtils.isNotBlank(body.getPassword())) {
            account.setPassword(EncryptUtils.md5(body.getPassword()));
            if(StringUtils.isNotBlank(record.getPassword())&&(StringUtils.isBlank(body.getOldPassword())||!record.getPassword().equals(EncryptUtils.md5(body.getOldPassword())))&&!"code".equals(session.getAttribute(SessionKeyConstant.LOGIN_TYPE_KEY))){
                response.setCode("50");
                response.setMsg("当前密码输入不正确");
                return response;
            }
        }
        account.setMobile(null);
        account.setId(null);
        loginAccountMapper.updateByExampleSelective(account, example);
        record = loginAccountMapper.selectByExample(example).get(0);
        if(record.getPassword()!=null) {
            record.setPassword("Y");
        }else{
            record.setPassword("N");
        }
        account.setMobile(body.getMobile());
        try {
            session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY,JsonUtil.toString( record));
            session.setAttribute(SessionKeyConstant.LOGIN_TYPE_KEY, "pass");
        } catch (IOException e) {
            log.error("更新登陆账户-异常：反序列化redis值失败",e);
            session.removeAttribute(SessionKeyConstant.USER_LOGIN_KEY);
        }
        response.setData(record);
        response.setCode("0");
        response.setMsg("修改成功");
        return response;
    }

    @Override
    public CommonResponse<String> getImportantOperationVerifyCode(String mobile, RedisRequestSession session) {
        CommonResponse<String> response = new CommonResponse<String>();
        Random random = new Random();
        String code = String.valueOf(1000 + random.nextInt(8999));
        session.setAttribute(SessionKeyConstant.VERIFY_CODE_KEY, code);
        if (smsServiceWrap.sentRegValidCodeSms(mobile, code)) {
            response.setCode("0");
            return response;
        } else {
            response.resolveErrorInfo(ErrorTypeEnum.SMS_SEND_ERROR);
            return response;
        }
    }
}
