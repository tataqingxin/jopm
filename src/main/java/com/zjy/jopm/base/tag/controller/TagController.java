package com.zjy.jopm.base.tag.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.tag.service.TagService;
@Controller
@RequestMapping("/tagController")
public class TagController extends BaseController {

	@Autowired
	private TagService tagService;
	
	/**
	 * 返回所有标签
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getTagForm")
	public Map<String, Object> getTagForm(@RequestParam(value="pager.pageNo",required=false,defaultValue="1")int pageNo,
			@RequestParam(value="pager.pageSize",required=false,defaultValue="10")int pageSize,
			@RequestParam(value="sort",required=false,defaultValue="id")String sort){
		return tagService.getTagForm(pageNo,pageSize);
	}
	/**
	 * 获取角色树 
	 * @param tagId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("roleTree")
	public AjaxJson roleTree(@RequestParam("tagId")String tagId){
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(tagId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return tagService.roleTree(tagId);
	}
	
	@ResponseBody
	@RequestMapping("setRoleTag")
	public AjaxJson setRoleTag(@RequestParam("tagId")String tagId,@RequestParam("roleIds")String roleIds){
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(tagId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		if (StringUtil.isEmpty(roleIds)) {
			roleIds = "";
		}
		
		return tagService.setRoleTag(tagId,roleIds.split(","));
	}
	
	/**
	 * 获取标签树 
	 * @param tagId  
	 * @return
	 */
	@ResponseBody
	@RequestMapping("tagTree")
	public AjaxJson tagTree(@RequestParam("functionId")String functionId){
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(functionId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		return tagService.tagTree(functionId);
	}
	
	@ResponseBody
	@RequestMapping("setTagFunction")
	public AjaxJson setTagFunction(@RequestParam("functionId")String functionId,@RequestParam("tagIds")String tagIds){
		AjaxJson ajaxJson = new AjaxJson();
		
		if(StringUtil.isEmpty(functionId)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("参数有误");
			return ajaxJson;
		}
		
		if (StringUtil.isEmpty(tagIds)) {
			tagIds = "";
		}
		
		return tagService.setTagFunction(functionId,tagIds.split(","));
	}
}
