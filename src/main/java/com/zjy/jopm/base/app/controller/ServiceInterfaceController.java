/** 
 * @Description:[服务接口管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.controller.ServiceInterfaceController.java
 * @ClassName:ServiceInterfaceController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:52:15
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午2:52:15  
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
import com.zjy.jopm.base.app.entity.ServiceInterfaceEntity;
import com.zjy.jopm.base.app.service.ServiceInterfaceService;
import com.zjy.jopm.base.app.service.ApplicationService;

/**
 * @ClassName: ServiceInterfaceController 
 * @Description: [服务接口管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:52:15 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/app/serviceInterfaceController")
public class ServiceInterfaceController extends BaseController {
	
	@Autowired
	private ServiceInterfaceService serviceInterfaceService;
	
	@Autowired
	private ApplicationService applicationService;
	
	/**
	 * 
	 * @Title: serviceInterfaceList 
	 * @Description: [服务接口列表]
	 * @param serviceInterfaceEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/serviceInterfaceList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> serviceInterfaceList(ServiceInterfaceEntity serviceInterfaceEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException {
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
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		
		return this.serviceInterfaceService.getServiceInterfaceEntityQuiGrid(serviceInterfaceEntity, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @Title: insertServiceInterface 
	 * @Description: [保存实体]
	 * @param serviceInterfaceEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertServiceInterface", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertServiceInterface(ServiceInterfaceEntity serviceInterfaceEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String applicationId = request.getParameter("applicationId");
		if(StringUtil.isEmpty(applicationId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		ApplicationEntity applicationEntity = new ApplicationEntity();
		applicationEntity.setId(applicationId);
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		
		return this.serviceInterfaceService.insertServiceInterfaceEntity(serviceInterfaceEntity);
	}
	
	/**
	 * 
	 * @Title: updateServiceInterface 
	 * @Description: [修改实体]
	 * @param serviceInterfaceEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/updateServiceInterface", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateServiceInterface(ServiceInterfaceEntity serviceInterfaceEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String applicationId = request.getParameter("applicationId");
		if(StringUtil.isEmpty(applicationId) || StringUtil.isEmpty(serviceInterfaceEntity.getId())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		ApplicationEntity applicationEntity = new ApplicationEntity();
		applicationEntity.setId(applicationId);
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		
		return this.serviceInterfaceService.updateServiceInterfaceEntity(serviceInterfaceEntity);
	}
	
	/**
	 * 
	 * @Title: detailServiceInterface 
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/detailServiceInterface", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailServiceInterface(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.serviceInterfaceService.expandServiceInterfaceEntity(id);
	}
	
	/**
	 * 
	 * @Title: deleteServiceInterface 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteServiceInterface", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteServiceInterface(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.serviceInterfaceService.deleteServiceInterfaceEntity(id);
	}

}
