/** 
 * @Description:[功能操作接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.OperarionServiceImpl.java
 * @ClassName:OperarionServiceImpl
 * @Author:Lu Guoqiang  
 * @CreateDate:2016年5月9日 下午3:28:19
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:28:19  
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
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.app.entity.OperationResourceRelationEntity;
import com.zjy.jopm.base.app.entity.ResourceEntity;
import com.zjy.jopm.base.app.service.OperationService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: OperarionServiceImpl 
 * @Description: [功能操作接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:28:19 
 * @since JDK 1.6 
 */
@Service("operationService")
@Transactional
public class OperationServiceImpl extends BaseServiceimpl implements OperationService {
	
	@Autowired
	private LogExtService logExtService;

	@Override
	public Map<String, Object> getOperationEntityQuiGrid(OperationEntity operationEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException {
		String hql="FROM OperationEntity WHERE 1=1" ;
		List<Object> param = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(operationEntity.getName())){
			hql+=" AND name LIKE ?";
			param.add("%"+operationEntity.getName()+"%");
		}
		
		if(StringUtil.isNotEmpty(operationEntity.getCode())){
			hql+=" AND code LIKE ?";
			param.add("%"+operationEntity.getCode()+"%");
		}
		
		hql += " AND functionEntity.id = ?";
		param.add(operationEntity.getFunctionEntity().getId());
		
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
		for (Object object : pageList.getResultList()) {
			OperationEntity entity = (OperationEntity) object;
			
			OperationResourceRelationEntity operationResourceRelationEntity = new OperationResourceRelationEntity();
			operationResourceRelationEntity.setOperationEntity(operationEntity);
			hql = "FROM ResourceEntity WHERE 1=1 AND id IN (SELECT resourceEntity.id FROM OperationResourceRelationEntity WHERE operationEntity.id = ?)";
			List<ResourceEntity> resourceEntityList = super.queryListByHql(hql, new Object[]{entity.getId()});
			operationEntity.setResourceEntityList(resourceEntityList);
		}
		
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	@Override
	public AjaxJson insertOperationEntity(String functionId, OperationEntity operationEntity, Object[] resourceIds) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, functionId);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，无法继续保存操作");
			return ajaxJson;
		}
		operationEntity.setFunctionEntity(functionEntity);
		
		IconEntity iconEntity = super.expandEntity(IconEntity.class, operationEntity.getIconEntity().getId());
		if(null != iconEntity){
			operationEntity.setIconEntity(iconEntity);// 设置图标信息
		}
		
		Boolean success = this.existsName(functionId, operationEntity.getId(), operationEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("操作名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(functionId, operationEntity.getId(), operationEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("操作编码已存在，请更改");
			return ajaxJson;
		}
		
		success = super.insertEntity(operationEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存操作过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		for (Object resourceId : resourceIds) {
			ResourceEntity resourceEntity = super.expandEntity(ResourceEntity.class, String.valueOf(resourceId));
			if (null== resourceEntity) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("资源不存在，无法继续保存操作");
				return ajaxJson;
			}
			
			//保存操作和资源关系
			OperationResourceRelationEntity operationResourceRelationEntity = new OperationResourceRelationEntity();
			operationResourceRelationEntity.setOperationEntity(operationEntity);
			operationResourceRelationEntity.setResourceEntity(resourceEntity);
			success = super.insertEntity(operationResourceRelationEntity);
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("保存操作和资源关系过程中遇到错误，请稍后重试");
				return ajaxJson;
			}
		}
		
		//记录日志
		String logContent = "保存功能下操作：["+operationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson updateOperationEntity(String functionId, OperationEntity operationEntity, Object[] resourceIds) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		OperationEntity operation = super.expandEntity(OperationEntity.class, operationEntity.getId());
		if(null == operation){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能操作不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, functionId);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，无法继续更改操作");
			return ajaxJson;
		}
		operationEntity.setFunctionEntity(functionEntity);
		
		IconEntity iconEntity = super.expandEntity(IconEntity.class, operationEntity.getIconEntity().getId());
		if(null != iconEntity){
			operationEntity.setIconEntity(iconEntity);// 设置图标信息
		}
		
		Boolean success = this.existsName(functionId, operationEntity.getId(), operationEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("操作名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(functionId, operationEntity.getId(), operationEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("操作编码已存在，请更改");
			return ajaxJson;
		}
		
		JumpBeanUtil.copyBeanNotNull2Bean(operationEntity, operation);
		
		success = super.updateEntity(operation);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改操作过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//删除原资源和操作关系
		String hql = "DELETE OperationResourceRelationEntity WHERE operationEntity.id= ?";
		Integer deleteCount = super.runUpdateByHql(hql, new Object[]{operationEntity.getId()});
		if(deleteCount == 0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除资源和操作关系过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		for (Object resourceId : resourceIds) {
			ResourceEntity resourceEntity = super.expandEntity(ResourceEntity.class, String.valueOf(resourceId));
			if (null== resourceEntity) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("资源不存在，无法继续更改操作");
				return ajaxJson;
			}
			
			//保存新的操作和资源关系
			OperationResourceRelationEntity operationResourceRelationEntity = new OperationResourceRelationEntity();
			operationResourceRelationEntity.setOperationEntity(operationEntity);
			operationResourceRelationEntity.setResourceEntity(resourceEntity);
			success = super.insertEntity(operationResourceRelationEntity);
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("更改操作和资源关系过程中遇到错误，请稍后重试");
				return ajaxJson;
			}
		}
		
		//记录日志
		String logContent = "修改功能下操作["+operationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson expandOperationEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		OperationEntity operationEntity = super.expandEntity(OperationEntity.class, id);
		if(null == operationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("操作不存在，可能已删除");
			return ajaxJson;
		}
		
		OperationResourceRelationEntity operationResourceRelationEntity = new OperationResourceRelationEntity();
		operationResourceRelationEntity.setOperationEntity(operationEntity);
		String hql = "FROM ResourceEntity WHERE 1=1 AND id IN (SELECT DISTINCT resourceEntity.id FROM OperationResourceRelationEntity WHERE operationEntity.id = ?)";
		List<ResourceEntity> resourceEntityList = super.queryListByHql(hql, new Object[]{operationEntity.getId()});
		operationEntity.setResourceEntityList(resourceEntityList);
		
		ajaxJson.setObj(operationEntity);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteOperationEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		OperationEntity operationEntity = super.expandEntity(OperationEntity.class, id);
		if(null == operationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("操作不存在，可能已删除");
			return ajaxJson;
		}
		
		//删除原资源和操作关系
		String hql = "DELETE OperationResourceRelationEntity WHERE operationEntity.id= ?";
		Integer deleteCount = super.runUpdateByHql(hql, new Object[]{operationEntity.getId()});
		if(deleteCount == 0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除资源和操作关系过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		//删除资源实体
		Boolean success = super.deleteEntity(operationEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除操作过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "删除功能下操作：["+operationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}

	/**
	 * 
	 * @Title: existsName 
	 * @Description: [某功能下的操作名是否存在]
	 * @param functionId
	 * @param id
	 * @param name
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsName(String functionId, String id, String name) throws JumpException {
		OperationEntity operationEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			operationEntity = super.expandEntity(OperationEntity.class, id);
			if(null != operationEntity && operationEntity.getName().equals(name)){
				return false;
			}
		}
		
		String hql = "FROM OperationEntity WHERE functionEntity.id = ? AND name = ? ";
		operationEntity = super.expandEntityByHql(hql, new Object[]{functionId, name});
		if(null == operationEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsCode 
	 * @Description: [某功能下的操作编码是否存在]
	 * @param functionId
	 * @param id
	 * @param code
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsCode(String functionId, String id, String code) throws JumpException {
		OperationEntity operationEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			operationEntity = super.expandEntity(OperationEntity.class, id);
			if(null != operationEntity && operationEntity.getCode().equals(code)){
				return false;
			}
		}
		
		String hql = "FROM OperationEntity WHERE functionEntity.id = ? AND code = ? ";
		operationEntity = super.expandEntityByHql(hql, new Object[]{functionId, code});
		if(null == operationEntity){
			return false;
		}
		
		return true;
	}
}
