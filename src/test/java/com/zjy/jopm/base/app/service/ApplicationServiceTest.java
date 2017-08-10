/** 
 * @Description:[应用功能单元测试]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.app.service.ApplicationServiceTest.java
 * @ClassName:ApplicationServiceTest
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月18日 下午6:23:52
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月18日 下午6:23:52 
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
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.dict.service.ext.DictExtService;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.IconService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.org.service.OrganizationService;

/**
 * @ClassName: ApplicationServiceTest 
 * @Description: [应用功能单元测试] 
 * @author Lu Guoqiang
 * @date 2016年5月18日 下午6:23:52 
 * @since JDK 1.6 
 */
public class ApplicationServiceTest extends AbstractTestCase {

	@Autowired
	private DictExtService dictExtService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private IconService iconService;
	
	@Test
	public void testInsertApplication(){
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
		applicationEntity.setUrl("/appController/insertApplicaion");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.expandApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.deleteApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testUpdateApplication(){
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
		applicationEntity.setUrl("/appController/updateApplicaion");
		
		AjaxJson ajaxJson = this.applicationService.insertApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		applicationEntity.setName("App1");
		ajaxJson = this.applicationService.updateApplicationEntity(applicationEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.expandApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.deleteApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testExpandApplication(){
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
		
		ajaxJson = this.applicationService.expandApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.deleteApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testDeleteApplication(){
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
		
		ajaxJson = this.applicationService.expandApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.deleteApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testQueryApplicationList(){
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
		iconEntity.setBigIconPath("/path/imag.jpg");;
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
		
		this.dictExtService.initAllDictionaryGroups();// 初始化字典
		
		Map<String, Object> applicationMap = this.applicationService.getApplicationEntityQuiGrid(applicationEntity, 1, 10, null, null);
		Assert.assertThat(3, CoreMatchers.equalTo(applicationMap.size()));
		
		List<Map<String,Object>> result = (List<Map<String, Object>>) applicationMap.get("rows");
		Assert.assertThat(1, CoreMatchers.equalTo(result.size()));
	}
	
	@Test
	public void testEnableApplicationStatus(){
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
		
		this.dictExtService.initAllDictionaryGroups();// 初始化字典
		
		ajaxJson = this.applicationService.enableApplicationStatus(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.applicationService.deleteApplicationEntity(applicationEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
}
