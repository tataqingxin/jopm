/** 
 * @Description:[应用信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.ApplicationEntity.java
 * @ClassName:ApplicationEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午3:31:04
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:31:04  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

/**
 * @ClassName: ApplicationEntity 
 * @Description: [应用信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:31:04 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_APPLICATION")
public class ApplicationEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 5667281167802233127L;

	private String name;// 应用名称       
	private String code;// 应用编码      
	private String type;// 应用类型       
	private String description;// 应用描述 
	private String status;// 应用状态     
	private Date createtime;// 创建时间 
	private Date disabletime;// 禁用时间
	private String url;// URL
	private String instanceCode;// 实例号   
	private String companyCode;//公司编码
	private String appSerial;//应用在公司所属编号
	private String strategy;//开通策略
	
	private IconEntity iconEntity;// 图标
	private OrganizationEntity organizationEntity;// 机构
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
	 * @return the status
	 */
	@Column(name = "STATUS", length=16, nullable=false)
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the createtime
	 */
	@Column(name = "CREATETIME")
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * @return the disabletime
	 */
	@Column(name = "DISABLETIME")
	public Date getDisabletime() {
		return disabletime;
	}
	/**
	 * @param disabletime the disabletime to set
	 */
	public void setDisabletime(Date disabletime) {
		this.disabletime = disabletime;
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
	
	@Column(name = "INSTANCE_CODE")
	public String getInstanceCode() {
		return instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this.instanceCode = instanceCode;
	}
	
	@Column(name = "COMPANY_CODE")
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@Column(name = "APP_SERIAL")
	public String getAppSerial() {
		return appSerial;
	}
	public void setAppSerial(String appSerial) {
		this.appSerial = appSerial;
	}
	@Column(name = "STRATEGY")
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	/**
	 * @return the typeName
	 */
	@Transient
	public String getTypeName() {
		List<DictionaryEntity> appTypeList = DictionaryGroupEntity.allTypes.get(Constants.DICT_GROUP_APP_TYPE);
		for (DictionaryEntity dictionaryEntity : appTypeList) {
			if(dictionaryEntity.getCode().equals(this.getType())){
				return dictionaryEntity.getName();
			}
		}
		return null;
	}
	/**
	 * @return the statusName
	 */
	@Transient
	public String getStatusName() {
		List<DictionaryEntity> statusList = DictionaryGroupEntity.allTypes.get(Constants.DICT_GROUP_STATUS);
		for (DictionaryEntity dictionaryEntity : statusList) {
			if(dictionaryEntity.getCode().equals(this.getStatus())){
				return dictionaryEntity.getName();
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appSerial == null) ? 0 : appSerial.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result
				+ ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((disabletime == null) ? 0 : disabletime.hashCode());
		result = prime * result
				+ ((iconEntity == null) ? 0 : iconEntity.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((instanceCode == null) ? 0 : instanceCode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((organizationEntity == null) ? 0 : organizationEntity
						.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((strategy == null) ? 0 : strategy.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		ApplicationEntity other = (ApplicationEntity) obj;
		if (appSerial == null) {
			if (other.appSerial != null)
				return false;
		} else if (!appSerial.equals(other.appSerial))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (disabletime == null) {
			if (other.disabletime != null)
				return false;
		} else if (!disabletime.equals(other.disabletime))
			return false;
		if (iconEntity == null) {
			if (other.iconEntity != null)
				return false;
		} else if (!iconEntity.equals(other.iconEntity))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (instanceCode == null) {
			if (other.instanceCode != null)
				return false;
		} else if (!instanceCode.equals(other.instanceCode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organizationEntity == null) {
			if (other.organizationEntity != null)
				return false;
		} else if (!organizationEntity.equals(other.organizationEntity))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (strategy == null) {
			if (other.strategy != null)
				return false;
		} else if (!strategy.equals(other.strategy))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
