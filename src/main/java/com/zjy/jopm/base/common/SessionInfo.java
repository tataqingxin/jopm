package com.zjy.jopm.base.common;

import java.io.Serializable;
import java.util.List;

import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.user.entity.UserEntity;



public class SessionInfo implements Serializable {
	
	/**
	 * @Fields serialVersionUID : [功能描述] 
	 */
	private static final long serialVersionUID = -5157164650327801521L;

	private UserEntity user; // 用户
	private int identity;// 登录用户身份 0普通用户 1特殊用户
	private List<String> functionList;// 功能
	private List<String> resourceList;// 功能操作
	private List<RoleEntity> roleList;
	
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	
	private String organizationId; //所属机构Id
	
	
	
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	/**
	 * @return the functionList
	 */
	public List<String> getFunctionList() {
		return functionList;
	}

	/**
	 * @param functionList the functionList to set
	 */
	public void setFunctionList(List<String> functionList) {
		this.functionList = functionList;
	}

	/**
	 * @return the resourceList
	 */
	public List<String> getResourceList() {
		return resourceList;
	}

	/**
	 * @param resourceList the resourceList to set
	 */
	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}

	/**
	 * @return the roleList
	 */
	public List<RoleEntity> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<RoleEntity> roleList) {
		this.roleList = roleList;
	}
	
}
