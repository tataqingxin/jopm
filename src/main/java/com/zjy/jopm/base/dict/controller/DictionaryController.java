/** 
 * @Description:[字典数值管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.controller.DictionaryGroupController.java
 * @ClassName:DictionaryGroupController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:47:49
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午2:47:49  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.controller;

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
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.service.DictionaryService;
import com.zjy.jopm.base.dict.service.ext.DictExtService;

/**
 * 
 * @ClassName: DictionaryController 
 * @Description: [字典数值管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:47:49  
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/dictionaryController")
public class DictionaryController extends BaseController {

	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private DictExtService dictExtService;
	
	/**
	 * 
	* @Title: getDctionaryGroupList 
	* @Description: 根据字典分组code来获取数值
	* @param @param dictionaryGroupEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return Map<String,Object> 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/getDctionaryList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDctionaryList(DictionaryEntity dictionaryEntity, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> results = new HashMap<String, Object>();
		String pageNo = request.getParameter("pager.pageNo");
		String pageSize = request.getParameter("pager.pageSize");
		if(StringUtil.isEmpty(pageNo)){
			pageNo="1";
		}
		if(StringUtil.isEmpty(pageSize)){
			pageSize="10";
		}
		String order = request.getParameter("sort");
		String groupId = request.getParameter("groupId");
		results = dictionaryService.getDictionaryQuiGrid(dictionaryEntity, pageNo, pageSize, order,groupId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return results;
	}
	
	
	/**
	 * 根据数值id获取实体
	* @Title: dictionaryGroup 
	* @Description: [功能描述]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/dictionary", method = RequestMethod.POST)
	@ResponseBody
	public DictionaryEntity dictionary(HttpServletRequest request,
			HttpServletResponse response){
		String id=request.getParameter("id");
		DictionaryEntity dictionaryEntity=null;
		if(StringUtil.isNotEmpty(id)){
			dictionaryEntity =dictionaryService.dictionary(id);
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dictionaryEntity;
	}
	
	
	/**
	 * 
	* @Title: saveOrUpdateDctionaryGroup 
	* @Description: 保存 更新 字典分组
	* @param @param dictionaryGroupEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/saveOrUpdateDctionary", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateDctionary(DictionaryEntity DictionaryEntity, HttpServletRequest request,
			HttpServletResponse response){
		String groupId=request.getParameter("groupId");
		AjaxJson j =dictionaryService.saveOrUpdateDctionary(DictionaryEntity,groupId);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	/**
	 * 删除字典数值
	* @Title: delDctionaryGroup 
	* @Description: [功能描述]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/delDctionary", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson delDctionary(HttpServletRequest request,
			HttpServletResponse response){
		String id=request.getParameter("id");
		AjaxJson j =dictionaryService.delDctionary(id);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	
	/**
	 * 
	 * @Title: select 
	 * @Description: [获取类型列表]
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>>
	 * @throws JumpException
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody 
	public List<Map<String, Object>> select(HttpServletRequest request, HttpServletResponse response) throws JumpException {
		String typeGroupCode=request.getParameter("typeGroupCode");
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		if(StringUtil.isNotEmpty(typeGroupCode)){
			list = dictExtService.getTypeList(typeGroupCode);
		}
		return list;

	}
	
}
