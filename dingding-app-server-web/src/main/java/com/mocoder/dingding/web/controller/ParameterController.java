package com.mocoder.dingding.web.controller;

import com.mocoder.dingding.constants.RedisKeyConstant;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import com.mocoder.dingding.utils.encryp.EncryptUtils;
import com.mocoder.dingding.utils.web.RedisUtil;
import com.mocoder.dingding.vo.CommonRequest;
import com.mocoder.dingding.vo.CommonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * 登录账户相关功能的控制器，包括登录、注册、获取验证码等，其中获取验证码也对应不同的入口，这样是为了区分开是哪个功能调用的获取验证码，从来增加一些条件判断
 */
@Controller
@RequestMapping("/param/")
public class ParameterController {

    @RequestMapping("getSessionId")
    @ResponseBody
    public CommonResponse<String> getSessionId(CommonRequest request,RedisRequestSession session){
        CommonResponse<String> response = new CommonResponse<String>();
        String uuid = UUID.randomUUID().toString();
        String digested = EncryptUtils.md5(request.getDeviceid());
        String sessionId=new StringBuffer(digested.substring(3,7)).append('-').append(digested.substring(9, 13)).append('-').append(uuid).toString();
        //临时保存sessioId，用于验证用户请求使用的sessionId为系统分配的
        RedisUtil.setString(RedisKeyConstant.TEMP_SESSION_ID_PREFIX+sessionId,"ok",60*30L);
        response.setCode("0");
        response.setData(sessionId);
        return response;
    }
}
