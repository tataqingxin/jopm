package com.zjy.jopm.service.open.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationResourceRelationEntity;
import com.zjy.jopm.base.app.service.ext.AppExtService;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.sdk.service.OpenSecurityService;


@Service("openSecurityService")
@Transactional
public class OpenSecurityServiceImpl implements OpenSecurityService {

	@Autowired
	private AppExtService appExtService;

	@Override
	public boolean hasVisitAuth(String requestUrl,String userName) {
		// 是否是功能表中管理的URL
		boolean isFunctionUrl = false;
		String hql = "FROM FunctionEntity WHERE 1=1";
		List<FunctionEntity> functionEntityList = this.appExtService
				.queryListByHql(hql);
		for (FunctionEntity functionEntity : functionEntityList) {
			String applicationUrl = functionEntity.getApplicationEntity().getUrl();
			String functionUrl = applicationUrl + functionEntity.getUrl();
			if (functionUrl.contains(requestUrl)) {
				isFunctionUrl = true;
				break;
			}
		}
		
		boolean isResourceUrl = false;
		hql = "FROM OperationResourceRelationEntity";
		List<OperationResourceRelationEntity> operationResourceRelationEntityListAll = appExtService.queryListByHql(hql);
		for (OperationResourceRelationEntity operationResourceRelationEntity : operationResourceRelationEntityListAll) {
			if (null != operationResourceRelationEntity.getResourceEntity()) {
				String applicationUrl = operationResourceRelationEntity.getOperationEntity().getFunctionEntity().getApplicationEntity().getUrl();
				String resourcesUrl = applicationUrl + operationResourceRelationEntity.getResourceEntity().getUrl();
				if (resourcesUrl.contains(requestUrl)) {
					isResourceUrl = true;
					break;
				}
			}
		}


		// 未绑定功能菜单和功能按钮的资源地址，可以直接访问
		if (!isFunctionUrl && !isResourceUrl) {
			return true;
		}
		
		
		UserEntity userEntity = appExtService.expandEntityByHql("from UserEntity where userName = ?", userName);
		if (userEntity == null || StringUtil.isEmpty(userEntity.getId())) {
			return false;
		}
		
		// @TODO: CONTACT WITH LUGUOQIANG FOR THE FOLLOWING CODE!!!
		

		List<String> functionList = new ArrayList<String>();
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> _param = null;

		//获取用户拥有的所有角色
		hql = "SELECT DISTINCT roleEntity.id FROM RoleUserRelationEntity WHERE userEntity.id = ?";
		List<String> roleIds = appExtService.queryListByHql(hql, userEntity.getId());
		if (null != roleIds && roleIds.size() == 0) {
			
			return false;
		}
		
		//设置用户角色
		
		for (String roleId : roleIds) {
			List<String> _operationIds = new ArrayList<String>();// 操作集合
			
			_param = new HashMap<String, Object>();
			_param.put("roleId", roleId);
			hql = "FROM RoleFunctionRelationEntity WHERE roleEntity.id = :roleId";
			List<RoleFunctionRelationEntity> roleFunctionRelationEntityList = appExtService.queryListByHql(hql, _param);
			for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunctionRelationEntityList) {
				if (null != roleFunctionRelationEntity.getFunctionEntity()) {
					String functionUrl = roleFunctionRelationEntity.getFunctionEntity().getUrl();
					
					String applicationUrl = roleFunctionRelationEntity.getFunctionEntity().getApplicationEntity().getUrl();
					functionUrl = applicationUrl + functionUrl;
					
					if (!functionList.contains(functionUrl)) {
						functionList.add(functionUrl);// 设置用户功能操作权限
					}
				}
				if (null != roleFunctionRelationEntity.getOperationEntity()) {
					String operationId = roleFunctionRelationEntity.getOperationEntity().getId();
					if (!_operationIds.contains(operationId)) {
						_operationIds.add(operationId);
					}
				}
			}
			
			if (_operationIds.size() > 0) {
				_param.put("operationIds", _operationIds.size()>0?_operationIds:"");
				hql = "FROM OperationResourceRelationEntity WHERE operationEntity.id IN (:operationIds)";
				List<OperationResourceRelationEntity> operationResourceRelationEntityList = appExtService.queryListByHql(hql, _param);
				for (OperationResourceRelationEntity operationResourceRelationEntity : operationResourceRelationEntityList) {
					if (null != operationResourceRelationEntity.getResourceEntity()) {
						String applicationUrl = operationResourceRelationEntity.getOperationEntity().getFunctionEntity().getApplicationEntity().getUrl();
						String resourcesUrl = applicationUrl + operationResourceRelationEntity.getResourceEntity().getUrl();
						resourceList.add(resourcesUrl);// 设置用户功能操作权限
					}
				}
			} 
		}
		
		
		

		if (isFunctionUrl) {
			if (null != functionList && functionList.contains(requestUrl)) {
				return true;
			}
		}

		if (isResourceUrl) {
			if (null != resourceList && resourceList.contains(requestUrl)) {
				return true;
			}
		}

		return false;

	}
}
