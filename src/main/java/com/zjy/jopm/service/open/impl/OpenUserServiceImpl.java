package com.zjy.jopm.service.open.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.sdk.entityVO.Role;
import com.zjy.jopm.sdk.entityVO.User;
import com.zjy.jopm.sdk.service.OpenUserService;

@Service("openUserService")
@Transactional
public class OpenUserServiceImpl extends BaseServiceimpl implements OpenUserService {

	@Override
	public User getUserInfo(String userName) {
		String hql = "from UserEntity where username=:userName";
		List<UserEntity> userList = new ArrayList<UserEntity>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName",userName);
		userList = this.queryListByHql(hql, params);
		
		if(!CollectionUtils.isEmpty(userList)){
			UserEntity uold = userList.get(0);
			User unew = new User();
			unew.setOrgCode(uold.getOrganizationEntity().getCode());
			unew.setOrgName(uold.getOrganizationEntity().getName());
			unew.setUserName(userName);
			unew.setRealName(uold.getRealName());
			unew.setSex(uold.getSex());
			
			return unew;
		}
		return null;
	}

	@Override
	public List<Role> getUserRoles(String userName) {
		String hql = "from RoleUserRelationEntity relation where relation.userEntity.userName=:userName";
		List<Role> list = new ArrayList<Role>();
		List<RoleUserRelationEntity> oldList = new ArrayList<RoleUserRelationEntity>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName",userName);
		oldList = this.queryListByHql(hql, params);
		
		if(!CollectionUtils.isEmpty(oldList)){
			for(RoleUserRelationEntity oldRole : oldList){
				Role newRole = new Role();
				newRole.setCode(oldRole.getRoleEntity().getCode());
				newRole.setName(oldRole.getRoleEntity().getName());
				list.add(newRole);
			}
		}
		return list;
	}

	@Override
	public List<User> getUserList(String orgCode) {

		String hql = "from UserEntity user where user.organizationEntity.code=:orgCode";
		List<UserEntity> list = new ArrayList<UserEntity>();
		List<User> newList = new ArrayList<User>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode",orgCode);
		list = this.queryListByHql(hql, params);
		
		if(!CollectionUtils.isEmpty(list)){
			for(UserEntity uold : list){
				User unew = new User();
				unew.setOrgCode(uold.getOrganizationEntity().getCode());
				unew.setOrgName(uold.getOrganizationEntity().getName());
				unew.setUserName(uold.getUserName());
				unew.setRealName(uold.getRealName());
				unew.setSex(uold.getSex());
				newList.add(unew);
			}
		}
		return newList;
	
	}


}
