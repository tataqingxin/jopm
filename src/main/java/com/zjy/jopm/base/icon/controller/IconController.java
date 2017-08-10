/** 
 * @Description:[图标管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.icon.controller.IconController.java
 * @ClassName:IconController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:48:06
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午2:48:06  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.icon.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.exception.JumpException;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.service.ext.DictExtService;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.IconService;
import com.zjy.jopm.base.icon.service.ext.IconExtService;

/**
 * @ClassName: IconController 
 * @Description: [图标管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:48:06 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/iconController")
public class IconController extends BaseController {
	@Autowired
	private IconService iconService;
	@Autowired
	private IconExtService iconExtService;
	@Autowired
	private DictExtService dictExtService;
	
	/**
	 * 
	 * @Title: iconList 
	 * @Description: [获取图标列表]
	 * @param iconEntity
	 * @param response
	 * @param request
	 * @return Map<String, Object>
	 * @throws JumpException
	 */
	@RequestMapping(value = "/iconList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> iconList(IconEntity iconEntity,
			HttpServletResponse response, HttpServletRequest request) throws JumpException{
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
		Map<String, Object> results = iconService.getIconDataGrid(iconEntity, pageNo, pageSize, sort,direction);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return results;
	}
	
	/**
	 * 
	 * @Title: deleteIcon 
	 * @Description: [删除图标]
	 * @param id
	 * @param request
	 * @param response
	 * @return AjaxJson
	 * @throws JumpException
	 */
	@RequestMapping(value = "/deleteIcon", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson deleteIcon(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws JumpException {
		IconEntity iconEntity = this.iconService.expandEntity(IconEntity.class, id);
		AjaxJson json = this.iconService.deleteIcon(iconEntity);
		return json;
	}
	/**
	 * 
	 * @Title: select 
	 * @Description: [获取图标类型列表]
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>>
	 * @throws JumpException
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody 
	public List<Map<String, Object>> select(HttpServletRequest request, HttpServletResponse response) throws JumpException {
		List<Map<String, Object>> list = dictExtService.getTypeList("iconType");
		return list;

	}
	
	/**
	 * 
	 * @Title: insertIcon 
	 * @Description: [新增/修改图标]
	 * @param file
	 * @param iconEntity
	 * @param request
	 * @param response
	 * @return AjaxJson
	 * @throws JumpException
	 * @throws IOException
	 */
	@RequestMapping(value = "/insertIcon", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson insertIcon(IconEntity iconEntity,HttpServletRequest request, HttpServletResponse response) throws JumpException, IOException {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile small = multipartRequest.getFile("small");
			MultipartFile big = multipartRequest.getFile("big");
			MultipartFile medium = multipartRequest.getFile("medium");
			if(StringUtils.isEmpty(iconEntity.getId())){
					AjaxJson  result= iconService.insertIcon(small,big,medium,request, iconEntity);
					return result;
			}
			AjaxJson result = iconService.updateIcon(small,big,medium,request, iconEntity);
			return result;
	}
	/**
	 * 
	 * @Title: viewIcon 
	 * @Description: [查看图标]
	 * @param id
	 * @param request
	 * @param response
	 * @return IconEntity
	 */
	@RequestMapping(value = "/viewIcon", method = RequestMethod.POST)
	@ResponseBody
	public IconEntity viewIcon(@RequestParam(value="id") String id,HttpServletRequest request,
			HttpServletResponse response){
		IconEntity iconEntity = this.iconService.expandEntity(IconEntity.class, id);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return iconEntity;
	}
	
	
	/**
	 * 
	 * @Title: getIconTreeList 
	 * @Description: [获取图标列表]
	 * @param request
	 * @param response
	 * @return AjaxJson
	 */
	@RequestMapping(value = "/getIconTreeList", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson getIconTreeList(HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson iconTreeList = iconExtService.getIconTreeList();
		return iconTreeList;
	}
	
}
