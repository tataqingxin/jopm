/** 
 * @Description:[角色功能单元测试]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.role.service.RoleServiceTest.java
 * @ClassName:RoleServiceTest
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月18日 下午6:22:33
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月18日 下午6:22:33  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.service;

import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.service.ext.DictExtService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.org.service.OrganizationService;
import com.zjy.jopm.base.role.entity.RoleEntity;

/**
 * @ClassName: RoleServiceTest 
 * @Description: [角色功能单元测试] 
 * @author Lu Guoqiang 
 * @date 2016年5月18日 下午6:22:33 
 * @since JDK 1.6 
 */
public class RoleServiceTest extends AbstractTestCase{

	@Autowired
	private DictExtService dictExtService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private RoleService roleService;
	
	@Test
	public void testInsertRole(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setOrganizationEntity(organizationEntity);
		roleEntity.setName("Role");
		roleEntity.setCode("role");
		roleEntity.setType(Constants.SYSTEM_TYPE);
		roleEntity.setDescription("description");
		
		AjaxJson ajaxJson = this.roleService.insertRoleEntity(roleEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testUpdateRole(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setOrganizationEntity(organizationEntity);
		roleEntity.setName("Role");
		roleEntity.setCode("role");
		roleEntity.setType(Constants.SYSTEM_TYPE);
		roleEntity.setDescription("description");
		
		AjaxJson ajaxJson = this.roleService.insertRoleEntity(roleEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		roleEntity.setName("Role1");
		ajaxJson = this.roleService.updateRoleEntity(roleEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testExpandRole(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setOrganizationEntity(organizationEntity);
		roleEntity.setName("Role");
		roleEntity.setCode("role");
		roleEntity.setType(Constants.SYSTEM_TYPE);
		roleEntity.setDescription("description");
		
		AjaxJson ajaxJson = this.roleService.insertRoleEntity(roleEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.roleService.expandRoleEntity(roleEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testDeleteRole(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setOrganizationEntity(organizationEntity);
		roleEntity.setName("Role");
		roleEntity.setCode("role");
		roleEntity.setType(Constants.SYSTEM_TYPE);
		roleEntity.setDescription("description");
		
		AjaxJson ajaxJson = this.roleService.insertRoleEntity(roleEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.roleService.deleteRoleEntity(roleEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testQueryRoleList(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		ApplicationEntity applicationEntity = new ApplicationEntity();         
		applicationEntity.setOrganizationEntity(organizationEntity);
		applicationEntity.setName("App");
		applicationEntity.setCode("app");
		applicationEntity.setStatus(Constants.ENABLE_STATUS);
		applicationEntity.setType(Constants.SYSTEM_TYPE);
		applicationEntity.setUrl("/appController/applicaionList");
		
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setOrganizationEntity(organizationEntity);
		roleEntity.setName("Role");
		roleEntity.setCode("role");
		roleEntity.setType(Constants.SYSTEM_TYPE);
		roleEntity.setDescription("description");
		
		AjaxJson ajaxJson = this.roleService.insertRoleEntity(roleEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		this.dictExtService.initAllDictionaryGroups();// 初始化字典
		
		Map<String, Object> roleMap = this.roleService.getRoleEntityQuiGrid(roleEntity, 1, 10, null, null);
		Assert.assertThat(3, CoreMatchers.equalTo(roleMap.size()));
		
		List<Map<String,Object>> result = (List<Map<String, Object>>) roleMap.get("rows");
		Assert.assertThat(1, CoreMatchers.equalTo(result.size()));
	}
	
}
