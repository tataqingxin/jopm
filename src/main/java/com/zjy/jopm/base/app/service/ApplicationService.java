/** 
 * @Description:[应用管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.impl.ApplicationService.java
 * @ClassName:ApplicationService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:09:56
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:09:56  
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
import com.zjy.jopm.base.app.entity.ApplicationEntity;

/**
 * @ClassName: ApplicationService 
 * @Description: [应用管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:09:56 
 * @since JDK 1.6 
 */
public interface ApplicationService extends BaseService {
	
	/**
	 * 
	 * @Title: getApplicationEntityQuiGrid 
	 * @Description: [某机构的应用列表]
	 * @param applicationEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return
	 * @throws JumpException
	 */
	public Map<String, Object> getApplicationEntityQuiGrid(ApplicationEntity applicationEntity, int pageNo, int pageSize, String sort, String direction)throws JumpException;
	
	/**
	 * 
	 * @Title: insertApplicationEntity
	 * @Description: [保存应用数据]
	 * @param applicationEntity
	 * @return
	 */
	public AjaxJson insertApplicationEntity(ApplicationEntity applicationEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: updateApplicationEntity 
	 * @Description: [修改应用数据]
	 * @param applicationEntity
	 * @return
	 */
	public AjaxJson updateApplicationEntity(ApplicationEntity applicationEntity) throws JumpException;
	
	/**
	 * 
	 * @Title: expandApplicationEntity
	 * @Description: [查找单个应用数据]
	 * @param id
	 * @return
	 */
	public AjaxJson expandApplicationEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: deleteApplicationEntity
	 * @Description: [删除单个应用数据]
	 * @param id
	 * @return
	 */
	public AjaxJson deleteApplicationEntity(String id) throws JumpException;
	
	/**
	 * 
	 * @Title: enableApplicationStatus 
	 * @Description: [启用/禁用应用状态]
	 * @param id
	 * @return
	 */
	public AjaxJson enableApplicationStatus(String id) throws JumpException;

	/**
	 * 提供sdk调用
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getAppDataQuiGrid(int pageNo,int pageSize);

	public AjaxJson updateStrategy(String id, String strategy);
	
}
