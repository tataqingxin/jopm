package com.zjy.jopm.base.app.service;

import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

public interface AppMirrorService  extends BaseService {

	/**
	 * 
	 * @Title: getAppMirrorQuiGrid 
	 * @Description: [某机构的应用镜像列表]
	 * @param AppMirrorEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getAppMirrorQuiGrid(AppMirrorEntity appMirror, int pageNo, int pageSize, String sort, String direction)throws JumpException;
	
	/**
	 * @Title: insertAppMirror
	 * @Description: [保存应用镜像]
	 * @param appMirror
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson insertAppMirror(AppMirrorEntity appMirror) throws JumpException;
	
	/**
	 * @Title: updateAppMirror
	 * @Description: [更新应用镜像]
	 * @param appMirror
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson updateAppMirror(AppMirrorEntity appMirror) throws JumpException;
	
	/**
	 * @Title: expandAppMirror
	 * @Description: [查询单个应用镜像]
	 * @param id
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson expandAppMirror(String id) throws JumpException;
	
	/**
	 * @Title: deleteAppMirror
	 * @Description: [删除应用镜像] 
	 * @param id
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson deleteAppMirror(String id) throws JumpException;

	/**
	 * 
	 * @param organizationEntity
	 * @param sessionInfo
	 * @return
	 */
	public AjaxJson OrganizationServiceTree(OrganizationEntity organizationEntity, SessionInfo sessionInfo,String orgId) throws JumpException;

	/**
	 * 
	 * @param odgIds
	 * @param appId
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson addAppMirror(String orgIds, String appId) throws JumpException;

	/**
	 * 
	 * @param appId
	 * @param path
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson getOrgList(String appId, String orgId) throws JumpException;

	/**
	 * 
	 * @param appId
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson appMirrorCancel(String appId) throws JumpException;
}
