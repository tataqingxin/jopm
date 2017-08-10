package com.zjy.jopm.base.tag.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.unistc.core.common.entity.IdEntity;

/**
 * @author zhu chengjie
 * 2017年5月9日下午3:02:34
 * 
 */
@Entity
@Table(name = "T_SYS_TAG")
public class TagEntity extends IdEntity implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -8873470752508756810L;

	private String name; //标签名 
	
	private String code; //标签编码
	
	private String roleName = "无";
	
	public TagEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TagEntity(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}
	@Column(name = "NAME", length=64, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "CODE", length=32, nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Transient
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
