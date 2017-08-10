/** 
 * @Description:[应用接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.ApplicationServiceImpl.java
 * @ClassName:ApplicationServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:23:03
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:23:03  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;








import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.JumpBeanUtil;
import com.unistc.utils.PropertiesUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.ServiceInterfaceEntity;
import com.zjy.jopm.base.app.service.ApplicationService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: ApplicationServiceImpl 
 * @Description: [应用接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:23:03 
 * @since JDK 1.6 
 */
@Service("applicationService")
@Transactional
public class ApplicationServiceImpl extends BaseServiceimpl implements ApplicationService {
	
	@Autowired
	private LogExtService logExtService;
		
	@Override
	public Map<String, Object> getApplicationEntityQuiGrid(ApplicationEntity applicationEntity, int pageNo, int pageSize, String sort, String direction)throws JumpException {
		
		String hql = "";
		//获取当前登录用户      系统应用都看不到,所以type为2
		if(applicationEntity.getOrganizationEntity() == null){
			hql="FROM ApplicationEntity WHERE 1=1 " ;
		}else{			
			hql="FROM ApplicationEntity WHERE 1=1 and type = '2'" ;
		}
//		String hql="FROM ApplicationEntity WHERE 1=1 " ;
		Map<String,Object> paramMap = new HashMap<String, Object>();

		if(StringUtil.isNotEmpty(applicationEntity.getName())){
			hql+=" AND name LIKE :name";
			paramMap.put("name", "%"+applicationEntity.getName()+"%");
		}
		
		if(StringUtil.isNotEmpty(applicationEntity.getCode())){
			hql+=" AND code LIKE :code";
			paramMap.put("code", "%"+applicationEntity.getCode()+"%");
		}
		
		HttpSession session = ContextHolderUtil.getSession();
		SessionInfo sessionInfo = null;		
		if (null != session) {
			sessionInfo= (SessionInfo)session.getAttribute(Globals.USER_SESSION);
			if(sessionInfo.getUser().getOrganizationEntity() != null){				
				//如果登录用户是普通管理员，则不不让看到系统应用
				if(sessionInfo.getIdentity()==0){
//					hql+=" AND type = :type";
					//普通应用类型为2
//					paramMap.put("type", "2");
					
					List<String> orgIdList = null;
					
					orgIdList = getOrgListByRootId(sessionInfo.getUser().getOrganizationEntity().getId());
					
					
					hql += " AND organizationEntity.id in (:ids) ";
					paramMap.put("ids",orgIdList);
				}
			}
		}
		
		
		if(StringUtil.isNotEmpty(sort)){
			hql+=" ORDER BY :sort ";
			paramMap.put("sort",sort);
			if(Constants.DESC.equals(direction)){
				hql += SortDirection.desc;
			}else if(Constants.ASC.equals(direction)){
				hql += SortDirection.asc;
			}else{
				hql += SortDirection.desc;
			}
		}
		
		QueryCondition queryCondition= new QueryCondition(hql, paramMap, pageNo, pageSize);
		
		PageList pageList=super.queryListByHqlWithPage(queryCondition);// 结果集
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}
	
	private List<String> getOrgListByRootId(String organizationId) {
		List<String> orgIdList = new ArrayList<String>();
		orgIdList.add(organizationId);
		OrganizationEntity organization = new OrganizationEntity();
		organization = super.expandEntity(OrganizationEntity.class, organizationId);
		List<OrganizationEntity> ogrList = new ArrayList<OrganizationEntity>(); 
		if(null!=organization.getChildOrganizationEntity()){
			ogrList = getChildOrgList(organization);
			for (OrganizationEntity organizationEntity : ogrList) {
				orgIdList.add(organizationEntity.getId());
			}
		}
		return orgIdList;
	}

	private List<OrganizationEntity> getChildOrgList(OrganizationEntity organization) {
		List<OrganizationEntity> ogrList = new ArrayList<OrganizationEntity>(); 
		List<OrganizationEntity> childOgrList = new ArrayList<OrganizationEntity>(); 
		for (OrganizationEntity organizationEntity : organization.getChildOrganizationEntity()) {
			ogrList.add(organizationEntity);
			if(null!=organizationEntity.getChildOrganizationEntity()){
				childOgrList = getChildOrgList(organizationEntity);
				ogrList.addAll(childOgrList);
			}
		}
		return ogrList;
	}

