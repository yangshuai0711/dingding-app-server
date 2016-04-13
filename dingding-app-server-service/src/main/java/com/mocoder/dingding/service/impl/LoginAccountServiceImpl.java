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
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.LoginAccountRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.LOGIN_DUPLICATE_ERROR);
            return response;
        }
        LoginAccount record = queryLoginAccounts(mobile);
        if (record != null) {
            if (EncryptUtils.md5(password).equals(record.getPassword())) {
                record.setPassword(null);
                session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, record);
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
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class) != null) {
            response.resolveErrorInfo(ErrorTypeEnum.LOGIN_DUPLICATE_ERROR);
            return response;
        }
        String code = session.getAttribute(SessionKeyConstant.VERIFY_CODE_KEY, String.class);
        if (verifyCode != null && verifyCode.equals(code)) {
            LoginAccount record = queryLoginAccounts(mobile);
            if (record != null) {
                record.setPassword(null);
                response.setData(record);
                response.setCode("0");
                response.setMsg("登录成功");
                session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, record);
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
        String code = session.getAttribute(SessionKeyConstant.VERIFY_CODE_KEY, String.class);
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
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class) != null) {
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
        account.setPassword(null);
        session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, account);
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
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class) != null) {
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
        if (session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class) != null) {
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
        String code = session.getAttribute(SessionKeyConstant.VERIFY_CODE_KEY, String.class);
        if (!body.getVerifyCode().equals(code)) {
            response.resolveErrorInfo(ErrorTypeEnum.VALIDATE_CODE_ERROR);
            return response;
        }
        LoginAccount login = session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class);
        if (login == null||!login.getMobile().equals(body.getMobile())) {
            response.resolveErrorInfo(ErrorTypeEnum.USER_OPERATE_NOT_PERMIT);
            return response;
        }
        LoginAccount account = new LoginAccount();
        try {
            PropertyUtils.copyProperties(account, body);
        } catch (Exception e) {
            response.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
        }
        if(StringUtils.isNotBlank(body.getPassword())) {
            account.setPassword(EncryptUtils.md5(body.getPassword()));
        }
        account.setMobile(null);
        LoginAccountCriteria example = new LoginAccountCriteria();
        example.createCriteria().andMobileEqualTo(account.getMobile());
        loginAccountMapper.updateByExampleSelective(account, example);
        account.setPassword(null);
        account.setMobile(body.getMobile());
        session.setAttribute(SessionKeyConstant.USER_LOGIN_KEY, account);
        session.removeAttribute(SessionKeyConstant.VERIFY_CODE_KEY);
        response.setData(account);
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
