/** 
 * @Description:[操作日志信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.log.entity.LogEntity.java
 * @ClassName:LogEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午2:59:48
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午2:59:48  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.log.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.unistc.core.common.entity.IdEntity;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: LogEntity 
 * @Description: [操作日志信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午2:59:48 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_LOG")
public class LogEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -4553403043429155900L;
	
	private String logLevel;// 日志级别
	private String broswer;// 浏览器标识
	private String content;// 日志内容
	private String operateType;// 操作类型
	private String operateTime;// 操作时间
	
	private UserEntity userEntity;// 用户
	
	private OrganizationEntity organizationEntity;// 机构
	private DepartmentEntity departmentEntity;// 部门
	
	/**
	 * @return the logLevel
	 */
	@Column(name = "LOGLEVEL", length=16, nullable=false)
	public String getLogLevel() {
		return logLevel;
	}
	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	/**
	 * @return the broswer
	 */
	@Column(name = "BROSWER", length=32, nullable=false)
	public String getBroswer() {
		return broswer;
	}
	/**
	 * @param broswer the broswer to set
	 */
	public void setBroswer(String broswer) {
		this.broswer = broswer;
	}
	/**
	 * @return the content
	 */
	@Column(name = "CONTENT", length=256, nullable=false)
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the operateType
	 */
	@Column(name = "OPERATE_TYPE", length=16, nullable=false)
	public String getOperateType() {
		return operateType;
	}
	/**
	 * @param operateType the operateType to set
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	/**
	 * @return the operateTime
	 */
	@Column(name = "OPERATE_TIME")
	public String getOperateTime() {
		return operateTime;
	}
	/**
	 * @param operateTime the operateTime to set
	 */
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	/**
	 * @return the userEntity
	 */
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	public UserEntity getUserEntity() {
		return userEntity;
	}
	/**
	 * @param userEntity the userEntity to set
	 */
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	/**
	 * @return the organizationEntity
	 */
	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID")
	@Transient 
	public OrganizationEntity getOrganizationEntity() {
		return organizationEntity;
	}
	/**
	 * @param organizationEntity the organizationEntity to set
	 */
	public void setOrganizationEntity(OrganizationEntity organizationEntity) {
		this.organizationEntity = organizationEntity;
	}
	/**
	 * @return the departmentEntity
	 */
	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	@Transient 
	public DepartmentEntity getDepartmentEntity() {
		return this.departmentEntity;
	}
	/**
	 * @param departmentEntity the departmentEntity to set
	 */
	public void setDepartmentEntity(DepartmentEntity departmentEntity) {
		this.departmentEntity = departmentEntity;
	}
	
	

}
