/** 
 * @Description:[功能管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.FunctionService.java
 * @ClassName:FunctionService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:11:48
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:11:48  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service;

import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.app.entity.FunctionEntity;

/**
 * @ClassName: FunctionService 
 * @Description: [功能管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:11:48 
 * @since JDK 1.6 
 */
public interface FunctionService extends BaseService {

	/**
	 * 
	 * @Title: getFunctionEntityQuiGrid 
	 * @Description: [某应用下的功能列表]
	 * @param functionEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getFunctionEntityQuiGrid(FunctionEntity functionEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException;
	
	/**
	 * 
	 * @Title: insertFunctionEntity
	 * @Description: [保存功能数据]
	 * @param functionEntity
	 * @return
	 */
	public AjaxJson insertFunctionEntity(FunctionEntity functionEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: updateFunctionEntity
	 * @Description: [修改功能数据]
	 * @param functionEntity
	 * @return
	 */
	public AjaxJson updateFunctionEntity(FunctionEntity functionEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: expandApplicationEntity
	 * @Description: [查找单个功能数据]
	 * @param id
	 * @return
	 */
	public AjaxJson expandFunctionEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: deleteFunctionEntity
	 * @Description: [删除单个功能数据]
	 * @param id
	 * @return
	 */
	public AjaxJson deleteFunctionEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: enableFunctionStatus 
	 * @Description: [启用/禁用功能状态]
	 * @param id
	 * @return
	 */
	public AjaxJson enableFunctionStatus(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: getApplicaitonTreeNode 
	 * @Description: [获取某个应用节点]
	 * @param applicationId
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson getApplicaitonTreeNode(String applicationId) throws JumpException;
	
	/**
	 * 
	 * @Title: getFunctionTree 
	 * @Description: [获取某个应用的功能树]
	 * @param applicationId
	 * @param functionId
	 * @return
	 */
	public AjaxJson getFunctionTree(String applicationId, String functionId) throws JumpException;
	
	/**
	 * 
	 * @Title: getMenuTree 
	 * @Description: [获取功能树-菜单]
	 * @param applicationId
	 * @param functionId
	 * @param path 
	 * @return
	 */
	public AjaxJson getMenuTree(String applicationId, String functionId, String path) throws JumpException;
}
