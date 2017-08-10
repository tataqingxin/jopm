/** 
 * @Description:[角色权限信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity.java
 * @ClassName:RoleFunctionRelationEntity
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月6日 下午3:57:31
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:57:31  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;

/**
 * @ClassName: RoleFunctionRelationEntity 
 * @Description: [角色权限信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:57:31 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_ROLE_FUNCTION")
public class RoleFunctionRelationEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -3126529132506334935L;

	private RoleEntity roleEntity;// 角色
	private FunctionEntity functionEntity;// 功能
	private OperationEntity operationEntity;// 操作
	
	/**
	 * @return the roleEntity
	 */
	@ManyToOne
    @JoinColumn(name = "ROLE_ID")
	public RoleEntity getRoleEntity() {
		return roleEntity;
	}
	/**
	 * @param roleEntity the roleEntity to set
	 */
	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}
	/**
	 * @return the functionEntity
	 */
	@ManyToOne
    @JoinColumn(name = "FUNCTION_ID")
	public FunctionEntity getFunctionEntity() {
		return functionEntity;
	}
	/**
	 * @param functionEntity the functionEntity to set
	 */
	public void setFunctionEntity(FunctionEntity functionEntity) {
		this.functionEntity = functionEntity;
	}
	/**
	 * @return the operationEntity
	 */
	@ManyToOne
	@JoinColumn(name = "OPERATION_ID")
	public OperationEntity getOperationEntity() {
		return operationEntity;
	}
	/**
	 * @param operationEntity the operationEntity to set
	 */
	public void setOperationEntity(OperationEntity operationEntity) {
		this.operationEntity = operationEntity;
	}
	
	
}
