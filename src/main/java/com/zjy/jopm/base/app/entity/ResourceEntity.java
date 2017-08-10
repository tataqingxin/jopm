/** 
 * @Description:[资源信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.ResourceEntity.java
 * @ClassName:ResourceEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午3:54:22
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:54:22  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: ResourceEntity 
 * @Description: [资源信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:54:22 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_RESOURCE")
public class ResourceEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -4165439277442898968L;

	private String url;// 资源地址
	private String description;// 资源描述
	
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
	
	
}
