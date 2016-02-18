package com.mocoder.dingding.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * XXX控制器
 * author: wenjun
 * Time: 2015.03.11 09:47
 */
@Controller
@RequestMapping("/main")
public class IndexController{
	
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

}
