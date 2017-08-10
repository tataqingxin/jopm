/** 
 * @Description:[角色信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.role.entity.RoleEntity.java
 * @ClassName:RoleEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午2:55:08
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午2:55:08  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

/**
 * @ClassName: RoleEntity 
 * @Description: [角色信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午2:55:08 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_ROLE")
public class RoleEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 6026072369290486641L;
	
	private String name;// 角色名称
	private String code;// 角色编码
	private String type;// 角色类型
	private String description;// 角色描述
	
	private OrganizationEntity organizationEntity;// 所属机构
	
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
	 * @return the type
	 */
	@Column(name = "TYPE", length=16, nullable=false)
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	/**
	 * @return the typeName
	 */
	@Transient
	public String getTypeName() {
		List<DictionaryEntity> roleTypeList = DictionaryGroupEntity.allTypes.get(Constants.DICT_GROUP_ROLE_TYPE);
		for (DictionaryEntity dictionaryEntity : roleTypeList) {
			if(dictionaryEntity.getCode().equals(this.getType())){
				return dictionaryEntity.getName();
			}
		}
		return null;
	}
	
}
