/** 
 * @Description:[对外用户管理接口]   
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
package com.zjy.jopm.base.user.service.ext;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: UserService 
 * @Description: [对外用户管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:14:33 
 * @since JDK 1.6 
 */
public interface UserExtService extends BaseService {
	
	/**
	 * 
	* @Title: getUserListByOrgId 
	* @Description: 通过机构id获取用户
	* @param @return  参数说明 
	* @return List<UserEntity> 返回类型 
	* @throws JumpException 异常类型
	 */
	public List<UserEntity> getUserListByOrgId(String orgId);
	

	/**
	 * 
	* @Title: getUserListByOrgId 
	* @Description: 通过机构id获取用户
	* @param @return  参数说明 
	* @return List<UserEntity> 返回类型 
	* @throws JumpException 异常类型
	 */
	public List<UserEntity> getUserListByDepartId(String departId);
	
	/**
	 * 
	* @Title: getUserListByRoleId 
	* @Description: 根据角色获取人员
	* @param @param roleId
	* @param @return  参数说明 
	* @return List<UserEntity> 返回类型 
	* @throws JumpException 异常类型
	 */
	public List<UserEntity> getUserListByRoleId(String roleId);
	
	/**
	  * 
	 * @Title: getOrgTreeByOrgId 
	 * @Description: 根据所选择的机构 组成机构树
	 * @param @param orgId
	 * @param @return  参数说明 
	 * @return List<TreeNode> 返回类型 
	 * @throws JumpException 异常类型
	  */
	 public AjaxJson getOrgUserTreeByorgId(String orgId,String roleId);
	 
	 /**
	  * 
	  * @Title: login 
	  * @Description: [登陆]
	  * @param account
	  * @param password
	  * @return
	  */
	 public AjaxJson login(String account, String password) throws JumpException;
	 
	 /**
	  * 
	  * @Title: logout 
	  * @Description: [登出]
	  * @return
	  */
	 public AjaxJson logout() throws JumpException;
	 
	 /**
	  * 
	 * @Title: getOrgDepartTreeByorgId 
	 * @Description: [机构部门树]
	 * @param @param orgId
	 * @param @return  参数说明 
	 * @return AjaxJson 返回类型 
	 * @throws JumpException 异常类型
	  */
	 public AjaxJson getOrgDepartTreeByorgId(String orgId);
	 
	 /**
	  * 
	 * @Title: getDepartIdsByUser 
	 * @Description: [得到用户所属的部门]
	 * @param @param userId
	 * @param @return  参数说明 
	 * @return List<String> 返回类型 
	 * @throws JumpException 异常类型
	  */
	 public List<String> getDepartIdsByUser(String userId);
	 
	 
	 
	 /**
	  * 
	  * @Title: sendUpPwdMail 
	  * @Description: [给修改密码的用户发送邮件]
	  * @param userName
	 * @param request 
	  * @return AjaxJson
	  * @throws JumpException
	  */
	 public AjaxJson sendUpPwdMail(String userName, HttpServletRequest request) throws JumpException;
	 

}
