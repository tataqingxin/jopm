package com.zjy.jopm.base.app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.service.AppMirrorService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

@Controller
@RequestMapping("/appMirrorController")
public class AppMirrorController extends BaseController {
	
	@Autowired
	private AppMirrorService appMirrorService;
	
	@RequestMapping(value = "/appMirrorList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> appMirrorList(AppMirrorEntity appMirrior, HttpServletRequest request, HttpServletResponse response) throws JumpException {
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
		
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isNotEmpty(organizationId)){
			OrganizationEntity organization = new OrganizationEntity();
			organization.setId(organizationId);
			appMirrior.setOrganizationEntity(organization);
		}
		
		String appId = request.getParameter("applicationId");
		if(StringUtil.isNotEmpty(appId)){
			ApplicationEntity application = new ApplicationEntity();
			application.setId(appId);
			appMirrior.setApplicationEntity(application);
		}
		
		return this.appMirrorService.getAppMirrorQuiGrid(appMirrior, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @param appMirrior
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertAppMirror", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertAppMirror(@RequestParam(value="orgIds",required=true) String orgIds, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String appId = request.getParameter("appId");
		if(StringUtil.isEmpty(appId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.appMirrorService.addAppMirror(orgIds,appId);
	}
	
	@RequestMapping(value = "/updateAppMirror", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateAppMirror(AppMirrorEntity appMirrior, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isEmpty(organizationId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String appId = request.getParameter("applicationId");
		if(StringUtil.isEmpty(appId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		appMirrior.setOrganizationEntity(organizationEntity);
		
		ApplicationEntity application = new ApplicationEntity();
		application.setId(appId);
		appMirrior.setApplicationEntity(application);;
		
		return this.appMirrorService.updateAppMirror(appMirrior);
	}
	
	/**
	 * 
	 * @Title: detailAppMirror 
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/detailAppMirror", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailAppMirror(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.appMirrorService.expandAppMirror(id);
	}
	
	/**
	 * 
	 * @Title: deleteAppMirror 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteAppMirror", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteAppMirror(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.appMirrorService.deleteAppMirror(id);
	}
	
	/**
	 * 
	* @Title: OrganizationServiceTree 
	* @Description:机构树
	* @param @param organizationEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return List<TreeNode> 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/organizationTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson organizationTree(OrganizationEntity organizationEntity,HttpServletRequest request, HttpServletResponse response){
		//获取当前登录用户
		String orgId = request.getParameter("orgId");
		SessionInfo sessionInfo = (SessionInfo)ContextHolderUtil.getSession().getAttribute(Globals.USER_SESSION);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.appMirrorService.OrganizationServiceTree(organizationEntity,sessionInfo,orgId);
	}
	
	/**
	 * 
	 * @Title: getOrgList 
	 * @Description: [已经被开通的机构]
	 * @param roleId
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/getOrgList", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson getOrgList(@RequestParam String appId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		String orgId = request.getParameter("orgId");
		if(StringUtil.isEmpty(appId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		return this.appMirrorService.getOrgList(appId,  orgId);
	}
	
	/**
	 * 
	 * @Title: permissionTree 
	 * @Description: [角色权限树]
	 * @param roleId
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/appMirrorCancel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson appMirrorCancel(@RequestParam String appId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(appId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.appMirrorService.appMirrorCancel(appId);
	}

}
