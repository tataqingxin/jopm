/** 
 * @Description:[功能接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.FunctionServiceImpl.java
 * @ClassName:FunctionServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:25:27
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:25:27  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

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
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.JumpBeanUtil;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.FunctionResourceRelationEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.app.service.FunctionService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.quiUtil.QuiUtils;
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: FunctionServiceImpl 
 * @Description: [功能接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:25:27 
 * @since JDK 1.6 
 */
@Service("functionService")
@Transactional
public class FunctionServiceImpl extends BaseServiceimpl implements FunctionService {
	
	@Autowired
	private LogExtService logExtService;

	@Override
	public Map<String, Object> getFunctionEntityQuiGrid(FunctionEntity functionEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException {
		String hql="FROM FunctionEntity WHERE 1=1" ;
		List<Object> param = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(functionEntity.getName())){
			hql+=" AND name LIKE ?";
			param.add("%"+functionEntity.getName()+"%");
		}
		
		if(StringUtil.isNotEmpty(functionEntity.getCode())){
			hql+=" AND code LIKE ?";
			param.add("%"+functionEntity.getCode()+"%");
		}
		
		hql += " AND applicationEntity.id = ?";
		param.add(functionEntity.getApplicationEntity().getId());
		
		if (StringUtil.isNotEmpty(functionEntity.getId())) {
			hql += " AND parentFunctionEntity.id = ?";
			param.add(functionEntity.getId());
		}
		
		if (StringUtil.isNotEmpty(functionEntity.getStatus())) {
			hql += " AND status = ?";
			param.add(functionEntity.getStatus());
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
		
		for (FunctionEntity fun : (List<FunctionEntity>)pageList.getResultList()) {
			hql = "select tagEntity.name from TagFunctionRelationEntity where functionEntity.id = ?";
			List<String> nameList = super.queryListByHql(hql, fun.getId());
			StringBuffer s = new StringBuffer();
			for (String string : nameList) {
				s.append(string+",");
			}
			if(nameList.size() > 0){				
				fun.setTagName(s.substring(0, s.length()-1));
			}
		}
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}
	
	@Override
	public AjaxJson insertFunctionEntity(FunctionEntity functionEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();

		String applicationId = functionEntity.getApplicationEntity().getId();
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续保存操作");
			return ajaxJson;
		}
		functionEntity.setApplicationEntity(applicationEntity);
		
		IconEntity iconEntity = super.expandEntity(IconEntity.class, functionEntity.getIconEntity().getId());
		if(null != iconEntity){
			functionEntity.setIconEntity(iconEntity);// 设置图标信息
		}
		
		Boolean success = this.existsName(applicationId, functionEntity.getId(), functionEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(applicationId, functionEntity.getId(), functionEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能编码已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsURL(applicationId, functionEntity.getId(), functionEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能URL已存在，请更改");
			return ajaxJson;
		}
		
		success = super.insertEntity(functionEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "保存功能：["+functionEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson updateFunctionEntity(FunctionEntity functionEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity function = super.expandEntity(FunctionEntity.class, functionEntity.getId());
		if(null == function){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		String applicationId = functionEntity.getApplicationEntity().getId();
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续更改操作");
			return ajaxJson;
		}
		functionEntity.setApplicationEntity(applicationEntity);
		
		IconEntity iconEntity = super.expandEntity(IconEntity.class, functionEntity.getIconEntity().getId());
		if(null != iconEntity){
			functionEntity.setIconEntity(iconEntity);// 设置图标信息
		}
		
		Boolean success = this.existsName(applicationId, functionEntity.getId(), functionEntity.getName());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能名已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsCode(applicationId, functionEntity.getId(), functionEntity.getCode());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能编码已存在，请更改");
			return ajaxJson;
		}
		
		success = this.existsURL(applicationId, functionEntity.getId(), functionEntity.getUrl());
		if(success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能URL已存在，请更改");
			return ajaxJson;
		}
		
		JumpBeanUtil.copyBeanNotNull2Bean(functionEntity, function);
		
		success = super.updateEntity(function);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "修改功能：["+functionEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_UPDATE.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson expandFunctionEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, id);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，可能已删除");
			return ajaxJson;
		}
		
		ajaxJson.setObj(functionEntity);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteFunctionEntity(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, id);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，可能已删除");
			return ajaxJson;
		}
		
		List<FunctionResourceRelationEntity> functionResourceRelationList = super.queryListByProperty(FunctionResourceRelationEntity.class, "functionEntity.id", functionEntity.getId());
		if(functionResourceRelationList.size()>0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能和资源存在关联数据数据，不能删除");
			return ajaxJson;
		}
		
		List<OperationEntity> operationList = super.queryListByProperty(OperationEntity.class, "functionEntity.id", functionEntity.getId());
		if(operationList.size()>0){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能和操作存在关联数据数据，不能删除");
			return ajaxJson;
		}
		
		Boolean success = super.deleteEntity(functionEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "删除功能：["+functionEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}

	@Override
	public AjaxJson enableFunctionStatus(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, id);
		if(null == functionEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("功能不存在，可能已删除");
			return ajaxJson;
		}
		
		String status = functionEntity.getStatus();
		if(Constants.ENABLE_STATUS.equals(functionEntity.getStatus())) {
			status = Constants.DISABLE_STATUS;// 如果启用，则禁用
		}
		if(Constants.DISABLE_STATUS.equals(functionEntity.getStatus())) {
			status = Constants.ENABLE_STATUS;// 如果禁用，则启用
		}
		functionEntity.setStatus(status);
		
		Boolean success = super.updateEntity(functionEntity);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("启禁用过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		//记录日志
		String logContent = "启/禁用功能：["+functionEntity.getName()+"]," + ajaxJson.getMessage();
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_DEL.toString());
		
		return ajaxJson;
	}

