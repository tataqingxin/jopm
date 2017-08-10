	package com.zjy.jopm.jportal.webos.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.HTTPUtil;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.service.ApplicationService;
import com.zjy.jopm.base.app.service.FunctionService;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.jportal.webos.service.WebosService;
import com.zjy.jopm.sdk.util.CasSessionUtil;

@Service("webosService")
@Transactional
public class WebosServiceImpl extends BaseServiceimpl implements WebosService {

	private static String projectAdd;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private FunctionService functionService;

	@Override
	public String getMenu(String menuLevel, String userId, String systemId,
			String parentMenuId, String mf, String departId) {
//		String roleUserhql = "from RoleUserRelationEntity where userEntity.id = ?";
		
//		List<RoleUserRelationEntity> userRoleList = this.queryListByHql(roleUserhql, "ff80808157f9e5d30157f9f526f20033");
//		
//		//获取角色ID的集合
//		List<String> userRoleIdList = new ArrayList<String>();;
//		if(null!=userRoleList){
//			for (RoleUserRelationEntity userRole : userRoleList) {
//				userRoleIdList.add(userRole.getRoleEntity().getId());
//			}
//		}
//		
//		List<FunctionEntity> funcList = new ArrayList<FunctionEntity>();
//		String hql = "from RoleFunctionRelationEntity where functionEntity.applicationEntity.id=:appId and functionEntity.parentFunctionEntity is null "
//				+ "and roleEntity.id in (:roleIds)";
//		Map<String,Object> param = new HashMap<String,Object>();
//		param.put("appId","ff80808157f9e5d30157f9f527340052");
//		param.put("roleIds", userRoleIdList);
//		funcList = this.queryListByHql(hql, param);
//		System.out.println(funcList);
		return null;
	}
	
	@SuppressWarnings("null")
	@Override
	public String checkUserIsAdmin(String userId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray ja = new JSONArray();
		String hql = "from RoleUserRelationEntity t1 where t1.userEntity.id=? and t1.roleEntity.id=(select u.id from RoleEntity u where "
				+ " u.code=?)";
		list = this.queryListByHql(hql, userId, "admin");
		if (null != list || list.size() > 0) {
			ja = JSONArray.fromObject(list);
		}
		return ja.toString();
	}

