package com.zjy.jopm.visitor.controller;

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
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.visitor.entity.VisitorEntity;
import com.zjy.jopm.visitor.service.VisitorService;

@Controller
@RequestMapping(value="/visitorController")
public class VisitorController extends BaseController{

	@Autowired
	private VisitorService visitorService;
	
	
	@RequestMapping(value = "/visitorList")
	@ResponseBody 
	public Map<String, Object> visitorList(VisitorEntity visitor, HttpServletRequest request, HttpServletResponse response)throws JumpException{
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
		
		return this.visitorService.getVisitorQuiGrid(visitor, pageNo, pageSize, sort, direction);
	}
	
	@RequestMapping(value = "/insertVisitor", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertVisitor(String functionIds, HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(functionIds)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.visitorService.insertVisitor(functionIds);
	}
	
	@RequestMapping(value = "/detailVisitor", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson detailVisitor(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.visitorService.expandVisitor(id);
	}
	
	@RequestMapping(value = "/deleteVisitor", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteVisitor(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isEmpty(id)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return this.visitorService.deleteVisitor(id);
	}
	
	@RequestMapping(value = "/appFunctionTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson appFuntionTree(HttpServletRequest request, HttpServletResponse response) throws JumpException {
		
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
		
		return this.visitorService.getAppFunctionTree(applicationId, functionId, path);
	}
	
	@RequestMapping(value = "/getVisitorSwitch")
	@ResponseBody
	public Object getVisitorSwitch() throws JumpException{
		return this.visitorService.getVisitorSwitch();
	}
	
	@RequestMapping(value = "/changeVisitorSwitch")
	@ResponseBody
	public AjaxJson changeVisitorSwitch(HttpServletRequest request) throws JumpException{
		String visitorSwitch = request.getParameter("visitorSwitch");
		return this.visitorService.changeVisitorSwitch(visitorSwitch);
	}
	
	@RequestMapping(value = "/getVisitorFunction")
	@ResponseBody
	public Object getVisitorFunction(HttpServletRequest request) throws JumpException{
		return this.visitorService.getVisitorFunction(request);
	}
}
