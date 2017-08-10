/** 
 * @Description:[操作管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.controller.OperationController.java
 * @ClassName:OperationController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:51:44
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午2:51:44  
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
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.app.service.OperationService;
import com.zjy.jopm.base.icon.entity.IconEntity;

/**
 * @ClassName: OperationController 
 * @Description: [操作管理功能] 
 * @author HogwartsRow
 * @date 2016年5月9日 下午2:51:44 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/app/operationController")
public class OperationController extends BaseController {
	
	@Autowired
	private OperationService operationService;
	
	/**
	 * 
	 * @Title: operationList 
	 * @Description: [查询列表]
	 * @param operationEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/operationList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> operationList(OperationEntity operationEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException {
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
		FunctionEntity functionEntity= new FunctionEntity();
		functionEntity.setId(functionId);
		operationEntity.setFunctionEntity(functionEntity);
		
		return this.operationService.getOperationEntityQuiGrid(operationEntity, pageNo, pageSize, sort, direction);
	}
	
	/**
	 * 
	 * @Title: insertOperation 
	 * @Description: [保存实体]
	 * @param operationEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/insertOperation", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertOperation(OperationEntity operationEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String functionId = request.getParameter("functionId");
		if(StringUtil.isEmpty(functionId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String iconId = request.getParameter("iconId");
		if(StringUtil.isNotEmpty(iconId)){
			IconEntity iconEntity = new IconEntity();
			iconEntity.setId(iconId);
			operationEntity.setIconEntity(iconEntity);
		}
		
		String resourceIds = request.getParameter("resourceIds");
		if (StringUtil.isEmpty(resourceIds)) {
			resourceIds = "";
		}
		
		return this.operationService.insertOperationEntity(functionId, operationEntity, resourceIds.split(","));
	}
	
	/**
	 * 
	 * @Title: updateOperation 
	 * @Description: [修改实体]
	 * @param operationEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/updateOperation", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson updateOperation(OperationEntity operationEntity, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		String functionId = request.getParameter("functionId");
		if(StringUtil.isEmpty(functionId) || StringUtil.isEmpty(operationEntity.getId())){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		String iconId = request.getParameter("iconId");
		if(StringUtil.isNotEmpty(iconId)){
			IconEntity iconEntity = new IconEntity();
			iconEntity.setId(iconId);
			operationEntity.setIconEntity(iconEntity);
		}
		
		String resourceIds = request.getParameter("resourceIds");
		if (StringUtil.isEmpty(resourceIds)) {
			resourceIds = "";
		}
		
		return this.operationService.updateOperationEntity(functionId, operationEntity, resourceIds.split(","));
	}
	
	/**
	 * 
	 * @Title: detailOperation
	 * @Description: [实体详情]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/detailOperation", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailOperation(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.operationService.expandOperationEntity(id);
	}
	
	/**
	 * 
	 * @Title: deleteOperation 
	 * @Description: [删除实体]
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteOperation", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteOperation(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.operationService.deleteOperationEntity(id);
	}

}