	@Override
	public AjaxJson insertApplicationEntity(ApplicationEntity applicationEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = applicationEntity.getOrganizationEntity().getId();
		OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, organizationId);
		if(null == organizationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("机构不存在，无法继续保存操作");
			return ajaxJson;
		}
		applicationEntity.setOrganizationEntity(organizationEntity);
		
		IconEntity iconEntity = super.expandEntity(IconEntity.class, applicationEntity.getIconEntity().getId());
		if(null != iconEntity){
			applicationEntity.setIconEntity(iconEntity);// 设置图标信息
		}
		
		Boolean success = this.existsName(organizationId, applicationEntity.getId(), applicationEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(organizationId, applicationEntity.getId(), applicationEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用编码已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsURL(organizationId, applicationEntity.getId(), applicationEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用URL已存在，请更改");
			return ajaxJson;
		}
		
		if(StringUtil.isNotEmpty(applicationEntity.getCompanyCode())&&StringUtil.isNotEmpty(applicationEntity.getAppSerial())){
			success = this.existsCompanySerial(applicationEntity.getCompanyCode(), applicationEntity.getId(), applicationEntity.getAppSerial());
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("该应用在所属公司编码已经存在，请更改");
				return ajaxJson;
			}
		}
		
		int uuCode = UUID.randomUUID().toString().hashCode();
		
		//有可能是负数
		if(uuCode<0){
			uuCode = -uuCode;
		}
		// 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
		String code = String.format("%015d", uuCode);
		applicationEntity.setCreatetime(new Date());// 设置创建时间
		applicationEntity.setInstanceCode(code);
		success = super.insertEntity(applicationEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "保存应用：["+applicationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		
		return ajaxJson;
	}
	
	private Boolean existsCompanySerial(String companyCode, String id,String appSerial) {
		Boolean flag = false;
		ApplicationEntity app = new ApplicationEntity();
		String hql = "FROM ApplicationEntity WHERE companyCode = ? and appSerial = ?";
		app = super.expandEntityByHql(hql, new Object[]{companyCode, appSerial});
		if(StringUtil.isEmpty(id)){
			if(null==app){
				flag = true;
			}
		}else {
			if(null!=app){
				if(id.equals(app.getId())){
					flag = true;
				}
			}else{
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public AjaxJson updateApplicationEntity(ApplicationEntity applicationEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ApplicationEntity application = super.expandEntity(ApplicationEntity.class, applicationEntity.getId());
		
		if (null == application) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		String organizationId = applicationEntity.getOrganizationEntity().getId();
		OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, organizationId);
		if(null == organizationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("机构不存在，无法继续更改操作");
			return ajaxJson;
		}
		applicationEntity.setOrganizationEntity(organizationEntity);
		
		IconEntity iconEntity = super.expandEntity(IconEntity.class, applicationEntity.getIconEntity().getId());
		if(null != iconEntity){
			applicationEntity.setIconEntity(iconEntity);// 设置图标信息
		}
		
		Boolean success = this.existsName(organizationId, applicationEntity.getId(), applicationEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(organizationId, applicationEntity.getId(), applicationEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用编码已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsURL(organizationId, applicationEntity.getId(), applicationEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用URL已存在，请更改");
			return ajaxJson;
		}
		
		if(StringUtil.isNotEmpty(applicationEntity.getCompanyCode())&&StringUtil.isNotEmpty(applicationEntity.getAppSerial())){
			success = this.existsCompanySerial(applicationEntity.getCompanyCode(), applicationEntity.getId(), applicationEntity.getAppSerial());
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("该应用在所属公司编码已经存在，请更改");
				return ajaxJson;
			}
		}
		
		JumpBeanUtil.copyBeanNotNull2Bean(applicationEntity, application);
		
		success = super.updateEntity(application);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "修改应用：["+applicationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}
	
	@Override
	public AjaxJson expandApplicationEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, id);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，可能已删除");
			return ajaxJson;
		}
		
		ajaxJson.setObj(applicationEntity);
		
		return ajaxJson;
	}
	
	@Override
	public AjaxJson deleteApplicationEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, id);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，可能已删除");
			return ajaxJson;
		}
		
		List<ServiceInterfaceEntity> serviceInterfaceList = super.queryListByProperty(ServiceInterfaceEntity.class, "applicationEntity.id", applicationEntity.getId());
		if(serviceInterfaceList.size()>0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用和服务接口存在关联数据，不能删除");
			return ajaxJson;
		}
		
		List<FunctionEntity> functionList = super.queryListByProperty(FunctionEntity.class, "applicationEntity.id", applicationEntity.getId());
		if(functionList.size()>0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用和功能存在关联数据数据，不能删除");
			return ajaxJson;
		}
		
		boolean success = super.deleteEntity(applicationEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "删除应用：["+applicationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}
	
	@Override
	public AjaxJson enableApplicationStatus(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, id);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，可能已删除");
			return ajaxJson;
		}
		
		String status = applicationEntity.getStatus();
		if(Constants.ENABLE_STATUS.equals(applicationEntity.getStatus())) {
			status = Constants.DISABLE_STATUS;// 如果启用，则禁用
		}
		if(Constants.DISABLE_STATUS.equals(applicationEntity.getStatus())) {
			status = Constants.ENABLE_STATUS;// 如果禁用，则启用
		}
		applicationEntity.setStatus(status);
		
		Boolean success = super.updateEntity(applicationEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("启/禁用过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "启/禁用应用：["+applicationEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}

	/**
	 * 
	 * @Title: existsName 
	 * @Description: [校验应用名是否存在]
	 * @param organizationId
	 * @param id
	 * @param name
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsName(String organizationId, String id, String name) throws JumpException {
		ApplicationEntity applicationEntity = null;
		
		if (StringUtil.isNotEmpty(id)) {
			applicationEntity = super.expandEntity(ApplicationEntity.class, id);
			if(null != applicationEntity && applicationEntity.getName().equals(name)){
				return false;
			}
		}
		
		String hql = "FROM ApplicationEntity WHERE organizationEntity.id = ? AND name = ? ";
		applicationEntity = super.expandEntityByHql(hql, new Object[]{organizationId, name});
		if(null == applicationEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsCode 
	 * @Description: [校验应用编码是否存在]
	 * @param organizationId
	 * @param id
	 * @param code
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsCode(String organizationId, String id, String code) throws JumpException {
		ApplicationEntity applicationEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			applicationEntity = super.expandEntity(ApplicationEntity.class, id);
			if(null != applicationEntity && applicationEntity.getCode().equals(code)){
				return false;
			}
			
		}
		
		String hql = "FROM ApplicationEntity WHERE organizationEntity.id = ? AND code = ? ";
		applicationEntity = super.expandEntityByHql(hql, new Object[]{organizationId, code});
		if(null == applicationEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsURL 
	 * @Description: [校验应用URL是否存在]
	 * @param organizationId
	 * @param id
	 * @param url
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsURL(String organizationId, String id, String url) throws JumpException {
		ApplicationEntity applicationEntity = null;
		
		
		if(StringUtil.isNotEmpty(id)){
			applicationEntity = super.expandEntity(ApplicationEntity.class, id);
			if(null != applicationEntity && applicationEntity.getUrl().equals(url)){
				return false;
			}
		}
		
		String hql = "FROM ApplicationEntity WHERE organizationEntity.id = ? AND url = ? ";
		applicationEntity = super.expandEntityByHql(hql, new Object[]{organizationId, url});
		if(null == applicationEntity){
			return false;
		}
		
		return true;
	}

	@Override
	public Map<String, Object> getAppDataQuiGrid(int pageNo, int pageSize) {

		//获取当前登录用户
		String hql="FROM ApplicationEntity WHERE 1=1 " ;
		Map<String,Object> paramMap = new HashMap<String, Object>();
		hql+=" AND type = :type";
		//普通应用类型为2
		paramMap.put("type", "2");
		
		QueryCondition queryCondition= new QueryCondition(hql, paramMap, pageNo, pageSize);
		PageList pageList=super.queryListByHqlWithPage(queryCondition);// 结果集
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	/**
	 * 
	 * @Title: updateStrategy 
	 * @Description: [修改开通策略]
	 * @param id 应用id
	 * @param strategy 开通策略 
	 * @return
	 */
	@Override
	public AjaxJson updateStrategy(String id, String strategy) {
		AjaxJson ajaxJson = new AjaxJson();
		String hql = "update ApplicationEntity set strategy = ? where id = ?";
		Integer count = super.runUpdateByHql(hql, strategy,id);
		if(count < 1){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("修改失败");
		}
		return ajaxJson;
	}

}
