package com.zjy.jopm.base.app.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

@Entity
@Table(name = "T_SYS_SERVICE_AUTH")
public class ServiceAuthEntity extends IdEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -795547793370984254L;
	
	private ApplicationEntity applicationEntity;
	
	private ServiceInterfaceEntity serviceInterfaceEntity;

	@ManyToOne
	@JoinColumn(name = "APPID")
	public ApplicationEntity getApplicationEntity() {
		return applicationEntity;
	}

	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}

	@ManyToOne
	@JoinColumn(name = "SERVICEID")
	public ServiceInterfaceEntity getServiceInterfaceEntity() {
		return serviceInterfaceEntity;
	}

	public void setServiceInterfaceEntity(
			ServiceInterfaceEntity serviceInterfaceEntity) {
		this.serviceInterfaceEntity = serviceInterfaceEntity;
	}
	
	

}
