package com.mocoder.dingding.web.controller;

import com.mocoder.dingding.dao.LoginAccountMapper;
import com.mocoder.dingding.model.LoginAccountCriteria;
import com.mocoder.dingding.utils.bean.RedisRequestSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * XXX控制器
 * author: wenjun
 * Time: 2015.03.11 09:47
 */
@Controller
@RequestMapping("/test")
public class IndexController{

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Resource
	private LoginAccountMapper loginAccountMapper;
	
	private String vmRootPath = "biz-index/";
	
	/**
	 * 主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,	HttpServletResponse response, ModelMap model, String orderNo) {
		String result = vmRootPath.concat("index");
		return result;
	}

	@RequestMapping("del")
	@ResponseBody
	public String del(String mobile){
		try {
			RedisRequestSession.cleanUserAllDeviceSession(mobile);
			LoginAccountCriteria example = new LoginAccountCriteria();
			example.createCriteria().andMobileEqualTo(mobile);
			if(loginAccountMapper.deleteByExample(example)>0){
                return "ok";
            }
		} catch (Exception e) {
			e.printStackTrace();
			return  e.toString()+Arrays.toString(e.getStackTrace());
		}
		return "fail";
	}

}
