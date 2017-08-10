/** 
 * @Description:[功能描述]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.OperationResourceRelationEntity.java
 * @ClassName:OperationResourceRelationEntity
 * @Author:HogwartsRow 
 * @CreateDate:2016年5月6日 下午4:01:06
 * @UpdateUser:HogwartsRow  
 * @UpdateDate:2016年5月6日 下午4:01:06  
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
 * @ClassName: OperationResourceRelationEntity 
 * @Description: [功能描述] 
 * @author HogwartsRow
 * @date 2016年5月6日 下午4:01:06 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_OPRA_RESOURCE")
public class OperationResourceRelationEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 6839201368346410614L;
	
	private OperationEntity operationEntity;// 功能操作
	private ResourceEntity resourceEntity;// 资源
	
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
