/** 
 * @Description:[对外日志管理接口]   
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
package com.zjy.jopm.base.log.service.ext;

import com.unistc.core.common.service.BaseService;

/**
 * @ClassName: LogService 
 * @Description: [对外日志管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:12:51 
 * @since JDK 1.6 
 */
public interface LogExtService extends BaseService {
	
	/**
	 * 
	 * @Title: insertLog 
	 * @Description: [添加日志]
	 * @param LogContent 内容
	 * @param loglevel 级别
	 * @param operatetype 类型
	 * @return true/false
	 */
	void insertLog(String LogContent, String loglevel,String operatetype);

}
