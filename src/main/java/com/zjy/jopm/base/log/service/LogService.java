/** 
 * @Description:[日志管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.log.service.LogService.java
 * @ClassName:LogService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:12:51
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:12:51  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.log.service;

import java.util.Map;

import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.log.entity.LogEntity;

/**
 * @ClassName: LogService
 * @Description: [日志管理接口]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:12:51
 * @since JDK 1.6
 */
public interface LogService extends BaseService {

	/**
	 * 
	 * @Title: getLogDataGrid 
	 * @Description: [功能描述]
	 * @param logEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction 
	 * @return Map<String, Object>
	 * @throws JumpException
	 */
	Map<String, Object> getLogDataGrid(LogEntity logEntity, int pageNo, int pageSize, String sort, String direction) throws JumpException;

}
