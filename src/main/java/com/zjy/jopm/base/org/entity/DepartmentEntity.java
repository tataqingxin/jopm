/** 
 * @Description:[部门对象信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.entity.DepartmentEntity.java
 * @ClassName:DepartmentEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午2:33:19
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午2:33:19  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;

import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: DepartmentEntity 
 * @Description: [部门对象信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午2:33:19 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_DEPARTMENT")
public class DepartmentEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 5920737980412677483L;
	
	private String name;// 部门名称
	private String code;// 部门编码
	private Integer range;// 部门排序
	private String description;// 部门描述

	private DepartmentEntity parentDepartmentEntity;// 父部门
	private List<DepartmentEntity> childDepartmentEntity;// 子部门
	private OrganizationEntity organizationEntity;// 所属机构
	
	private String departId;
	
	
	@Transient
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
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
	 * @return the range
	 */
	@Column(name = "ORDER_NO")
	public Integer getRange() {
		return range;
	}
	/**
	 * @param range the range to set
	 */
	public void setRange(Integer range) {
		this.range = range;
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
	 * @return the parentDepartmentEntity
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_DEPARTMENT_ID")
	public DepartmentEntity getParentDepartmentEntity() {
		return parentDepartmentEntity;
	}
	/**
	 * @param parentDepartmentEntity the parentDepartmentEntity to set
	 */
	public void setParentDepartmentEntity(DepartmentEntity parentDepartmentEntity) {
		this.parentDepartmentEntity = parentDepartmentEntity;
	}
	/**
	 * @return the childDepartmentEntity
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentDepartmentEntity")
	@JsonBackReference
	public List<DepartmentEntity> getChildDepartmentEntity() {
		return childDepartmentEntity;
	}
	/**
	 * @param childDepartmentEntity the childDepartmentEntity to set
	 */
	@JsonBackReference
	public void setChildDepartmentEntity(List<DepartmentEntity> childDepartmentEntity) {
		this.childDepartmentEntity = childDepartmentEntity;
	}
	/**
	 * @return the organizationEntity
	 */
	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID")
	public OrganizationEntity getOrganizationEntity() {
		return organizationEntity;
	}
	/**
	 * @param organizationEntity the organizationEntity to set
	 */
	public void setOrganizationEntity(OrganizationEntity organizationEntity) {
		this.organizationEntity = organizationEntity;
	}
	
	
}
