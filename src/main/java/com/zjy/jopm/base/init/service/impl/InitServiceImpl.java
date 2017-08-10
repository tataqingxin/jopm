package com.zjy.jopm.base.init.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.PasswordUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.OperationEntity;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.dict.service.ext.DictExtService;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.init.service.InitService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;

/**
 * @ClassName: InitServiceImpl
 * @Description: [初始化实现]
 * @author xuanx
 * @date 2016年5月17日 上午10:33:30
 * @since JDK 1.6
 */
@Service("initService")
@Transactional
public class InitServiceImpl extends BaseServiceimpl implements InitService {
	
	@Autowired
	private DictExtService dictExtService;

	/**
	 * @Title: saveUserOrgInfo
	 * @Description: [初始化机构 人员信息 保存 ]
	 * @param @param orgName 机构名称
	 * @param @param orgCode 机构编码
	 * @param @param userName 系统管理员用户
	 * @param @param passWord 密码
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson InitDataInfo(String orgName, String orgCode,
			String userName, String passWord) {
		AjaxJson j = new AjaxJson();
		Map<String,Object> map=new HashMap<String, Object>();
		//验证字段
		j = ValidationField(j, orgName, orgCode, userName, passWord);
		if (!j.isSuccess()) {
			return j;
		}
		//保存之前 清空 各个表
		deleteAllTable();
		//保存机构
		String orgId = saveOrg(j, orgName, orgCode);
		if (StringUtil.isEmpty(orgId)) {
			return j;
		}else{
			map.put("org", "机构[<span class='font_bule'>"+orgName+"</span>]创建成功！");
		}
		//保存用户
		OrganizationEntity org=this.expandEntity(OrganizationEntity.class, orgId);
		String userId = saveUser(j, userName, passWord,org);
		if (StringUtil.isEmpty(userId)) {
			return j;
		}else{
			map.put("user", "用户[<span class='font_bule'>"+userName+"</span>]创建成功！");
		}
		UserEntity user=this.expandEntity(UserEntity.class, userId);
		//初始化数据
		j = InitInfo(map,j, org, user);
		return j;
	}

	/**
	 * @param map 
	 * @Title: InitInfo
	 * @Description: [初始化信息 应用 角色 功能 资源 ]
	 * @param @param orgId
	 * @param @param userId 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	@SuppressWarnings("unchecked")
	private AjaxJson InitInfo(Map<String, Object> map, AjaxJson j, OrganizationEntity org, UserEntity user) {
		Document document;
		try {
			InputStream inStream = this.getClass().getResourceAsStream(
					"/basicDataInit-config.xml");
			SAXReader reader = new SAXReader();
			document = reader.read(inStream);// 读取XML文件
			//初始化字典
			initDictionary(map,document.selectNodes("/basic/dics/dic"));
			dictExtService.initAllDictionaryGroups();
			//初始化图标
			initIcon(map,document.selectNodes("/basic/icons/icon"));
			//初始化角色 返回集合id
			List<String> roleIds = initRole(map,document
					.selectNodes("/basic/roles/role"),org);
			//初始化应用 功能 
            initApplicationFunction(map,document
					.selectNodes("/basic/applications/application"),org);
			//初始化角色人员
			initUserRole(roleIds,user);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMessage("初始化失败");
			e.printStackTrace();
		}
		j.setObj(map);
		return j;
	}
	/**
	 * 
	* @param map 
	 * @Title: initApplicationFunction 
	* @Description: [初始化应用 功能]
	* @param @param selectNodes
	* @param @param org  参数说明 
	* @return void 返回类型 
	* @throws JumpException 异常类型
	 */
    @SuppressWarnings("unchecked")
	private void initApplicationFunction(Map<String, Object> map, List<Node> selectNodes,
			OrganizationEntity org) {
    	String message="";
    	for (Node roleNode : selectNodes) {
			Element roleNodeElement = ((Element) roleNode);
			//应用名称
			String appName = roleNodeElement.attributeValue("name");
			//应用编码
			String appCode = roleNodeElement.attributeValue("code");
			//应用状态
			String appStatus = roleNodeElement.attributeValue("status");
			//应用类型
			String appType = roleNodeElement.attributeValue("type");
			//应用类型
			String url = roleNodeElement.attributeValue("url");
			//角色名称
			String roleCode = roleNodeElement.attributeValue("roleCode");
			//图标名称
			String iconName = roleNodeElement.attributeValue("iconName");
			if(StringUtil.isNotBlank(iconName)&&
					StringUtil.isNotBlank(appName)&&
					StringUtil.isNotBlank(appCode)&&
					StringUtil.isNotBlank(appStatus)&&
					StringUtil.isNotBlank(appType)&&
					StringUtil.isNotBlank(url)&&
					StringUtil.isNotBlank(roleCode)){
				IconEntity icon=this.expandEntityByProperty(IconEntity.class, "name", iconName);
				ApplicationEntity app=new ApplicationEntity();
				app.setName(appName);
				app.setCode(appCode);
				app.setUrl(url);
				app.setType(appType);
				app.setStatus(appStatus);
				app.setCreatetime(new Date());
				app.setIconEntity(icon);
				app.setOrganizationEntity(org);
				this.saveEntity(app);
				message+=appName+",";
				initFunction(map,app,roleCode,roleNodeElement.selectNodes("functions/function"));
			}
    	}
    	if(StringUtil.isNotEmpty(message)){
    		message=message.substring(0, message.lastIndexOf(","));
			map.put("app", "应用[<span class='font_bule'>"+message+"</span>]创建成功");
		}
	}
    /**
     * 
    * @param map 
     * @Title: initFunction 
    * @Description: [初始化 功能]
    * @param @param app
    * @param @param roleName
    * @param @param selectNodes  参数说明 
    * @return void 返回类型 
    * @throws JumpException 异常类型
     */
	@SuppressWarnings("unchecked")
	private void initFunction(Map<String, Object> map, ApplicationEntity app, String roleCode,
			List<Node> selectNodes) {
		String message="";
		if (selectNodes != null && selectNodes.size() > 0) {
			for (Node itemNode : selectNodes) {
				Element itemElement = ((Element) itemNode);
				//功能名称
				String name = itemElement
						.attributeValue("name");
				//功能编码
				String code = itemElement
						.attributeValue("code");
				//功能地址
				String url = itemElement
						.attributeValue("url");
				//功能状态
				String status = itemElement
						.attributeValue("status");
				//图标名称
				String iconName = itemElement
						.attributeValue("iconName");
				if(StringUtil.isNotBlank(name)&&
						StringUtil.isNotBlank(code)&&
						StringUtil.isNotBlank(url)&&
						StringUtil.isNotBlank(status)&&
						StringUtil.isNotBlank(iconName)){
					RoleEntity role=this.expandEntityByProperty(RoleEntity.class, "code", roleCode);
					IconEntity icon=this.expandEntityByProperty(IconEntity.class, "name", iconName);
					FunctionEntity function=new FunctionEntity();
					function.setName(name);
					function.setCode(code);
					function.setStatus(status);
					function.setIconEntity(icon);
					function.setUrl(url);
					function.setApplicationEntity(app);
					this.saveEntity(function);
					RoleFunctionRelationEntity RoleFunctionRelationEntity=new RoleFunctionRelationEntity();
					RoleFunctionRelationEntity.setFunctionEntity(function);
					RoleFunctionRelationEntity.setRoleEntity(role);
					this.saveEntity(RoleFunctionRelationEntity);
					message+=name+",";
					initOperation(function,roleCode,itemElement.selectNodes("operations/operation"));
				}
			}
		}
		if(StringUtil.isNotEmpty(message)){
			message=message.substring(0, message.lastIndexOf(","));
			map.put("func_tion", "功能[<span class='font_bule'>"+message+"</span>]创建成功");
		}
	}
    
