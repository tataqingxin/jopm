package com.zjy.jopm.base.user.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.CollectionUtil;
import com.unist.util.StringUtil;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.entity.UserPasswordEntity;
import com.zjy.jopm.base.user.service.UserPasswordService;

@Service("userPasswordServiceImpl")
@Transactional
public class UserPasswordServiceImpl extends BaseServiceimpl implements UserPasswordService {

	@Override
	public UserPasswordEntity getRecordByUserId(String userId) {
		List<UserPasswordEntity> queryListByHql = super.queryListByHql("from UserPasswordEntity upe where upe.user.id = ?", userId);
		if (CollectionUtil.isEmpty(queryListByHql)) {
			return null;
		} else {
			return queryListByHql.get(0);
		}
	}

	@Override
	public AjaxJson updateUserPasswordRecord(String userId, String oldPassword, String newPassword) {
		
		
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(false);
		
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(oldPassword) || StringUtil.isEmpty(newPassword)) {
			ajaxJson.setMessage("关键参数为空，请重试");
			return ajaxJson;
		}
		
		UserEntity userEntity = super.expandEntity(UserEntity.class, userId);
		
		if (userEntity == null || StringUtil.isEmpty(userEntity.getId())) {
			ajaxJson.setMessage("用户信息不存在，请重试");
			return ajaxJson;
		}
		
		if (!oldPassword.equals(userEntity.getPassword())) {
			ajaxJson.setMessage("旧密码错误，请重试");
			return ajaxJson;
		}
		
		userEntity.setPassword(newPassword);
		
		// 更新用户密码
		updateEntity(userEntity);
		
		
		UserPasswordEntity userPasswordEntity = new UserPasswordEntity();
		userPasswordEntity.setUser(userEntity);
		userPasswordEntity.setLastModifyPasswordTime(new Date());
		
		super.saveEntity(userPasswordEntity);
		
		ajaxJson.setSuccess(true);
		ajaxJson.setMessage("密码更新成功");
		
		return ajaxJson;
	}

}
