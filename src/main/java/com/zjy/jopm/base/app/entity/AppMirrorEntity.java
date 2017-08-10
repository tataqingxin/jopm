package com.zjy.jopm.base.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

@Entity
@Table(name = "T_SYS_MIRROR")
public class AppMirrorEntity extends IdEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2860966119981349364L;
	
	private String code;// 镜像编码      
	private OrganizationEntity organizationEntity;  //所属组织机构 
	private ApplicationEntity applicationEntity;// 应用
	private String isEffective = "y";  //开通状态
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne
	@JoinColumn(name = "ORGANIZATIONID")
	public OrganizationEntity getOrganizationEntity() {
		return organizationEntity;
	}
	public void setOrganizationEntity(OrganizationEntity organizationEntity) {
		this.organizationEntity = organizationEntity;
	}
	
	@ManyToOne
	@JoinColumn(name = "APPLICATION_ID")
	public ApplicationEntity getApplicationEntity() {
		return applicationEntity;
	}
	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}
	@Column(name = "IS_EFFECTIVE")
	public String getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}

}
