/** 
 * @Description:[用户管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.user.service.UserService.java
 * @ClassName:UserService
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:14:33
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午3:14:33  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.user.service;

import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: UserService 
 * @Description: [用户管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:14:33 
 * @since JDK 1.6 
 */
public interface UserService extends BaseService {
   /**
    * 用户列表
   * @Title: getUserListQuiGrid 
             pager.pageNo：当前页
			pager.pageSize: 每页多少条记录
			sort：传递当前排序的字段名
			direction：传递当前的排序方向
			orgId:机构或者部门id
			isInclude:是否包含子部门人员
   * @return Map<String,Object> 返回类型 
   * @throws JumpException 异常类型
    */
	Map<String, Object> getUserListQuiGrid(UserEntity user, String pageNo,
			String pageSize, String sort, String direction,String isInclude,String orgId);
    /**
     * 保存 编辑user
    * @Title: saveOrUpdateUser 
    * @Description: [功能描述]
    * @param @param selectId
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
    AjaxJson saveOrUpdateUser(UserEntity user,String selectId,String orgId);
    /**
     * 
    * @Title: deleteUser 
    * @Description: [删除用户id]
    * @param @param ids
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson deleteUser(String ids);
	/**
	 * 
	* @Title: updateStatus 
	* @Description: [启用 停用]
	* @param @param ids
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	AjaxJson updateStatus(String ids);
	/**
	 * 
	* @Title: resetPassWrod 
	* @Description: [重置密码]
	* @param @param ids
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	AjaxJson resetPassWrod(String ids);
	
	/**
     * 
    * @Title: updatePasswWord 
    * @Description: [修改密码]
    * @param @param id
    * @param @param passwordNew
    * @param @param passwordOld
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	public AjaxJson updatePasswWord(String id, String passwordNew,
			String passwordOld);
	/**
	 * 
	* @Title: saveOrUpdateUserForEamil 
	* @Description: [保存用户并发送邮件]
	* @param @param user
	* @param @param selectId
	* @param @param orgId
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	AjaxJson saveOrUpdateUserForEamil(UserEntity user, String selectId,
			String orgId);

}