	/**
	 * 
	* @Title: initOperation 
	* @Description: [初始化操作]
	* @param @param function
	* @param @param roleCode
	* @param @param selectNodes  参数说明 
	* @return void 返回类型 
	* @throws JumpException 异常类型
	 */
	private void initOperation(FunctionEntity function, String roleCode,
			List<Node> selectNodes) {
		if (selectNodes != null && selectNodes.size() > 0) {
			for (Node itemNode : selectNodes) {
				Element itemElement = ((Element) itemNode);
				//操作名称
				String name = itemElement
						.attributeValue("name");
				//操作编码
				String code = itemElement
						.attributeValue("code");
				//图标名称
				String iconName = itemElement
						.attributeValue("iconName");
				if(StringUtil.isNotBlank(name)&&
						StringUtil.isNotBlank(code)&&
						StringUtil.isNotBlank(iconName)){
					IconEntity icon=this.expandEntityByProperty(IconEntity.class, "name", iconName);
					OperationEntity operationEntity=new OperationEntity();
					operationEntity.setName(name);
					operationEntity.setCode(code);
					operationEntity.setIconEntity(icon);
					operationEntity.setFunctionEntity(function);
					this.saveEntity(operationEntity);
				}
			}
		}
	}

	/**
     * 初始化角色用户
    * @Title: initUserRole 
    * @Description: [功能描述]
    * @param @param roleIds
    * @param @param user  参数说明 
    * @return void 返回类型 
    * @throws JumpException 异常类型
     */
	private void initUserRole(List<String> roleIds, UserEntity user) {
		 for(String roleId:roleIds){
			 RoleEntity role=this.expandEntity(RoleEntity.class, roleId);
			 RoleUserRelationEntity roleUserRelation=new RoleUserRelationEntity();
			 roleUserRelation.setRoleEntity(role);
			 roleUserRelation.setUserEntity(user);
			 this.saveEntity(roleUserRelation);
		 }
	}

