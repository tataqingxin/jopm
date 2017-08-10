/** 
 * @Description:[角色和用户关系信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.role.entity.RoleUserRelationEntity.java
 * @ClassName:RoleUserRelationEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午4:07:24
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月6日 下午4:07:24  
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
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: RoleUserRelationEntity 
 * @Description: [角色和用户关系信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午4:07:24 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_USER_ROLE")
public class RoleUserRelationEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 1730465121598353926L;
	
	private RoleEntity roleEntity;// 角色
	private UserEntity userEntity;// 用户
	
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
	 * @return the userEntity
	 */
	@ManyToOne
    @JoinColumn(name = "USER_ID")
	public UserEntity getUserEntity() {
		return userEntity;
	}
	/**
	 * @param userEntity the userEntity to set
	 */
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	
}
