/** 
 * @Description:[对外用户接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.user.service.impl.UserExtServiceImpl.java
 * @ClassName:UserExtServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:30:38
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:30:38  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.user.service.impl.ext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.PasswordUtil;
import com.unistc.utils.ResourceUtil;
import com.zjy.jopm.base.app.entity.OperationResourceRelationEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SendMailUtil;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.email.entity.EmailLoseEntity;
import com.zjy.jopm.base.email.service.EmailLoseService;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.user.entity.UserDepartmentRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.ext.UserExtService;

/**
 * @ClassName: UserExtServiceImpl
 * @Description: [对外用户接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:30:38
 * @since JDK 1.6
 */
@Service("userExtService")
@Transactional
public class UserExtServiceImpl extends BaseServiceimpl implements
		UserExtService {

	@Autowired
	private LogExtService logExtService;
	
	@Autowired
	private EmailLoseService emailLoseService;

	/**
	 * @Title: getUserListByOrgId
	 * @Description: 通过机构id获取用户
	 * @param @return 参数说明
	 * @return List<UserEntity> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public List<UserEntity> getUserListByOrgId(String orgId) {
		String hql = "from UserEntity where delFlag='0'  ";
		List<UserEntity> userList = new ArrayList<UserEntity>();
		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(orgId)) {
			//去掉部门下的用户
			List<String> userIds=new ArrayList<String>();
			String departHql = "from UserDepartmentRelationEntity";
			List<Object> departParam = new ArrayList<Object>();
			List<UserDepartmentRelationEntity> userRelation = this
					.queryListByHql(departHql, departParam);
			userIds = getIds(userRelation);
			hql += " AND organizationEntity.id= :orgId and id not in(:userIds)";
			param.put("orgId",orgId);
			param.put("userIds",userIds.size()>0?userIds:"");
		}
		userList = this.queryListByHql(hql, param);
		return userList;
	}

	private List<String> getIds(List<UserDepartmentRelationEntity> userRelation) {
		List<String> userIds=new ArrayList<String>();
		for (UserDepartmentRelationEntity us : userRelation) {
			 String id=us.getUserEntity().getId();
			 userIds.add(id);
		}
		return userIds;
	}

	/**
	 * @Title: getUserListByOrgId
	 * @Description: 通过机构id获取用户
	 * @param @return 参数说明
	 * @return List<UserEntity> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public List<UserEntity> getUserListByDepartId(String departId) {
		String hql = "from UserEntity where delFlag='0'  ";
		List<UserEntity> userList = new ArrayList<UserEntity>();
		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(departId)) {
			List<String> userIds=new ArrayList<String>();
			String departHql = "from UserDepartmentRelationEntity where departmentEntity.id=?";
			List<UserDepartmentRelationEntity> users = this.queryListByHql(
					departHql, departId);
			userIds = getIds(users);
			hql += " AND id in (:userIds)";
			param.put("userIds",userIds.size()>0?userIds:"");
		}
		userList = this.queryListByHql(hql, param);
		return userList;
	}

	/**
	 * @Title: getUserListByRoleId
	 * @Description: 根据角色获取人员
	 * @param @param roleId
	 * @param @return 参数说明
	 * @return List<UserEntity> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public List<UserEntity> getUserListByRoleId(String roleId) {
		String hql = "from UserEntity where  delFlag='0'  ";
		List<UserEntity> userList = new ArrayList<UserEntity>();
		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(roleId)) {
			List<String> userIds=new ArrayList<String>();
			String roleHql = "from RoleUserRelationEntity where roleEntity.id=?";
			List<UserDepartmentRelationEntity> users = this.queryListByHql(
					roleHql, roleId);
			userIds = getIds(users);
			hql += " AND id in (:userIds)";
			param.put("userIds",userIds.size()>0?userIds:"");;
		}
		userList = this.queryListByHql(hql, param);
		return userList;
	}

	/**
	 * @Title: getOrgTreeByOrgId
	 * @Description: 根据所选择的机构 组成机构树
	 * @param @param orgId
	 * @param @return 参数说明
	 * @return List<TreeNode> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson getOrgUserTreeByorgId(String orgId, String roleId) {
		AjaxJson j = new AjaxJson();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		if (StringUtil.isNotEmpty(orgId) && StringUtil.isNotEmpty(roleId)) {
			OrganizationEntity organization = this.expandEntity(
					OrganizationEntity.class, orgId);
			//当传进来的orgId 不是机构id的时候 获取不到任何数据
			if (organization != null) {
				//组装根节点
				boolean isparent = false;
				//判断机构下面是否有人员或者部门
				List<DepartmentEntity> departMentEntityList = this.queryListByHql("from DepartmentEntity where parentDepartmentEntity is null and organizationEntity.id=?",orgId);
				//普通用户
				List<UserEntity> userEntity = this.queryListByHql("from UserEntity where organizationEntity.id = ? and type = '0'", orgId);
//				List<UserEntity> userEntity = this.queryListByProperty(
//						UserEntity.class, "organizationEntity.id",
//						organization.getId());
				if (departMentEntityList.size() > 0 || userEntity.size() > 0) {
					isparent = true;
				}
				//拥有角色的人员
				List<String> roleUserIds = new ArrayList<String>();
				List<RoleUserRelationEntity> roleUsers = this
						.queryListByProperty(RoleUserRelationEntity.class,
								"roleEntity.id", roleId);
				for (RoleUserRelationEntity roleUser : roleUsers) {
					roleUserIds.add(roleUser.getUserEntity().getId());
				}
				appendTreeNode(nodes, organization.getId(),
						organization.getName(), isparent, "", false, true);
				//组装人员
				//机构下面的普通人员
				List<UserEntity> orgUserList = this.queryListByHql("from UserEntity where organizationEntity.id = ? and type = '0'", orgId);
//				List<UserEntity> orgUserList = this
//						.getUserListByOrgId(organization.getId());
				for (UserEntity user : orgUserList) {
					String id = user.getId();
					//判断用户是否有角色
					if (roleUserIds.size()>0&&roleUserIds.contains(id)) {
						appendTreeNode(nodes, id, user.getUserName(), false,
								organization.getId(), true, false);
					} else {
						appendTreeNode(nodes, id, user.getUserName(), false,
								organization.getId(), false, false);
					}
				}
				//组装部门以及用户
				appendDepartOrUser(nodes, departMentEntityList, roleUserIds,
						organization.getId());
			}
		}
		j.setObj(nodes);
		return j;
	}

	/**
	 * 组装部门以及用户
	 * 
	 * @Title: appendDepartOrUser
	 * @Description: [功能描述]
	 * @param @param nodes
	 * @param @param departMentEntityList
	 * @param @param roleUserIds
	 * @param @param parentId 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private void appendDepartOrUser(List<TreeNode> nodes,
			List<DepartmentEntity> departMentEntityList,
			List<String> roleUserIds, String parentId) {
		//组装部门
		for (DepartmentEntity depart : departMentEntityList) {
				appendTreeNode(nodes, depart.getId(), depart.getName(), true,
						parentId, false, true);
				//组装部门下的人员
				List<UserEntity> departUserList = this.getUserListByDepartId(depart
						.getId());
				for (UserEntity user : departUserList) {
					String id = user.getId();
					//判断用户是否有角色
					if (roleUserIds.contains(id)) {
						appendTreeNode(nodes, id, user.getUserName(), false,
								depart.getId(), true, false);
					} else {
						appendTreeNode(nodes, id, user.getUserName(), false,
								depart.getId(), false, false);
					}
				}
				//循环遍历部门以及子部门or人员
				if (depart.getChildDepartmentEntity().size() > 0) {
					appendDepartOrUser(nodes, depart.getChildDepartmentEntity(),
							roleUserIds,depart.getId());
				}
		   }
}

	/**
	 * 组装树
	 * 
	 * @Title: appendTreeNode
	 * @Description: [功能描述]
	 * @param @param nodes
	 * @param @param id name ,parentID
	 * @param @param check 是否勾选
	 * @param @param noCheck 是否显示复习框
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private void appendTreeNode(List<TreeNode> nodes, String id, String name,
			boolean isparent, String parentId, boolean check, boolean noCheck) {
		TreeNode node = new TreeNode();
		node.setId(id);
		node.setName(name);
		node.setParentId(parentId);
		node.setOpen(isparent);
		node.setChecked(check);
		node.setNocheck(noCheck);
		nodes.add(node);
	}

	/**
	 * @Title: login
	 * @Description: [登陆]
	 * @param account
	 * @param password
	 * @param captcha
	 * @return
	 * @see com.zjy.jopm.base.user.service.ext.UserExtService#login(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public AjaxJson login(String account, String password) {
		AjaxJson ajaxJson = new AjaxJson();

		HttpSession session = ContextHolderUtil.getSession();
		SessionInfo sessionInfo = new SessionInfo();
		UserEntity userEntity = null;

		// 如果是特殊用户，直接返回成功
		if (ResourceUtil.getConfigByName("specialAccount").equals(account) && ResourceUtil.getConfigByName("specialAccountPwd").equals(password)) {
			userEntity = new UserEntity();
			userEntity.setUserName(ResourceUtil.getConfigByName("specialAccount"));
			sessionInfo.setUser(userEntity);
			sessionInfo.setIdentity(Constants.SPECIAL_ACCOUNT);
			session.setAttribute(Globals.USER_SESSION, sessionInfo);
			return ajaxJson;
		}

		//校验用户密码是否有效
		String hql = "FROM UserEntity WHERE delFlag='0' ";

		boolean isMatcher = false;// 是否匹配

		//登陆方式：手机号
		String regex = "^\\d{11}$";
		Matcher matcher = Pattern.compile(regex).matcher(account);
		isMatcher = matcher.matches();
		if (isMatcher) {
			hql += " AND phone = ? AND password = ?";
		} else {
			//登陆方式：邮箱
			regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			matcher = Pattern.compile(regex).matcher(account);
			isMatcher = matcher.matches();
			if (isMatcher) {
				hql += " AND email = ? AND password = ?";
			}
		}

		//登陆方式：用户名
		if (!isMatcher) {
			hql += " AND userName = ? AND password = ?";
		}

		//对密码进行加密处理
		password = PasswordUtil.encrypt(account, password, PasswordUtil.getStaticSalt());
		userEntity = super.expandEntityByHql(hql, new Object[] { account, password });
		if (null == userEntity) {
			ajaxJson.setMessage("用户名或密码错误");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}

		// 如果用户名密码校验通过
		if (Constants.DISABLE_STATUS.equals(userEntity.getStatus())) {
			ajaxJson.setMessage("用户已禁用，请联系管理员");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}

		sessionInfo.setUser(userEntity);
		sessionInfo.setIdentity(Constants.NORMAL_ACCOUNT);
		sessionInfo.setOrganizationId(userEntity.getOrganizationEntity().getId());

		List<String> functionList = new ArrayList<String>();
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> _param = null;

		//获取用户拥有的所有角色
		hql = "SELECT DISTINCT roleEntity.id FROM RoleUserRelationEntity WHERE userEntity.id = ?";
		List<String> roleIds = super.queryListByHql(hql, userEntity.getId());
		if (null != roleIds && roleIds.size() == 0) {
			ajaxJson.setMessage("用户角色未分配，请联系管理员");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		
		//设置用户角色
		_param = new HashMap<String, Object>();
		_param.put("roleIds", roleIds.size() > 0 ? roleIds : "");
		hql = "FROM RoleEntity WHERE id IN (:roleIds)";
		List<RoleEntity> roleEntityList = super.queryListByHql(hql, _param);
		sessionInfo.setRoleList(roleEntityList);
		
		for (String roleId : roleIds) {
			List<String> _operationIds = new ArrayList<String>();// 操作集合
			
			_param = new HashMap<String, Object>();
			_param.put("roleId", roleId);
			hql = "FROM RoleFunctionRelationEntity WHERE roleEntity.id = :roleId";
			List<RoleFunctionRelationEntity> roleFunctionRelationEntityList = super.queryListByHql(hql, _param);
			for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunctionRelationEntityList) {
				if (null != roleFunctionRelationEntity.getFunctionEntity()) {
					String functionUrl = roleFunctionRelationEntity.getFunctionEntity().getUrl();
					if (!functionList.contains(functionUrl)) {
						functionList.add(functionUrl);// 设置用户功能操作权限
					}
				}
				if (null != roleFunctionRelationEntity.getOperationEntity()) {
					String operationId = roleFunctionRelationEntity.getOperationEntity().getId();
					if (!_operationIds.contains(operationId)) {
						_operationIds.add(operationId);
					}
				}
			}
			
			if (_operationIds.size() > 0) {
				_param.put("operationIds", _operationIds.size()>0?_operationIds:"");
				hql = "FROM OperationResourceRelationEntity WHERE operationEntity.id IN (:operationIds)";
				List<OperationResourceRelationEntity> operationResourceRelationEntityList = super.queryListByHql(hql, _param);
				for (OperationResourceRelationEntity operationResourceRelationEntity : operationResourceRelationEntityList) {
					if (null != operationResourceRelationEntity.getResourceEntity()) {
						resourceList.add(operationResourceRelationEntity.getResourceEntity().getUrl());// 设置用户功能操作权限
					}
				}
			} 
		}
		sessionInfo.setFunctionList(functionList);
		sessionInfo.setResourceList(resourceList);

		session.setAttribute(Globals.USER_SESSION, sessionInfo);

		return ajaxJson;
	}

	/**
	 * @Title: logout
	 * @Description: [登出]
	 * @return
	 * @see com.zjy.jopm.base.user.service.ext.UserExtService#logout()
	 */
	@Override
	public AjaxJson logout() {
		AjaxJson ajaxJson = new AjaxJson();
		HttpSession session = ContextHolderUtil.getSession();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(Globals.USER_SESSION);
		if (null == sessionInfo) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("登出失败，可能已经登出");
			return ajaxJson;
		}
		UserEntity userEntity = sessionInfo.getUser();
		//记录日志
		this.logExtService.insertLog("用户" + userEntity.getUserName() + "已退出",
				Globals.LOG_TYPE_EXIT.toString(),
				Globals.LOG_LEAVEL_INFO.toString());
		session.removeAttribute(Globals.USER_SESSION);
		session.invalidate();

		return ajaxJson;
	}

	/**
	 * @Title: getOrgDepartTreeByorgId
	 * @Description: [机构部门树]
	 * @param @param orgId
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson getOrgDepartTreeByorgId(String orgId) {
		AjaxJson j = new AjaxJson();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		if (StringUtil.isNotEmpty(orgId)) {
			OrganizationEntity organization = this.expandEntity(
					OrganizationEntity.class, orgId);
			if (organization != null) {
				//组装根节点
				boolean isparent = false;
				//判断机构下面是否有人员或者部门
				List<DepartmentEntity> departMentEntityList = this
						.queryListByProperty(DepartmentEntity.class,
								"organizationEntity.id", organization.getId());
				if (departMentEntityList.size() > 0) {
					isparent = true;
				}
				//组装机构树
				appendOrgNode(nodes, organization.getId(),
						organization.getName(), isparent,"");
				if(departMentEntityList.size()>0){
					//组装部门以及用户
					appendDepart(nodes, departMentEntityList,
							organization.getId());
				}
			}
		}
		j.setObj(nodes);
		return j;
	}
	

	/**
	 * 组装部门
	 * 
	 * @Title: appendDepartOrUser
	 * @Description: [功能描述]
	 * @param @param nodes
	 * @param @param departMentEntityList
	 * @param @param roleUserIds
	 * @param @param parentId 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private void appendDepart(List<TreeNode> nodes,
			List<DepartmentEntity> departMentEntityList,String parentId) {
		//组装部门
		for (DepartmentEntity depart : departMentEntityList) {
			if (depart.getParentDepartmentEntity()==null) {
				appendOrgNode(nodes, depart.getId(), depart.getName(),true,parentId);
			}else{
				appendOrgNode(nodes, depart.getId(), depart.getName(),false,depart.getParentDepartmentEntity().getId());
			}
//			//循环遍历部门以及子部门or人员
//			if (depart.getChildDepartmentEntity().size() > 0) {
//				appendDepart(nodes, depart.getChildDepartmentEntity(),
//						depart.getId());
//			}
		}
	}

	/**
	 * 
	* @Title: appendOrgNode 
	* @Description: [组装机构树]
	* @param @param nodes
	* @param @param id
	* @param @param name
	* @param @param isparent 是否显示下级
	* @param @param parentId  父id
	* @return void 返回类型 
	* @throws JumpException 异常类型
	 */
	private void appendOrgNode(List<TreeNode> nodes, String id, String name,
			boolean isparent, String parentId) {
		TreeNode node = new TreeNode();
		node.setId(id);
		node.setName(name);
		node.setParentId(parentId);
		node.setOpen(isparent);
		nodes.add(node);
	}

	/**
	 * @Title: getDepartIdsByUser
	 * @Description: [得到用户所属的部门]
	 * @param @param userId
	 * @param @return 参数说明
	 * @return List<String> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public List<String> getDepartIdsByUser(String userId) {
		List<String> departIds = new ArrayList<String>();
		if (StringUtil.isNotBlank(userId)) {
			String userHql = "from UserDepartmentRelationEntity where userEntity.id=?";
			List<UserDepartmentRelationEntity> users = this.queryListByHql(
					userHql, userId);
			for(UserDepartmentRelationEntity userDepartmentRelationEntity:users){
				DepartmentEntity depatEntity=userDepartmentRelationEntity.getDepartmentEntity();
				departIds.add(depatEntity.getId());
			}
		}
		return departIds;
	}
	
	/**
	 * 
	 * @Title: sendUpPwdMail
	 * @Description: [给修改密码的用户发送邮件]
	 * @param userName
	 * @return AjaxJson
	 * @throws JumpException 
	 * @see com.zjy.jopm.base.user.service.ext.UserExtService#sendUpPwdMail(java.lang.String)
	 */
	@Override
	public AjaxJson sendUpPwdMail(String userName, HttpServletRequest request) throws JumpException {
		AjaxJson json = new AjaxJson();
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		UserEntity user = this.expandEntityByProperty(UserEntity.class, "userName", userName);
		if(user!=null){
			EmailLoseEntity lose = new EmailLoseEntity();
			lose.setCreateTime(new Date());
			emailLoseService.insertEntity(lose);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", path+"webpage/base/user/updateForgetPassWord.html?user="+user.getId()+"&emailId="+lose.getId());
			String subject = "修改密码链接地址";
			boolean flag = SendMailUtil.sendUpdPwdUrlMail(user.getEmail(), subject, map);
			if(flag){
				json.setSuccess(true);
				json.setMessage("发送邮件成功，请登录邮箱验证");
				return json;
			}
			emailLoseService.deleteEntity(lose);
			json.setSuccess(false);
			json.setMessage("发送邮件失败，请重新发送");
			return json;
		}
		json.setSuccess(false);
		json.setMessage("该用户不存在，请重新填写");
		return json;
	}

}
