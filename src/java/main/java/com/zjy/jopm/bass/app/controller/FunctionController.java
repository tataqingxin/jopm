/** 
 * @Description:[功能管理功能-菜单]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.controller.FunctionController.java
 * @ClassName:FunctionController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:48:50
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午2:48:50  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
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
import com.unistc.core.common.model.SortDirection;
import com.unistc.exception.JumpException;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.service.FunctionService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.icon.entity.IconEntity;

/**
 * @ClassName: FunctionController 
 * @Description: [功能管理功能-菜单] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:48:50 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/app/functionController")
public class FunctionController extends BaseController {
	
	@Autowired
	private FunctionService functionService;
	
	/**
	 * 
	 * @Title: serviceInterfaceList 
	 * @Description: [功能列表]
	 * @param functionEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/functionList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> serviceInterfaceList(FunctionEntity functionEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException {
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
			direction = SortDirection.desc.toString();
		}
		
		String applicationId = request.getParameter("applicationId");
		ApplicationEntity applicationEntity = new ApplicationEntity();
		applicationEntity.setId(applicationId);
		functionEntity.setApplicationEntity(applicationEntity);
		
		String functionId = request.getParameter("functionId");
		functionEntity.setId(functionId);
		
		return this.functionService.getFunctionEntityQuiGrid(functionEntity, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @Title: insertFunction 
	 * @Description: [保存实体]
	 * @param functionEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertFunction", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertFunction(FunctionEntity functionEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String applicationId = request.getParameter("applicationId");
		if(StringUtil.isEmpty(applicationId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		ApplicationEntity applicationEntity = new ApplicationEntity();
		applicationEntity.setId(applicationId);
		functionEntity.setApplicationEntity(applicationEntity);
		
		String iconId = request.getParameter("iconId");
		IconEntity iconEntity = new IconEntity();
		iconEntity.setId(iconId);
		functionEntity.setIconEntity(iconEntity);
		
		String parentFunctionId = request.getParameter("functionId");
		if (StringUtil.isNotEmpty(parentFunctionId)) {
			FunctionEntity parentFunctionEntity = new FunctionEntity();
			parentFunctionEntity.setId(parentFunctionId);
			functionEntity.setParentFunctionEntity(parentFunctionEntity);
		}
		
		return this.functionService.insertFunctionEntity(functionEntity);
	}
	
	/**
	 * 
	 * @Title: updateFunction 
	 * @Description: [修改实体]
	 * @param functionEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/updateFunction", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateFunction(FunctionEntity functionEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String applicationId = request.getParameter("applicationId");
		if(StringUtil.isEmpty(applicationId) || StringUtil.isEmpty(functionEntity.getId())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		ApplicationEntity applicationEntity = new ApplicationEntity();
		applicationEntity.setId(applicationId);
		functionEntity.setApplicationEntity(applicationEntity);
		
		String iconId = request.getParameter("iconId");
		IconEntity iconEntity = new IconEntity();
		iconEntity.setId(iconId);
		functionEntity.setIconEntity(iconEntity);
		
		return this.functionService.updateFunctionEntity(functionEntity);
	}
	
	/**
	 * 
	 * @Title: detailFunction 
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/detailFunction", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailFunction(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.functionService.expandFunctionEntity(id);
	}
	
	/**
	 * 
	 * @Title: deleteFunction 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteFunction", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteFunction(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.functionService.deleteFunctionEntity(id);
	}
	
	/**
	 * 
	 * @Title: enableFunctionStatus 
	 * @Description: [启用/禁用]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/enableFunctionStatus", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson enableFunctionStatus(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson json = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			json.setSuccess(false);
			json.setMessage("参数有误");
			return json;
		}
		
		return this.functionService.enableFunctionStatus(id);
	}
	
	/**
	 * 
	 * @Title: getApplicationTreeNode 
	 * @Description: [获取功能应用树的根节 点]
	 * @param applicationId
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/getApplicationTreeNode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson getApplicationTreeNode(@RequestParam String applicationId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson json = new AjaxJson();
		
		if(StringUtil.isEmpty(applicationId)){
			json.setSuccess(false);
			json.setMessage("参数有误");
			return json;
		}
		
		return this.functionService.getApplicaitonTreeNode(applicationId);
	}

	/**
	 * 
	 * @Title: functionTree 
	 * @Description: [功能树]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/functionTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson functionTree(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson json = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			json.setSuccess(false);
			json.setMessage("参数有误");
			return json;
		}
		String type = request.getParameter("type");
		String applicationId = request.getParameter("applicationId");
		
		String functionId = null;
		if (Constants.PERMISSION_TREE_TYPE[1].equals(type)) {
			functionId = id;
		}
		
		return this.functionService.getFunctionTree(applicationId, functionId);
	}
	
	/**
	 * 
	 * @Title: menuTree 
	 * @Description: [菜单树]
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/menuTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson menuTree(HttpServletRequest request, HttpServletResponse response) throws JumpException {
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		
		String applicationId = null;
		if (Constants.PERMISSION_TREE_TYPE[0].equals(type)) {
			applicationId = id;
		}
		
		String functionId = null;
		if (Constants.PERMISSION_TREE_TYPE[1].equals(type)) {
			functionId = id;
			applicationId = request.getParameter("rootId");
		}
		
		return this.functionService.getMenuTree(applicationId, functionId, path);
	}
	
}
