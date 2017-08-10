/** 
 * @Description:[用户接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.user.service.impl.UserServiceImpl.java
 * @ClassName:UserServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:30:38
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:30:38  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.user.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.JumpBeanUtil;
import com.unistc.utils.PasswordUtil;
import com.unistc.utils.ResourceUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SendMailUtil;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.log.entity.LogEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.quiUtil.QuiUtils;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.user.entity.UserDepartmentRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.UserService;

/**
 * @ClassName: UserServiceImpl
 * @Description: [用户接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:30:38
 * @since JDK 1.6
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceimpl implements UserService {

	@Autowired
	private LogExtService logExtService;

	/**
	 * 用户列表
	 * 
	 * @Title: getUserListQuiGrid pager.pageNo：当前页 pager.pageSize: 每页多少条记录
	 *         sort：传递当前排序的字段名 direction：传递当前的排序方向 orgId:机构或者部门id
	 * @return Map<String,Object> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public Map<String, Object> getUserListQuiGrid(UserEntity user,
			String pageNo, String pageSize, String sort, String direction,
			String isInclude, String orgId) {
		//用户列表只能看到普通用户
		String hql = "FROM UserEntity WHERE delFlag='0' and type='0'";
		int pNO = Integer.valueOf(pageNo);
		int pSize = Integer.valueOf(pageSize);
		Map<String, Object> param = new HashMap<String, Object>();
		//		List<Object> param = new ArrayList<Object>();
		if (StringUtil.isNotEmpty(user.getUserName())) {
			hql += " and userName LIKE :userName";
			param.put("userName", "%" + user.getUserName() + "%");
		}
		if (StringUtil.isNotEmpty(user.getRealName())) {
			hql += " AND realName LIKE :realName";
			param.put("realName", "%" + user.getRealName() + "%");
		}
		if (StringUtil.isNotEmpty(user.getStatus())) {//状态  0 停用    1 启动 
			hql += " AND status= :status";
			param.put("status", user.getStatus());
		}
		//判断orgId是机构还是部门
		OrganizationEntity organization = this.expandEntity(
				OrganizationEntity.class, orgId);
		List<String> userIds = new ArrayList<String>();
		if (organization != null && StringUtil.isNotEmpty(organization.getId())) {//机构
			//是否包含下级人员 0 不包含 1 包含
			String departHql = "from UserDepartmentRelationEntity where departmentEntity.id in("
					+ " select t.id from DepartmentEntity t where t.organizationEntity.id =?)";
			List<Object> departParam = new ArrayList<Object>();
			departParam.add(organization.getId());
			List<UserDepartmentRelationEntity> userRelation = this
					.queryListByHql(departHql, departParam);
			userIds = getUserIds(userRelation);
			if (isInclude.equals("1")) {
				//得到部门id 去中间表中查找user 获取userId 
				hql += " AND organizationEntity.id= :orgId";
				param.put("orgId", organization.getId());
			} else {
				hql += " AND organizationEntity.id= :orgId and id not in(:userIds)";
				param.put("orgId", organization.getId());
				param.put("userIds", userIds.size() > 0 ? userIds : "");
			}
		} else {
			DepartmentEntity depart = this.expandEntity(DepartmentEntity.class,
					orgId);//部门
			if (depart != null && StringUtil.isNotEmpty(depart.getId())) {
				if (isInclude.equals("1")) {
					//得到部门id 去中间表中查找user 获取userId 
					List<String> departIds = new ArrayList<String>();
					List<DepartmentEntity> departLists = this
							.queryListByProperty(DepartmentEntity.class, "id",
									depart.getId());
					departIds = getAllChildDepartIds(departIds, departLists);
					Map<String, Object> deparam = new HashMap<String, Object>();
					deparam.put("departIds", departIds.size() > 0 ? departIds
							: "");
					String departHql = "from UserDepartmentRelationEntity where departmentEntity.id in(:departIds)";
					List<UserDepartmentRelationEntity> userRelation = this
							.queryListByHql(departHql, deparam);
					userIds = getUserIds(userRelation);
					hql += " AND id in(:userIds)";
					param.put("userIds", userIds.size() > 0 ? userIds : "");
				} else {
					String departHql = "from UserDepartmentRelationEntity where departmentEntity.id=?";
					List<UserDepartmentRelationEntity> userRelation = this
							.queryListByHql(departHql, orgId);
					userIds = getUserIds(userRelation);
					hql += " AND id in(:userIds)";
					param.put("userIds", userIds.size() > 0 ? userIds : "");
				}
			} else {
				//说明 orgId 有值 但在数据找不到数据的情况下 返回空数据
				Map<String, Object> results = new HashMap<String, Object>();
				results.put("pager.pageNo", 1);
				results.put("pager.totalRows", 0);
				List<Map<String, Object>> listRows = new ArrayList<Map<String, Object>>();
				results.put("rows", listRows);
				return results;
			}
		}

		if (StringUtil.isNotEmpty(sort)) {
			hql += " ORDER BY :sort ";
			param.put("sort", sort);
			if (Constants.DESC.equals(direction)) {
				hql += SortDirection.desc;
			} else if (Constants.ASC.equals(direction)) {
				hql += SortDirection.asc;
			} else {
				hql += SortDirection.desc;
			}
		}
		QueryCondition queryCondition = new QueryCondition(hql, param, pNO,
				pSize);
		PageList pageList = this.queryListByHqlWithPage(queryCondition);// 结果集
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	private List<String> getAllChildDepartIds(List<String> departList,
			List<DepartmentEntity> departLists) {
		for (DepartmentEntity depart : departLists) {
			departList.add(depart.getId());
			if (depart.getChildDepartmentEntity().size() > 0) {
				getAllChildDepartIds(departList,
						depart.getChildDepartmentEntity());
			}
		}
		return departList;
	}

	/**
	 * 组装userIds
	 * 
	 * @Title: getUserIds
	 * @Description: [功能描述]
	 * @param @param userIds
	 * @param @param userRelation
	 * @param @return 参数说明
	 * @return String 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private List<String> getUserIds(
			List<UserDepartmentRelationEntity> userRelation) {
		List<String> userIds = new ArrayList<String>();
		for (UserDepartmentRelationEntity us : userRelation) {
			String id = us.getUserEntity().getId();
			userIds.add(id);
		}
		return userIds;
	}

	/**
	 * 保存 编辑user
	 * 
	 * @Title: saveOrUpdateUser
	 * @Description: [功能描述]
	 * @param @param selectId
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson saveOrUpdateUser(UserEntity user, String selectId,
			String orgId) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(selectId)) {
			j.setSuccess(false);
			j.setMessage("请选择机构或者部门进行用户的操作");
			return j;
		}
		//选择的是机构节点
		OrganizationEntity org = this.expandEntity(OrganizationEntity.class,
				orgId);
		user.setOrganizationEntity(org);
		//用户名不允许为空
		if (StringUtil.isEmpty(user.getUserName())) {
			j.setSuccess(false);
			j.setMessage("用户名不允许为空！");
			return j;
		}
		//判断用户名是否存在
		//		List<UserEntity> userList=this.queryListByProperty(UserEntity.class, "userName", user.getUserName());
		String userHql = "from UserEntity WHERE delFlag='0' and userName=?";
		List<UserEntity> userList = this.queryListByHql(userHql,
				user.getUserName());
		boolean flag = false;
		user.setDelFlag("0");//1：删除;0:未删除
		user.setStatus("1");//状态  0 停用    1 启动 
		user.setCreatetime(new Date());
		String logContent = "";
		if (StringUtil.isNotEmpty(user.getId())) {
			UserEntity us = this.expandEntity(UserEntity.class, user.getId());
			user.setPassword(us.getPassword());
			if (!us.getUserName().equals(user.getUserName())) {
				if (userList.size() > 0) {
					j.setMessage("用户名不能重复");
					j.setSuccess(false);
					return j;
				}
			}
			JumpBeanUtil.copyBeanNotNull2Bean(user, us);
			flag = this.updateEntity(us);
			logContent = "更新用户：[" + user.getUserName() + "]," + j.getMessage();
		} else {
			//密码不允许为空
			if (StringUtil.isEmpty(user.getPassword())) {
				j.setSuccess(false);
				j.setMessage("密码不允许为空！");
				return j;
			}
//			String pString = PasswordUtil.encrypt(user.getUserName(),
//					user.getPassword(), PasswordUtil.getStaticSalt());
			String pString = user.getPassword().intern();
			if (!user.getPassword().equals(pString)) {//需要修改
				user.setPassword(pString);
			}
			if (userList.size() > 0) {
				j.setMessage("用户名不能重复");
				j.setSuccess(false);
				return j;
			}
			flag = this.insertEntity(user);
			logContent = "保存用户：[" + user.getUserName() + "]," + j.getMessage();
		}
		//记录日志
		this.logExtService.insertLog(logContent,
				Globals.LOG_LEAVEL_INFO.toString(),
				Globals.LOG_TYPE_INSERT.toString());

		String[] ids = selectId.split(",");
		List<UserDepartmentRelationEntity> userDepartRelation = this
				.queryListByProperty(UserDepartmentRelationEntity.class,
						"userEntity.id", user.getId());
		for (UserDepartmentRelationEntity ud : userDepartRelation) {
			//将关联表中的数据 删除
			flag = this.deleteEntity(ud);
		}
		for (String departId : ids) {
			//将勾选的部门 保存到关联表中
			if (!departId.equals(orgId)) {
				DepartmentEntity depart = this.expandEntity(
						DepartmentEntity.class, departId);
				UserDepartmentRelationEntity userDepart = new UserDepartmentRelationEntity();
				userDepart.setDepartmentEntity(depart);
				userDepart.setUserEntity(user);
				flag = this.saveEntity(userDepart);
			}
		}
		if (!flag) {
			j.setSuccess(false);
			j.setMessage("操作失败");
		}
		return j;
	}

	/**
	 * @Title: deleteUser
	 * @Description: [删除用户id]
	 * @param @param ids
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson deleteUser(String ids) {
		AjaxJson j = new AjaxJson();
		String message = "";
		//获取当前登录用户
		SessionInfo sessionInfo = (SessionInfo) ContextHolderUtil.getSession()
				.getAttribute(Globals.USER_SESSION);
		UserEntity loginUser = sessionInfo.getUser();
		String[] userIds = ids.split(",");
		for (String id : userIds) {
			UserEntity user = this.expandEntity(UserEntity.class, id);
			if (user != null) {
				if (loginUser != null
						&& StringUtil.isNotEmpty(loginUser.getId())) {
					if (loginUser.getId().equals(user.getId())) {
						message = "用户[" + loginUser.getUserName()
								+ "]为当前登录用户，不允许删除";
						continue;
					}
				}
				//判断 角色用户表 是否存在
				boolean flag = false;
				List<RoleUserRelationEntity> roleUserRelationEntity = this
						.queryListByProperty(RoleUserRelationEntity.class,
								"userEntity.id", id);
				if (roleUserRelationEntity.size() > 0) {
					//					flag=true;
					message = "用户[" + user.getUserName() + "]与角色绑定关系，不允许删除";
					continue;
				}
				//判断 日志是否存在
				List<LogEntity> logEntity = this.queryListByProperty(
						LogEntity.class, "userEntity.id", id);
				if (logEntity.size() > 0) {
					flag = true;
				}

				List<UserDepartmentRelationEntity> departUserList = this
						.queryListByProperty(
								UserDepartmentRelationEntity.class,
								"userEntity.id", id);
				if (departUserList.size() > 0) {
					this.deleteEntityBatch(departUserList);
				}

				//当 角色 日志 部门管理 有数据的时候 逻辑删除
				String logContent = "删除用户：[" + user.getUserName() + "]"
						+ j.getMessage();
				if (flag) {
					user.setDelFlag("1");
					user.setUserName(user.getId() + "_" + user.getUserName());
					this.updateEntity(user);
					return j;
				}
				//当角色 日志 部门管理 没有数据的时候 物理删除
				this.deleteEntity(user);
				//记录日志
				this.logExtService.insertLog(logContent,
						Globals.LOG_LEAVEL_INFO.toString(),
						Globals.LOG_TYPE_DEL.toString());

			}
		}
		if (StringUtil.isNotBlank(message)) {
			j.setMessage(message);
		}
		return j;
	}

	/**
	 * @Title: updateStatus
	 * @Description: [启用 停用]
	 * @param @param ids
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson updateStatus(String ids) {
		//获取当前登录用户
		SessionInfo sessionInfo = (SessionInfo) ContextHolderUtil.getSession()
				.getAttribute(Globals.USER_SESSION);
		UserEntity loginUser = sessionInfo.getUser();
		String message = "";
		AjaxJson j = new AjaxJson();
		String[] userIds = ids.split(",");
		for (String id : userIds) {
			//状态  0 停用    1 启动 
			UserEntity user = this.expandEntity(UserEntity.class, id);
			if (user != null) {
				if (loginUser != null
						&& StringUtil.isNotEmpty(loginUser.getId())) {
					if (loginUser.getId().equals(user.getId())) {
						message = "用户[" + loginUser.getUserName()
								+ "]为当前登录用户，不允许修改";
						continue;
					}
				}
				if (user.getStatus().equals("0")) {
					user.setStatus("1");
				} else {
					user.setStatus("0");
				}
				this.updateEntity(user);
			}
		}
		if (StringUtil.isNotBlank(message)) {
			j.setMessage(message);
		}
		return j;
	}

	/**
	 * @Title: resetPassWrod
	 * @Description: [重置密码]
	 * @param @param ids
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson resetPassWrod(String ids) {
		AjaxJson j = new AjaxJson();
		String initPassword = ResourceUtil.getConfigByName("initPassword");//初始化密码
		if (StringUtil.isEmpty(initPassword)) {
			j.setSuccess(false);
			j.setMessage("请配置初始化密码！");
			return j;
		}
		//获取当前登录用户
		SessionInfo sessionInfo = (SessionInfo) ContextHolderUtil.getSession()
				.getAttribute(Globals.USER_SESSION);
		UserEntity loginUser = sessionInfo.getUser();
		String[] userIds = ids.split(",");
		String message = "";
		for (String id : userIds) {
			UserEntity user = this.expandEntity(UserEntity.class, id);
			if (user != null) {
				if (loginUser != null
						&& StringUtil.isNotEmpty(loginUser.getId())) {
					if (loginUser.getId().equals(user.getId())) {
						message = "用户[" + loginUser.getUserName()
								+ "]为当前登录用户，不允许修改";
						continue;
					}
				}
				String pString = PasswordUtil.encrypt(user.getUserName(),
						initPassword, PasswordUtil.getStaticSalt());
				user.setPassword(pString);
				this.updateEntity(user);
			}
		}
		if (StringUtil.isNotBlank(message)) {
			j.setMessage(message);
		}
		return j;
	}

	/**
	 * @Title: updatePasswWord
	 * @Description: [修改密码]
	 * @param @param id
	 * @param @param passwordNew
	 * @param @param passwordOld
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson updatePasswWord(String id, String passwordNew,
			String passwordOld) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean flag = false;
		if (StringUtil.isNotBlank(id)) {
			UserEntity user = this.expandEntity(UserEntity.class, id);
			if (user != null) {
				//获取当前登录用户
				SessionInfo sessionInfo = (SessionInfo) ContextHolderUtil
						.getSession().getAttribute(Globals.USER_SESSION);
				if (sessionInfo != null) {
					UserEntity loginUser = sessionInfo.getUser();
					if (loginUser != null
							&& StringUtil.isNotEmpty(loginUser.getId())) {
						if (loginUser.getId().equals(user.getId())) {
							ajaxJson.setSuccess(false);
							ajaxJson.setMessage("该用户为登录用户，不允许修改！");
							return ajaxJson;
						}
					}
					if (StringUtil.isNotEmpty(passwordOld)) {
//						String pString = PasswordUtil.encrypt(
//								user.getUserName(), passwordOld,
//								PasswordUtil.getStaticSalt());
						String pString = passwordOld.intern();
						if (pString.equals(user.getPassword())) {
							if (StringUtil.isNotEmpty(passwordNew)) {
//								pString = PasswordUtil.encrypt(
//										user.getUserName(), passwordNew,
//										PasswordUtil.getStaticSalt());
								pString = passwordNew.intern();
								user.setPassword(pString);
								flag = this.updateEntity(user);
								if (!flag) {
									ajaxJson.setSuccess(false);
									ajaxJson.setMessage("操作失败");
									return ajaxJson;
								}
							} else {
								ajaxJson.setSuccess(false);
								ajaxJson.setMessage("请输入新密码！");
							}
						} else {
							ajaxJson.setSuccess(false);
							ajaxJson.setMessage("旧密码输入错误！请认真填写!");
						}
					} else {
						ajaxJson.setSuccess(false);
						ajaxJson.setMessage("请输入旧密码！");
					}
				} else {
					if (StringUtil.isNotEmpty(passwordNew)) {
//						String pString = PasswordUtil.encrypt(
//								user.getUserName(), passwordNew,
//								PasswordUtil.getStaticSalt());
						String pString = passwordNew.intern();
						user.setPassword(pString);
						flag = this.updateEntity(user);
						if (!flag) {
							ajaxJson.setSuccess(false);
							ajaxJson.setMessage("操作失败");
							return ajaxJson;
						}
						ajaxJson.setSuccess(true);
						ajaxJson.setMessage("操作成功");
						return ajaxJson;
					} else {
						ajaxJson.setSuccess(false);
						ajaxJson.setMessage("请输入旧密码！");
					}
				}

			} else {
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("没有找到相关联的用户！可以该用户已经删除！");
			}
		} else {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("没有找到相关联的用户！可以该用户已经删除！");
		}
		return ajaxJson;
	}

	/**
	 * @Title: saveOrUpdateUserForEamil
	 * @Description: [保存用户并发送邮件]
	 * @param @param user
	 * @param @param selectId
	 * @param @param orgId
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson saveOrUpdateUserForEamil(UserEntity user, String selectId,
			String orgId){

		AjaxJson j = new AjaxJson();
		if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(selectId)) {
			j.setSuccess(false);
			j.setMessage("请选择机构或者部门进行用户的操作");
			return j;
		}
		//选择的是机构节点
		OrganizationEntity org = this.expandEntity(OrganizationEntity.class,
				orgId);
		user.setOrganizationEntity(org);
		//用户名不允许为空
		if (StringUtil.isEmpty(user.getUserName())) {
			j.setSuccess(false);
			j.setMessage("用户名不允许为空！");
			return j;
		}
		if (StringUtil.isEmpty(user.getEmail())) {
			j.setSuccess(false);
			j.setMessage("邮箱不允许为空！");
			return j;
		}
		//判断用户名是否存在
		//		List<UserEntity> userList=this.queryListByProperty(UserEntity.class, "userName", user.getUserName());
		String userHql = "from UserEntity WHERE delFlag='0' and userName=?";
		List<UserEntity> userList = this.queryListByHql(userHql,
				user.getUserName());
		boolean flag = false;
		user.setDelFlag("0");//1：删除;0:未删除
		user.setStatus("1");//状态  0 停用    1 启动 
		user.setCreatetime(new Date());
		String logContent = "";

		if (StringUtil.isNotEmpty(user.getId())) {
			UserEntity us = this.expandEntity(UserEntity.class, user.getId());
			user.setPassword(us.getPassword());
			if (!us.getUserName().equals(user.getUserName())) {
				if (userList.size() > 0) {
					j.setMessage("用户名不能重复");
					j.setSuccess(false);
					return j;
				}
			}

			String emailHql = "from UserEntity WHERE   email=?";
			List<UserEntity> emailList = this.queryListByHql(emailHql,
					user.getEmail());
			if (!us.getEmail().equals(user.getEmail())) {
				if (emailList.size() > 0) {
					j.setMessage("邮箱不能重复！");
					j.setSuccess(false);
					return j;
				}
			}

			JumpBeanUtil.copyBeanNotNull2Bean(user, us);
			flag = this.updateEntity(us);
			logContent = "更新用户：[" + user.getUserName() + "]," + j.getMessage();
		} else {
			String password = getPassword(user.getUserName());
			String pString = PasswordUtil.encrypt(user.getUserName(), password,
					PasswordUtil.getStaticSalt());
			user.setPassword(pString);

			String emailHql = "from UserEntity WHERE   email=?";
			List<UserEntity> emailList = this.queryListByHql(emailHql,
					user.getEmail());
			if (emailList.size() > 0) {
				j.setMessage("邮箱不能重复！");
				j.setSuccess(false);
				return j;
			}

			if (userList.size() > 0) {
				j.setMessage("用户名不能重复");
				j.setSuccess(false);
				return j;
			}
			flag = this.insertEntity(user);
			if (flag) {
				//发送邮件
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("username", user.getUserName());
				map.put("pwd", password);
				String subject = "办公平台账号信息";
				SendMailUtil.sendSystemUserInfoMail(user.getEmail(), subject,
						map);
			}
			logContent = "保存用户：[" + user.getUserName() + "]," + j.getMessage();
		}
		//记录日志
		this.logExtService.insertLog(logContent,
				Globals.LOG_LEAVEL_INFO.toString(),
				Globals.LOG_TYPE_INSERT.toString());

		String[] ids = selectId.split(",");
		List<UserDepartmentRelationEntity> userDepartRelation = this
				.queryListByProperty(UserDepartmentRelationEntity.class,
						"userEntity.id", user.getId());
		for (UserDepartmentRelationEntity ud : userDepartRelation) {
			//将关联表中的数据 删除
			flag = this.deleteEntity(ud);
		}
		for (String departId : ids) {
			//将勾选的部门 保存到关联表中
			if (!departId.equals(orgId)) {
				DepartmentEntity depart = this.expandEntity(
						DepartmentEntity.class, departId);
				UserDepartmentRelationEntity userDepart = new UserDepartmentRelationEntity();
				userDepart.setDepartmentEntity(depart);
				userDepart.setUserEntity(user);
				flag = this.saveEntity(userDepart);
			}
		}
		if (!flag) {
			j.setSuccess(false);
			j.setMessage("操作失败");
		}  
		return j;

	}

	/**
	 * @Title: getPassword
	 * @Description: [获取随机密码]
	 * @param @param args
	 * @param @return 参数说明
	 * @return String 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private String getPassword(String userName) {
		String in=new String(userName);
		List<String> list=Arrays.asList(in.split(""));
		Collections.shuffle(list);
		String out=new String();
		for(String s:list){
			out+=s;
		}
		return out;
	}

}
