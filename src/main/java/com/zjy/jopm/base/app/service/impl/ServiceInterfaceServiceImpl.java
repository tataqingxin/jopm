/** 
 * @Description:[服务接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.ServiceInterfaceServiceImpl.java
 * @ClassName:ServiceInterfaceServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:31:13
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:31:13  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.ServiceInterfaceEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.quiUtil.QuiUtils;
import com.zjy.jopm.base.app.service.ServiceInterfaceService;

import jodd.util.StringUtil;

/**
 * @ClassName: ServiceInterfaceServiceImpl 
 * @Description: [服务接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:31:13 
 * @since JDK 1.6 
 */
@Service("serviceInterfaceService")
@Transactional
public class ServiceInterfaceServiceImpl extends BaseServiceimpl implements ServiceInterfaceService {
	
	@Autowired
	private LogExtService logExtService;

	@Override
	public Map<String, Object> getServiceInterfaceEntityQuiGrid(ServiceInterfaceEntity serviceInterfaceEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException {
		String hql="FROM ServiceInterfaceEntity WHERE 1=1" ;
		List<Object> param = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(serviceInterfaceEntity.getName())){
			hql+=" AND name LIKE ?";
			param.add("%"+serviceInterfaceEntity.getName()+"%");
		}
		
		if(StringUtil.isNotEmpty(serviceInterfaceEntity.getCode())){
			hql+=" AND code LIKE ?";
			param.add("%"+serviceInterfaceEntity.getCode()+"%");
		}
		
		hql += " AND applicationEntity.id = ?";
		param.add(serviceInterfaceEntity.getApplicationEntity().getId());
		
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
	public AjaxJson insertServiceInterfaceEntity(ServiceInterfaceEntity serviceInterfaceEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		String applicationId  = serviceInterfaceEntity.getApplicationEntity().getId();
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续保存操作");
			return ajaxJson;
		}
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		
		Boolean success = this.existsName(applicationId, serviceInterfaceEntity.getId(), serviceInterfaceEntity.getName());
		/*if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口名已存在，请更改");
			return ajaxJson;
		}*/
		
		success = this.existsCode(applicationId, serviceInterfaceEntity.getId(), serviceInterfaceEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口编码已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsURL(applicationId, serviceInterfaceEntity.getId(), serviceInterfaceEntity.getUrl());
		/*if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口URL已存在，请更改");
			return ajaxJson;
		}*/
		
		success = super.insertEntity(serviceInterfaceEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "保存服务接口：["+serviceInterfaceEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson updateServiceInterfaceEntity(ServiceInterfaceEntity serviceInterfaceEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ServiceInterfaceEntity serviceInterface = super.expandEntity(ServiceInterfaceEntity.class, serviceInterfaceEntity.getId());
		if(null == serviceInterface){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		String applicationId  = serviceInterfaceEntity.getApplicationEntity().getId();
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续更改操作");
			return ajaxJson;
		}
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		
		Boolean success = this.existsName(applicationId, serviceInterfaceEntity.getId(), serviceInterfaceEntity.getName());
		/*if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口名已存在，请更改");
			return ajaxJson;
		}*/
				
		success = this.existsCode(applicationId, serviceInterfaceEntity.getId(), serviceInterfaceEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口编码已存在，请更改");
			return ajaxJson;
		}
		
		/*success = this.existsURL(applicationId, serviceInterfaceEntity.getId(), serviceInterfaceEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口URL已存在，请更改");
			return ajaxJson;
		}*/
		
		JumpBeanUtil.copyBeanNotNull2Bean(serviceInterfaceEntity, serviceInterface);
		
		success = super.updateEntity(serviceInterface);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "修改服务接口：["+serviceInterfaceEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson expandServiceInterfaceEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ServiceInterfaceEntity serviceInterfaceEntity = super.expandEntity(ServiceInterfaceEntity.class, id);
		if(null == serviceInterfaceEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口不存在，可能已删除");
			return ajaxJson;
		}
		
		ajaxJson.setObj(serviceInterfaceEntity);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteServiceInterfaceEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ServiceInterfaceEntity serviceInterfaceEntity = super.expandEntity(ServiceInterfaceEntity.class, id);
		if(null == serviceInterfaceEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("服务接口不存在，可能已删除");
			return ajaxJson;
		}
		
		Boolean success = super.deleteEntity(serviceInterfaceEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "删除服务接口：["+serviceInterfaceEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}

	
	/**
	 * 
	 * @Title: existsName 
	 * @Description: [某应用下的服务接口名是否存在]
	 * @param applicationId
	 * @param id
	 * @param name
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsName(String applicationId, String id, String name) throws JumpException {
		ServiceInterfaceEntity serviceInterfaceEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			serviceInterfaceEntity = super.expandEntity(ServiceInterfaceEntity.class, id);
			if(null != serviceInterfaceEntity && serviceInterfaceEntity.getName().equals(name)){
				return false;
			}
		}
		
		String hql = "FROM ServiceInterfaceEntity WHERE applicationEntity.id = ? AND name = ? ";
		serviceInterfaceEntity = super.expandEntityByHql(hql, new Object[]{applicationId, name});
		if(null == serviceInterfaceEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsCode 
	 * @Description: [某应用下的服务接口编码是否存在]
	 * @param applicationId
	 * @param id
	 * @param code
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsCode(String applicationId, String id, String code) throws JumpException {
		ServiceInterfaceEntity serviceInterfaceEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			serviceInterfaceEntity = super.expandEntity(ServiceInterfaceEntity.class, id);
			if(null != serviceInterfaceEntity && serviceInterfaceEntity.getCode().equals(code)){
				return false;
			}
		}
		
		String hql = "FROM ServiceInterfaceEntity WHERE applicationEntity.id = ? AND code = ? ";
		serviceInterfaceEntity = super.expandEntityByHql(hql, new Object[]{applicationId, code});
		if(null == serviceInterfaceEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsURL 
	 * @Description: [某应用下的服务接口URL是否存在]
	 * @param applicationId
	 * @param id
	 * @param url
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsURL(String applicationId, String id, String url) throws JumpException {
		ServiceInterfaceEntity serviceInterfaceEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			serviceInterfaceEntity = super.expandEntity(ServiceInterfaceEntity.class, id);
			if(null != serviceInterfaceEntity && serviceInterfaceEntity.getCode().equals(url)){
				return false;
			}
		}
		
		String hql = "FROM ServiceInterfaceEntity WHERE applicationEntity.id = ? AND url = ? ";
		serviceInterfaceEntity = super.expandEntityByHql(hql, new Object[]{applicationId, url});
		if(null == serviceInterfaceEntity){
			return false;
		}
		
		return true;
	}

}
