/** 
 * @Description:[角色接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.role.service.impl.RoleServiceImpl.java
 * @ClassName:RoleServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:30:11
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:30:11  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.quiUtil.QuiUtils;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.role.service.RoleService;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.sdk.util.CasSessionUtil;

/**
 * @ClassName: RoleServiceImpl 
 * @Description: [角色接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:30:11 
 * @since JDK 1.6 
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseServiceimpl implements RoleService {
	
	@Autowired
	private LogExtService logExtService;

	@Override
	public Map<String, Object> getRoleEntityQuiGrid(RoleEntity roleEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException {
		String hql="FROM RoleEntity WHERE 1=1" ;
		List<Object> param = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(roleEntity.getName())){
			hql+=" AND name LIKE ?";
			param.add("%"+roleEntity.getName()+"%");
		}
		
		if(StringUtil.isNotEmpty(roleEntity.getCode())){
			hql+=" AND code LIKE ?";
			param.add("%"+roleEntity.getCode()+"%");
		}
		
		HttpSession session = ContextHolderUtil.getSession();
		SessionInfo sessionInfo = null;
		if (null != session) {
			sessionInfo= (SessionInfo)session.getAttribute(Globals.USER_SESSION);
			//如果登录用户是普通管理员，则不不让看到系统角色
			if(sessionInfo.getIdentity()==0){
				hql+=" AND type = ?";
				//普通角色类型为2
				param.add("2");
			}
		}
		
		hql += " AND organizationEntity.id = ?";
		param.add(roleEntity.getOrganizationEntity().getId());
		
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
	public AjaxJson insertRoleEntity(RoleEntity roleEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		String organizationId = roleEntity.getOrganizationEntity().getId();
		OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, organizationId);
		if(null == organizationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("机构不存在，无法继续保存操作");
			return ajaxJson;
		}
		roleEntity.setOrganizationEntity(organizationEntity);
		
		Boolean success = this.existsName(organizationId, roleEntity.getId(), roleEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(organizationId, roleEntity.getId(), roleEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色编码已存在，请更改");
			return ajaxJson;
		}
		
		success = super.insertEntity(roleEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "保存角色：["+roleEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson updateRoleEntity(RoleEntity roleEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		RoleEntity role = super.expandEntity(RoleEntity.class, roleEntity.getId());
		if(null == role){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		String organizationId = roleEntity.getOrganizationEntity().getId();
		OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, organizationId);
		if(null == organizationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("机构不存在，无法继续更改操作");
			return ajaxJson;
		}
		roleEntity.setOrganizationEntity(organizationEntity);
		
		Boolean success = this.existsName(organizationId, roleEntity.getId(), roleEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(organizationId, roleEntity.getId(), roleEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色编码已存在，请更改");
			return ajaxJson;
		}
		
		JumpBeanUtil.copyBeanNotNull2Bean(roleEntity, role);
		
		success = super.updateEntity(role);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "修改角色：["+roleEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson expandRoleEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		RoleEntity roleEntity = super.expandEntity(RoleEntity.class, id);
		if(null == roleEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色不存在，可能已删除");
			return ajaxJson;
		}
		
		ajaxJson.setObj(roleEntity);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteRoleEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		RoleEntity roleEntity = super.expandEntity(RoleEntity.class, id);
		if(null == roleEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色不存在，可能已删除");
			return ajaxJson;
		}
		
		String hql = "FROM RoleFunctionRelationEntity WHERE roleEntity.id = ?";
		List<RoleFunctionRelationEntity> roleFunctionRelationEntityList = super.queryListByHql(hql, new Object[]{id});
		if(null != roleFunctionRelationEntityList && roleFunctionRelationEntityList.size() > 0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色和功能存在关联数据，不能删除");
			return ajaxJson;
		}
		
		hql = "FROM RoleUserRelationEntity WHERE roleEntity.id = ?";
		List<RoleUserRelationEntity> roleUserEntityList = super.queryListByHql(hql, id);
		if(null != roleUserEntityList && roleUserEntityList.size() > 0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色和用户存在关联数据，不能删除");
			return ajaxJson;
		}
		
		boolean success = super.deleteEntity(roleEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "删除角色：["+roleEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}
	
	@Override
	public AjaxJson setRoleUser(String id, Object[] userIds) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		RoleEntity roleEntity = super.expandEntity(RoleEntity.class, id);
		if(null == roleEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色不存在，可能已删除");
			return ajaxJson;
		}

		String hql = null;
		List<UserEntity> userEntityList = null;
		Map<String, Object> _param = null;
		if (null != userIds && userIds.length > 0 && StringUtil.isNotEmpty(userIds[0])) {
			_param = new HashMap<String, Object>();
			_param.put("ids", userIds);
			hql = "FROM UserEntity WHERE id IN (:ids)";
			userEntityList = super.queryListByHql(hql, _param);
			if (userEntityList.size() != userIds.length) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("某个用户不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		HttpSession session = ContextHolderUtil.getSession();
		if (null != session) {
			SessionInfo sessionInfo= (SessionInfo)session.getAttribute(Globals.USER_SESSION);
			if (null != sessionInfo && Constants.NORMAL_ACCOUNT == sessionInfo.getIdentity()) {
				List<RoleEntity> roleList = sessionInfo.getRoleList();
				for (RoleEntity role : roleList) {
					if (role.getCode().equals(roleEntity.getCode())) {
						Boolean _self = false;
						
						UserEntity userEntity = sessionInfo.getUser();
						for (Object userId : userIds) {
							if (userId.equals(userEntity.getId())) {
								_self = true;
								break;
							}
						}
						
						if (!_self) {
							ajaxJson.setSuccess(false);
							ajaxJson.setMessage("不能解除自己的角色");
							return ajaxJson;
						}
					}
				}
			}
		}
		
		hql = "FROM RoleUserRelationEntity WHERE roleEntity.id = ?";
		List<RoleUserRelationEntity> roleUserRelationEntityList = this.queryListByHql(hql, roleEntity.getId());
		Boolean success = super.deleteEntityBatch(roleUserRelationEntityList);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("设置人员角色过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		if (null != userEntityList) {
			roleUserRelationEntityList = new ArrayList<RoleUserRelationEntity>();
			RoleUserRelationEntity roleUserRelationEntity = null;
			for (UserEntity userEntity : userEntityList) {
				roleUserRelationEntity = new RoleUserRelationEntity();
				roleUserRelationEntity.setRoleEntity(roleEntity);
				roleUserRelationEntity.setUserEntity(userEntity);
				roleUserRelationEntityList.add(roleUserRelationEntity);
			}
			
			success = super.insertEntityBatch(roleUserRelationEntityList);
			if(!success){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("设置人员角色过程中遇到错误，请稍后重试");
				return ajaxJson;
			}
		}

		
		//记录日志
		String logContent = "为["+roleEntity.getName()+"]设置人员," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson setRolePermission(String id, Object[] functionIds, Object[] operationIds,HttpServletRequest request) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		RoleEntity roleEntity = super.expandEntity(RoleEntity.class, id);
		if(null == roleEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("角色不存在，可能已删除");
			return ajaxJson;
		}
		
		String hql = null;
		List<FunctionEntity> functionEntityList = new ArrayList<FunctionEntity>();
		List<OperationEntity> operationEntityList = new ArrayList<OperationEntity>();
		Map<String, Object> _param = null;
		if (functionIds != null && functionIds.length > 0 && StringUtil.isNotEmpty(functionIds[0])) {
			_param = new HashMap<String, Object>();
			_param.put("ids", functionIds);
			hql = "FROM FunctionEntity WHERE id IN (:ids)";
			functionEntityList = super.queryListByHql(hql, _param);
			if (functionEntityList.size() == 0 || functionEntityList.size() != functionIds.length) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("某个功能不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		if (operationIds != null && operationIds.length > 0 && StringUtil.isNotEmpty(operationIds[0])) {
			_param = new HashMap<String, Object>();
			_param.put("ids", operationIds);
			hql = "FROM OperationEntity WHERE id IN (:ids)";
			
			operationEntityList = super.queryListByHql(hql, _param);
			if (operationEntityList.size() == 0 || operationEntityList.size() != operationIds.length) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("某个操作不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		hql = "FROM RoleFunctionRelationEntity WHERE roleEntity.id = ?";
		List<RoleFunctionRelationEntity> roleFunctionRelationEntityList = this.queryListByHql(hql, roleEntity.getId());
		Boolean success = super.deleteEntityBatch(roleFunctionRelationEntityList);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("设置角色权限过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		RoleFunctionRelationEntity roleFunctionRelationEntity = null;
		for (FunctionEntity functionEntity : functionEntityList) {
			roleFunctionRelationEntity = new RoleFunctionRelationEntity();
			roleFunctionRelationEntity.setRoleEntity(roleEntity);
			roleFunctionRelationEntity.setFunctionEntity(functionEntity);
			_param = new HashMap<String, Object>();
			_param.put("functionId", functionEntity.getId());
			_param.put("ids", operationIds);
			hql = "FROM OperationEntity WHERE functionEntity.id = :functionId AND id IN (:ids)";
			operationEntityList = super.queryListByHql(hql, _param);
			if (null == operationEntityList || operationEntityList.size() == 0) {
				success = super.insertEntity(roleFunctionRelationEntity);
				if(!success){
					ajaxJson.setSuccess(false);
					ajaxJson.setMessage("设置角色权限过程中遇到错误，请稍后重试");
					return ajaxJson;
				}
			} else {
				for (OperationEntity operationEntity : operationEntityList) {
					roleFunctionRelationEntity = new RoleFunctionRelationEntity();
					roleFunctionRelationEntity.setRoleEntity(roleEntity);
					roleFunctionRelationEntity.setFunctionEntity(functionEntity);
					roleFunctionRelationEntity.setOperationEntity(operationEntity);
					success = super.insertEntity(roleFunctionRelationEntity);
					if(!success){
						ajaxJson.setSuccess(false);
						ajaxJson.setMessage("设置角色权限过程中遇到错误，请稍后重试");
						return ajaxJson;
					}
				}
			}
		}
		
		hql = "select applicationEntity from FunctionEntity t where t.id = ?";
		String hql1 = "";
		CasSessionUtil casSessionUtil = new CasSessionUtil(request);
		String userOrgId = casSessionUtil.getUserOrgId();
		for (String funId : (String[])functionIds) {
			ApplicationEntity app = super.expandEntityByHql(hql,funId);
			hql1 = "select distinct organizationEntity.id from AppMirrorEntity where applicationEntity.id = ?";
			if(app != null){				
				List<String> orgCodeList = super.queryListByHql(hql1, app.getId());
				if(!orgCodeList.contains(userOrgId)){
					hql1 = "from OrganizationEntity where id = ?" ;
					OrganizationEntity org = super.expandEntityByHql(hql1,userOrgId);
					AppMirrorEntity appMirrorEntity = new AppMirrorEntity();
					//随机生成uuid
					int uuCode = UUID.randomUUID().toString().hashCode();
					//有可能是负数
					if(uuCode<0){
						uuCode = -uuCode;
					}
					// 0 代表前面补充0     
					// 4 代表长度为4     
					// d 代表参数为正数型
					String code = String.format("%015d", uuCode);
					appMirrorEntity.setCode(code);
					appMirrorEntity.setApplicationEntity(app);
					appMirrorEntity.setOrganizationEntity(org);
					super.insertEntity(appMirrorEntity);
				}
			}
		}
		
		//记录日志
		String logContent = "设置["+roleEntity.getName()+"]权限," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	/**
	 * 
	 * @Title: existsName 
	 * @Description: [校验角色名是否存在]
	 * @param organizationId
	 * @param id
	 * @param name
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsName(String organizationId, String id, String name) throws JumpException {
		RoleEntity roleEntity = null;
		
		String hql = "FROM RoleEntity WHERE organizationEntity.id = ? AND name = ? ";
		if(StringUtil.isEmpty(id)){
			roleEntity = super.expandEntityByHql(hql, new Object[]{organizationId, name});
			if(null == roleEntity){
				return false;
			}
		}else{
			hql += " AND id = ?";
			
			//未改动角色名称的情况
			roleEntity = super.expandEntity(RoleEntity.class, id);
			if(null != roleEntity && roleEntity.getName().equals(name)){
				return false;
			}
			
			roleEntity = super.expandEntityByHql(hql, new Object[]{organizationId, name, id});
			if(null == roleEntity){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsCode 
	 * @Description: [校验角色编码是否存在]
	 * @param organizationId
	 * @param id
	 * @param code
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsCode(String organizationId, String id, String code) throws JumpException {
		RoleEntity roleEntity = null;
		
		String hql = "FROM RoleEntity WHERE organizationEntity.id = ? AND code = ? ";
		if(StringUtil.isEmpty(id)){
			roleEntity = super.expandEntityByHql(hql, new Object[]{organizationId, code});
			if(null == roleEntity){
				return false;
			}
		}else{
			hql += " AND id = ?";
			
			//未改动角色编码的情况
			roleEntity = super.expandEntity(RoleEntity.class, id);
			if(null != roleEntity && roleEntity.getCode().equals(code)){
				return false;
			}
			
			roleEntity = super.expandEntityByHql(hql, new Object[]{organizationId, code, id});
			if(null == roleEntity){
				return false;
			}
		}
		
		return true;
	}

}
