package com.mocoder.dingding.web.interceptor;

import com.mocoder.dingding.constants.RequestAttributeKeyConstant;
import com.mocoder.dingding.constants.SessionKeyConstant;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.utils.web.WebUtil;
import com.mocoder.dingding.vo.CommonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 限制型公开资源请求拦截器(短信验证码等不需要登录的)
 * Created by yangshuai3 on 2016/1/28.
 */
public class LoginStatusInterceptor extends EncryptParameterValidateInterceptor {

    @Override
    protected boolean validate(HttpServletRequest request, HttpServletResponse response) {
        if (!super.validate(request, response)) {
            return false;
        }
        RedisRequestSession session = (RedisRequestSession) request.getAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_REQUEST_SESSION);
        CommonResponse resp = new CommonResponse();
        LoginAccount user = session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, LoginAccount.class);
        resp.resolveErrorInfo(ErrorTypeEnum.NOT_LOGIN);
        try {
            WebUtil.writeResponse(response, resp);
        } catch (IOException e) {
            logger.error("登录拦截器：写出登录结果异常",e);
        }
        return true;
    }
}
