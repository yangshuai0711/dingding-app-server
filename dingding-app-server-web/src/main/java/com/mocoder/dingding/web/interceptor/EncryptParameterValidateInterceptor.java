package com.mocoder.dingding.web.interceptor;

import com.mocoder.dingding.constants.EncryptionConstant;
import com.mocoder.dingding.constants.RequestAttributeKeyConstant;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.utils.web.WebUtil;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.vo.CommonRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 加密参数请求拦截器(重要或者限制性操作如获取短信验证码等，不需要登录)
 * Created by yangshuai3 on 2016/1/28.
 */
public class EncryptParameterValidateInterceptor extends BaseParamValidateInterceptor {

    @Override
    protected boolean validate(HttpServletRequest request, HttpServletResponse response) {
        if(!super.validate(request,response)){
            return false;
        }
        CommonRequest req = (CommonRequest) request.getAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_COMMON_REQUEST);
        CommonResponse resp = new CommonResponse();
        if(StringUtils.isBlank(req.getSign())){
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
            resp.setMsg("参数sign不能为空");
            try {
                WebUtil.writeResponse(response, resp);
            } catch (IOException e1) {
                logger.error("请求拦截器:写出返回值失败",e1);
            }
            return false;
        }
        if(StringUtils.isBlank(req.getBody())){
            req.setBody("");
        }
        String tmpStr = req.getBody()+req.getTimestamp()+req.getSessionid()+ EncryptionConstant.PUBLIC_ENCRYPTION_KEY;
        String targetToken = null;
        try {
            targetToken = DigestUtils.md5DigestAsHex(tmpStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("请求拦截器:不支持的字符编码格式utf-8",e);
            resp.resolveErrorInfo(ErrorTypeEnum.SYSTEM_ENV_EXCEPTION);
            resp.setMsg("服务器不支持字符编码格式utf-8");
            try {
                WebUtil.writeResponse(response, resp);
            } catch (IOException e1) {
                logger.error("请求拦截器:写出返回值失败",e);
            }
            return false;
        }
        if(!req.getSign().equals(targetToken)){
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
            resp.setMsg("参数sign验证失败");
            try {
                WebUtil.writeResponse(response, resp);
            } catch (IOException e1) {
                logger.error("请求拦截器:写出返回值失败",e1);
            }
            return false;
        }
        return true;
    }
}
