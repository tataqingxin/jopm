package com.zjy.jopm.base.tag.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;

/**
 * @author zhu chengjie
 * 2017年5月9日下午3:02:11
 * 
 */
@Entity
@Table(name = "T_SYS_TAG_FUNCTION")
public class TagFunctionRelationEntity extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5859723613260332034L;
	
	private TagEntity tagEntity;//标签
	
	private FunctionEntity functionEntity;// 功能
	@ManyToOne
    @JoinColumn(name = "TAG_ID")
	public TagEntity getTagEntity() {
		return tagEntity;
	}

	public void setTagEntity(TagEntity tagEntity) {
		this.tagEntity = tagEntity;
	}
	@ManyToOne
    @JoinColumn(name = "FUNCTION_ID")
	public FunctionEntity getFunctionEntity() {
		return functionEntity;
	}

	public void setFunctionEntity(FunctionEntity functionEntity) {
		this.functionEntity = functionEntity;
	}
	
	

}
