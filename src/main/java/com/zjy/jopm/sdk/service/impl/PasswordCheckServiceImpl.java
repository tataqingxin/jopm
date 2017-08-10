package com.zjy.jopm.sdk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.DateUtil;
import com.zjy.jopm.base.user.entity.UserPasswordEntity;
import com.zjy.jopm.base.user.service.UserPasswordService;
import com.zjy.jopm.sdk.entityVO.PasswordCheckResult;
import com.zjy.jopm.sdk.service.OpenParamService;
import com.zjy.jopm.sdk.service.PasswordCheckService;

@Service("passwordCheckService")
@Transactional
public class PasswordCheckServiceImpl implements PasswordCheckService {

	@Autowired
	private UserPasswordService userPasswordService;
	
	@Autowired
	private OpenParamService openParamService;
	
	private static final String KEY_JOPM_GLOBAL_WAN_CONTEXT_ROOT = "JOPM_GLOBAL_WAN_CONTEXT_ROOT";

	private static final String KEY_JOPM_URL_RESET_PASSWORD = "JOPM_URL_RESET_PASSWORD";
	
	@Override
	public PasswordCheckResult checkByUserId(String userId) {
		
		PasswordCheckResult result = new PasswordCheckResult();
		
		DateUtil dateUtil = new DateUtil();
		
		UserPasswordEntity recordByUserId = userPasswordService.getRecordByUserId(userId);
		if (recordByUserId == null) {
			result.setLastModifyTime(null);
		} else {
			result.setLastModifyTime(dateUtil.parseDateToString(recordByUserId.getLastModifyPasswordTime(), DateUtil.FORMAT_DATETIME_STRING));
		}

		result.setPasswordModifyUrl(openParamService.getValue(KEY_JOPM_GLOBAL_WAN_CONTEXT_ROOT) + openParamService.getValue(KEY_JOPM_URL_RESET_PASSWORD));
		// result.setPasswordModifyUrl("http://192.168.0.87:6280/jopm/webpage/base/user/resetPassword.jsp");
		return result;
	}

	public UserPasswordService getUserPasswordService() {
		return userPasswordService;
	}

	public void setUserPasswordService(UserPasswordService userPasswordService) {
		this.userPasswordService = userPasswordService;
	}

}
