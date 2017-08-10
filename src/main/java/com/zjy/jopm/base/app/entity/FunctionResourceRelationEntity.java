/** 
 * @Description:[功能与资源关系信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.FunctionResourceRelationEntity.java
 * @ClassName:FunctionResourceRelationEntity
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月6日 下午4:00:33
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月6日 下午4:00:33  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: FunctionResourceRelationEntity 
 * @Description: [功能与资源关系信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午4:00:33 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_FUNC_RESOURCE")
public class FunctionResourceRelationEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 777874701929034491L;

	private FunctionEntity functionEntity;// 功能
	private ResourceEntity resourceEntity;// 资源
	
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
	 * @return the resourceEntity
	 */
	@ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
	public ResourceEntity getResourceEntity() {
		return resourceEntity;
	}
	/**
	 * @param resourceEntity the resourceEntity to set
	 */
	public void setResourceEntity(ResourceEntity resourceEntity) {
		this.resourceEntity = resourceEntity;
	}
	
	
}
