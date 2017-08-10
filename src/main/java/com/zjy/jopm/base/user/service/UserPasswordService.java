package com.zjy.jopm.base.user.service;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.zjy.jopm.base.user.entity.UserPasswordEntity;

public interface UserPasswordService extends BaseService {
	
	UserPasswordEntity getRecordByUserId(String userId);
	
	AjaxJson updateUserPasswordRecord(String userId, String oldPassword, String newPassword);

}
