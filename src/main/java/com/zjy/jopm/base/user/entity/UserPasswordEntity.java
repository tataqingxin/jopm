package com.zjy.jopm.base.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.unistc.core.common.entity.IdEntity;

@Entity
@Table(name = "t_sys_user_password")
public class UserPasswordEntity extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5546877683281086656L;

	private UserEntity user;
	private Date lastModifyPasswordTime;

	@OneToOne
	@JoinColumn(name = "USER_ID")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Column(name = "LAST_MODIFY_PASSWORD_TIME")
	public Date getLastModifyPasswordTime() {
		return lastModifyPasswordTime;
	}

	public void setLastModifyPasswordTime(Date lastModifyPasswordTime) {
		this.lastModifyPasswordTime = lastModifyPasswordTime;
	}

}
