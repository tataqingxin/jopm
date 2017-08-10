/** 
 * @Description:[部门与用户关系信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.user.entity.DepartmentUserRelationEntity.java
 * @ClassName:DepartmentUserRelationEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午3:25:25
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月6日 下午3:25:25  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.org.entity.DepartmentEntity;

/**
 * @ClassName: DepartmentUserRelationEntity 
 * @Description: [部门与用户关系信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:25:25 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_USER_DEPARTMENT")
public class UserDepartmentRelationEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 4747662831187907449L;

	private DepartmentEntity departmentEntity;// 部门
	private UserEntity userEntity;// 用户
	
	/**
	 * @return the departmentEntity
	 */
	@ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
	public DepartmentEntity getDepartmentEntity() {
		return this.departmentEntity;
	}
	/**
	 * @param departmentEntity the departmentEntity to set
	 */
	public void setDepartmentEntity(DepartmentEntity departmentEntity) {
		this.departmentEntity = departmentEntity;
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
