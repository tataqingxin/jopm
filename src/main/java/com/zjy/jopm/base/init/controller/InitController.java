package com.zjy.jopm.base.init.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.init.service.InitService;

/**
 * 
 * @ClassName: InitController 
 * @Description: [初始化] 
 * @author xuanx
 * @date 2016年5月17日 上午10:31:33 
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/initController")
public class InitController extends BaseController{
	
	@Autowired
	private InitService initService;
	
	
	@RequestMapping(value = "/InitDataInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson InitDataInfo(HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j=new AjaxJson();
		String orgName=request.getParameter("orgName");
		String orgCode=request.getParameter("orgCode");
		String userName=request.getParameter("userName");
		String passWord=request.getParameter("password");
		j=initService.InitDataInfo(orgName,orgCode,userName,passWord);
		return j;
	}
	
	
}
