/** 
 * @Description:[机构管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.controller.OrganizationController.java
 * @ClassName:OrganizationController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:50:24
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午2:50:24  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.org.service.OrganizationService;

/**
 * @ClassName: OrganizationController 
 * @Description: [机构管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:50:24 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/organizationController")
public class OrganizationController extends BaseController {
	
	@Autowired
	private OrganizationService organizationService;
	
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
	public AjaxJson organizationTree(OrganizationEntity organizationEntity,
			HttpServletRequest request, HttpServletResponse response){
		//获取当前登录用户
		SessionInfo sessionInfo = (SessionInfo)ContextHolderUtil.getSession().getAttribute(Globals.USER_SESSION);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.organizationService.OrganizationServiceTree(organizationEntity,sessionInfo);
	}
	
	/**
	 * 
	* @Title: saveOrUpdate 
	* @Description: 保存或者编辑机构
	* @param @param organizationEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdate(OrganizationEntity organizationEntity,HttpServletRequest request, HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.organizationService.saveOrUpdateOrganization(organizationEntity);
	}
	/**
	 * 
	* @Title: organization 
	* @Description: 根据机构id或者机构实体
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return OrganizationEntity 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/organization", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson organization(HttpServletRequest request, HttpServletResponse response){
		AjaxJson j=new AjaxJson();
		OrganizationEntity organizationEntity=new OrganizationEntity();
		String orgId=request.getParameter("id");
        if(StringUtil.isEmpty(orgId)){
			j.setSuccess(false);
			j.setMessage("请传入机构id");
			return j;
		}
		if(StringUtil.isNotEmpty(orgId)){
			organizationEntity=this.organizationService.expandEntity(OrganizationEntity.class, orgId);
			 if(organizationEntity==null){
					j.setSuccess(false);
					j.setMessage("没有找到对象");
					return j;
				}
		}
		j.setSuccess(true);
		j.setObj(organizationEntity);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	/**
	 * 
	* @Title: delOrganizationEntity 
	* @Description: 删除机构
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/delOrganizationEntity", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson delOrganizationEntity(HttpServletRequest request,
			HttpServletResponse response){
		String id=request.getParameter("id");
		AjaxJson j = this.organizationService.delOrganizationEntity(id);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	

}
