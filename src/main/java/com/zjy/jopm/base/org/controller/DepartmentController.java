/** 
 * @Description:[部门管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.controller.DepartmentController.java
 * @ClassName:DepartmentController
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午2:46:48
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午2:46:48  
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
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.service.DepartmentService;

/**
 * @ClassName: DepartmentController 
 * @Description: [部门管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:46:48 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/departmentController")
public class DepartmentController extends BaseController {
	
	@Autowired
	private DepartmentService departmentService;
	/**
	 * 
	* @Title: departmentTree 
	* @Description:部门树
	* @param @param DepartmentEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return List<TreeNode> 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/departmentTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson departmentTree(DepartmentEntity departmentEntity,
			HttpServletRequest request, HttpServletResponse response){
		//获取当前登录用户
		response.addHeader("Access-Control-Allow-Origin", "*");
		String orgId=request.getParameter("orgId");
		return this.departmentService.departmentServiceTree(departmentEntity,orgId,false);
	}
	
	/**
	 * 
	* @Title: saveOrUpdate 
	* @Description: 保存或者编辑部门
	* @param @param organizationEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdate(DepartmentEntity departmentEntity,HttpServletRequest request, HttpServletResponse response){
		String orgId=request.getParameter("orgId");
		String id=request.getParameter("departId");
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.departmentService.saveOrUpdateDepartment(departmentEntity,orgId,id);
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
	@RequestMapping(value = "/department", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson department(HttpServletRequest request, HttpServletResponse response){
		DepartmentEntity departmentEntity=new DepartmentEntity();
		String departId=request.getParameter("id");
		AjaxJson j=new AjaxJson();
		 if(StringUtil.isEmpty(departId)){
				j.setSuccess(false);
				j.setMessage("请传入部门id");
				return j;
			}
		if(StringUtil.isNotEmpty(departId)){
			departmentEntity=this.departmentService.expandEntity(DepartmentEntity.class, departId);
			if(departmentEntity==null){
				j.setSuccess(false);
				j.setMessage("没有找到对象");
				return j;
			}
		}
		j.setSuccess(true);
		j.setObj(departmentEntity);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	
	
	/**
	 * 
	* @Title: delDepartmentEntity
	* @Description: 删除部门
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/delDepartmentEntity", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson delDepartmentEntity(HttpServletRequest request,
			HttpServletResponse response){
		String id=request.getParameter("id");
		AjaxJson j = this.departmentService.delDepartmentEntity(id);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}

}
