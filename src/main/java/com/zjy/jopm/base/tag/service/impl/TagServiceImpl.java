package com.zjy.jopm.base.tag.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.stereotype.Service;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.tag.entity.TagEntity;
import com.zjy.jopm.base.tag.entity.TagFunctionRelationEntity;
import com.zjy.jopm.base.tag.entity.TagRoleRelationEntity;
import com.zjy.jopm.base.tag.service.TagService;
@Service
public class TagServiceImpl extends BaseServiceimpl implements TagService{

	@Override
	public Map<String, Object> getTagForm(int pageNo, int pageSize) {
		List<TagEntity> TagList = super.queryListByClass(TagEntity.class);
		String hql = "select roleEntity.name from TagRoleRelationEntity where tagEntity.id = ?";
		for (TagEntity tagEntity : TagList) {
			StringBuffer s = new StringBuffer();
			List<String> roleNameList = super.queryListByHql(hql,tagEntity.getId());
			for (String string : roleNameList) {
				s.append(string+",");
			}
			if(roleNameList.size() > 0){				
				tagEntity.setRoleName(s.substring(0, s.length()-1));
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pager.pageNo", pageNo);
		resultMap.put("pager.totalRows", TagList.size());
		resultMap.put("rows", TagList);
		return resultMap;
	}

	@Override
	public AjaxJson roleTree(String tagId) {
		AjaxJson j = new AjaxJson();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		if (StringUtil.isNotEmpty(tagId)) {
			
			String hql = "from RoleEntity where type = '1'";
			List<RoleEntity> roleList = super.queryListByHql(hql);
			
			if(roleList != null &&roleList.size() != 0){
				for (RoleEntity role : roleList) {					
					String hql1 = "SELECT DISTINCT tagEntity.id FROM TagRoleRelationEntity WHERE roleEntity.id = ?";
					List<String> tagIdList = super.queryListByHql(hql1, role.getId());
					if(tagIdList.contains(tagId)){
						TreeNode node = new TreeNode();
						node.setId(role.getId());
						node.setName(role.getName());
						node.setChecked(true);
						node.setIsParent(false);
						node.setNocheck(false);
						nodes.add(node);
					}else{
						TreeNode node = new TreeNode();
						node.setId(role.getId());
						node.setName(role.getName());
						node.setChecked(false);
						node.setIsParent(false);
						node.setNocheck(false);
						nodes.add(node);
					}
				}
			}
		}
		j.setObj(nodes);
		return j;
	}

	@Override
	public AjaxJson setRoleTag(String tagId, String[] roleIds) {
		AjaxJson ajaxJson = new AjaxJson(); 
		TagEntity tag = super.expandEntity(TagEntity.class, tagId);
		
		if(null == tag){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("不存在，可能已删除");
			return ajaxJson;
		}
		
		String hql = null;
		List<RoleEntity> roleList = null;
		Map<String, Object> _param = null;
		if (null != roleIds && roleIds.length > 0 && StringUtil.isNotEmpty(roleIds[0])) {
			_param = new HashMap<String, Object>();
			_param.put("ids", roleIds);
			hql = "FROM RoleEntity WHERE id IN (:ids)";
			roleList = super.queryListByHql(hql, _param);
			if (roleList.size() != roleIds.length) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("某个角色不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		hql = "FROM TagRoleRelationEntity WHERE tagEntity.id = ?";
		List<TagRoleRelationEntity> TagRoleRelationEntityList = this.queryListByHql(hql, tag.getId());
		Boolean success = super.deleteEntityBatch(TagRoleRelationEntityList);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("设置标签角色过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		if (null != roleList) {
			TagRoleRelationEntityList = new ArrayList<TagRoleRelationEntity>();
			TagRoleRelationEntity tagRoleRelationEntity = null;
			for (RoleEntity roleEntity : roleList) {
				tagRoleRelationEntity = new TagRoleRelationEntity();
				tagRoleRelationEntity.setRoleEntity(roleEntity);
				tagRoleRelationEntity.setTagEntity(tag);;
				TagRoleRelationEntityList.add(tagRoleRelationEntity);
			}
			
			success = super.insertEntityBatch(TagRoleRelationEntityList);
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("设置标签角色过程中遇到错误，请稍后重试");
				return ajaxJson;
			}
		}

		return ajaxJson;
	}

	@Override
	public AjaxJson tagTree(String functionId) {
		AjaxJson j = new AjaxJson();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		if (StringUtil.isNotEmpty(functionId)) {
						
			List<TagEntity> tagList = super.queryListByClass(TagEntity.class);
			
			if(tagList != null &&tagList.size() != 0){
				for (TagEntity tag : tagList) {					
					String hql1 = "SELECT DISTINCT functionEntity.id FROM TagFunctionRelationEntity WHERE tagEntity.id = ?";
					List<String> functionIdList = super.queryListByHql(hql1, tag.getId());
					if(functionIdList.contains(functionId)){
						TreeNode node = new TreeNode();
						node.setId(tag.getId());
						node.setName(tag.getName());
						node.setChecked(true);
						node.setIsParent(false);
						node.setNocheck(false);
						nodes.add(node);
					}else{
						TreeNode node = new TreeNode();
						node.setId(tag.getId());
						node.setName(tag.getName());
						node.setChecked(false);
						node.setIsParent(false);
						node.setNocheck(false);
						nodes.add(node);
					}
				}
			}
		}
		j.setObj(nodes);
		return j;
	}

	@Override
	public AjaxJson setTagFunction(String functionId, String[] tagIds) {
		AjaxJson ajaxJson = new AjaxJson(); 
		FunctionEntity function = super.expandEntity(FunctionEntity.class, functionId);
		
		if(null == function){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("不存在，可能已删除");
			return ajaxJson;
		}
		
		String hql = null;
		List<TagEntity> tagList = null;
		Map<String, Object> _param = null;
		if (null != tagIds && tagIds.length > 0 && StringUtil.isNotEmpty(tagIds[0])) {
			_param = new HashMap<String, Object>();
			_param.put("ids", tagIds);
			hql = "FROM TagEntity WHERE id IN (:ids)";
			tagList = super.queryListByHql(hql, _param);
			if (tagList.size() != tagIds.length) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("某个角色不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		hql = "FROM TagFunctionRelationEntity WHERE functionEntity.id = ?";
		List<TagFunctionRelationEntity> tagFunctionRelationEntityList = this.queryListByHql(hql, function.getId());
		Boolean success = super.deleteEntityBatch(tagFunctionRelationEntityList);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("设置标签角色过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		if (null != tagList) {
			tagFunctionRelationEntityList = new ArrayList<TagFunctionRelationEntity>();
			TagFunctionRelationEntity tagFunctionRelationEntity = null;
			for (TagEntity tagEntity : tagList) {
				tagFunctionRelationEntity = new TagFunctionRelationEntity();
				tagFunctionRelationEntity.setTagEntity(tagEntity);
				tagFunctionRelationEntity.setFunctionEntity(function);
				tagFunctionRelationEntityList.add(tagFunctionRelationEntity);
			}
			
			success = super.insertEntityBatch(tagFunctionRelationEntityList);
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("设置标签角色过程中遇到错误，请稍后重试");
				return ajaxJson;
			}
		}

		return ajaxJson;
		
	}

}
