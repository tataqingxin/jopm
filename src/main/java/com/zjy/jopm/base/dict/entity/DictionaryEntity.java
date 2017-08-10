/** 
 * @Description:[字典数值信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.entity.DictionaryEntity.java
 * @ClassName:DictionaryEntity
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月6日 下午2:44:32
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午2:44:32  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: DictionaryEntity 
 * @Description: 字典数值信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午2:44:32 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_DICTIONARY")
public class DictionaryEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -4795491734117986863L;
	
	private String name;// 字典名称
	private String code;// 字典编码
	
	private DictionaryGroupEntity dictionaryGroupEntity;// 字典分组

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
	 * @return the dictionaryGroupEntity
	 */
	@ManyToOne
	@JoinColumn(name = "DICTIONARY_GROUP_ID")
	public DictionaryGroupEntity getDictionaryGroupEntity() {
		return dictionaryGroupEntity;
	}

	/**
	 * @param dictionaryGroupEntity the dictionaryGroupEntity to set
	 */
	public void setDictionaryGroupEntity(DictionaryGroupEntity dictionaryGroupEntity) {
		this.dictionaryGroupEntity = dictionaryGroupEntity;
	}

	
}
