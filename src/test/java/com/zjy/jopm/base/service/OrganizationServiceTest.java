package com.zjy.jopm.base.service;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.org.service.OrganizationService;
/**
 * 
 * @ClassName: OrganizationServiceTest 
 * @Description: 机构测试用例
 * @author xuanx
 * @date 2016年5月12日 上午11:48:43 
 * @since JDK 1.6
 */
public class OrganizationServiceTest extends AbstractTestCase{
	
	@Autowired
	private OrganizationService organizationService;
	
	@Test
	public void OrganizationServiceTreeTest(){
		SessionInfo sessionInfo = new SessionInfo();
		//登陆用户身份标识
		sessionInfo.setIdentity(0);//0普1 特
		sessionInfo.setOrganizationId("1111");
		OrganizationEntity organizationEntity=new OrganizationEntity();
		AjaxJson node=organizationService.OrganizationServiceTree(organizationEntity, sessionInfo);
		Assert.assertNotNull("查询",node);
	}
	
	@Test
	public void saveOrUpdateTest(){
		OrganizationEntity organizationEntity=saveObject();
		organizationEntity.setName("子机构");
		organizationEntity.setCode("0000000zijigou");
		organizationEntity.setRange(1);
		organizationEntity.setOrgarizationId("");
		AjaxJson ajaxjson=organizationService.saveOrUpdateOrganization(organizationEntity);
		Assert.assertNotNull("操作",ajaxjson);
	}
	
	@Test
	public void delOrganizationEntityTest(){
		OrganizationEntity organizationEntity=saveObject();
		String id=organizationEntity.getId();
		AjaxJson ajaxjson=organizationService.delOrganizationEntity(id);
		Assert.assertNotNull("操作",ajaxjson);
	}
	
	
	
	
	private  OrganizationEntity saveObject(){
		OrganizationEntity organizationEntity=new OrganizationEntity();
		organizationEntity.setName("机构");
		organizationEntity.setCode("0000000zijigou");
		organizationEntity.setRange(1);
		organizationService.insertEntity(organizationEntity);
		return organizationEntity;
	}

}
