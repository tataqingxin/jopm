/** 
 * @Description:[字典分组信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.entity.DictionaryGroupEntity.java
 * @ClassName:DictionaryGroupEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午2:42:07
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午2:42:07  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.unistc.core.common.entity.IdEntity;

/**
 * @ClassName: DictionaryGroupEntity 
 * @Description: [字典分组信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午2:42:07 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_DICTIONARY_GROUP")
public class DictionaryGroupEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -8477635454831233121L;
	
	private String name;// 分组名称
	private  String code;// 分组编码
	
	public static Map<String, DictionaryGroupEntity> allTypeGroups = new HashMap<String,DictionaryGroupEntity>();
	public static Map<String, List<DictionaryEntity>> allTypes = new HashMap<String,List<DictionaryEntity>>();
	
	
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
	
	
	

}
