package com.zjy.jopm.visitor.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.visitor.entity.VisitorEntity;

public interface VisitorService extends BaseService{

	/**
	 * 
	 * @Title: getvisitorQuiGrid 
	 * @Description: [游客可以访问的功能列表]
	 * @param VisitorEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getVisitorQuiGrid(VisitorEntity visitor, int pageNo, int pageSize, String sort, String direction)throws JumpException;
	
	/**
	 * @Title: insertVisitor
	 * @Description: [保存游客可以访问的功能]
	 * @param visitor
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson insertVisitor(String functionIds) throws JumpException;
	
	/**
	 * @Title: updateVisitor
	 * @Description: [更新]
	 * @param visitor
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson updateVisitor(VisitorEntity visitor) throws JumpException;
	
	/**
	 * @Title: expandVisitor
	 * @Description: [查询单个]
	 * @param id
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson expandVisitor(String id) throws JumpException;
	
	/**
	 * @Title: deleteVisitor
	 * @Description: [删除] 
	 * @param id
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson deleteVisitor(String id) throws JumpException;

	/**
	 * 获取应用功能树
	 * @param applicationId
	 * @param functionId
	 * @param path
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson getAppFunctionTree(String applicationId, String functionId, String path) throws JumpException;

	public Object getVisitorSwitch() throws JumpException;

	public Object getVisitorFunction(HttpServletRequest request) throws JumpException;

	public AjaxJson changeVisitorSwitch(String visitorSwitch) throws JumpException;
	
}