	/**
	 * 
	 * @Title: existsName 
	 * @Description: [某应用下的功能名是否存在]
	 * @param applicationId
	 * @param id
	 * @param name
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsName(String applicationId, String id, String name) throws JumpException {
		FunctionEntity functionEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			functionEntity = super.expandEntity(FunctionEntity.class, id);
			if(null != functionEntity && functionEntity.getName().equals(name)){
				return false;
			}
		}
		
		String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND name = ? ";
		functionEntity = super.expandEntityByHql(hql, new Object[]{applicationId, name});
		if(null == functionEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsCode 
	 * @Description: [某应用下的功能编码是否存在]
	 * @param applicationId
	 * @param id
	 * @param code
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsCode(String applicationId, String id, String code) throws JumpException {
		FunctionEntity functionEntity = null;
		
		if(StringUtil.isNotEmpty(id)){
			functionEntity = super.expandEntity(FunctionEntity.class, id);
			if(null != functionEntity && functionEntity.getCode().equals(code)){
				return false;
			}
		}
		
		String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND code = ? ";
		functionEntity = super.expandEntityByHql(hql, new Object[]{applicationId, code});
		if(null == functionEntity){
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @Title: existsURL 
	 * @Description: [某应用下的功能URL是否存在]
	 * @param applicationId
	 * @param id
	 * @param url
	 * @return
	 * @throws JumpException
	 */
	private Boolean existsURL(String applicationId, String id, String url) throws JumpException {
		FunctionEntity functionEntity = null;
		
		
		if(StringUtil.isNotEmpty(id)){
			functionEntity = super.expandEntity(FunctionEntity.class, id);
			if(null != functionEntity && functionEntity.getUrl().equals(url)){
				return false;
			}
		}
		
		String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND url = ? ";
		functionEntity = super.expandEntityByHql(hql, new Object[]{applicationId, url});
		if(null == functionEntity){
			return false;
		}
		
		return true;
	}
	
	@Override
	public AjaxJson getApplicaitonTreeNode(String applicationId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，可能已删除");
			return ajaxJson;
		}
		
		TreeNode treeNode = new TreeNode();
		treeNode.setId(applicationId);
		treeNode.setName(applicationEntity.getName());
		treeNode.setParentId(String.valueOf(0));
		// treeNode.setUrl(applicationEntity.getUrl());
		treeNode.setNocheck(true);
		String hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ?";
		treeNode.setIsParent(super.countByHql(hql, new Object[]{applicationId}) > 0);
		treeNode.setType(Constants.PERMISSION_TREE_TYPE[0]);
		
