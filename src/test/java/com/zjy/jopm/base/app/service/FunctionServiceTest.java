/** 
 * @Description:[功能单元测试]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.app.service.FunctionServiceTest.java
 * @ClassName:FunctionServiceTest
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月18日 下午7:55:24
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月18日 下午7:55:24  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.app.service;

import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.IconService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.org.service.OrganizationService;

/**
 * @ClassName: FunctionServiceTest 
 * @Description: [功能单元测试] 
 * @author Lu Guoqiang 
 * @date 2016年5月18日 下午7:55:24 
 * @since JDK 1.6 
 */
public class FunctionServiceTest extends AbstractTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private FunctionService functionService;
	@Autowired
	private IconService iconService;
	
	@Test
	public void testInsertFunction(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		IconEntity iconEntity = new IconEntity();
		iconEntity.setName("Icon");
		iconEntity.setType(Constants.DICT_GROUP_APP_TYPE);
		iconEntity.setIconPath("/path/imag.jpg");
		iconEntity.setMediumIconPath("/path/imag.jpg");
		iconEntity.setBigIconPath("/path/imag.jpg");
		iconEntity.setDescription("description");
		
		success = this.iconService.insertEntity(iconEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		ApplicationEntity applicationEntity = new ApplicationEntity();         
		applicationEntity.setOrganizationEntity(organizationEntity);
		applicationEntity.setIconEntity(iconEntity);
		applicationEntity.setName("App");
		applicationEntity.setCode("app");
		applicationEntity.setStatus(Constants.ENABLE_STATUS);
		applicationEntity.setType(Constants.SYSTEM_TYPE);
		applicationEntity.setUrl("/appController/insertFunction");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		FunctionEntity functionEntity = new FunctionEntity();
		functionEntity.setApplicationEntity(applicationEntity);
		functionEntity.setIconEntity(iconEntity);
		functionEntity.setName("Function");
		functionEntity.setCode("fc");
		functionEntity.setUrl("/functionController/insertFunction");
		functionEntity.setStatus(Constants.ENABLE_STATUS);
		
		ajaxJson = this.functionService.insertFunctionEntity(functionEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testUpdateFunction(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		IconEntity iconEntity = new IconEntity();
		iconEntity.setName("Icon");
		iconEntity.setType(Constants.DICT_GROUP_APP_TYPE);
		iconEntity.setIconPath("/path/imag.jpg");
		iconEntity.setMediumIconPath("/path/imag.jpg");
		iconEntity.setBigIconPath("/path/imag.jpg");
		iconEntity.setDescription("description");
		
		success = this.iconService.insertEntity(iconEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		ApplicationEntity applicationEntity = new ApplicationEntity();         
		applicationEntity.setOrganizationEntity(organizationEntity);
		applicationEntity.setIconEntity(iconEntity);
		applicationEntity.setIconEntity(iconEntity);
		applicationEntity.setName("App");
		applicationEntity.setCode("app");
		applicationEntity.setStatus(Constants.ENABLE_STATUS);
		applicationEntity.setType(Constants.SYSTEM_TYPE);
		applicationEntity.setUrl("/appController/updateApplicaion");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		FunctionEntity functionEntity = new FunctionEntity();
		functionEntity.setApplicationEntity(applicationEntity);
		functionEntity.setIconEntity(iconEntity);
		functionEntity.setName("Function");
		functionEntity.setCode("fc");
		functionEntity.setUrl("/functionController/updateFunction");
		functionEntity.setStatus(Constants.ENABLE_STATUS);
		
		ajaxJson = this.functionService.insertFunctionEntity(functionEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		functionEntity.setName("Function1");
		ajaxJson = this.functionService.updateFunctionEntity(functionEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testExpandFunction(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		IconEntity iconEntity = new IconEntity();
		iconEntity.setName("Icon");
		iconEntity.setType(Constants.DICT_GROUP_APP_TYPE);
		iconEntity.setIconPath("/path/imag.jpg");
		iconEntity.setMediumIconPath("/path/imag.jpg");
		iconEntity.setBigIconPath("/path/imag.jpg");
		iconEntity.setDescription("description");
		
		success = this.iconService.insertEntity(iconEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		ApplicationEntity applicationEntity = new ApplicationEntity();         
		applicationEntity.setOrganizationEntity(organizationEntity);
		applicationEntity.setIconEntity(iconEntity);
		applicationEntity.setName("App");
		applicationEntity.setCode("app");
		applicationEntity.setStatus(Constants.ENABLE_STATUS);
		applicationEntity.setType(Constants.SYSTEM_TYPE);
		applicationEntity.setUrl("/appController/detailApplicaion");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		FunctionEntity functionEntity = new FunctionEntity();
		functionEntity.setApplicationEntity(applicationEntity);
		functionEntity.setIconEntity(iconEntity);
		functionEntity.setName("Function");
		functionEntity.setCode("fc");
		functionEntity.setUrl("/functionController/detailFunction");
		functionEntity.setStatus(Constants.ENABLE_STATUS);
		
		ajaxJson = this.functionService.insertFunctionEntity(functionEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.functionService.expandFunctionEntity(functionEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testDeleteFunction(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		IconEntity iconEntity = new IconEntity();
		iconEntity.setName("Icon");
		iconEntity.setType(Constants.DICT_GROUP_APP_TYPE);
		iconEntity.setIconPath("/path/imag.jpg");
		iconEntity.setMediumIconPath("/path/imag.jpg");
		iconEntity.setBigIconPath("/path/imag.jpg");
		iconEntity.setDescription("description");
		
		success = this.iconService.insertEntity(iconEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		ApplicationEntity applicationEntity = new ApplicationEntity();         
		applicationEntity.setOrganizationEntity(organizationEntity);
		applicationEntity.setIconEntity(iconEntity);
		applicationEntity.setName("App");
		applicationEntity.setCode("app");
		applicationEntity.setStatus(Constants.ENABLE_STATUS);
		applicationEntity.setType(Constants.SYSTEM_TYPE);
		applicationEntity.setUrl("/appController/deleteApplicaion");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		FunctionEntity functionEntity = new FunctionEntity();
		functionEntity.setApplicationEntity(applicationEntity);
		functionEntity.setIconEntity(iconEntity);
		functionEntity.setName("Function");
		functionEntity.setCode("fc");
		functionEntity.setUrl("/functionController/insertFunction");
		functionEntity.setStatus(Constants.ENABLE_STATUS);
		
		ajaxJson = this.functionService.insertFunctionEntity(functionEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.functionService.deleteFunctionEntity(functionEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testQueryFunctionList(){
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setName("Org");
		organizationEntity.setCode("org");
		
		Boolean success = this.organizationService.insertEntity(organizationEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		IconEntity iconEntity = new IconEntity();
		iconEntity.setName("Icon");
		iconEntity.setType(Constants.DICT_GROUP_APP_TYPE);
		iconEntity.setIconPath("/path/imag.jpg");
		iconEntity.setMediumIconPath("/path/imag.jpg");
		iconEntity.setBigIconPath("/path/imag.jpg");
		iconEntity.setDescription("description");
		
		success = this.iconService.insertEntity(iconEntity);
		Assert.assertThat(success, CoreMatchers.equalTo(true));
		
		ApplicationEntity applicationEntity = new ApplicationEntity();         
		applicationEntity.setOrganizationEntity(organizationEntity);
		applicationEntity.setIconEntity(iconEntity);
		applicationEntity.setName("App");
		applicationEntity.setCode("app");
		applicationEntity.setStatus(Constants.ENABLE_STATUS);
		applicationEntity.setType(Constants.SYSTEM_TYPE);
		applicationEntity.setUrl("/appController/applicaionList");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		FunctionEntity functionEntity = new FunctionEntity();
		functionEntity.setApplicationEntity(applicationEntity);
		functionEntity.setIconEntity(iconEntity);
		functionEntity.setName("Function");
		functionEntity.setCode("fc");
		functionEntity.setUrl("/functionController/FunctionList");
		functionEntity.setStatus(Constants.ENABLE_STATUS);
		
		ajaxJson = this.functionService.insertFunctionEntity(functionEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		Map<String, Object> functionMap = this.functionService.getFunctionEntityQuiGrid(functionEntity, 1, 10, null, null);
		Assert.assertThat(3, CoreMatchers.equalTo(functionMap.size()));
		
		List<Map<String,Object>> result = (List<Map<String, Object>>) functionMap.get("rows");
		Assert.assertThat(0, CoreMatchers.equalTo(result.size()));
	}
	
}
