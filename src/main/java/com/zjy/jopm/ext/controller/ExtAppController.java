package com.zjy.jopm.ext.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zjy.jopm.base.app.service.ServiceAuthService;
import com.zjy.jopm.ext.service.AppDataService;

@Controller
@RequestMapping("extAppController")
public class ExtAppController {

	@Autowired
	private AppDataService extappService;
	@Autowired
	private ServiceAuthService serviceAuthService;
	
	/**
	 * 获取当前可用的应用标识符
	 * @return
	 */
	@RequestMapping(value = "/appInstanceCodeList")
	@ResponseBody
	public List<String> getAppInstanceCodeList(){
		
		return extappService.getAppInstanceCode();
	}
	
	/**
	 * 返回INSTANCE_CODE以及URL
	 * @return
	 */
	@RequestMapping(value = "/getAppInstanceCodeAndUrl")
	@ResponseBody
	public List<Map<String,Object>> getAppInstanceCodeAndUrl(){
		
		return serviceAuthService.getAppInstanceCodeAndUrl();
	}
}
