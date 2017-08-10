package com.zjy.jopm.jportal.webos.service;


import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.unistc.core.common.service.BaseService;
import com.zjy.jopm.base.user.entity.UserEntity;

public interface WebosService extends BaseService {

	/**
	 * @TODO 获取所有菜单
	 * @param menuLevel
	 *            菜单级别
	 * @param userId
	 *            用户id
	 * @param systemId
	 *            系统id
	 * @return 返回
	 */
	public String getMenu(String menuLevel, String userId, String systemId,
			String parentMenuId, String mf, String departId);

	/**
	 * @author HeJiaLiang
	 * @TODO 查询用户是否为管理员 2014-12-26
	 * @param userId
	 * @return 用户角色list
	 */
	public String checkUserIsAdmin(String userId);

	/**
	 * @author HeJiaLiang
	 * @TODO 查询用户拥有权限菜单的id 2014-12-26
	 * @param userId
	 * @param systemId
	 *            系统id
	 * @return
	 */
	public String queryUserMenuIds(String userId, String systemId);

	/**
	 * @author HeJiaLiang
	 * @TODO 根据用户id获取用户信息 2015-1-13
	 * @param userId
	 * @return
	 */
	public String getUserInfo(String userId);

	/**
	 * @todo 通过用户名和机构代码查询用户
	 * @param username
	 * @param departcode
	 * @return
	 */
	public UserEntity getUserInfo(String username, String departid,
			String password);

	/**
	 * @TODO 获取全部菜单 jopm+数校
	 * @param request
	 * @return
	 */
	public Object getAllMenu(HttpServletRequest request, String userId,
			String systemId, String departId, String deskfunctionIds);

	/**
	 * 检查数校有权限的功能
	 * <p>
	 * Title: getSxAllMenu
	 * </p>
	 * <p>
	 * @author:hejialiang
	 * </p>
	 * <p>
	 * @date:2015年7月20日下午4:05:45
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param systemId
	 * @param mf
	 * @param departId
	 * @param haveFunctinIds
	 *            已经拥有的权限
	 * @param functionIds
	 *            从jprotal传过来的功能id，判断该功能是否有权限
	 * @return
	 */
	public String getSxAllMenu(String systemId, String mf, String departId,
			String haveFunctinIds, String functionIds);

	/**
	 * 获取公共菜单
	 * @param request
	 * @param userId
	 * @param systemId
	 * @param mf
	 * @param departId
	 * @return
	 */
	public Object getBaseMenu(HttpServletRequest request, String userId,
			String systemId, String mf, String departId);

	/**
	 * 检查左侧菜单是否有权限
	 * @param request
	 * @param userId
	 * @param departId
	 * @param systemId
	 * @param mf
	 * @param functionIds
	 * @return
	 */
	public Object checkLeftMenuIsAuthority(HttpServletRequest request,
			String userId, String departId, String systemId, String mf,
			String functionIds);

	/**
	 * 检查桌面菜单是否有权限
	 * @param request
	 * @param userId
	 * @param departId
	 * @param systemId
	 * @param mf
	 * @param functionIds
	 * @return
	 */
	public Object checkDeskMenuIsAuthority(HttpServletRequest request,
			String userId, String departId, String systemId, String mf,
			String functionIds);

	/**
	 * 搜索条目
	 * @param request
	 * @param userId
	 * @param departId
	 * @param systemId
	 * @param mf
	 * @param functionIds
	 * @return
	 */
	public Object searchClause(HttpServletRequest request, String userId,
			String departId, String systemId, String mf, String functionIds);

	/**
	 * 商城下载应用
	 * @param request
	 * @param orgCode
	 * @param appName
	 * @param appCode
	 * @param demoService
	 * @return
	 */
	public Object downloadApp(JSONObject requestBody);

}
