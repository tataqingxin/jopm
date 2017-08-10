/** 
 * @Description:[用户信息]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.user.entity.UserEntity.java
 * @ClassName:UserEntity
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月6日 下午3:03:44
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月6日 下午3:03:44  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.user.entity;

import java.io.Serializable;
import java.util.Date;
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
 * @ClassName: UserEntity 
 * @Description: [用户信息] 
 * @author Lu Guoqiang
 * @date 2016年5月6日 下午3:03:44 
 * @since JDK 1.6 
 */
@Entity
@Table(name = "T_SYS_USER")
public class UserEntity extends IdEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -6278578760106995760L;

	private String userName;// 用户名    
	private String password;// 密码    
	private String sex;// 性别         
	private String realName;// 真实姓名     
	private String phone;// 手机号码       
	private String address;// 联系地址     
	private String email;// 电子邮箱       
	private String status;// 状态  0 停用    1 启动 
	private String description;// 描述  
	private Date createtime;// 创建时间  
	private Date disabletime;// 禁用时间
	private String delFlag;//删除标识 1：删除;0:未删除
	private String type;//用户类型
	 
	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public UserEntity(String userName, String password, String sex,
			String realName, String phone, String address, String email,
			String status, String description, Date createtime,
			Date disabletime, String delFlag, String type,
			OrganizationEntity organizationEntity) {
	
		super();
		this.userName = userName;
		this.password = password;
		this.sex = sex;
		this.realName = realName;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.status = status;
		this.description = description;
		this.createtime = createtime;
		this.disabletime = disabletime;
		this.delFlag = delFlag;
		this.type = type;
		this.organizationEntity = organizationEntity;
	}

	private OrganizationEntity organizationEntity;// 机构
	/**
	 * @return the organizationEntity
	 */
	@ManyToOne
    @JoinColumn(name = "ORGANIZATION_ID")
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
	 * @return the userName
	 */
	@Column(name = "USERNAME", length=64, nullable=false)
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	@Column(name = "PASSWORD", length=32, nullable=false)
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the sex
	 */
	@Column(name = "SEX", length=16)
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the realName
	 */
	@Column(name = "REALNAME", length=32)
	public String getRealName() {
		return realName;
	}
	/**
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}
	/**
	 * @return the phone
	 */
	@Column(name = "PHONE", length=32)
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the address
	 */
	@Column(name = "ADDRESS", length=128)
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the email
	 */
	@Column(name = "EMAIL", length=64)
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the status
	 */
	@Column(name = "STATUS", length=16)
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the createtime
	 */
	@Column(name = "CREATETIME")
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * @return the disabletime
	 */
	@Column(name = "DISABLETIME")
	public Date getDisabletime() {
		return disabletime;
	}
	/**
	 * @param disabletime the disabletime to set
	 */
	public void setDisabletime(Date disabletime) {
		this.disabletime = disabletime;
	}
	
	@Column(name = "DEL_FLAG", length=16)
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	@Column(name = "USER_TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the statusName
	 */
	@Transient
	public String getStatusName() {
		List<DictionaryEntity> iconTypeList = DictionaryGroupEntity.allTypes.get(Constants.DICT_GROUP_STATUS);
		for (DictionaryEntity dictionaryEntity : iconTypeList) {
			if(dictionaryEntity.getCode().equals(this.getStatus())){
				return dictionaryEntity.getName();
			}
		}
		return null;
	}
	
	/**
	 * @return the sexName
	 */
	@Transient
	public String getSexName() {
		List<DictionaryEntity> iconTypeList = DictionaryGroupEntity.allTypes.get(Constants.DICT_GROUP_SEX);
		for (DictionaryEntity dictionaryEntity : iconTypeList) {
			if(dictionaryEntity.getCode().equals(this.getSex())){
				return dictionaryEntity.getName();
			}
		}
		return null;
	}
	
	
}