	@Override
	public String queryUserMenuIds(String userId, String systemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserInfo(String userId) {
		Map<String, String> map = new HashMap<String, String>();
		UserEntity user = new UserEntity();
		user = this.expandEntity(UserEntity.class, userId);
		if (null != user) {
			map.put("realName", user.getRealName());
			map.put("departCode", user.getOrganizationEntity().getCode());
			map.put("id", user.getId());
		}
		JSONArray ja = JSONArray.fromObject(map);
		return ja.toString();
	}

	@Override
	public UserEntity getUserInfo(String username, String departid,
			String password) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取所有桌面应用（有权限的）。开始菜单和桌面管理公用一个
	 * 
	 * @param userId
	 * @param systemId
	 * @param mf
	 * @param departId
	 * @param functionIds
	 * @return
	 */
	@Override
	public Object getAllMenu(HttpServletRequest request, String userId,
			String systemId, String departId, String deskfunctionIds) {
		projectAdd = getPath(request);
		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		List<ApplicationEntity> appList = new ArrayList<ApplicationEntity>();
		//如果是没有机构的用户
//		String hqll = "SELECT DISTINCT type FROM UserEntity WHERE id = ?";
//		List<String> type = super.queryListByHql(hqll, userId);
		
		String hql2 = "from UserDepartmentRelationEntity where userEntity.id = ?";
		List<Object> userDepartmentRelationList = super.queryListByHql(hql2, userId);
		if(userDepartmentRelationList != null && userDepartmentRelationList.size() != 0){			
			String hql = "from ApplicationEntity where organizationEntity.id=? order by id";
			List<Object> param = new ArrayList<Object>();
			param.add(departId);
			appList = this.queryListByHql(hql, param);
		}else{  //没有部门查出全部
			appList = super.queryListByClass(ApplicationEntity.class);
		}
		
//		if("1".equals(type.get(0))){
//			//2、根据机构、用户获取角色
//			String hql = "SELECT DISTINCT id FROM RoleEntity WHERE id IN (SELECT roleEntity.id FROM RoleUserRelationEntity WHERE userEntity.id = ?)";
//			List<String> roleIds = super.queryListByHql(hql, new Object[]{userId});
//			//3、根据角色获取功能
//			Map<String, Object> _param = null;
//			_param = new HashMap<String, Object>();
//			_param.put("ids", roleIds.size()>0?roleIds:"");
//			hql = "SELECT DISTINCT functionEntity.id FROM RoleFunctionRelationEntity WHERE roleEntity.id IN (:ids)";
//			List<String> functionIds = super.queryListByHql(hql, _param);
//		
//			_param = new HashMap<String, Object>();
//			_param.put("ids", functionIds.size()>0?functionIds:"");
//			hql = "SELECT DISTINCT applicationEntity.id FROM FunctionEntity WHERE id IN (:ids)";
//			List<String> applicationIds = super.queryListByHql(hql, _param);
//			//5、某用户具有权限的应用列表
//			_param = new HashMap<String, Object>();
//			_param.put("ids", applicationIds.size()>0?applicationIds:"");
//			hql = "FROM ApplicationEntity WHERE id IN (:ids) AND status = '1' ";
//			appList = super.queryListByHql(hql, _param);
//		}else{
//			String hql = "from ApplicationEntity where organizationEntity.id=? order by id";
//			List<Object> param = new ArrayList<Object>();
//			param.add(departId);
//			appList = this.queryListByHql(hql, param);
//		}

		
		for(int i = 0,j = appList.size();i<j;i++){
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", appList.get(i).getId());
			node.put("menuName", appList.get(i).getName());
			node.put("appIcon", projectAdd+appList.get(i).getIconEntity().getIconPath());
			List<Map<String, Object>> childFunction = new ArrayList<Map<String, Object>>();
			childFunction = getAppChildren(appList.get(i).getId(),deskfunctionIds,userId,appList.get(i).getUrl(),request);
			if(null!=childFunction&&childFunction.size()>0){
				node.put("children", getAppChildren(appList.get(i).getId(),deskfunctionIds,userId,appList.get(i).getUrl(),request));
				childs.add(node);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", childs);
		return result;
	}
	
	/**
	 * 获取应用的子功能
	 * @param appID
	 * @return
	 */
	private List<Map<String, Object>> getAppChildren(String appID,String deskfunctionIds,String userId,String appUrl,HttpServletRequest request){
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();	
		String roleUserhql = "select roleEntity.id from RoleUserRelationEntity where userEntity.id = ?";
		List<String> userRoleIdList = super.queryListByHql(roleUserhql, userId);
		//查找系统角色id
		String roleSyshql = "select roleEntity.id from RoleUserRelationEntity where userEntity.id = ? and roleEntity.type = '1'";
		List<String> userRoleSysIdList = super.queryListByHql(roleSyshql, userId);
		List<FunctionEntity> functionEntityListByTag = null;
		if(userRoleSysIdList != null && userRoleSysIdList.size() != 0){			
			String sysFunListHql = "SELECT functionEntity FROM TagFunctionRelationEntity t WHERE t.tagEntity.id IN (SELECT DISTINCT tagEntity.id "
					+ "FROM TagRoleRelationEntity WHERE roleEntity.id IN (:userRoleSysIds)) and t.functionEntity.applicationEntity.id = (:appId)";
			Map<String,Object> idSysParam = new HashMap<String,Object>();
			idSysParam.put("userRoleSysIds", userRoleSysIdList);
			idSysParam.put("appId", appID);
			functionEntityListByTag = super.queryListByHql(sysFunListHql, idSysParam);
		}
//		List<RoleUserRelationEntity> userRoleList = this.queryListByHql(roleUserhql, userId);
//		
//		//获取角色ID的集合
//		List<String> userRoleIdList = new ArrayList<String>();
//		if(null!=userRoleList){
//			for (RoleUserRelationEntity userRole : userRoleList) {
//				userRoleIdList.add(userRole.getRoleEntity().getId());
//			}
//		}
		List<RoleFunctionRelationEntity> authRolefuncList = new ArrayList<RoleFunctionRelationEntity>();
		String idHql = "from RoleFunctionRelationEntity where roleEntity.id in (:roleIds)";
		Map<String,Object> idParam = new HashMap<String,Object>();
		idParam.put("roleIds", userRoleIdList);
		authRolefuncList = this.queryListByHql(idHql, idParam);
		List<String> authorIds = new ArrayList<String>();
		for (RoleFunctionRelationEntity roleFunctionRelationEntity : authRolefuncList) {
			authorIds.add(roleFunctionRelationEntity.getFunctionEntity().getId());
		}
		List<RoleFunctionRelationEntity> rolefuncList = new ArrayList<RoleFunctionRelationEntity>();
		String hql = "from RoleFunctionRelationEntity where functionEntity.applicationEntity.id=:appId and functionEntity.parentFunctionEntity.id is null "
				+ "and roleEntity.id in (:roleIds) order by id  ";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("appId",appID);
		param.put("roleIds", userRoleIdList);
//		CasSessionUtil cas = new CasSessionUtil(request);
		String orgIdhql = "select organizationEntity.id from UserEntity where id = ?";
		String OrgId = super.expandEntityByHql(orgIdhql, userId);
		if(StringUtil.isNotEmpty(appID)){
			rolefuncList = this.queryListByHql(hql, param);
			List<RoleFunctionRelationEntity> roleEfffuncList = new ArrayList<RoleFunctionRelationEntity>();
			for (RoleFunctionRelationEntity roleFunctionRelationEntity : rolefuncList) {
				hql = "from AppMirrorEntity where organizationEntity.id = ? and "
						+ "applicationEntity.id = (select applicationEntity.id from FunctionEntity where id = ?)"
						+ "and isEffective = 'y'";
				AppMirrorEntity appEff = super.expandEntityByHql(hql, OrgId ,roleFunctionRelationEntity.getFunctionEntity().getId());
				if(appEff == null){
					roleEfffuncList.add(roleFunctionRelationEntity);
				}
			}
			if(roleEfffuncList != null){				
				rolefuncList.removeAll(roleEfffuncList);
			}
		}
		if(null!=rolefuncList&&rolefuncList.size()>0 || functionEntityListByTag != null && functionEntityListByTag.size()>0) {
			Set<FunctionEntity> funcList = new LinkedHashSet<FunctionEntity>();
			for(RoleFunctionRelationEntity roleFunction : rolefuncList){
				funcList.add(roleFunction.getFunctionEntity());
			}
			if(functionEntityListByTag != null && functionEntityListByTag.size() != 0){				
				funcList.addAll(functionEntityListByTag);
			}
			for(FunctionEntity function : funcList){
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", function.getId());
				node.put("menuName", function.getName());
				node.put("iconOpen", "libs/icons/tree_close.gif");
				node.put("iconClose", "libs/icons/tree_open.gif");
				if (StringUtil.isNotEmpty(deskfunctionIds) && deskfunctionIds.contains(function.getId())) {
					node.put("status", "added");
				}
				if(function.getChildFunctionEntity().size()>0){
					node.put("children", getChildFunction(function.getChildFunctionEntity(),deskfunctionIds,authorIds));
					node.put("type", "1");
					if(!checkHasChilds(function.getChildFunctionEntity())){
						node.put("enable", "no");
					}
				}else{
					node.put("type", "0");
					if(StringUtil.isNotEmpty(appUrl)){
						if(checkFunctionUrl(function.getUrl())){
							node.put("url",function.getUrl());
						}else{
							node.put("url",appUrl+function.getUrl());
						}
					}else{
						node.put("url",projectAdd+function.getUrl());
					}
					node.put("openType", "dialog");
				}
				node.put("appIcon",projectAdd+function.getIconEntity().getBigIconPath());
				node.put("icon", projectAdd+function.getIconEntity().getIconPath());
				mapList.add(node);		
			}
		}
		return mapList;
	}

	/**
	 * 获取功能下的子功能
	 * @param funcList
	 * @param deskfunctionIds
	 * @return
	 */
	private List<Map<String, Object>> getChildFunction(List<FunctionEntity> funcList,
			String deskfunctionIds,List<String> authorIds) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(FunctionEntity function : funcList){
			if(authorIds.contains(function.getId())){
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", function.getId());
				node.put("menuName", function.getName());
				node.put("iconOpen", "libs/icons/tree_close.gif");
				node.put("iconClose", "libs/icons/tree_open.gif");
				if (StringUtil.isNotEmpty(deskfunctionIds) && deskfunctionIds.contains(function.getId())) {
					node.put("status", "added");
				}
				if(null!=function.getChildFunctionEntity()&&function.getChildFunctionEntity().size()>0){
					//如果有子功能，添加
					node.put("children", getChildFunction(function.getChildFunctionEntity(),deskfunctionIds,authorIds));
					node.put("type", "1");
					if(!checkHasChilds(function.getChildFunctionEntity())){
						node.put("enable", "no");
					}
				}else{
					node.put("type", "0");
				}
				node.put("appIcon",projectAdd+function.getIconEntity().getIconPath());
				node.put("icon", projectAdd+function.getIconEntity().getIconPath());
				mapList.add(node);			
			}
		}
		return mapList;
	}
	
	public boolean checkHasChilds(List<FunctionEntity> funcList){
		boolean flag = false;
		for(FunctionEntity function:funcList){
			if(function.getChildFunctionEntity().size()==0){
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public String getSxAllMenu(String systemId, String mf, String departId,
			String haveFunctinIds, String functionIds) {
		return null;
	}

	/**
	 * 获取公共菜单（省去校验是否第一次登陆，直接放在第一桌面）
	 * 
	 * @param request
	 * @param userId
	 * @param systemId
	 * @param mf
	 * @param departId
	 * @return
	 */
	@Override
	public Object getBaseMenu(HttpServletRequest request, String userId,
			String systemId, String mf, String departId) {
		projectAdd = getPath(request);
		List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
		List<ApplicationEntity> appList = new ArrayList<ApplicationEntity>();
		String hql = "from ApplicationEntity where organizationEntity.id=?";
		List<Object> param = new ArrayList<Object>();
		param.add(departId);
		appList = this.queryListByHql(hql, param);
		// 获取应用集合（非系统应用）
		int orderNum = 1;
		if (null != appList) {
			for (int i = 0, j = appList.size(); i < j; i++) {
				List<RoleFunctionRelationEntity> roleFuncList = getFunctionList(appList.get(i).getId(),userId);
				Set<FunctionEntity> funcList = new HashSet<FunctionEntity>();
				for(RoleFunctionRelationEntity roleFunction : roleFuncList){
					funcList.add(roleFunction.getFunctionEntity());
				}
				for (FunctionEntity function : funcList) {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", function.getId());
					node.put("menuName", function.getName());
					node.put("iconOpen", "libs/icons/tree_close.gif");
					node.put("iconClose", "libs/icons/tree_open.gif");
					node.put("icon", projectAdd + function.getIconEntity().getBigIconPath());
					node.put("appIcon", projectAdd + function.getIconEntity().getBigIconPath());
					node.put("orderNum", orderNum);
					if (null != function.getChildFunctionEntity()) {
//						node.put("children", installFunction(function.getChildFunctionEntity()));
					} else {
						if(StringUtil.isNotEmpty(function.getApplicationEntity().getUrl())){
							if(checkFunctionUrl(function.getUrl())){
								node.put("url",function.getUrl());
							}else{
								node.put("url",function.getApplicationEntity().getUrl()+function.getUrl());
							}
						}else{
							node.put("url", projectAdd+function.getUrl());
						}
						node.put("type", "0");
					}
					orderNum++;
					childs.add(node);
				}
			}
		}
		List<Map<String, Object>> result1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> result2 = new HashMap<String, Object>();
		result2.put("id", 1);
		result2.put("group", childs);
		result1.add(result2);
		List<Map<String, Object>> resultAS = new ArrayList<Map<String, Object>>();
		//总共五个桌面，第一次后面都为空
		for (int i = 2; i < 6; i++) {
			Map<String, Object> resultA = new HashMap<String, Object>();
			resultA.put("id", i);
			resultA.put("group", resultAS);
			result1.add(resultA);
		}
		Map<String, Object> result3 = new HashMap<String, Object>();
		result3.put("list", result1);
		return result3;
	}

	// 获取没有父功能的功能集合
	private List<RoleFunctionRelationEntity> getFunctionList(String appID,String userId) {
		String roleUserhql = "from RoleUserRelationEntity where userEntity.id = ?";
		
		List<RoleUserRelationEntity> userRoleList = this.queryListByHql(roleUserhql, userId);
		
		//获取角色ID的集合
		List<String> userRoleIdList = new ArrayList<String>();;
		if(null!=userRoleList){
			for (RoleUserRelationEntity userRole : userRoleList) {
				userRoleIdList.add(userRole.getRoleEntity().getId());
			}
		}
		
		List<RoleFunctionRelationEntity> funcList = new ArrayList<RoleFunctionRelationEntity>();
		String hql = "from RoleFunctionRelationEntity where functionEntity.applicationEntity.id=:appId and functionEntity.parentFunctionEntity.id is null "
				+ "and roleEntity.id in (:roleIds) order by functionEntity.code desc";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("appId",appID);
		param.put("roleIds", userRoleIdList);
		if (null != appID) {
			funcList = this.queryListByHql(hql, param);
		}
		return funcList;
	}

	// 组装子功能（一级）
	private List<Map<String, Object>> installFunction(
			List<FunctionEntity> funcList,List<String> authorIds) {
		List<Map<String, Object>> childsFunction = new ArrayList<Map<String, Object>>();
		if (null != funcList) {
			for (FunctionEntity function : funcList) {
				if(authorIds.contains(function.getId())){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", function.getId());
					map.put("menuName", function.getName());
					map.put("appIcon", projectAdd + function.getIconEntity().getBigIconPath());
					map.put("icon", projectAdd + function.getIconEntity().getIconPath());
					map.put("openType", "dialog");
					map.put("type", "1");
					if(StringUtil.isNotEmpty(function.getApplicationEntity().getUrl())){
						if(checkFunctionUrl(function.getUrl())){
							map.put("url",function.getUrl());
						}else{
							map.put("url",function.getApplicationEntity().getUrl()+function.getUrl());
						}
					}else{
						map.put("url", projectAdd+function.getUrl());
					}
					childsFunction.add(map);
				}
			}
		}
		return childsFunction;
	}

	// 获取项目路径
	private String getPath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		return basePath;
	}
	
	@Override
	public Object checkLeftMenuIsAuthority(HttpServletRequest request,
			String userId, String departId, String systemId, String mf,
			String functionIds) {
		projectAdd=getPath(request);
		
		//查询所有用户所属角色有权限的功能集合
		List<RoleFunctionRelationEntity> roleFunctionList = getRoleFunctionList(userId);
		Set<FunctionEntity> funcList = new HashSet<FunctionEntity>();
		for(RoleFunctionRelationEntity roleFunction : roleFunctionList){
			funcList.add(roleFunction.getFunctionEntity());
		}
		
		//最终返回的结果集
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		//判断左侧菜单是否在用户所属角色的功能里
		String[] paramFunctionIds = functionIds.split(",");
		for (String paramFunctionId : paramFunctionIds) {
			for (FunctionEntity function : funcList) {
				if (paramFunctionId.equals(function.getId())) {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", function.getId());
					node.put("menuName", function.getName());
					node.put("iconOpen", "libs/icons/tree_close.gif");
					node.put("iconClose", "libs/icons/tree_open.gif");
					node.put("icon", projectAdd+function.getIconEntity().getBigIconPath());
					if(null!=function.getChildFunctionEntity()){
						node.put("type", "0");
					}else{
						node.put("type", "1");
					}
					node.put("openType","dialog");
					if(StringUtil.isNotEmpty(function.getApplicationEntity().getUrl())){
						if(checkFunctionUrl(function.getUrl())){
							node.put("url",function.getUrl());
						}else{
							node.put("url",function.getApplicationEntity().getUrl()+function.getUrl());
						}
					}else{
						node.put("url", projectAdd+function.getUrl());
					}
					result.add(node);
					break;
				}
			}
		}
		
		Map<String, Object> returnObject = new HashMap<String, Object>();
		returnObject.put("list", result);
		return returnObject;
	}
	
	// 获取没有子功能的功能集合
	private List<RoleFunctionRelationEntity> getRoleFunctionList(String userId) {
		String userRoleIdHql = "from RoleUserRelationEntity where userEntity.id = ?";
		
		List<RoleUserRelationEntity> userRoleList = this.queryListByHql(userRoleIdHql, userId);
		
		//获取角色ID的集合
		List<String> userRoleIdList = new ArrayList<String>();;
		if(null!=userRoleList){
			for (RoleUserRelationEntity userRole : userRoleList) {
				userRoleIdList.add(userRole.getRoleEntity().getId());
			}
		}
		
		//查询所有用户所属角色有权限的功能集合
		String roleFunctionHql = "from RoleFunctionRelationEntity where roleEntity.id in (:roleIds)";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("roleIds", userRoleIdList);
		List<RoleFunctionRelationEntity> roleFunctionList = this.queryListByHql(roleFunctionHql, param);
		
		return roleFunctionList;
	}
	
	@Override
	public Object checkDeskMenuIsAuthority(HttpServletRequest request,
			String userId, String departId, String systemId, String mf,
			String functionIds) {

		StringBuffer sbuf = appendDeskFunctionIds(functionIds);
		//所有桌面集合
		Set<Map<String, Object>> Childs = new LinkedHashSet<Map<String, Object>>();
		//页面集合
		StringBuffer loPageBu=new StringBuffer();
		// n屏桌面
		JSONArray desks = JSONArray.fromObject(sbuf.toString());
		for (Object object : desks) {
			JSONObject desk = (JSONObject)object;
			// 第n屏
			String currentPage = desk.getString("loPage");
			loPageBu.append(currentPage);
			// 这一屏上的菜单或分组
			String menuIdString = desk.getString("menuIds");
			// 这一屏上的菜单或分组的顺序
			String menuIdsAndOrder = desk.getString("menuIdsAndOrder");
			// 这一屏上的自定义文件夹集合
			String folderString = desk.getString(currentPage);
			JSONArray folderArray = JSONArray.fromObject(folderString);
			List<Map<String, Object>> currentPageContent = new ArrayList<Map<String,Object>>();
			List<RoleFunctionRelationEntity> roleFunctionList = getRoleFunctionList(userId);
			List<String> authorIds = new ArrayList<String>();
			for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunctionList) {
				authorIds.add(roleFunctionRelationEntity.getFunctionEntity().getId());
			}
			Set<FunctionEntity> funcList = new HashSet<FunctionEntity>();
			for(RoleFunctionRelationEntity roleFunction : roleFunctionList){
				funcList.add(roleFunction.getFunctionEntity());
			}
			for (Object element : folderArray) {
				//遍历文件夹
				JSONObject folder = (JSONObject)element;
				Map<String, Object> node = new HashMap<String, Object>();
				String folderid = folder.getString("folderId");
				String name = folder.getString("name");
				String icon = folder.getString("icon");
				node.put("menuName", name);
				node.put("id", folderid);
				node.put("icon", icon);
				node.put("appIcon", icon);
				node.put("iconType", "folder");
				node.put("orderNum", folder.getString("orderNum"));
				String deskfolderids = folder.getString("deskfolderIds");
				List<Map<String, Object>> functionInFolder = new ArrayList<Map<String,Object>>();
				if (StringUtil.isNotEmpty(deskfolderids)) {
					String[] paramFunctionIds = deskfolderids.split(",");
					for (String paramFunctionId : paramFunctionIds) {
						for (FunctionEntity function : funcList) {
							if (paramFunctionId.equals(function.getId())) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", function.getId());
								map.put("menuName", function.getName());
								map.put("iconOpen", "libs/icons/tree_close.gif");
								map.put("iconClose", "libs/icons/tree_open.gif");
								map.put("icon", projectAdd+function.getIconEntity().getBigIconPath());
								if(null!=function.getChildFunctionEntity()){
									map.put("type", "0");
								}else{
									map.put("type", "1");
								}
								map.put("openType","dialog");
								if(StringUtil.isNotEmpty(function.getApplicationEntity().getUrl())){
									if(checkFunctionUrl(function.getUrl())){
										map.put("url",function.getUrl());
									}else{
										map.put("url",function.getApplicationEntity().getUrl()+function.getUrl());
									}
								}else{
									map.put("url", projectAdd+function.getUrl());
								}
								functionInFolder.add(map);
								break;
							}
						}
					}
				}
				node.put("children", functionInFolder);
				currentPageContent.add(node);
			}
			
			//判断menu是否有下级，如果有，就给node增加children属性。
			
			// if 有下级 node.put("isgroup", true);
			// 没有则node.put("openType","dialog");
			
			// 关键字段type:0 目录;1 菜单
			if(StringUtil.isNotEmpty(menuIdString)){
				String[] menuIds = menuIdString.split(",");
				for (String menuId : menuIds) {
					for (FunctionEntity function : funcList) {
						if (menuId.equals(function.getId())&&authorIds.contains(menuId)) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", function.getId());
							map.put("menuName", function.getName());
							map.put("iconOpen", "libs/icons/tree_close.gif");
							map.put("iconClose", "libs/icons/tree_open.gif");
							map.put("icon", projectAdd+function.getIconEntity().getBigIconPath());
							map.put("orderNum", getOrderNum(menuIdsAndOrder,function.getId()));
							if(null!=function.getChildFunctionEntity()&&function.getChildFunctionEntity().size()>0){
								map.put("type", "0");
								map.put("isgroup", true);
								map.put("children", installFunction(function.getChildFunctionEntity(),authorIds));
							}else{
								map.put("type", "1");
								if(StringUtil.isNotEmpty(function.getApplicationEntity().getUrl())){
									if(checkFunctionUrl(function.getUrl())){
										map.put("url",function.getUrl());
									}else{
										map.put("url",function.getApplicationEntity().getUrl()+function.getUrl());
									}
								}else{
									map.put("url", projectAdd+function.getUrl());
								}
								map.put("openType","dialog");
							}
							currentPageContent.add(map);
							break;
						}
					}
				}
			}
			
			// 当前页面的元素顺序调整
			Map<String, Object> pageElementResult = new HashMap<String, Object>();
			pageElementResult.put("id", currentPage);
			
			Map<String, Object> currentMap = new HashMap<String, Object>();;
			for(int i=0;i<currentPageContent.size()-1;i++){
				for(int j=0;j<currentPageContent.size()-i-1;j++){
					String num0=(String) currentPageContent.get(j).get("orderNum");
					String num1=(String) currentPageContent.get(j+1).get("orderNum");
			    	int nu0=Integer.valueOf(num0);
			    	int nu1=Integer.valueOf(num1);
			        if(nu0>nu1){
			            currentMap=currentPageContent.get(j+1);
			            currentPageContent.set(j+1, currentPageContent.get(j));
			            currentPageContent.set(j,currentMap);
			        }
				}
			}
			pageElementResult.put("group", currentPageContent);
			Childs.add(pageElementResult);
		}
		Set<Map<String, Object>> childChilds = new LinkedHashSet<Map<String, Object>>();
		if(StringUtil.isNotEmpty(loPageBu)){
			for(int i=1;i<6;i++){
				List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
				Map<String, Object> pageResult = new HashMap<String, Object>();
					for(Map<String,Object> obj:Childs){
						String page=(String) obj.get("id");
						String curPage=String.valueOf(i);
						if(curPage.equals(page)){
							pageResult.put("id", i);
							pageResult.put("group", obj.get("group"));
							childs.add(pageResult);
						}
					}
				 if(childs.size()==0){
					 pageResult.put("id", i);
					 pageResult.put("group", childs);
				} 
				 childChilds.add(pageResult);
			}
		}
		Map<String, Object> finalResult = new HashMap<String, Object>();
		finalResult.put("list", childChilds);
		return finalResult;
	
	}

	private StringBuffer appendDeskFunctionIds(String functionIds) {
		JSONArray json = new JSONArray();
		if (StringUtil.isNotEmpty(functionIds)) {
			JSONObject jsonObject = JSONObject.fromObject(functionIds);
			json = jsonObject.getJSONArray("list");
		}
		// 首先解析是那页的桌面
		Set<String> locationPage = new LinkedHashSet<String>();
		for (int z = 0; z < json.size(); z++) {
			JSONObject jsonOb = (JSONObject) json.get(z);
			String lPage = jsonOb.getString("locationPage");
			locationPage.add(lPage);
		}
		// 根据桌面来 获取ids
		String deskfolderIds = "";
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[");
		for (String loPage : locationPage) {
			String menuIds = "";
			String menuIdsAndOrder = "";
			sbuf.append("{\"loPage\":\"" + loPage + "\",");
			String sf = "";
			sf += "\"" + loPage + "\":[";
			for (int z = 0; z < json.size(); z++) {
				JSONObject jsonOb = (JSONObject) json.get(z);
				String type = jsonOb.getString("type");
				String lPage = jsonOb.getString("locationPage");
				String id = jsonOb.getString("id");
				String orderNum =jsonOb.getString("orderNum");
				if (lPage.equals(loPage)) {
					if (type.contains("2#")) {
						sf += "{";
						deskfolderIds = type.substring(2);
						sf += "\"folderId\":\"" + id + "\",";
						sf += "\"name\":\"" + jsonOb.getString("name") + "\",";
						sf += "\"icon\":\"" + jsonOb.getString("icon") + "\",";
						sf += "\"orderNum\":\"" + jsonOb.getString("orderNum") + "\",";
						sf += "\"deskfolderIds\":\"" + deskfolderIds + "\"";
						sf += "},";
					} else {
						// menuIds="402880004e9ae13d014e9ae9d5fa0002,402880004e9ae13d014e9ae9d6600003,402880004e9ae13d014e9ae9d7300005,402880004e9ae13d014e9ae9d6c70004";
						menuIds += id+ ",";
						menuIdsAndOrder += id+"|"+orderNum+ ",";
					}
				}
			}
			if (StringUtil.isNotEmpty(menuIds)) {
				menuIds = menuIds.substring(0, menuIds.lastIndexOf(","));
			}
			
			if (StringUtil.isNotEmpty(menuIdsAndOrder)) {
				menuIdsAndOrder = menuIdsAndOrder.substring(0,menuIdsAndOrder.lastIndexOf(","));
			}

			if (sf.toString().lastIndexOf(",") > 0) {
				sf = sf.toString().substring(0, sf.toString().lastIndexOf(","));
			}
			sf += "],";
			sbuf.append(sf);
			sbuf.append("\"menuIds\":\"" + menuIds + "\",");
			sbuf.append("\"menuIdsAndOrder\":\"" + menuIdsAndOrder + "\"},");
		}

		sbuf.append("]");
		return sbuf;
	}
	
	private String getOrderNum(String menuIdsAndOrder, String id) {
		String ordernum="";
		if(StringUtil.isNotEmpty(menuIdsAndOrder)){
			String[] array=menuIdsAndOrder.split(",");
			for(int i=0;i<array.length;i++){
				String val=array[i];
				if(val.contains(id)){
					ordernum=val.substring(val.lastIndexOf("|")+1);
				}
			}
		} 
		return ordernum;
	}

	@Override
	public Object searchClause(HttpServletRequest request, String userId,
			String departId, String systemId, String mf, String functionIds) {
		StringBuffer sbuf = appendDeskFunctionIds(functionIds);
		// 循环每个桌面的ids
		JSONArray stringBuffer = JSONArray.fromObject(sbuf.toString());

		String deskFucntionId = "";
		for (int s = 0; s < stringBuffer.size(); s++) {
			JSONObject jsonObSBuffer = (JSONObject) stringBuffer.get(s);
			String loPage = jsonObSBuffer.getString("loPage");
			String menuids = jsonObSBuffer.getString("menuIds");
			String folder = jsonObSBuffer.getString(loPage);
			JSONArray folderJson = JSONArray.fromObject(folder);
			// 当文件夹的时候
			if (folderJson.size() > 0) {
				for (int j = 0; j < folderJson.size(); j++) {
					JSONObject jsonOb = (JSONObject) folderJson.get(j);
					String deskfolderids = jsonOb.getString("deskfolderIds");
					deskFucntionId += deskfolderids + ",";
				}
			}
			deskFucntionId += menuids + ",";
		}

		if (deskFucntionId.length() > 1) {
			deskFucntionId = deskFucntionId.substring(0,deskFucntionId.lastIndexOf(","));
		}
		//最终返回的结果集
		List<Map<String, Object>> Childs = new ArrayList<Map<String, Object>>();
		//该用户有权限的菜单结果集
		List<RoleFunctionRelationEntity> roleFunctionList = getRoleFunctionList(userId);
		List<String> authorIds = new ArrayList<String>();
		for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunctionList) {
			authorIds.add(roleFunctionRelationEntity.getFunctionEntity().getId());
		}
		String[] deskMenuIds = deskFucntionId.split(",");
		for (String menuId : deskMenuIds) {
			for (RoleFunctionRelationEntity relation : roleFunctionList) {
				if (menuId.equals(relation.getFunctionEntity().getId())) {
					FunctionEntity function = relation.getFunctionEntity();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", function.getId());
					map.put("menuName", function.getName());
					map.put("key", function.getName());
					map.put("suggest", function.getName());
					map.put("value", function.getId());
					if(null!=function.getChildFunctionEntity()){
						map.put("type", "0");
						if(StringUtil.isNotEmpty(function.getApplicationEntity().getUrl())){
							if(checkFunctionUrl(function.getUrl())){
								map.put("url",function.getUrl());
							}else{
								map.put("url",function.getApplicationEntity().getUrl()+function.getUrl());
							}
						}else{
							map.put("url", projectAdd+function.getUrl());
						}
						map.put("openType","dialog");
					}else{
						map.put("type", "1");
						map.put("isgroup", true);
						map.put("children", installFunction(function.getChildFunctionEntity(),authorIds));
					}
					Childs.add(map);
					break;
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", Childs);
		return result;
	}

	@Override
	public Object downloadApp(JSONObject requestBody) {
		final String pOrgCode = requestBody.getString("orgCode");
		final String pAppName = requestBody.getString("appName");
		final String pAppCode = requestBody.getString("appCode");
		final String pDemoService = requestBody.getString("demoService");
		final String url = "http://192.168.0.10:8080/paas/webservice/installApp";
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HashMap<String, String> form = new HashMap<String, String>();
					form.put("orgCode", pOrgCode);
					form.put("appName", pAppName);
					form.put("appCode", pAppCode);
					String appUrl = HTTPUtil.request(url,form , false);
					installApp(appUrl,pDemoService,pAppName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		JSONObject jsob = new JSONObject();
		jsob.put("success", "success");
		return jsob;
	}
	public void installApp(String appUrl,String pDemoService,String appCode){
		HashMap<String, String> form = new HashMap<String, String>();
		form.put("appCode", appCode);
		String url = "http://"+appUrl.replaceAll("\"", "")+"/"+"demo/appConfigController/getAppConfig";
		String result = HTTPUtil.request(url, form, false);
		ApplicationEntity application = new ApplicationEntity();
		JSONArray jsar = new JSONArray(); 
		JSONObject jsob = new JSONObject();
		if(StringUtil.isNotEmpty(result)){
			jsob = JSONObject.fromObject(result);
			application.setCode(jsob.getString("appCode"));
			application.setCreatetime(new Date());
			application.setName(jsob.getString("appName"));
			application.setUrl(jsob.getString("appUrl"));
			IconEntity icon = this.expandEntity(IconEntity.class, "ff80808157f9e5d30157f9f52729004d");
			application.setIconEntity(icon);
			application.setOrganizationEntity(this.expandEntityByProperty(OrganizationEntity.class, "orgCode", jsob.getString("orgCode")));
			applicationService.insertApplicationEntity(application);
			jsar = jsob.getJSONArray("functions");
			if(null != jsar){
				for (int i=0;i<jsar.size();i++) {
					JSONObject json = jsar.getJSONObject(i);
					FunctionEntity function = new FunctionEntity();
					function.setApplicationEntity(application);
					function.setCode(json.getString("functionCode"));
					function.setIconEntity(this.expandEntity(IconEntity.class, "ff80808157f9e5d30157f9f52729004d"));
					function.setName(json.getString("functionName"));
					function.setUrl(json.getString("functionUrl"));
					functionService.insertEntity(function);
				}
			}
		}
	}
	
	public Boolean checkFunctionUrl(String functionUrl){
		Boolean flag = false;
		flag = functionUrl.startsWith("http");
		return flag;
	}
}
