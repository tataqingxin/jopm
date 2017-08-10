/** 
 * @Description:[应用服务接口信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.ServiceInterfaceEntity.java
 * @ClassName:ServiceInterfaceEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午3:38:46
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:38:46  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: ServiceInterfaceEntity 
 * @Description: [应用服务接口信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:38:46 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_SERVICE_INTERFACE")
public class ServiceInterfaceEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 2535999220572478016L;

	private String name;// 服务名称
	private String code;// 服务编码
	private String url;// URL
	private String description;// 描述
	
	private ApplicationEntity applicationEntity;// 应用

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
	 * @return the url
	 */
	@Column(name = "URL", length=128, nullable=false)
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @return the applicationEntity
	 */
	@ManyToOne
	@JoinColumn(name = "APPLICATION_ID")
	public ApplicationEntity getApplicationEntity() {
		return applicationEntity;
	}

	/**
	 * @param applicationEntity the applicationEntity to set
	 */
	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}
	
	
}
