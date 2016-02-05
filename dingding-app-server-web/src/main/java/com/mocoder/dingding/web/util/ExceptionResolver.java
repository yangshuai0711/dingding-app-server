package com.mocoder.dingding.web.util;

import com.mocoder.dingding.enums.ErrorTypeEnum;
import com.mocoder.dingding.utils.web.WebUtil;
import com.mocoder.dingding.vo.CommonResponse;
import com.mocoder.dingding.web.exception.ParameterValidateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yangshuai3 on 2016/2/4.
 */
public class ExceptionResolver extends DefaultHandlerExceptionResolver{
    private static final Logger log = LogManager.getLogger(ExceptionResolver.class);
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if(ex instanceof ParameterValidateException){
                WebUtil.writeResponse(response,((ParameterValidateException) ex).getResponse());
            }else{
                log.error("异常处理器:未知程序异常:request:"+request.getRequestURI(),ex);
                CommonResponse commonResp = new CommonResponse();
                commonResp.resolveErrorInfo(ErrorTypeEnum.SYSTEM_EXCEPTION);
                commonResp.setMsg(ex.getMessage());
                WebUtil.writeResponse(response,commonResp);
            }
            return new ModelAndView();
        } catch (IOException e) {
            log.error("异常处理器：写出返回值异常",e);
        }
        return super.doResolveException(request, response, handler, ex);
    }
}
