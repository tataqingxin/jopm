/** 
 * @Description:[资源管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.ResourceService.java
 * @ClassName:ResourceService
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:14:01
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午3:14:01  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service;

import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.app.entity.ResourceEntity;

/**
 * @ClassName: ResourceService 
 * @Description: [资源管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:14:01 
 * @since JDK 1.6 
 */
public interface ResourceService extends BaseService {

	/**
	 * 
	 * @Title: getResourceEntityQuiGrid 
	 * @Description: [某功能下的资源列表]
	 * @param functionId
	 * @param resourceEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getResourceEntityQuiGrid(String functionId, ResourceEntity resourceEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException;

	/**
	 * 
	 * @Title: insertResourceEntity
	 * @Description: [保存资源数据]
	 * @param functionId
	 * @param resourceEntity
	 * @return
	 */
	public AjaxJson insertResourceEntity(String functionId, ResourceEntity resourceEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: updateResourceEntity
	 * @Description: [修改资源数据]
	 * @param functionId
	 * @param resourceEntity
	 * @return
	 */
	public AjaxJson updateResourceEntity(String functionId, ResourceEntity resourceEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: expandResourceEntity
	 * @Description: [查找单个资源数据]
	 * @param id
	 * @return
	 */
	public AjaxJson expandResourceEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: deleteResourceEntity
	 * @Description: [删除单个资源数据]
	 * @param functionId
	 * @param id
	 * @return
	 */
	public AjaxJson deleteResourceEntity(String functionId, String id) throws JumpException;
	
	/**
	 * 
	 * @Title: getResourceListByFunctionId 
	 * @Description: [获取某个功能的资源数据]
	 * @param functionId
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson getResourceListByFunctionId(String functionId) throws JumpException;
	
}
