package com.zjy.jopm.base.config.entity;

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
 * 系统配置信息
 * @author root
 *
 */
@Entity
@Table(name = "T_BUSS_CONFIG")
public class Config extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 6026072369290486641L;
	
	private String key;
	private String value; 
	private String descr;//说明
	
	@Column(name = "key", length=50, nullable=false)
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Column(name = "value", length=50, nullable=false)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name = "descr", length=100, nullable=false)
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	
}
