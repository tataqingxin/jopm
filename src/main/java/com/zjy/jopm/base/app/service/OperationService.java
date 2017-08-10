/** 
 * @Description:[功能操作管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.OperationService.java
 * @ClassName:OperationService
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:13:19
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午3:13:19  
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
import com.zjy.jopm.base.app.entity.OperationEntity;

/**
 * @ClassName: OperationService 
 * @Description: 功能操作管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:13:19 
 * @since JDK 1.6 
 */
public interface OperationService extends BaseService {

	/**
	 * 
	 * @Title: getOperationEntityQuiGrid 
	 * @Description: [某功能下的操作列表]
	 * @param resourceEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getOperationEntityQuiGrid(OperationEntity operationEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException;

	/**
	 * 
	 * @Title: insertOperationEntity
	 * @Description: [保存操作数据]
	 * @param functionId
	 * @param resourceEntity
	 * @param resourceIds
	 * @return
	 */
	public AjaxJson insertOperationEntity(String functionId, OperationEntity operationEntity, Object[] resourceIds) throws JumpException;
	
	/**
	 * 
	 * @Title: updateOperationEntity
	 * @Description: [修改操作数据]
	 * @param functionId
	 * @param operationEntity
	 * @param resourceIds
	 * @return
	 */
	public AjaxJson updateOperationEntity(String functionId, OperationEntity operationEntity, Object[] resourceIds);
	
	/**
	 * 
	 * @Title: expandOperationEntity
	 * @Description: [查找单个操作数据]
	 * @param id
	 * @return
	 */
	public AjaxJson expandOperationEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: deleteOperationEntity
	 * @Description: [删除单个操作数据]
	 * @param id
	 * @return
	 */
	public AjaxJson deleteOperationEntity(String id) throws JumpException;
}
