/** 
 * @Description:[角色管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.role.base.service.RoleService.java
 * @ClassName:RoleService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:14:16
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午3:14:16  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.role.entity.RoleEntity;

/**
 * @ClassName: RoleService 
 * @Description: [角色管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:14:16 
 * @since JDK 1.6 
 */
public interface RoleService extends BaseService {

	/**
	 * 
	 * @Title: getRoleEntityQuiGrid 
	 * @Description: [某机构的角色列表]
	 * @param roleEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getRoleEntityQuiGrid(RoleEntity roleEntity, int pageNo, int pageSize, String sort, String direction)throws JumpException;
	
	/**
	 * 
	 * @Title: insertRoleEntity
	 * @Description: [保存角色数据]
	 * @param roleEntity
	 * @return
	 */
	public AjaxJson insertRoleEntity(RoleEntity roleEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: updateRoleEntity 
	 * @Description: [修改角色数据]
	 * @param roleEntity
	 * @return
	 */
	public AjaxJson updateRoleEntity(RoleEntity roleEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: expandRoleEntity
	 * @Description: [查找单个角色数据]
	 * @param id
	 * @return
	 */
	public AjaxJson expandRoleEntity(String id);
	
	/**
	 * 
	 * @Title: deleteRoleEntity
	 * @Description: [删除单个角色数据]
	 * @param id
	 * @return
	 */
	public AjaxJson deleteRoleEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: setRoleUser 
	 * @Description: [设置角色指定人员]
	 * @param id
	 * @param userIds
	 * @return
	 */
	public AjaxJson setRoleUser(String id, Object[] userIds) throws JumpException;
	
	/**
	 * 
	 * @Title: setRolePermission 
	 * @Description: [设置角色权限范围]
	 * @param id
	 * @param functionIds
	 * @param operationIds
	 * @return
	 */
	public AjaxJson setRolePermission(String id, Object[] functionIds, Object[] operationIds,HttpServletRequest request) throws JumpException;
}
