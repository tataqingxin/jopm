/** 
 * @Description:[服务接口管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.ServiceInterfaceService.java
 * @ClassName:ServiceInterfaceService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:14:56
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:14:56  
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
import com.zjy.jopm.base.app.entity.ServiceInterfaceEntity;

/**
 * @ClassName: ServiceInterfaceService 
 * @Description: [服务接口管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:14:56 
 * @since JDK 1.6 
 */
public interface ServiceInterfaceService extends BaseService {
	
	/**
	 * 
	 * @Title: getServiceInterfaceEntityQuiGrid 
	 * @Description: [某应用下的服务接口列表]
	 * @param serviceInterfaceEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getServiceInterfaceEntityQuiGrid(ServiceInterfaceEntity serviceInterfaceEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException;

	/**
	 * 
	 * @Title: insertServiceInterfaceEntity
	 * @Description: [保存服务接口数据]
	 * @param serviceInterfaceEntity
	 * @return
	 */
	public AjaxJson insertServiceInterfaceEntity(ServiceInterfaceEntity serviceInterfaceEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: updateServiceInterfaceEntity
	 * @Description: [修改服务接口数据]
	 * @param serviceInterfaceEntity
	 * @return
	 */
	public AjaxJson updateServiceInterfaceEntity(ServiceInterfaceEntity serviceInterfaceEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: expandApplicationEntity
	 * @Description: [查找单个服务接口数据]
	 * @param id
	 * @return
	 */
	public AjaxJson expandServiceInterfaceEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: deleteApplicationEntity
	 * @Description: [删除单个服务接口数据]
	 * @param id
	 * @return
	 */
	public AjaxJson deleteServiceInterfaceEntity(String id) throws JumpException;
	
}
