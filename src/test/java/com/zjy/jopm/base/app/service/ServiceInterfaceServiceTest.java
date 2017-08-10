/** 
 * @Description:[服务接口单元测试]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.app.service.ServiceInterfaceServiceTest.java
 * @ClassName:ServiceInterfaceServiceTest
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月18日 下午7:56:21
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月18日 下午7:56:21  
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
import com.zjy.jopm.base.app.entity.ServiceInterfaceEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.IconService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.org.service.OrganizationService;

/**
 * @ClassName: ServiceInterfaceServiceTest 
 * @Description: [服务接口单元测试] 
 * @author Lu Guoqiang 
 * @date 2016年5月18日 下午7:56:21 
 * @since JDK 1.6 
 */
public class ServiceInterfaceServiceTest extends AbstractTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private ServiceInterfaceService serviceInterfaceService;
	@Autowired
	private IconService iconService;
	
	@Test
	public void testInsertServiceInterface(){
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
		
		ServiceInterfaceEntity serviceInterfaceEntity = new ServiceInterfaceEntity();
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		serviceInterfaceEntity.setName("ServiceInterface");
		serviceInterfaceEntity.setCode("si");
		serviceInterfaceEntity.setUrl("/serviceInterfaceController/insertServiceInterface");
		serviceInterfaceEntity.setDescription("description");
		
		ajaxJson = this.serviceInterfaceService.insertServiceInterfaceEntity(serviceInterfaceEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testUpdateServiceInterface(){
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
		
		ServiceInterfaceEntity serviceInterfaceEntity = new ServiceInterfaceEntity();
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		serviceInterfaceEntity.setName("ServiceInterface");
		serviceInterfaceEntity.setCode("si");
		serviceInterfaceEntity.setUrl("/serviceInterfaceController/updateServiceInterface");
		serviceInterfaceEntity.setDescription("description");
		
		ajaxJson = this.serviceInterfaceService.insertServiceInterfaceEntity(serviceInterfaceEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		serviceInterfaceEntity.setName("ServiceInterface1");
//		ajaxJson = this.serviceInterfaceService.updateServiceInterfaceEntity(serviceInterfaceEntity);
//		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testExpandServiceInterface(){
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
		
		ServiceInterfaceEntity serviceInterfaceEntity = new ServiceInterfaceEntity();
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		serviceInterfaceEntity.setName("ServiceInterface");
		serviceInterfaceEntity.setCode("si");
		serviceInterfaceEntity.setUrl("/serviceInterfaceController/detailServiceInterface");
		serviceInterfaceEntity.setDescription("description");
		
		ajaxJson = this.serviceInterfaceService.insertServiceInterfaceEntity(serviceInterfaceEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.serviceInterfaceService.expandServiceInterfaceEntity(serviceInterfaceEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
	}
	
	@Test
	public void testDeleteServiceInterface(){
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
		
		ServiceInterfaceEntity serviceInterfaceEntity = new ServiceInterfaceEntity();
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		serviceInterfaceEntity.setName("ServiceInterface");
		serviceInterfaceEntity.setCode("si");
		serviceInterfaceEntity.setUrl("/serviceInterfaceController/deleteServiceInterface");
		serviceInterfaceEntity.setDescription("description");
		
		ajaxJson = this.serviceInterfaceService.insertServiceInterfaceEntity(serviceInterfaceEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		ajaxJson = this.serviceInterfaceService.deleteServiceInterfaceEntity(serviceInterfaceEntity.getId());
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testQueryServiceInterfaceList(){
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
		
		ServiceInterfaceEntity serviceInterfaceEntity = new ServiceInterfaceEntity();
		serviceInterfaceEntity.setApplicationEntity(applicationEntity);
		serviceInterfaceEntity.setName("ServiceInterface");
		serviceInterfaceEntity.setCode("si");
		serviceInterfaceEntity.setUrl("/serviceInterfaceController/ServiceInterfaceList");
		serviceInterfaceEntity.setDescription("description");
		
		ajaxJson = this.serviceInterfaceService.insertServiceInterfaceEntity(serviceInterfaceEntity);
		Assert.assertThat(ajaxJson.isSuccess(), CoreMatchers.equalTo(true));
		
		Map<String, Object> serviceInterfaceMap = this.serviceInterfaceService.getServiceInterfaceEntityQuiGrid(serviceInterfaceEntity, 1, 10, null, null);
		Assert.assertThat(3, CoreMatchers.equalTo(serviceInterfaceMap.size()));
		
		List<Map<String,Object>> result = (List<Map<String, Object>>) serviceInterfaceMap.get("rows");
		Assert.assertThat(1, CoreMatchers.equalTo(result.size()));
	}
	
}
