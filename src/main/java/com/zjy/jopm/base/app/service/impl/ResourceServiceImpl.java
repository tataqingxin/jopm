/** 
 * @Description:[资源接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.ResourceServiceImpl.java
 * @ClassName:ResourceServiceImpl
 * @Author:Lu Guoqiang  
 * @CreateDate:2016年5月9日 下午3:29:51
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:29:51  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.JumpBeanUtil;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.FunctionResourceRelationEntity;
import com.zjy.jopm.base.app.entity.OperationResourceRelationEntity;
import com.zjy.jopm.base.app.entity.ResourceEntity;
import com.zjy.jopm.base.app.service.ResourceService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: ResourceServiceImpl 
 * @Description: [资源接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:29:51 
 * @since JDK 1.6 
 */
@Service("resourceService")
@Transactional
public class ResourceServiceImpl extends BaseServiceimpl implements ResourceService {
	
	@Autowired
	private LogExtService logExtService;

	@Override
	public Map<String, Object> getResourceEntityQuiGrid(String functionId, ResourceEntity resourceEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException {
		String hql="FROM ResourceEntity WHERE 1=1" ;
		List<Object> param = new ArrayList<Object>();
		
		hql += " AND id IN (SELECT DISTINCT resourceEntity.id FROM FunctionResourceRelationEntity WHERE functionEntity.id = ?)";
		param.add(functionId);
		
		if(StringUtil.isNotEmpty(resourceEntity.getUrl())){
			hql+=" AND url LIKE ?";
			param.add("%"+resourceEntity.getUrl()+"%");
		}
		
		if(StringUtil.isNotEmpty(sort)){
			hql+=" ORDER BY ? ";
			param.add(sort);
			if(Constants.DESC.equals(direction)){
				hql += SortDirection.desc;
			}else if(Constants.ASC.equals(direction)){
				hql += SortDirection.asc;
			}else{
				hql += SortDirection.desc;
			}
		}
		
		QueryCondition queryCondition= new QueryCondition(hql, param, pageNo, pageSize);
		PageList pageList=super.queryListByHqlWithPage(queryCondition);// 结果集
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	@Override
	public AjaxJson insertResourceEntity(String functionId, ResourceEntity resourceEntity) {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, functionId);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，无法继续保存操作");
			return ajaxJson;
		}
		
		Boolean success = this.existsURL(functionId, resourceEntity.getId(), resourceEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("资源URL已存在，请更改");
			return ajaxJson;
		}
		
		success = super.insertEntity(resourceEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存资源过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//保存功能和资源关系
		FunctionResourceRelationEntity functionResourceRelationEntity = new FunctionResourceRelationEntity();
		functionResourceRelationEntity.setFunctionEntity(functionEntity);
		functionResourceRelationEntity.setResourceEntity(resourceEntity);
		success = super.insertEntity(functionResourceRelationEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存资源和功能关系过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "保存资源：["+resourceEntity.getUrl()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson updateResourceEntity(String functionId, ResourceEntity resourceEntity) {
		AjaxJson ajaxJson = new AjaxJson();
		
		ResourceEntity resource = super.expandEntity(ResourceEntity.class, resourceEntity.getId());
		if(null == resource){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能资源不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, functionId);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		Boolean success = this.existsURL(functionId, resourceEntity.getId(), resourceEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能URL已存在，请更改");
			return ajaxJson;
		}
		
		JumpBeanUtil.copyBeanNotNull2Bean(resourceEntity, resource);
		
		success = super.updateEntity(resource);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改资源过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "修改资源：["+resourceEntity.getUrl()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson expandResourceEntity(String id) {
		AjaxJson ajaxJson = new AjaxJson();
		
		ResourceEntity resourceEntity = super.expandEntity(ResourceEntity.class, id);
		if(null == resourceEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("资源不存在，可能已删除");
			return ajaxJson;
		}
		
		ajaxJson.setObj(resourceEntity);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteResourceEntity(String functionId, String id) {
		AjaxJson ajaxJson = new AjaxJson();
		
		ResourceEntity resourceEntity = super.expandEntity(ResourceEntity.class, id);
		if(null == resourceEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("资源不存在，可能已删除");
			return ajaxJson;
		}
		
		List<OperationResourceRelationEntity> resourceOperationList=this.queryListByProperty(OperationResourceRelationEntity.class,"resourceEntity.id",id);
		if(resourceOperationList.size()>0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("该资源已经被绑定操作，不允许删除！");
			return ajaxJson;
		}
		
		//删除资源和功能之间的关系
		String hql = "DELETE FunctionResourceRelationEntity WHERE functionEntity.id = ? AND resourceEntity.id = ?";
		
		Integer deleteCount = super.runUpdateByHql(hql, new Object[]{functionId, id});
		if(deleteCount == 0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除资源和功能关系过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		Boolean success = super.deleteEntity(resourceEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除资源过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "删除资源：["+resourceEntity.getUrl()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}


	/**
	 * 
	 * @Title: existsURL 
	 * @Description: [某功能下的资源URL是否存在]
	 * @param functionId
	 * @param id
	 * @param url
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsURL(String functionId, String id, String url) throws JumpException {
		ResourceEntity resourceEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			resourceEntity = super.expandEntity(ResourceEntity.class, id);
			if(null != resourceEntity && resourceEntity.getUrl().equals(url)){
				return false;
			}
		}
		
		String hql = "FROM ResourceEntity WHERE id IN (SELECT DISTINCT resourceEntity.id FROM FunctionResourceRelationEntity WHERE functionEntity.id = ?) AND url = ? ";
		resourceEntity = super.expandEntityByHql(hql, new Object[]{functionId, url});
		if(null == resourceEntity){
			return false;
		}
		
		return true;
	}

	@Override
	public AjaxJson getResourceListByFunctionId(String functionId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, functionId);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，无法继续操作");
			return ajaxJson;
		}
		List<ResourceEntity> resourceEntityList = new ArrayList<ResourceEntity>();
		String hql = "FROM FunctionResourceRelationEntity WHERE functionEntity.id = ?";
		List<FunctionResourceRelationEntity> functionResourceRelationEntityList = super.queryListByHql(hql, functionEntity.getId());
		for (FunctionResourceRelationEntity functionResourceRelationEntity : functionResourceRelationEntityList) {
			ResourceEntity resourceEntity = functionResourceRelationEntity.getResourceEntity();
			if (null != resourceEntity) {
				resourceEntityList.add(resourceEntity);
			}
		}
		
		ajaxJson.setObj(resourceEntityList);
		
		return ajaxJson;
	}
}