	/**
	 * @param map 
	 * @Title: initRole
	 * @Description: [初始化角色]
	 * @param @param selectNodes
	 * @param @return 参数说明
	 * @return List<String> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private List<String> initRole(Map<String, Object> map, List<Node> selectNodes,OrganizationEntity org) {
		List<String> roleIds=new ArrayList<String>();
		String message="";
		for (Node roleNode : selectNodes) {
			Element roleNodeElement = ((Element) roleNode);
			//角色名称
			String roleName = roleNodeElement.attributeValue("name");
			//角色名称
			String roleCode = roleNodeElement.attributeValue("code");
			//角色类型
			String roleType = roleNodeElement.attributeValue("type");
			//图标名称
//			String iconName = roleNodeElement.attributeValue("iconName");
			message+=roleName+",";
			if(StringUtil.isNotBlank(roleCode)&&StringUtil.isNotBlank(roleType)&&
					StringUtil.isNotBlank(roleName)){
				RoleEntity role=new RoleEntity();
				role.setName(roleName);
				role.setCode(roleCode);
				role.setType(roleType);
				role.setOrganizationEntity(org);
				this.saveEntity(role);
				roleIds.add(role.getId());
			}
		}
		if(StringUtil.isNotEmpty(message)){
			message=message.substring(0, message.lastIndexOf(","));
			map.put("role", "角色[<span class='font_bule'>"+message+"</span>]创建成功");
		}
		return roleIds;
	}

	/**
	 * 初始化图标
	 * @param map 
	 * 
	 * @Title: initIcon
	 * @Description: [功能描述]
	 * @param @param selectNodes 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private void initIcon(Map<String, Object> map, List<Node> selectNodes) {
		String message="";
		for (Node iconNode : selectNodes) {
			Element iconNodeElement = ((Element) iconNode);
			//图标名称
			String iconName = iconNodeElement.attributeValue("name");
			//字典数值
			String iconType = iconNodeElement.attributeValue("type");
			//路径
			String iconPath = iconNodeElement.attributeValue("path");
			if (StringUtil.isNotBlank(iconName)
					&& StringUtil.isNotBlank(iconType)
					&& StringUtil.isNotBlank(iconPath)) {
				IconEntity icon = new IconEntity();
				icon.setName(iconName);
				icon.setType(iconType);
				icon.setIconPath(iconPath);
				icon.setMediumIconPath(iconPath);
				icon.setBigIconPath(iconPath);
				this.saveEntity(icon);
				message+=iconName+",";
			}
		}
		
		if(StringUtil.isNotEmpty(message)){
			message=message.substring(0, message.lastIndexOf(","));
			map.put("icon", "图标[<span class='font_bule'>"+message+"</span>]创建成功");
		}
	}

	/**
	 * @param map 
	 * @Title: initDictionary
	 * @Description: [初始化字典]
	 * @param @param selectNodes 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	@SuppressWarnings("unchecked")
	private void initDictionary(Map<String, Object> map, List<Node> selectNodes) {
		String message="";
		for (Node dicNode : selectNodes) {
			Element dicNodeElement = ((Element) dicNode);
			String typeGroupName = dicNodeElement
					.attributeValue("typeGroupName");
			String typeGroupCode = dicNodeElement
					.attributeValue("typeGroupCode");
			if (StringUtil.isNotBlank(typeGroupName)
					&& StringUtil.isNotBlank(typeGroupCode)) {
				DictionaryGroupEntity typeGroup = new DictionaryGroupEntity();
				typeGroup.setName(typeGroupName);
				typeGroup.setCode(typeGroupCode);
				this.saveEntity(typeGroup);
				message+=typeGroupName+",";
				//保存数值
				List<Node> itemNodes = dicNodeElement.selectNodes("items/item");
				if (itemNodes != null && itemNodes.size() > 0) {
					for (Node itemNode : itemNodes) {
						Element itemElement = ((Element) itemNode);
						String typeName = itemElement
								.attributeValue("typeName");
						String typeCode = itemElement
								.attributeValue("typeCode");
						if (StringUtil.isNotBlank(typeName)
								&& StringUtil.isNotBlank(typeCode)) {
							DictionaryEntity type = new DictionaryEntity();
							type.setCode(typeCode);
							type.setName(typeName);
							type.setDictionaryGroupEntity(typeGroup);
							this.saveEntity(type);
						}
					}
				}
			}
		}
		if(StringUtil.isNotEmpty(message)){
			message=message.substring(0, message.lastIndexOf(","));
			map.put("dictionary", "字典[<span class='font_bule'>"+message+"</span>]创建成功");
		}
	}

	/**
	 * @param org 
	 * @Title: saveUser
	 * @Description: [保存用户]
	 * @param @param j
	 * @param @param userName
	 * @param @param password
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private String saveUser(AjaxJson j, String userName, String password, OrganizationEntity org) {
		UserEntity user = new UserEntity();
		password = PasswordUtil.encrypt(userName, password,
				PasswordUtil.getStaticSalt());
		user.setUserName(userName);
		user.setRealName("系统管理员");
		user.setPassword(password);
		user.setStatus("1");// 状态  0 停用    1 启动 
		user.setDelFlag("0");//1：删除;0:未删除
		user.setCreatetime(new Date());
		user.setOrganizationEntity(org);
		user.setSex("1");
		boolean flag = this.saveEntity(user);
		if (!flag) {
			return "";
		}
		return user.getId();
	}

	/**
	 * 保存机构
	 * 
	 * @Title: saveOrg
	 * @Description: [功能描述]
	 * @param @param j
	 * @param @param orgName
	 * @param @param orgCode 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private String saveOrg(AjaxJson j, String orgName, String orgCode) {
		OrganizationEntity org = new OrganizationEntity();
		org.setName(orgName);
		org.setCode(orgCode);
		org.setRange(1);
		boolean flag = this.saveEntity(org);
		if (!flag) {
			return "";
		}
		return org.getId();
	}

	/**
	 * @Title: ValidationField
	 * @Description: [验证字段]
	 * @param @param orgName
	 * @param @param orgCode
	 * @param @param userName
	 * @param @param passWord
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private AjaxJson ValidationField(AjaxJson j, String orgName,
			String orgCode, String userName, String passWord) {
		//判断机构名称是否为空
		if (StringUtil.isEmpty(orgName)) {
			j.setSuccess(false);
			j.setMessage("机构名称不允许为空！");
			return j;
		}
		//判断机构编码是否为空
		if (StringUtil.isEmpty(orgCode)) {
			j.setSuccess(false);
			j.setMessage("机构编码不允许为空！");
			return j;
		}
		//判断用户名称是否为空
		if (StringUtil.isEmpty(userName)) {
			j.setSuccess(false);
			j.setMessage("系统用户不允许为空！");
			return j;
		}
		//判断密码是否为空
		if (StringUtil.isEmpty(passWord)) {
			j.setSuccess(false);
			j.setMessage("密码不允许为空！");
			return j;
		}
		return j;
	}

	/**
	 * @Title: deleteAllTable
	 * @Description: [清空所有的表]
	 * @param 参数说明
	 * @return void 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	private void deleteAllTable() {
		// 清空角色 权限 中间表 1
		this.runUpdateByHql("delete from RoleFunctionRelationEntity");
		// 清空角色用户表 2
		this.runUpdateByHql("delete from RoleUserRelationEntity");
		// 清空角色表 3 
		this.runUpdateByHql("delete from RoleEntity");
		// 清空服务接口表4 
		this.runUpdateByHql("delete from ServiceInterfaceEntity");
		// 清空资源操作中间表5
		this.runUpdateByHql("delete from OperationResourceRelationEntity");
		// 清空操作表6
		this.runUpdateByHql("delete from OperationEntity");
		// 清空功能资源表7
		this.runUpdateByHql("delete from FunctionResourceRelationEntity");
		// 清空资源表8
		this.runUpdateByHql("delete from ResourceEntity");
		// 清空功能表9 防止主外键约束 删除失败
		this.runUpdateByHql("update FunctionEntity set parentFunctionEntity=null");
		this.runUpdateByHql("delete from FunctionEntity");
		// 清空应用表10
		this.runUpdateByHql("delete from ApplicationEntity");
		// 清空日志表11
		this.runUpdateByHql("delete from LogEntity");
		// 清空图标表12
		this.runUpdateByHql("delete from IconEntity");
		// 清空用户部门表13
		this.runUpdateByHql("delete from UserDepartmentRelationEntity");
		// 清空用户表14
		this.runUpdateByHql("delete from UserEntity");
		// 清空部门表15 防止主外键约束 删除失败
		this.runUpdateByHql("update DepartmentEntity set parentDepartmentEntity=null");
		this.runUpdateByHql("delete from DepartmentEntity");
		// 清空机构表16 防止主外键约束 删除失败
		this.runUpdateByHql("update OrganizationEntity set parentOrganizationEntity=null");
		this.runUpdateByHql("delete from OrganizationEntity");
		// 清空字典数值表17
		this.runUpdateByHql("delete from DictionaryEntity");
		// 清空字典分组表18
		this.runUpdateByHql("delete from DictionaryGroupEntity");
	}

}
