/** 
 * @Description:[用户管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.user.controller.UserController.java
 * @ClassName:UserController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:45:22
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午2:45:22  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.UserService;
import com.zjy.jopm.base.user.service.ext.UserExtService;

/**
 * @ClassName: UserController 
 * @Description: [用户管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:45:22 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserExtService userExtService;
	
	@RequestMapping(value = "/getUserList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getUserList(UserEntity user,HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> results = new HashMap<String, Object>();
		
		String orgId=request.getParameter("orgId");
		if(StringUtil.isEmpty(orgId)){
			results.put("pager.pageNo", 1);
			results.put("pager.totalRows", 0);
			List<Map<String, Object>> listRows = new ArrayList<Map<String, Object>>();
			results.put("rows", listRows);
			return results;
		}
		
		
		String departId=request.getParameter("departId");
		
		if(StringUtil.isNotEmpty(departId)){
			if(!departId.equals(orgId)){
				orgId=departId;
			}
		}
		
		String pageNo = request.getParameter("pager.pageNo");
		String pageSize = request.getParameter("pager.pageSize");
		String isInclude=request.getParameter("isInclude");//是否包含子部门 0 不包含 1 包含
		if(StringUtil.isEmpty(isInclude)){
			isInclude="0";
		}
		if(StringUtil.isEmpty(pageNo)){
			pageNo="1";
		}
		if(StringUtil.isEmpty(pageSize)){
			pageSize="10";
		}
		String sort = request.getParameter("sort");
		if(StringUtil.isEmpty(sort)){
			sort = "id";
		}
		String direction = request.getParameter("direction");
		if(StringUtil.isEmpty(direction)){
			direction = Constants.DESC;
		}
		results = userService.getUserListQuiGrid(user, pageNo, pageSize, sort,direction,isInclude,orgId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return results;
	}
	/**
	 * 
	* @Title: saveOrUpdateUser 
	* @Description:保存 编辑user
	* @param @param user
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/saveOrUpdateUser", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateUser(UserEntity user,HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String orgId=request.getParameter("orgId");//机构id
		String selectId=request.getParameter("departmentId");//选择树的id
		//设置用户类型为普通
		user.setType("0");
		j=userService.saveOrUpdateUser(user,selectId,orgId);
		return j;
	}
	/**
	 * [实体详情]
	* @Title: user 
	* @Description: [实体详情]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson user(HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String id=request.getParameter("id");//用户id
		if(StringUtil.isEmpty(id)){
			j.setSuccess(false);
			j.setMessage("请勾选数据，进行编辑");
			return j;
		}
		UserEntity user=userService.expandEntity(UserEntity.class, id);
		if(user==null){
			j.setSuccess(false);
			j.setMessage("没有找到对象");
			return j;
		}
		j.setSuccess(true);
		j.setObj(user); 
		List<String> ids=userExtService.getDepartIdsByUser(user.getId());
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("departIds", ids);
		map.put("organizationId", user.getOrganizationEntity().getId());
		j.setAttributes(map);
		return j;
	}
	
	/**
	 * 
	* @Title: deleteUser 
	* @Description: [删除user]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson deleteUser(HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String ids=request.getParameter("ids");//用户id
		if(StringUtil.isEmpty(ids)){
			j.setSuccess(false);
			j.setMessage("请勾选数据，进行删除");
			return j;
		}
		j=userService.deleteUser(ids);
		return j;
	}
	
	/**
	 * 
	* @Title: updateStatus 
	* @Description: [启用 停用]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson updateStatus(HttpServletRequest request,
	HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String ids=request.getParameter("ids");//用户id
		if(StringUtil.isEmpty(ids)){
			j.setSuccess(false);
			j.setMessage("请勾选数据，进行操作");
			return j;
		}
		j=userService.updateStatus(ids);
		return j;
	}
	
	@RequestMapping(value = "/resetPassWrod", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson resetPassWrod(HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String ids=request.getParameter("ids");//用户id
		if(StringUtil.isEmpty(ids)){
			j.setSuccess(false);
			j.setMessage("请勾选数据，进行操作");
			return j;
		}
		j=userService.resetPassWrod(ids);
		return j;
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
	@RequestMapping(value = "/departTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson departTree(HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		String organizationId = request.getParameter("organizationId");
		if(StringUtil.isEmpty(organizationId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		return this.userExtService.getOrgDepartTreeByorgId(organizationId);
	}
	
	/**
	 * 
	* @Title: updatePasswWord 
	* @Description: [修改密码]
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws JumpException  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/updatePasswWord", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson updatePasswWord (HttpServletRequest request, HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		String id = request.getParameter("id");
		String passwordNew = request.getParameter("passwordNew");//新密码
		String passwordOld = request.getParameter("passwordOld");//旧密码
		ajaxJson=this.userService.updatePasswWord(id,passwordNew,passwordOld);
		return ajaxJson;
	}
	
	/**
	 * 
	* @Title: saveOrUpdateUser 
	* @Description:保存 编辑user并发送邮件
	* @param @param user
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/saveOrUpdateUserForEamil", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateUserForEamil(UserEntity user,HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		String orgId=request.getParameter("orgId");//机构id
		String selectId=request.getParameter("departmentId");//选择树的id
		j=userService.saveOrUpdateUserForEamil(user,selectId,orgId);
		return j;
	}
	
	

	
	
	
}
