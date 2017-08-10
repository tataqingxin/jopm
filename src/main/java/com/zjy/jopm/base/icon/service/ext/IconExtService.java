/** 
 * @Description:[对外图标管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.icon.service.IconService.java
 * @ClassName:IconService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:12:04
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:12:04  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.icon.service.ext;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;

/**
 * @ClassName: IconService 
 * @Description: [对外图标管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:12:04 
 * @since JDK 1.6 
 */
public interface IconExtService extends BaseService {
	
	/**
	 * 
	 * @Title: getIconTreeList 
	 * @Description: [外部获取图片列表]
	 * @return AjaxJson
	 */
	AjaxJson getIconTreeList();
}
