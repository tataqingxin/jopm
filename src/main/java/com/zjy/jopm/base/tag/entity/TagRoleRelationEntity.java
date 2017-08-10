package com.zjy.jopm.base.tag.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.role.entity.RoleEntity;

/**
 * @author zhu chengjie
 * 2017年5月9日下午3:01:46
 * 
 */
@Entity
@Table(name = "T_SYS_ROLE_TAG")
public class TagRoleRelationEntity extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7305510456156715661L;
	
	private RoleEntity roleEntity;
	
	private TagEntity tagEntity;
	@ManyToOne
    @JoinColumn(name = "ROLE_ID")
	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}
	@ManyToOne
    @JoinColumn(name = "TAG_ID")
	public TagEntity getTagEntity() {
		return tagEntity;
	}

	public void setTagEntity(TagEntity tagEntity) {
		this.tagEntity = tagEntity;
	}

}
