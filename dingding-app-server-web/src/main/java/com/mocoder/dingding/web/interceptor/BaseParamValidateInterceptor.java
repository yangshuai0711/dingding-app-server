package com.mocoder.dingding.web.interceptor;

import com.mocoder.dingding.constants.*;
import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.utils.encryp.EncryptUtils;
import com.mocoder.dingding.utils.web.RedisUtil;
import com.mocoder.dingding.utils.web.WebUtil;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 基本参数拦截器
 * Created by yangshuai3 on 2016/1/28.
 */
public class BaseParamValidateInterceptor extends ValidatorInterceptor {

    @Value("${app.session.timeout.days}")
    private long appSessionExpireDays;

    @Override
    protected boolean validate(HttpServletRequest request, HttpServletResponse response) {
        CommonRequest req = null;
        try {
            req = WebUtil.HeaderToSimpleBean(request, CommonRequest.class);
        } catch (Exception e){
            logger.error("参数拦截器:获取请求头内容失败",e);
        }
        CommonResponse<String> resp = null;
        if(req==null){
            resp = new CommonResponse();
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_PARSE_ERROR);
            resp.setMsg("获取请求头内容失败");
        }else if(req.getAppversion()==null|| !AppVersionConstant.VERSION_1_0_0.equals(req.getAppversion())){
            resp = new CommonResponse();
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
            resp.setMsg("参数appVersion取值不正确");
        }else if(req.getPlatform()==null|| (!PlatformConstant.ANDROID.equals(req.getPlatform())&&!PlatformConstant.IOS.equals(req.getPlatform())&&!PlatformConstant.HTML5.equals(req.getPlatform()))){
            resp = new CommonResponse();
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
            resp.setMsg("参数platform取值不正确");
        }else if(!validateTimeStamp(req.getTimestamp())){
            resp = new CommonResponse();
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
            resp.setMsg("参数timeStamp取值不正确");
        }else if(!validateDeviceId(req.getDeviceid())){
            resp = new CommonResponse();
            resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_VALIDATE_ERROR);
            resp.setMsg("参数deviceId取值不正确");
        } else if(!validateSessionId(request,req)){
                resp = new CommonResponse();
                resp.resolveErrorInfo(ErrorTypeEnum.INPUT_PARAMETER_SESSION_ERROR);
                resp.setData(UUID.randomUUID().toString());
                resp.setMsg("参数sessionId取值不正确");
        }
        if(resp!=null){
            try {
                WebUtil.writeResponse(response, resp);
            } catch (IOException e) {
                logger.error("参数拦截器:写出返回值失败", e);
            }
            return false;
        }
        req.setBody(request.getParameter("body"));
        request.setAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_COMMON_REQUEST,req);
        return true;
    }

    private boolean validateSessionId(HttpServletRequest request, CommonRequest req) {
        String uri = request.getRequestURI().replace(request.getContextPath(),"");
        if("/param/getSessionId".equals(uri)) {
            return true;
        }
        String sessionId = req.getSessionid();
        if(sessionId==null){
            return false;
        }
        String digested = EncryptUtils.md5(req.getDeviceid());
        String prefix = new StringBuffer(digested.substring(3,7)).append('-').append(digested.substring(9, 13)).toString();
        if(!sessionId.startsWith(prefix)){
            return false;
        }
        RedisRequestSession session = new RedisRequestSession(sessionId, appSessionExpireDays *24*60);
        if(RedisUtil.getString(RedisKeyConstant.TEMP_SESSION_ID_PREFIX+sessionId,null)!=null){
            request.setAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_REQUEST_SESSION,session);
            return true;
        }
        if(session.getAttribute(SessionKeyConstant.USER_LOGIN_KEY, Object.class)!=null){
            request.setAttribute(RequestAttributeKeyConstant.REQUEST_ATTRIBUTE_KEY_REQUEST_SESSION,session);
            return true;
        }
        return false;
    }

    private boolean validateDeviceId(String deviceid) {
        if(!PlatformConstant.HTML5.equals(deviceid)&&deviceid==null){
            return false;
        }

        return true;
    }

    private boolean validateTimeStamp(String timeStamp){
        if(timeStamp==null){
            return false;
        }
        Long longValue = null;
        try {
             longValue = Long.valueOf(timeStamp);
        } catch (NumberFormatException e) {

        }
        if(longValue==null){
            return false;
        }
//        long now = new Date().getTime();
//        if(Math.abs(now-longValue)>1000*60*60*24*180){
//            return false;
//        }
        return true;
    }
}
