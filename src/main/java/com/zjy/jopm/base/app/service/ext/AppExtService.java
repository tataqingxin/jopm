/** 
 * @Description:[应用及子功能对外接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.app.service.ext.AppExtService.java
 * @ClassName:AppExtService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月13日 下午3:28:02
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月13日 下午3:28:02  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service.ext;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;

/**
 * @ClassName: AppExtService 
 * @Description: [应用及子功能对外接口] 
 * @author Lu Guoqiang
 * @date 2016年5月13日 下午3:28:02 
 * @since JDK 1.6 
 */
public interface AppExtService extends BaseService {
	
	/**
	 * 
	 * @Title: getOperationTree 
	 * @Description: [获取功能操作树]
	 * @param roleId
	 * @param applicationId
	 * @param functionId
	 * @param path
	 * @param orgId 
	 * @return
	 * @throws JumpException
	 */
	public AjaxJson getOperationTree(String roleId, String applicationId, String functionId, String path, String orgId) throws JumpException;

}
