package com.zjy.jopm.base.app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unist.util.StringUtil;
import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.service.ServiceAuthService;
import com.zjy.jopm.base.common.Constants;

@Controller
@RequestMapping("/serviceAuthController")
public class ServiceAuthController extends BaseController{

	@Autowired
	private ServiceAuthService serviceAuthService;
	
	@RequestMapping(value = "/applicationList")
	@ResponseBody
	public Map<String, Object> applicationList(ApplicationEntity applicationEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		int pageNo = 1;
		if(StringUtil.isNotEmpty(request.getParameter("pager.pageNo"))){
			pageNo = Integer.parseInt(request.getParameter("pager.pageNo"));
		}
		int pageSize = 10;
		if(StringUtil.isNotEmpty(request.getParameter("pager.pageSize"))){
			pageSize = Integer.parseInt(request.getParameter("pager.pageSize"));
		}
		
		String sort = request.getParameter("sort");
		if(StringUtil.isEmpty(sort)){
			sort = "id";
		}
		
		String direction = request.getParameter("direction");
		if(StringUtil.isEmpty(direction)){
			direction = Constants.DESC;
		}
		return this.serviceAuthService.getApplicationQuiGrid(applicationEntity, pageNo, pageSize, sort, direction);
	}
	
	@RequestMapping("/insertServiceAuth")
	@ResponseBody
	public AjaxJson insertServiceAuth(String serviceId,String applicationIds,HttpServletRequest request) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(serviceId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误，请稍后重试！");
			return ajaxJson;
		}
		/*if(StringUtil.isEmpty(applicationIds)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误，请稍后重试！");
		}*/
		ajaxJson = this.serviceAuthService.insertServiceAuth(serviceId, applicationIds);
		return ajaxJson;
	}
	
	@RequestMapping("/getAppServiceTree")
	@ResponseBody
	public AjaxJson getAppServiceTree(HttpServletRequest request, HttpServletResponse response) throws JumpException{
		String applicationId = request.getParameter("id");// 节点类型决定id代表applicationId OR functionId
		
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		return this.serviceAuthService.getAppServiceTree(applicationId, path);
	}
	
	@RequestMapping("/getServiceAuthList")
	@ResponseBody
	public AjaxJson getServiceAuthList(HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		String serviceId = request.getParameter("serviceId");// 节点类型决定id代表applicationId OR functionId
		if(StringUtil.isEmpty(serviceId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误，请重新尝试！");
		}else{
			ajaxJson = this.serviceAuthService.getServiceAuthList(serviceId);
		}
		return ajaxJson;
	}
	
	
}
