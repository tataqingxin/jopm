/** 
 * @Description:[应用及子功能对外接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.ext.AppExtServiceImpl.java
 * @ClassName:AppExtServiceImpl
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月13日 下午3:30:15
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月13日 下午3:30:15  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service.impl.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.app.service.ext.AppExtService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: AppExtServiceImpl 
 * @Description: [应用及子功能对外接口] 
 * @authorLu Guoqiang
 * @date 2016年5月13日 下午3:30:15 
 * @since JDK 1.6 
 */
@Service("appExtService")
@Transactional
public class AppExtServiceImpl extends BaseServiceimpl implements AppExtService {

	@Override
	public AjaxJson getOperationTree(String roleId, String applicationId, String functionId, String path, String orgId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();

		ApplicationEntity applicationEntity = null;
		if(StringUtil.isNotEmpty(applicationId)){
			applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
			if(null == applicationEntity){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("应用不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		FunctionEntity functionEntity = null;
		if(StringUtil.isNotEmpty(functionId)){
			functionEntity = super.expandEntity(FunctionEntity.class, functionId);
			if(null == functionEntity){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("功能不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		SessionInfo sessionInfo = (SessionInfo)ContextHolderUtil.getSession().getAttribute(Globals.USER_SESSION);
		
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>(); 
		//特殊用户-指定用户名、密码，除此用户皆为正常系统用户
		if (sessionInfo.getIdentity() == 1) {
			if (StringUtil.isEmpty(applicationId)) {
				//默认查询所有应用，同级节点
				String hql = "FROM ApplicationEntity WHERE 1=1";
				List<ApplicationEntity> applicationEntityList = super.queryListByHql(hql);
				for (ApplicationEntity application : applicationEntityList) {
					TreeNode parentTreeNode = new TreeNode();
					parentTreeNode.setId(application.getId());
					parentTreeNode.setName(application.getName());
					parentTreeNode.setIcon(path + application.getIconEntity().getIconPath());
					parentTreeNode.setParentId(String.valueOf(0));
					parentTreeNode.setType(Constants.PERMISSION_TREE_TYPE[0]);
					parentTreeNode.setNocheck(true);
					hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ?";
					parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
					treeNodeList.add(parentTreeNode);
				}
				
				ajaxJson.setAttributes(getFuncOperMap(roleId));
			} else {
				if (StringUtil.isEmpty(functionId)) {
					//无FunctionId传参的情况 
					String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND status = '1' AND parentFunctionEntity.id IS NULL";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId()});
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						childTreeNode.setIcon(path + function.getIconEntity().getIconPath());
						if(null != function.getParentFunctionEntity()){
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						
						//查询functionId所有的操作
						List<OperationEntity> operationEntityList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", function.getId());
						Boolean isParent = function.getChildFunctionEntity().size() > 0 || operationEntityList.size() > 0;
						childTreeNode.setIsParent(isParent);
						
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(false);
						treeNodeList.add(childTreeNode);
					}
				} else {
					//接收FunctionID传参的情况
					String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND parentFunctionEntity.id = ?";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId(), functionId});
					
					//查询functionId所有的操作
					List<OperationEntity> operationEntityList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", functionId);
					
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						childTreeNode.setIcon(path + function.getIconEntity().getIconPath());
						if(null != function.getParentFunctionEntity()){
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);

						//查询子功能所有的操作
						List<OperationEntity> childOperationEntityList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", function.getId());
						Boolean isParent = function.getChildFunctionEntity().size() > 0 || childOperationEntityList.size() > 0;
						childTreeNode.setIsParent(isParent);
						
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(false);
						treeNodeList.add(childTreeNode);
						
						
						
					}
					
					for (OperationEntity operation : operationEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(operation.getId());
						childTreeNode.setName(operation.getName());
						childTreeNode.setIcon(path + operation.getIconEntity().getIconPath());
						childTreeNode.setParentId(String.valueOf(0));
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[2]);
						childTreeNode.setIsParent(false);
						childTreeNode.setNocheck(false);
						treeNodeList.add(childTreeNode);
					}
				}
			}
		} else {
			//获取当前登录用户
//			UserEntity userEntity = sessionInfo.getUser();
			//根据用户获取机构
			OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, orgId);
			//默认获取所有的应用，同级节点
			if (StringUtil.isEmpty(applicationId)) {
//				String hql = "FROM ApplicationEntity WHERE organizationEntity.id = ?";
				//查询该机构开通的应用
				String hql = "FROM AppMirrorEntity WHERE 1=1 AND organizationEntity.id =? and isEffective = 'y'";
				List<AppMirrorEntity> appMirrorList = super.queryListByHql(hql, new Object[]{organizationEntity.getId()});
				
				List<String> appIdlist = new ArrayList<String>();
				for (AppMirrorEntity appMirror : appMirrorList) {
					TreeNode parentTreeNode = new TreeNode();
					ApplicationEntity application = appMirror.getApplicationEntity();
					appIdlist.add(application.getId());
					parentTreeNode.setId(application.getId());
					parentTreeNode.setName(application.getName());
					parentTreeNode.setIcon(path + application.getIconEntity().getIconPath());
					parentTreeNode.setParentId(String.valueOf(0));
					parentTreeNode.setType(Constants.PERMISSION_TREE_TYPE[0]);
					parentTreeNode.setNocheck(true);
					hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ?";
					parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
					treeNodeList.add(parentTreeNode);
				}
//				hql = "from ApplicationEntity where strategy = '1'";
//				List<ApplicationEntity> applicationList = super.queryListByHql(hql);
				List<ApplicationEntity> applicationList = getApplicationByStrategy();
				if(null == applicationList || applicationList.size() == 0) {					
					if (null == appMirrorList || appMirrorList.size() == 0) {
						ajaxJson.setSuccess(false);
						ajaxJson.setMessage("无法分配权限，所在机构无应用");
						return ajaxJson;
					}
				}
				for (ApplicationEntity application : applicationList) {
					if(!appIdlist.contains(application.getId())){
						TreeNode parentTreeNode = new TreeNode();
						parentTreeNode.setId(application.getId());
						parentTreeNode.setName(application.getName());
						parentTreeNode.setIcon(path + application.getIconEntity().getIconPath());
						parentTreeNode.setParentId(String.valueOf(0));
						parentTreeNode.setType(Constants.PERMISSION_TREE_TYPE[0]);
						parentTreeNode.setNocheck(true);
						hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ?";
						parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
						treeNodeList.add(parentTreeNode);
					}
				}
				ajaxJson.setAttributes(getFuncOperMap(roleId));
			} else {
				//获取某个应用的功能列表
				if (StringUtil.isEmpty(functionId)) {
					//无FunctionId传参的情况 
					String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND status = '1' AND parentFunctionEntity.id IS NULL";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId()});
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						childTreeNode.setIcon(path + function.getIconEntity().getIconPath());
						if(null != function.getParentFunctionEntity()){
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						
						//查询functionId所有的操作
						List<OperationEntity> operationEntityList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", function.getId());
						Boolean isParent = function.getChildFunctionEntity().size() > 0 || operationEntityList.size() > 0;
						childTreeNode.setIsParent(isParent);
						
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(false);
						treeNodeList.add(childTreeNode);
					}
				} else {
					//接收FunctionID传参的情况
					String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND parentFunctionEntity.id = ?";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId(), functionId});
					
					//查询functionId所有的操作
					List<OperationEntity> operationEntityList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", functionId);
					
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						childTreeNode.setIcon(path + function.getIconEntity().getIconPath());
						if(null != function.getParentFunctionEntity()){
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						
						//查询子功能所有的操作
						List<OperationEntity> childOperationEntityList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", function.getId());
						Boolean isParent = function.getChildFunctionEntity().size() > 0 || childOperationEntityList.size() > 0;
						childTreeNode.setIsParent(isParent);
						
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(false);
						treeNodeList.add(childTreeNode);
					}
					
					for (OperationEntity operation : operationEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(operation.getId());
						childTreeNode.setName(operation.getName());
						childTreeNode.setIcon(path + operation.getIconEntity().getIconPath());
						childTreeNode.setParentId(String.valueOf(0));
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[2]);
						childTreeNode.setIsParent(false);
						childTreeNode.setNocheck(false);
						treeNodeList.add(childTreeNode);
					}
				}
			}
		}
		ajaxJson.setObj(treeNodeList);
		
		return ajaxJson;
	}
	
	/**
	 * 
	 * @Title: getFuncOperMap 
	 * @Description: [默认情况返回选中角色对应的功能、操作数值]
	 * @param identity
	 * @return
	 */
	private Map<String, Object> getFuncOperMap(String roleId){
		Map<String, Object> funcOperMap = new HashMap<String, Object>();
		
		List<String> functionIds = new ArrayList<String>();
		List<String> operationIds = new ArrayList<String>();
		String hql = "FROM RoleFunctionRelationEntity WHERE roleEntity.id = ?";
		List<RoleFunctionRelationEntity> roleFunctionRelationEntityList = super.queryListByHql(hql, new Object[]{roleId});
		for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunctionRelationEntityList) {
			if (null != roleFunctionRelationEntity.getFunctionEntity()) {
				String functionId = roleFunctionRelationEntity.getFunctionEntity().getId();
				if (!functionIds.contains(functionId)) {
					functionIds.add(functionId);
				}
			}
			
			if (null != roleFunctionRelationEntity.getOperationEntity()) {
				String operationId = roleFunctionRelationEntity.getOperationEntity().getId();
				if (!operationIds.contains(operationId)) {
					operationIds.add(operationId);
				}
				
			}
		}
		funcOperMap.put("checkedFunctionIds", functionIds);
		funcOperMap.put("checkedOperationIds", operationIds);
		return funcOperMap;
	}
	
	/**
	 * 通过策略返回应用
	 * @return
	 */
	public List<ApplicationEntity> getApplicationByStrategy(){
		String hql = "from ApplicationEntity where strategy = '1'";
		List<ApplicationEntity> applicationList = super.queryListByHql(hql);
		return applicationList;
	}

}
