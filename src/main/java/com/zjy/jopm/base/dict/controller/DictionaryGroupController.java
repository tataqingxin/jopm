/** 
 * @Description:[字典分组管理功能]   
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
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.dict.service.DictionaryGroupService;
import com.zjy.jopm.base.dict.service.DictionaryService;
import com.zjy.jopm.base.dict.service.ext.DictExtService;

/**
 * @ClassName: DictionaryGroupController 
 * @Description: [字典分组管理功能] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:47:49 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/dictionaryGroupController")
public class DictionaryGroupController extends BaseController {
	
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private DictionaryGroupService dictionaryGroupService;
	@Autowired
	private DictExtService dictExtService;
	/**
	 * 
	* @Title: getDctionaryGroupList 
	* @Description: 获取字典分组数据
	* @param @param dictionaryGroupEntity
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return Map<String,Object> 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/getDctionaryGroupList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDctionaryGroupList(DictionaryGroupEntity dictionaryGroupEntity, HttpServletRequest request,
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
		results = dictionaryGroupService.getDctionaryGroupQuiGrid(dictionaryGroupEntity, pageNo, pageSize, order);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return results;
	}
	
	
	/**
	 * 根据分组id获取实体
	* @Title: dictionaryGroup 
	* @Description: [功能描述]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/dictionaryGroup", method = RequestMethod.POST)
	@ResponseBody
	public DictionaryGroupEntity dictionaryGroup(HttpServletRequest request,
			HttpServletResponse response){
		String groupId=request.getParameter("id");
		DictionaryGroupEntity dictionaryGroupEntity=null;
		if(StringUtil.isNotEmpty(groupId)){
			dictionaryGroupEntity =dictionaryGroupService.dictionaryGroup(groupId);
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dictionaryGroupEntity;
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
	@RequestMapping(value = "/saveOrUpdateDctionaryGroup", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateDctionaryGroup(DictionaryGroupEntity dictionaryGroupEntity, HttpServletRequest request,
			HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		if (StringUtil.isEmpty(dictionaryGroupEntity.getId())) {
			List<DictionaryGroupEntity> dic=dictionaryGroupService.queryListByProperty
					  (DictionaryGroupEntity.class, "code", dictionaryGroupEntity.getCode());
			if(dic.size()>0){
				j.setSuccess(false);
				j.setMessage("分组已存在！");
				return j;
			}
		    dic=dictionaryGroupService.queryListByProperty
					  (DictionaryGroupEntity.class, "name", dictionaryGroupEntity.getName());
			if(dic.size()>0){
				j.setSuccess(false);
				j.setMessage("名称已存在！");
				return j;
			}
			
		}else{
			DictionaryGroupEntity dic=dictionaryGroupService.expandEntity(DictionaryGroupEntity.class, dictionaryGroupEntity.getId());
			if(StringUtil.isNotEmpty(dictionaryGroupEntity.getCode())&&!dic.getCode().equals(dictionaryGroupEntity.getCode())){
				List<DictionaryGroupEntity> dics=dictionaryGroupService.queryListByProperty
						  (DictionaryGroupEntity.class, "code", dictionaryGroupEntity.getCode());
				if(dics.size()>0){
					j.setSuccess(false);
					j.setMessage("分组已存在！");
					return j;
				}
				
				dics=dictionaryGroupService.queryListByProperty
						  (DictionaryGroupEntity.class, "name", dictionaryGroupEntity.getName());
				if(dics.size()>0){
					j.setSuccess(false);
					j.setMessage("名称已存在！");
					return j;
				}
			}
		}
		boolean flag =dictionaryGroupService.saveOrUpdateDctionaryGroup(dictionaryGroupEntity);
		if (flag) {
			j.setSuccess(true);
			j.setMessage("操作成功！");
			dictExtService.initAllDictionaryGroups();
		} else {
			j.setSuccess(false);
			j.setMessage("操作失败！");
		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	/**
	 * 删除字典分组
	* @Title: delDctionaryGroup 
	* @Description: [功能描述]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/delDctionaryGroup", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson delDctionaryGroup(HttpServletRequest request,
			HttpServletResponse response){
		String groupId=request.getParameter("id");
		AjaxJson j =dictionaryGroupService.delDctionaryGroup(groupId);
		dictExtService.initAllDictionaryGroups();
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	
	
	/**
	 * 删除字典分组
	* @Title: delDctionaryGroup 
	* @Description: [功能描述]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/checkDctionaryGroupForCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson checkDctionaryGroupForCode(HttpServletRequest request,
			HttpServletResponse response){
		String code=request.getParameter("code");
		AjaxJson j =dictionaryGroupService.checkDctionaryGroupForCode(code);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return j;
	}
	
	
	/**
	 * 根据分组id获取实体
	* @Title: dictionaryGroup 
	* @Description: [功能描述]
	* @param @param request
	* @param @param response
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	@RequestMapping(value = "/getDictionaryList", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getDictionaryList(HttpServletRequest request,
			HttpServletResponse response){
		String code=request.getParameter("code");
		List<Map<String, Object>> map=dictionaryService.getDictionaryList(code);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return map;
	}
	
}
