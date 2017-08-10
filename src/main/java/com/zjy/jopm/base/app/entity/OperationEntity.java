/** 
 * @Description:[功能操作信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.OperationEntity.java
 * @ClassName:OperationEntity
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月6日 下午3:48:41
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:48:41  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.icon.entity.IconEntity;

/**
 * @ClassName: OperationEntity 
 * @Description: [功能操作信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:48:41 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_OPERATION")
public class OperationEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -4747532389567304397L;

	private String name;// 操作名称
	private String code;// 操作编码
	private String description;// 操作描述
	
	private IconEntity iconEntity;// 图标
	private FunctionEntity functionEntity;// 功能
	
	private List<ResourceEntity> resourceEntityList;// 资源
	
	/**
	 * @return the name
	 */
	@Column(name = "NAME", length=64, nullable=false)
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the code
	 */
	@Column(name = "CODE", length=64, nullable=false)
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", length=256)
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the iconEntity
	 */
	@ManyToOne
	@JoinColumn(name = "ICON_ID")
	public IconEntity getIconEntity() {
		return iconEntity;
	}
	/**
	 * @param iconEntity the iconEntity to set
	 */
	public void setIconEntity(IconEntity iconEntity) {
		this.iconEntity = iconEntity;
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
	 * @return the resoureEntityList
	 */
	@Transient
	public List<ResourceEntity> getResourceEntityList() {
		return resourceEntityList;
	}
	/**
	 * @param resoureEntityList the resoureEntityList to set
	 */
	public void setResourceEntityList(List<ResourceEntity> resourceEntityList) {
		this.resourceEntityList = resourceEntityList;
	}
	
	
}
