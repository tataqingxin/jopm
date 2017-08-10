/** 
 * @Description:[功能描述]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.role.controller.RoleController.java
 * @ClassName:RoleController
 * @Author:HogwartsRow 
 * @CreateDate:2016年5月9日 下午2:45:43
 * @UpdateUser:HogwartsRow  
 * @UpdateDate:2016年5月9日 下午2:45:43  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.controller;

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
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.service.ext.AppExtService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.role.service.RoleService;
import com.zjy.jopm.base.user.service.ext.UserExtService;

/**
 * @ClassName: RoleController 
 * @Description: [功能描述] 
 * @author HogwartsRow
 * @date 2016年5月9日 下午2:45:43 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/roleController")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AppExtService appExtService;
	@Autowired
	private UserExtService userExtService;
	
	/**
	 * 
	 * @Title: roleList 
	 * @Description: [查询列表]
	 * @param roleEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/roleList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> roleList(RoleEntity roleEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		int pageNo = 1;
		if(StringUtil.isNotEmpty(request.getParameter("pager.pageNo"))){
			pageNo = Integer.parseInt(request.getParameter("pager.pageNo"));
		}
		int pageSize = 10;
		if(StringUtil.isNotEmpty(request.getParameter("pager.pageSize"))){
			pageNo = Integer.parseInt(request.getParameter("pager.pageSize"));
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
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		roleEntity.setOrganizationEntity(organizationEntity);
		
		return this.roleService.getRoleEntityQuiGrid(roleEntity, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @Title: insertRole 
	 * @Description: [新增实体]
	 * @param roleEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertRole", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertRole(RoleEntity roleEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isEmpty(organizationId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		roleEntity.setOrganizationEntity(organizationEntity);
		
		return this.roleService.insertRoleEntity(roleEntity);
	}
	
	/**
	 * 
	 * @Title: updateRole 
	 * @Description: [修改实体]
	 * @param roleEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/updateRole", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateRole(RoleEntity roleEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isEmpty(organizationId) || StringUtil.isEmpty(roleEntity.getId())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		roleEntity.setOrganizationEntity(organizationEntity);
		
		return this.roleService.updateRoleEntity(roleEntity);
	}
	
	/**
	 * 
	 * @Title: detailRole 
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/detailRole", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailRole(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.roleService.expandRoleEntity(id);
	}
	
	/**
	 * 
	 * @Title: deleteRole 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteRole(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.roleService.deleteRoleEntity(id);
	}
	
	/**
	 * 
	 * @Title: setRoleUser 
	 * @Description: [设置人员角色]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/setRoleUser", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson setRoleUser(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String userIds = request.getParameter("userIds");
		if (StringUtil.isEmpty(userIds)) {
			userIds = "";
		}
		
		return this.roleService.setRoleUser(id, userIds.split(","));
	}
	
	/**
	 * 
	 * @Title: setRolePermission 
	 * @Description: [设置角色权限]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/setRolePermission", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson setRolePermission(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String functionIds = request.getParameter("functionIds");
		if (StringUtil.isEmpty(functionIds)) {
			functionIds = "";
		}
		
		String operationIds = request.getParameter("operationIds");
		if (StringUtil.isEmpty(operationIds)) {
			operationIds = "";
		}
		
		return this.roleService.setRolePermission(id, functionIds.split(","), operationIds.split(","),request);
	}

	/**
	 * 
	 * @Title: userTree 
	 * @Description: [用户树]
	 * @param roleId
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/userTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson userTree(@RequestParam String roleId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(roleId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String organizationId = request.getParameter("organizationId");
		
		return this.userExtService.getOrgUserTreeByorgId(organizationId, roleId);
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
	@RequestMapping(value = "/permissionTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson permissionTree(@RequestParam String roleId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(roleId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String orgId= request.getParameter("orgId");
		
		String id = request.getParameter("id");// 节点类型决定id代表applicationId OR functionId
		String type = request.getParameter("type");// 节点类型
		String rootId = request.getParameter("rootId");// 此值是指应用ID
		String functionId = null;// 应用
		String applicationId = null;// 功能
		if (Constants.PERMISSION_TREE_TYPE[0].equals(type)) {
			applicationId = id;
		} else if (Constants.PERMISSION_TREE_TYPE[1].equals(type)) {
			functionId = id;
			applicationId = rootId;
		} 
		
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		
		return this.appExtService.getOperationTree(roleId, applicationId, functionId, path, orgId);
	}
}
