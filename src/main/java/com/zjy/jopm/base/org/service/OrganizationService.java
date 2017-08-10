/** 
 * @Description:[机构管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.service.OrganizationService.java
 * @ClassName:OrganizationService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:13:44
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午3:13:44  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.service;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.org.entity.OrganizationEntity;

/**
 * @ClassName: OrganizationService 
 * @Description: [机构管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:13:44 
 * @since JDK 1.6 
 */
public interface OrganizationService extends BaseService {
    /**
     * 
    * @param sessionInfo 
     * @Title: OrganizationServiceTree 
    * @Description: 获取机构树 根据当前登录的用户
    * @param @param organizationEntity
    * @param @return  参数说明 
    * @return List<TreeNode> 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson OrganizationServiceTree(OrganizationEntity organizationEntity, SessionInfo sessionInfo);
    /**
     * 
    * @param parentId 
     * @Title: saveOrUpdateOrganization 
    * @Description: 保存或者更新
    * @param @param organizationEntity
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson saveOrUpdateOrganization(OrganizationEntity organizationEntity);
	
	/**
	 * 
	* @Title: delOrganizationEntity 
	* @Description: 删除机构
	* @param @param id
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	AjaxJson delOrganizationEntity(String id);

}
