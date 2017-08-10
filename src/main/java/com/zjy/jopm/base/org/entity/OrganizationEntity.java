/** 
 * @Description:[机构对象信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.entity.OrganizationEntity.java
 * @ClassName:OrganizationEntity
 * @Author:Lu Guoqiang   
 * @CreateDate:2016年5月6日 下午12:00:16
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月6日 下午12:00:16  
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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: OrganizationEntity 
 * @Description: [机构对象信息] 
 * @author Lu Guoqiang  
 * @date 2016年5月6日 下午12:00:16 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_ORGANIZATION")
public class OrganizationEntity implements Serializable {


	
	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 7784061893392308094L;
	
	private String name;// 机构名称
	private String code;// 机构编码
	private Integer range;// 机构顺序
	private String description;// 机构描述
	
	private OrganizationEntity parentOrganizationEntity;// 父机构
	private List<OrganizationEntity> childOrganizationEntity;// 子机构
	
	private String orgarizationId;
	
	private String id;

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Transient
	public String getOrgarizationId() {
		return orgarizationId;
	}
	public void setOrgarizationId(String orgarizationId) {
		this.orgarizationId = orgarizationId;
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
	 * @return the parentOrganizationEntity
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_ORGANIZATION_ID")
	public OrganizationEntity getParentOrganizationEntity() {
		return parentOrganizationEntity;
	}
	/**
	 * @param parentOrganizationEntity the parentOrganizationEntity to set
	 */
	public void setParentOrganizationEntity(OrganizationEntity parentOrganizationEntity) {
		this.parentOrganizationEntity = parentOrganizationEntity;
	}
	/**
	 * @return the childOrganizationEntity
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentOrganizationEntity")
	@JsonBackReference
	public List<OrganizationEntity> getChildOrganizationEntity() {
		return childOrganizationEntity;
	}
	/**
	 * @param childOrganizationEntity the childOrganizationEntity to set
	 */
	@JsonBackReference
	public void setChildOrganizationEntity(List<OrganizationEntity> childOrganizationEntity) {
		this.childOrganizationEntity = childOrganizationEntity;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((childOrganizationEntity == null) ? 0
						: childOrganizationEntity.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((orgarizationId == null) ? 0 : orgarizationId.hashCode());
		result = prime
				* result
				+ ((parentOrganizationEntity == null) ? 0
						: parentOrganizationEntity.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganizationEntity other = (OrganizationEntity) obj;
		if (childOrganizationEntity == null) {
			if (other.childOrganizationEntity != null)
				return false;
		} else if (!childOrganizationEntity
				.equals(other.childOrganizationEntity))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orgarizationId == null) {
			if (other.orgarizationId != null)
				return false;
		} else if (!orgarizationId.equals(other.orgarizationId))
			return false;
		if (parentOrganizationEntity == null) {
			if (other.parentOrganizationEntity != null)
				return false;
		} else if (!parentOrganizationEntity
				.equals(other.parentOrganizationEntity))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		return true;
	}
}
