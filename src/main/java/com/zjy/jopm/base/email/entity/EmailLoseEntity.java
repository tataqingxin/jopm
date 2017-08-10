package com.zjy.jopm.base.email.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

@Entity
@Table(name = "T_SYS_EMAIL")
public class EmailLoseEntity extends IdEntity implements Serializable{

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date createTime;
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
