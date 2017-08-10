/** 
 * @Description:[图标信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.icon.entity.IconEntity.java
 * @ClassName:IconEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午2:49:58
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月6日 下午2:49:58  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.icon.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;

/**
 * @ClassName: IconEntity 
 * @Description: [图标信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午2:49:58 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_ICON")
public class IconEntity implements Serializable {


	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -2766928434384500542L;

	private String name;// 图标名称
	private String type;// 图标类型
	private String iconPath;// 图标路径
	private String description;// 图标描述
	private String mediumIconPath;
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
	@Column(name = "MEDIUMPATH", length=128)
	public String getMediumIconPath() {
		return mediumIconPath;
	}
	public void setMediumIconPath(String mediumIconPath) {
		this.mediumIconPath = mediumIconPath;
	}

	private String bigIconPath;
	@Column(name = "BIGPATH", length=128)
	public String getBigIconPath() {
		return bigIconPath;
	}
	public void setBigIconPath(String bigIconPath) {
		this.bigIconPath = bigIconPath;
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
	 * @return the path
	 */
	@Column(name = "PATH", length=128, nullable=false)
	public String getIconPath() {
		return iconPath;
	}
	/**
	 * @param path the path to set
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
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
	 * @return the typeName
	 */
	@Transient
	public String getTypeName() {
		List<DictionaryEntity> iconTypeList = DictionaryGroupEntity.allTypes.get(Constants.DICT_GROUP_ICON_TYPE);
		for (DictionaryEntity dictionaryEntity : iconTypeList) {
			if(dictionaryEntity.getCode().equals(this.getType())){
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
				+ ((bigIconPath == null) ? 0 : bigIconPath.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((iconPath == null) ? 0 : iconPath.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mediumIconPath == null) ? 0 : mediumIconPath.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		IconEntity other = (IconEntity) obj;
		if (bigIconPath == null) {
			if (other.bigIconPath != null)
				return false;
		} else if (!bigIconPath.equals(other.bigIconPath))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (iconPath == null) {
			if (other.iconPath != null)
				return false;
		} else if (!iconPath.equals(other.iconPath))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mediumIconPath == null) {
			if (other.mediumIconPath != null)
				return false;
		} else if (!mediumIconPath.equals(other.mediumIconPath))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
