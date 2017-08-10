/** 
 * @Description:[资源管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.controller.ResourceController.java
 * @ClassName:ResourceController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:50:42
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午2:50:42  
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
import com.zjy.jopm.base.app.entity.ResourceEntity;
import com.zjy.jopm.base.app.service.ResourceService;

/**
 * @ClassName: ResourceController 
 * @Description: [资源管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:50:42 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/app/resourceController")
public class ResourceController extends BaseController {

	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 
	 * @Title: resourceList 
	 * @Description: [查询列表]
	 * @param resourceEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/resourceList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> resourceList(ResourceEntity resourceEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException {
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
		
		String functionId = request.getParameter("functionId");
		
		return this.resourceService.getResourceEntityQuiGrid(functionId, resourceEntity, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @Title: insertResource 
	 * @Description: [保存实体]
	 * @param resourceEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertResource", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertResource(ResourceEntity resourceEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String functionId = request.getParameter("functionId");
		if(StringUtil.isEmpty(functionId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.resourceService.insertResourceEntity(functionId, resourceEntity);
	}
	
	/**
	 * 
	 * @Title: updateResource 
	 * @Description: [修改实体]
	 * @param resourceEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/updateResource", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateResource(ResourceEntity resourceEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String functionId = request.getParameter("functionId");
		if(StringUtil.isEmpty(functionId) || StringUtil.isEmpty(resourceEntity.getId())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.resourceService.updateResourceEntity(functionId, resourceEntity);
	}
	
	/**
	 * 
	 * @Title: detailResource 
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/detailResource", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailResource(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.resourceService.expandResourceEntity(id);
	}
	
	/**
	 * 
	 * @Title: deleteResource 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteResource", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteResource(@RequestParam(value="id") String id, @RequestParam(value="functionId") String functionId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(functionId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.resourceService.deleteResourceEntity(functionId, id);
	}
	
	/**
	 * 
	 * @Title: getResourceList 
	 * @Description: [根据功能获取资源列表]
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/getResourceList", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson getResourceList(@RequestParam String functionId, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(functionId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.resourceService.getResourceListByFunctionId(functionId);
	}
	
}
