/** 
 * @Description:[部门管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.service.DepartmentService.java
 * @ClassName:DepartmentService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:10:38
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午3:10:38  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.service;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.org.entity.DepartmentEntity;

/**
 * @ClassName: DepartmentService 
 * @Description: [部门管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:10:38 
 * @since JDK 1.6 
 */
public interface DepartmentService extends BaseService {
    /**
     * 
    * @param orgId 
     * @Title: departmentServiceTree 
    * @Description: 部门树
    * @param @param departId
    * @param @param sessionInfo
    * @param @return  参数说明 
    * @return List<TreeNode> 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson departmentServiceTree(DepartmentEntity departmentEntity,String orgId,boolean isCheck);
    /**
     * 
    * @param id  所选部门id
     * @Title: saveOrUpdateDepartment 
    * @Description: 保存部门
    * @param @param departmentEntity
    * @param @param orgId
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson saveOrUpdateDepartment(DepartmentEntity departmentEntity, String orgId, String id);
	
	/**
	 * 
	* @Title: delDepartmentEntity 
	* @Description:删除部门
	* @param @param id
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	AjaxJson delDepartmentEntity(String id);

}
