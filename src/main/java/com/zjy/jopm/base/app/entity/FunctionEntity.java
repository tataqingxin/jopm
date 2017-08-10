/** 
 * @Description:[功能信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.entity.FunctionEntity.java
 * @ClassName:FunctionEntity
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月6日 下午3:44:03
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:44:03  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.entity;

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
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.icon.entity.IconEntity;

/**
 * @ClassName: FunctionEntity 
 * @Description: [功能信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:44:03 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_FUNCTION")
public class FunctionEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 2778862868258422210L;
	
	private String name;// 功能名称
	private String code;// 功能编码
	private String url;// URL
	private String status;// 状态
	private String tagName = "无";
	private FunctionEntity parentFunctionEntity;// 父功能
	private List<FunctionEntity> childFunctionEntity;// 子功能
	private ApplicationEntity applicationEntity;// 应用
	private IconEntity iconEntity;// 图标
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
	public FunctionEntity() {	
	}
	public FunctionEntity(ApplicationEntity applicationEntity) {
		super();
		this.applicationEntity = applicationEntity;
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
	 * @return the parentFunctionEntity
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_FUNCTION_ID")
	public FunctionEntity getParentFunctionEntity() {
		return parentFunctionEntity;
	}
	/**
	 * @param parentFunctionEntity the parentFunctionEntity to set
	 */
	public void setParentFunctionEntity(FunctionEntity parentFunctionEntity) {
		this.parentFunctionEntity = parentFunctionEntity;
	}
	/**
	 * @return the childFunctionEntity
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentFunctionEntity")
	@JsonBackReference
	public List<FunctionEntity> getChildFunctionEntity() {
		return childFunctionEntity;
	}
	/**
	 * @param childFunctionEntity the childFunctionEntity to set
	 */
	public void setChildFunctionEntity(List<FunctionEntity> childFunctionEntity) {
		this.childFunctionEntity = childFunctionEntity;
	}
	@Transient
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
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
		result = prime
				* result
				+ ((applicationEntity == null) ? 0 : applicationEntity
						.hashCode());
		result = prime
				* result
				+ ((childFunctionEntity == null) ? 0 : childFunctionEntity
						.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((iconEntity == null) ? 0 : iconEntity.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((parentFunctionEntity == null) ? 0 : parentFunctionEntity
						.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
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
		FunctionEntity other = (FunctionEntity) obj;
		if (applicationEntity == null) {
			if (other.applicationEntity != null)
				return false;
		} else if (!applicationEntity.equals(other.applicationEntity))
			return false;
		if (childFunctionEntity == null) {
			if (other.childFunctionEntity != null)
				return false;
		} else if (!childFunctionEntity.equals(other.childFunctionEntity))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentFunctionEntity == null) {
			if (other.parentFunctionEntity != null)
				return false;
		} else if (!parentFunctionEntity.equals(other.parentFunctionEntity))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tagName == null) {
			if (other.tagName != null)
				return false;
		} else if (!tagName.equals(other.tagName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
