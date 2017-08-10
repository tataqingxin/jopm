/** 
 * @Description:[应用管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.controller.ApplicationController.java
 * @ClassName:ApplicationController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:41:47
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午2:41:47  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.controller;

import java.text.ParseException;
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
import com.unistc.utils.Platform;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.service.ApplicationService;
import com.zjy.jopm.base.app.timing.TimingCleanEffApp;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

/**
 * @ClassName: ApplicationController 
 * @Description: [应用管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:41:47 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/app/applicationController")
public class ApplicationController extends BaseController {
	
	@Autowired
	private ApplicationService applicationService;

	/**
	 * 
	 * @Title: applicationList 
	 * @Description: [查询列表]
	 * @param applicationEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/applicationList", method = RequestMethod.POST)
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
//		
//		String organizationId = request.getParameter("organizationId");
//		OrganizationEntity organizationEntity = new OrganizationEntity();
//		organizationEntity.setId(organizationId);
//		applicationEntity.setOrganizationEntity(organizationEntity);
		
		return this.applicationService.getApplicationEntityQuiGrid(applicationEntity, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @Title: insertApplication 
	 * @Description: [新增实体]
	 * @param applicationEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertApplication", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertApplication(ApplicationEntity applicationEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isEmpty(organizationId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		applicationEntity.setOrganizationEntity(organizationEntity);
		
		String iconId = request.getParameter("iconId");
		IconEntity iconEntity = new IconEntity();
		iconEntity.setId(iconId);
		applicationEntity.setIconEntity(iconEntity);
		
		return this.applicationService.insertApplicationEntity(applicationEntity);
	}
	
	/**
	 * 
	 * @Title: updateApplication 
	 * @Description: [修改实体]
	 * @param application
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/updateApplication", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateApplication(ApplicationEntity applicationEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isEmpty(organizationId) || StringUtil.isEmpty(applicationEntity.getId())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
	 
		
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		applicationEntity.setOrganizationEntity(organizationEntity);
		
		if(StringUtil.isEmpty(applicationEntity.getType())){
			applicationEntity.setType("1");
		}
		
		String iconId = request.getParameter("iconId");
		IconEntity iconEntity = new IconEntity();
		iconEntity.setId(iconId);
		applicationEntity.setIconEntity(iconEntity);
		
		return this.applicationService.updateApplicationEntity(applicationEntity);
	}
	
	/**
	 * 
	 * @Title: detailApplication 
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/detailApplication", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailApplication(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.applicationService.expandApplicationEntity(id);
	}
	
	/**
	 * 
	 * @Title: deleteApplication 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteApplication", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteApplication(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.applicationService.deleteApplicationEntity(id);
	}
	
	/**
	 * 
	 * @Title: enableApplicationStatus 
	 * @Description: [启用/禁用]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/enableApplicationStatus", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson enableApplicationStatus(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson json = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			json.setSuccess(false);
			json.setMessage("参数有误");
			return json;
		}
		
		return this.applicationService.enableApplicationStatus(id);
	}
	
	@RequestMapping(value = "/updateStrategy", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson updateStrategy(@RequestParam(value="applicationId") String id,@RequestParam(value="strategy") String strategy){
		AjaxJson json = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			json.setSuccess(false);
			json.setMessage("参数有误");
			return json;
		}
		
		return this.applicationService.updateStrategy(id,strategy);
	}
	
	@ResponseBody
	@RequestMapping("testTiming")
	public void testTiming() throws ParseException{
		TimingCleanEffApp service = Platform.getBean("timingCleanEffApp");
		try {
			service.cleanEffAppjob();
			System.out.println("定时清理任务执行成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
