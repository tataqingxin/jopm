package com.zjy.jopm.base.app.service;

import java.util.List;
import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.app.entity.ApplicationEntity;

public interface ServiceAuthService extends BaseService{

	public AjaxJson insertServiceAuth(String serviceId,String applicationIds) throws JumpException;

	public AjaxJson getAppServiceTree(String applicationId, String path) throws JumpException;

	public Map<String, Object> getApplicationQuiGrid(ApplicationEntity applicationEntity, int pageNo, int pageSize,
			String sort, String direction) throws JumpException;

	public AjaxJson getServiceAuthList(String serviceId) throws JumpException;

	public List<Map<String,Object>> getAppInstanceCodeAndUrl();
}
