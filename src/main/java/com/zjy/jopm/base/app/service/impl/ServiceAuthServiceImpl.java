package com.zjy.jopm.base.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.CollectionUtil;
import com.unist.util.StringUtil;
import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.ServiceAuthEntity;
import com.zjy.jopm.base.app.entity.ServiceInterfaceEntity;
import com.zjy.jopm.base.app.service.ServiceAuthService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.quiUtil.QuiUtils;

@Service("serviceAuthService")
@Transactional
public class ServiceAuthServiceImpl extends BaseServiceimpl implements ServiceAuthService{

	@Override
	public AjaxJson insertServiceAuth(String serviceId, String applicationIds) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		ServiceInterfaceEntity service = new ServiceInterfaceEntity();
		service = super.expandEntity(ServiceInterfaceEntity.class, serviceId);
		if(StringUtil.isNotEmpty(service.getId())){
			List<ServiceAuthEntity> serviceAuthList = new ArrayList<ServiceAuthEntity>();
			String hql = "from ServiceAuthEntity where serviceInterfaceEntity.id=?";
			serviceAuthList = super.queryListByHql(hql, new Object[]{serviceId});
			if(serviceAuthList.size()>0){
				super.deleteEntityBatch(serviceAuthList);
			}
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("数据错误，保存失败！");
			return ajaxJson;
		}
		
		
		String ids[] = null;
		
		if (StringUtil.isEmpty(applicationIds.trim())) {
			// do nothing
		} else {
			ids = applicationIds.split(",");
			List<ServiceAuthEntity> authList = new ArrayList<ServiceAuthEntity>();
			for (String applicationId : ids) {
				if (StringUtil.isEmpty(applicationId)) {
					continue;
				}
				ServiceAuthEntity serviceAuth = new ServiceAuthEntity();
				serviceAuth.setServiceInterfaceEntity(service);
				ApplicationEntity application = new ApplicationEntity();
				application = super.expandEntity(ApplicationEntity.class, applicationId);
				if(StringUtil.isNotEmpty(application.getId())){
					serviceAuth.setApplicationEntity(application);
					authList.add(serviceAuth);
				}
			}
			if (CollectionUtil.isNotEmpty(authList)) {
				super.saveEntityBatch(authList);
			}
		}
		
		ajaxJson.setSuccess(true);
		ajaxJson.setMessage("保存成功！");
		return ajaxJson;
	}

	@Override
	public AjaxJson getAppServiceTree(String applicationId, String path) throws JumpException {
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
		
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>(); 
		//特殊用户-指定用户名、密码，除此用户皆为正常系统用户
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
				hql = "SELECT COUNT(*) FROM ServiceInterfaceEntity WHERE applicationEntity.id = ?";
				parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
				treeNodeList.add(parentTreeNode);
			}
		} else{
			//无FunctionId传参的情况 
			String hql = "FROM ServiceInterfaceEntity WHERE applicationEntity.id = ? ";
			List<ServiceInterfaceEntity> serviceEntityList = super.queryListByHql(hql, new Object[]{applicationEntity.getId()});
			for (ServiceInterfaceEntity service : serviceEntityList) {
				TreeNode childTreeNode = new TreeNode();
				childTreeNode.setId(service.getId());
				childTreeNode.setName(service.getName());
				childTreeNode.setType(Constants.PERMISSION_TREE_TYPE[1]);
				
				childTreeNode.setIsParent(false);
				
				childTreeNode.setRootId(applicationEntity.getId());
				childTreeNode.setNocheck(true);
				treeNodeList.add(childTreeNode);
			}
		}
		ajaxJson.setObj(treeNodeList);
		
		return ajaxJson;
	}

	@Override
	public Map<String, Object> getApplicationQuiGrid(ApplicationEntity applicationEntity, int pageNo, int pageSize,
			String sort, String direction) throws JumpException {

		//获取当前登录用户
		String hql="FROM ApplicationEntity WHERE 1=1" ;
		Map<String,Object> paramMap = new HashMap<String, Object>();

		if(StringUtil.isNotEmpty(applicationEntity.getName())){
			hql+=" AND name LIKE :name";
			paramMap.put("name", "%"+applicationEntity.getName()+"%");
		}
		
		if(StringUtil.isNotEmpty(applicationEntity.getCode())){
			hql+=" AND code LIKE :code";
			paramMap.put("code", "%"+applicationEntity.getCode()+"%");
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

	@Override
	public AjaxJson getServiceAuthList(String serviceId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		List<String> appIds = new ArrayList<String>();
		Map<String,Object> result = new HashMap<String, Object>();
		List<ServiceAuthEntity> serviceAuthList = new ArrayList<ServiceAuthEntity>();
		String hql = "FROM ServiceAuthEntity WHERE serviceInterfaceEntity.id=?";
		serviceAuthList = super.queryListByHql(hql, new Object[]{serviceId});
		if(serviceAuthList.size()>0){
			for (ServiceAuthEntity serviceAuth : serviceAuthList) {
				appIds.add(serviceAuth.getApplicationEntity().getId());
			}
		}
		result.put("checkedAppIds", appIds);
		ajaxJson.setAttributes(result);
		return ajaxJson;
	}

	@Override
	public List<Map<String,Object>> getAppInstanceCodeAndUrl() {
		
//		List<ApplicationEntity> applicationList = super.queryListByClass(ApplicationEntity.class);
//		
//		String hql = "select serviceInterfaceEntity.url from ServiceAuthEntity where applicationEntity.id = ?";
//		
//		ArrayList<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		
//		for (ApplicationEntity application : applicationList) {
//			if(application.getUrl() != null ){				
//				List<Object> urlList = super.queryListByHql(hql, application.getId());
//				if(urlList != null && urlList.size() != 0){ 				
//					HashMap<String, Object> hashMap = new HashMap<String, Object>();
//					hashMap.put("instanceCode", application.getInstanceCode());
//					hashMap.put("authServiceList", urlList);
//					resultList.add(hashMap);
//				}
//			}
//		}
//		
//		return resultList;
		
		ArrayList<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		List<ServiceAuthEntity> queryListByClass = super.queryListByClass(ServiceAuthEntity.class);
		
		for (ServiceAuthEntity serviceAuthEntity : queryListByClass) {
			String instanceCode = serviceAuthEntity.getApplicationEntity().getInstanceCode();
			String authServiceList = serviceAuthEntity.getServiceInterfaceEntity().getUrl();
			int flag = 0;
			for (Map<String,Object> map : resultList) {				
				if(instanceCode.equals(map.get("instanceCode"))){
					flag = 1;		
					((ArrayList)map.get("authServiceList")).add(authServiceList);
				}			
			}
			if(flag == 0){
				List<String> list = new ArrayList<String>();
				list.add(authServiceList);
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("instanceCode", instanceCode);
				hashMap.put("authServiceList", list);
				resultList.add(hashMap);
			}
		}
		return resultList;
	}

}