		ajaxJson.setObj(treeNode);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson getFunctionTree(String applicationId, String functionId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		ApplicationEntity applicationEntity = super.expandEntity(ApplicationEntity.class, applicationId);
		if(null == applicationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，可能已删除");
			return ajaxJson;
		}
		
		if(StringUtil.isNotEmpty(functionId)){
			FunctionEntity functionEntity = super.expandEntity(FunctionEntity.class, functionId);
			if(null == functionEntity){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("功能不存在，可能已删除");
				return ajaxJson;
			}
		}
		
		String hql = null;
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>(); 
		
		//FunctionID为空的时候，默认返回根节点；相反则返回功能节点列表
		if(StringUtil.isEmpty(functionId)){
			hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND parentFunctionEntity.id IS NULL";
			List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationId});
			for (FunctionEntity functionEntity : functionEntityList) {
				TreeNode childTreeNode = new TreeNode();
				childTreeNode.setId(functionEntity.getId());
				childTreeNode.setName(functionEntity.getName());
				if (null != functionEntity.getParentFunctionEntity()) {
					childTreeNode.setParentId(functionEntity.getParentFunctionEntity().getId());
				}
				// childTreeNode.setUrl(functionEntity.getUrl());
				childTreeNode.setIsParent(functionEntity.getChildFunctionEntity().size() > 0);
				childTreeNode.setNocheck(true);
				childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
				treeNodeList.add(childTreeNode);
			}
		}else{
			hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND parentFunctionEntity.id = ?";
			List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationId, functionId});
			for (FunctionEntity functionEntity : functionEntityList) {
				TreeNode childTreeNode = new TreeNode();
				childTreeNode.setId(functionEntity.getId());
				childTreeNode.setName(functionEntity.getName());
				if (null != functionEntity.getParentFunctionEntity()) {
					childTreeNode.setParentId(functionEntity.getParentFunctionEntity().getId());
				}
				// childTreeNode.setUrl(functionEntity.getUrl());
				childTreeNode.setIsParent(functionEntity.getChildFunctionEntity().size() > 0);
				childTreeNode.setNocheck(true);
				childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
				treeNodeList.add(childTreeNode);
			}
		}
		
		ajaxJson.setObj(treeNodeList);
		
		return ajaxJson;
	}
	
	@Override
	public AjaxJson getMenuTree(String applicationId, String functionId, String path) throws JumpException {
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
		
		HttpSession session = ContextHolderUtil.getSession();
		if (null == session) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("会话已失效，请重新登陆");
			return ajaxJson;
		}
		
		SessionInfo sessionInfo = (SessionInfo)session.getAttribute(Globals.USER_SESSION);
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>(); 
		//特殊用户-指定用户名、密码，除此用户皆为正常系统用户
		if (sessionInfo.getIdentity() == 1) {
			//2、若数据初始化完成，则展示基础功能节点
			if (StringUtil.isEmpty(applicationId)) {
				//1、特殊入口
				TreeNode treeNode = new TreeNode();
				treeNode.setId(null);
				treeNode.setName("初始化数据");
				treeNode.setParentId(String.valueOf(0));
				treeNodeList.add(treeNode);
				treeNode.setUrl("webpage/base/init/init.html");
				
				String hql = "FROM ApplicationEntity WHERE 1=1 AND status = '1' ";
				List<ApplicationEntity> applicationEntityList = super.queryListByHql(hql);
				for (ApplicationEntity application : applicationEntityList) {
					TreeNode parentTreeNode = new TreeNode();
					parentTreeNode.setIcon(path+application.getIconEntity().getIconPath());
					parentTreeNode.setId(application.getId());
					parentTreeNode.setName(application.getName());
					parentTreeNode.setParentId(String.valueOf(0));
					parentTreeNode.setUrl(application.getUrl());
					parentTreeNode.setType(Constants.PERMISSION_TREE_TYPE[0]);
					parentTreeNode.setNocheck(true);
					hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ? AND status = '1' ";
					parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
					treeNodeList.add(parentTreeNode);
				}
			} else {
				if (StringUtil.isEmpty(functionId)) {
					//3、无FunctionId传参的情况 
					String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND status = '1' ";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId()});
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						if (null != function.getParentFunctionEntity()) {
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setIcon(path+function.getIconEntity().getIconPath());
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(true);
						childTreeNode.setUrl(function.getUrl());
						childTreeNode.setIsParent(function.getChildFunctionEntity().size() > 0);
						treeNodeList.add(childTreeNode);
					}
				} else {
					//4、接收FunctionID传参的情况
					String hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND parentFunctionEntity.id = ? AND status = '1' ";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId(), functionId});
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						childTreeNode.setIcon(path+function.getIconEntity().getIconPath());
						if (null != function.getParentFunctionEntity()) {
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(true);
						childTreeNode.setUrl(function.getUrl());
						childTreeNode.setIsParent(function.getChildFunctionEntity().size() > 0);
						treeNodeList.add(childTreeNode);
					}
				}
			}
		} else {
			//获取当前登录用户
			UserEntity userEntity = sessionInfo.getUser();
			//1、根据用户获取机构，不考虑跨机构的情况
			//OrganizationEntity organizationEntity = userEntity.getOrganizationEntity();
			//2、根据机构、用户获取角色
			String hql = "SELECT DISTINCT id FROM RoleEntity WHERE  id IN (SELECT roleEntity.id FROM RoleUserRelationEntity WHERE userEntity.id = ?)";
			List<String> roleIds = super.queryListByHql(hql, new Object[]{userEntity.getId()});
			//3、根据角色获取功能
			Map<String, Object> _param = null;
			_param = new HashMap<String, Object>();
			_param.put("ids", roleIds.size()>0?roleIds:"");
			hql = "SELECT functionEntity FROM RoleFunctionRelationEntity r WHERE r.roleEntity.id IN (:ids)";
			List<FunctionEntity> functionList = super.queryListByHql(hql, _param);
			List<String> functionIds = new ArrayList<String>();
			//获取机构id
			String organizationId = sessionInfo.getOrganizationId();
			hql = "from AppMirrorEntity where organizationEntity.id = ? and applicationEntity.id =? and isEffective  = 'y'";
			for (FunctionEntity fun : functionList) {
				AppMirrorEntity appMir = super.expandEntityByHql(hql, organizationId,fun.getApplicationEntity().getId());
				if(appMir != null){
					functionIds.add(fun.getId());
				}
			}
			
			//new 4、从标签获取功能,且去重
			hql = "SELECT DISTINCT functionEntity.id FROM TagFunctionRelationEntity WHERE tagEntity.id IN (SELECT DISTINCT tagEntity.id "
					+ "FROM TagRoleRelationEntity WHERE roleEntity.id IN (:ids))";
			List<String> tagfunctionIds = super.queryListByHql(hql, _param);
			functionIds.addAll(tagfunctionIds);
			Set<String> functionIdSet = new HashSet<String>();
			functionIdSet.addAll(functionIds);
			functionIds = new ArrayList<String>();
			functionIds.removeAll(functionIds);
			functionIds.addAll(functionIdSet);		
			
			//默认获取所有的应用，同级节点
			if (StringUtil.isEmpty(applicationId)) {
				//4、根据功能获取应用
				_param = new HashMap<String, Object>();
				_param.put("ids", functionIds.size()>0?functionIds:"");
				hql = "SELECT DISTINCT applicationEntity.id FROM FunctionEntity WHERE id IN (:ids)  ";
				List<String> applicationIds = super.queryListByHql(hql, _param);
				//5、某用户具有权限的应用列表
				_param = new HashMap<String, Object>();
				_param.put("ids", applicationIds.size()>0?applicationIds:"");
				hql = "FROM ApplicationEntity WHERE id IN (:ids) AND status = '1' ";
				List<ApplicationEntity> applicationEntityList = super.queryListByHql(hql, _param);
				for (ApplicationEntity application : applicationEntityList) {
					TreeNode parentTreeNode = new TreeNode();
					parentTreeNode.setId(application.getId());
					parentTreeNode.setIcon(path + application.getIconEntity().getIconPath());
					parentTreeNode.setName(application.getName());
					parentTreeNode.setParentId(String.valueOf(0));
					parentTreeNode.setUrl(application.getUrl());
					parentTreeNode.setNocheck(true);
					parentTreeNode.setType(Constants.PERMISSION_TREE_TYPE[0]);
					hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ? AND status = '1' ";
					parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
					treeNodeList.add(parentTreeNode);
				}
			} else {
				//6、获取某个应用的功能列表
				if (StringUtil.isEmpty(functionId)) {
					//7、无FunctionId传参的情况 
					_param = new HashMap<String, Object>();
					_param.put("applicationId", applicationEntity.getId());
					_param.put("ids", functionIds.size()>0?functionIds:"");
					hql = "FROM FunctionEntity WHERE applicationEntity.id = :applicationId AND id IN (:ids) AND status = '1' ";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, _param);
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						if (null != function.getParentFunctionEntity()) {
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setIcon(path+function.getIconEntity().getIconPath());
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(true);
						childTreeNode.setUrl(function.getUrl());
						childTreeNode.setIsParent(function.getChildFunctionEntity().size() > 0);
						treeNodeList.add(childTreeNode);
					}
				} else {
					//8、接收FunctionID传参的情况
					hql = "FROM FunctionEntity WHERE applicationEntity.id = ? AND parentFunctionEntity.id = ? AND status = '1' ";
					List<FunctionEntity> functionEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId(), functionId});
					for (FunctionEntity function : functionEntityList) {
						TreeNode childTreeNode = new TreeNode();
						childTreeNode.setId(function.getId());
						childTreeNode.setName(function.getName());
						if (null != function.getParentFunctionEntity()) {
							childTreeNode.setParentId(function.getParentFunctionEntity().getId());
						}
						childTreeNode.setIcon(path + function.getIconEntity().getIconPath());
						childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
						childTreeNode.setRootId(applicationEntity.getId());
						childTreeNode.setNocheck(true);
						childTreeNode.setUrl(function.getUrl());
						childTreeNode.setIsParent(function.getChildFunctionEntity().size() > 0);
						treeNodeList.add(childTreeNode);
					}
				}
			}
		}
		
		ajaxJson.setObj(treeNodeList);
		
		return ajaxJson;
	}
}
