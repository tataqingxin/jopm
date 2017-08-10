package com.zjy.jopm.base.email.service;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;

public interface EmailLoseService extends BaseService{

	AjaxJson expandEmailLose(String emailId) throws Exception;

}
