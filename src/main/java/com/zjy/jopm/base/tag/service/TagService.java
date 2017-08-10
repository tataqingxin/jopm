package com.zjy.jopm.base.tag.service;

import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;

public interface TagService extends BaseService{

	public Map<String, Object> getTagForm(int pageNo, int pageSize);

	public AjaxJson roleTree(String tagId);

	public AjaxJson setRoleTag(String tagId, String[] roleIds);

	public AjaxJson tagTree(String functionId);

	public AjaxJson setTagFunction(String functionId, String[] tagIds);

}
