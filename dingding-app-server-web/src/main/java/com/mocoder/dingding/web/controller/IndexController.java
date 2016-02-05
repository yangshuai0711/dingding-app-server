package com.mocoder.dingding.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;

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

    public static void main(String[] args) throws UnsupportedEncodingException {
        String res = DigestUtils.md5DigestAsHex("123".getBytes("utf-8"));
        System.out.printf(res);
    }
}
