package com.zjy.jopm.visitor.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.StringUtil;
import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.ResourceUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.quiUtil.QuiUtils;
import com.zjy.jopm.visitor.entity.VisitorEntity;
import com.zjy.jopm.visitor.service.VisitorService;

@Service("visitorService")
@Transactional
public class VisitorServiceImpl extends BaseServiceimpl implements VisitorService{

	private static String projectAdd;
	
	
	@Override
	public Map<String, Object> getVisitorQuiGrid(VisitorEntity visitor,
			int pageNo, int pageSize, String sort, String direction)
			throws JumpException {
		String hql="FROM VisitorEntity WHERE 1=1 " ;
		List<Object> param = new ArrayList<Object>();
		
		QueryCondition queryCondition= new QueryCondition(hql, param, pageNo, pageSize);
		PageList pageList=super.queryListByHqlWithPage(queryCondition);// 结果集
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	@Override
	public AjaxJson insertVisitor(String functionIds) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		String ids[] = null;
		if(StringUtil.isNotEmpty(functionIds)){
			ids = functionIds.split(",");
		}else{
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("请选择功能！");
			return ajaxJson;
		}
		if(null!=ids){
			List<VisitorEntity> visitorList = new ArrayList<VisitorEntity>();
			visitorList = super.queryListByClass(VisitorEntity.class);
			this.deleteEntityBatch(visitorList);
		}
		for (String functionId : ids) {
			FunctionEntity function = new FunctionEntity();
			function = super.expandEntity(FunctionEntity.class, functionId);
			if(null!=function){
				VisitorEntity visitor = new VisitorEntity();
				visitor.setFunctionEntity(function);
				visitor.setApplicationEntity(function.getApplicationEntity());
				this.insertEntity(visitor);
			}else{
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("保存过程中遇到错误，请稍后重试");
			}
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson updateVisitor(VisitorEntity visitor) throws JumpException {
		return null;
	}

	@Override
	public AjaxJson expandVisitor(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		VisitorEntity visitor = new VisitorEntity();
		visitor = this.expandEntity(VisitorEntity.class, id);
		if(null==visitor){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("该游客功能不存在，请稍后重试！");
		}else{
			ajaxJson.setObj(visitor);
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteVisitor(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		VisitorEntity visitor = new VisitorEntity();
		visitor = this.expandEntity(VisitorEntity.class, id);
		if(null==visitor){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("该游客功能不存在，请稍后重试！");
		}
		Boolean flag = this.deleteEntity(visitor);
		if(!flag){
			ajaxJson.setSuccess(flag);
			ajaxJson.setMessage("删除过程中遇到错误，请稍后重试");
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson getAppFunctionTree(String applicationId, String functionId,String path) throws JumpException {
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
				hql = "SELECT COUNT(*) FROM FunctionEntity WHERE applicationEntity.id = ?";
				parentTreeNode.setIsParent(super.countByHql(hql, new Object[]{application.getId()}) > 0);
				treeNodeList.add(parentTreeNode);
			}
			
			ajaxJson.setAttributes(getFuncOperMap());
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
		ajaxJson.setObj(treeNodeList);
		
		return ajaxJson;
	}
	
	private Map<String, Object> getFuncOperMap(){
		Map<String, Object> funcOperMap = new HashMap<String, Object>();
		
		List<String> functionIds = new ArrayList<String>();
		List<VisitorEntity> visitorEntityList = super.queryListByClass(VisitorEntity.class);
		for (VisitorEntity visitor : visitorEntityList) {
			if (null != visitor.getFunctionEntity()) {
				String functionId = visitor.getFunctionEntity().getId();
				if (!functionIds.contains(functionId)) {
					functionIds.add(functionId);
				}
			}
		}
		funcOperMap.put("checkedFunctionIds", functionIds);
		return funcOperMap;
	}

	@Override
	public Object getVisitorSwitch() throws JumpException {
		Map<String,Object> result = new HashMap<String, Object>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = "config.properties";
		InputStream is = classLoader.getResourceAsStream(path);
//		FileInputStream inputStream = new FileInputStream(path);
		Properties prop = new Properties();
		// 加载
		try {
			prop.load(is);
			String visitorSwitch = prop.getProperty("visitorSwitch");
			result.put("visitorSwitch", visitorSwitch);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Object getVisitorFunction(HttpServletRequest request) throws JumpException {
		projectAdd = getPath(request);
		List<VisitorEntity> visitorList = new ArrayList<VisitorEntity>();
		visitorList = super.queryListByClass(VisitorEntity.class);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (VisitorEntity visitor : visitorList) {
			Map<String,Object> node = new HashMap<String, Object>();
			node.put("id", visitor.getFunctionEntity().getId());
			node.put("menuName", visitor.getFunctionEntity().getName());
			node.put("type", "1");
			node.put("iconOpen", "libs/icons/tree_close.gif");
			node.put("iconClose", "libs/icons/tree_open.gif");
			node.put("icon", projectAdd+visitor.getFunctionEntity().getIconEntity().getBigIconPath());
			if(StringUtil.isNotEmpty(visitor.getFunctionEntity().getApplicationEntity().getUrl())){
				if(checkFunctionUrl(visitor.getFunctionEntity().getUrl())){
					node.put("url",visitor.getFunctionEntity().getUrl());
				}else{
					node.put("url",visitor.getApplicationEntity().getUrl()+visitor.getFunctionEntity().getUrl());
				}
			}else{
				node.put("url", projectAdd+visitor.getFunctionEntity().getUrl());
			}
			
			result.add(node);
		}
		Map<String,Object> returnObject = new HashMap<String, Object>();
		returnObject.put("list", result);
		return returnObject;
	}

	// 获取项目路径
	private String getPath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		return basePath;
	}
	
	public Boolean checkFunctionUrl(String functionUrl){
		Boolean flag = false;
		flag = functionUrl.startsWith("http");
		return flag;
	}

	@Override
	public AjaxJson changeVisitorSwitch(String visitorSwitch) {
		AjaxJson ajaxJson = new AjaxJson();
		if(StringUtil.isNotEmpty(visitorSwitch)){
			String path = "config.properties";
			try {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				
				InputStream is = classLoader.getResourceAsStream(path);
//				FileInputStream inputStream = new FileInputStream(path);
				Properties prop = new Properties();
				// 加载
				prop.load(is);
				// 设置
				prop.setProperty("visitorSwitch", visitorSwitch);
				// 写到配置文件
				OutputStream outputStream = new FileOutputStream(ResourceUtil.getClassPath()+path);
				prop.store(outputStream, "update message");
				is.close();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("修改失败，请稍后重试！");
		}
		return ajaxJson;
	}
	
}
