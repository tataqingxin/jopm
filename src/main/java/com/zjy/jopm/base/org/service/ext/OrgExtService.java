/** 
 * @Description:[机构部门对外接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.service.ext.OrgExtService.java
 * @ClassName:OrgExtService
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月13日 下午3:37:14
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月13日 下午3:37:14  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.service.ext;

import java.util.List;

import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.org.entity.DepartmentEntity;

/**
 * @ClassName: OrgExtService 
 * @Description: [机构部门对外接口] 
 * @author Lu Guoqiang
 * @date 2016年5月13日 下午3:37:14 	
 * @since JDK 1.6 
 */
public interface OrgExtService extends BaseService {
	/**
	 * 
	* @Title: getDepartmentListByUserId 
	* @Description: 根据用户id获取所属的部门
	* @param @param userId
	* @param @return  参数说明 
	* @return List<DepartmentEntity> 返回类型 
	* @throws JumpException 异常类型
	 */
	 public List<DepartmentEntity> getDepartmentListByUserId(String userId);
	 
	 

}
