package com.zjy.jopm.visitor.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;

@Entity
@Table(name = "T_SYS_VISITOR")
public class VisitorEntity extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4609883680437079667L;

	private ApplicationEntity applicationEntity;
	
	private FunctionEntity functionEntity;

	@ManyToOne
	@JoinColumn(name = "APPID")
	public ApplicationEntity getApplicationEntity() {
		return applicationEntity;
	}

	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}

	@ManyToOne
	@JoinColumn(name = "FUNCTIONID")
	public FunctionEntity getFunctionEntity() {
		return functionEntity;
	}

	public void setFunctionEntity(FunctionEntity functionEntity) {
		this.functionEntity = functionEntity;
	}
	
	
	
}
