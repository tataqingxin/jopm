package com.zjy.jopm.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.zjy.jopm.service.open.OpenOrgService;
import com.zjy.jopm.service.open.vo.OrgInfo;
import com.zjy.jopm.service.open.vo.Role;
import com.zjy.jopm.service.open.vo.User;

public class OpenOrgServiceImplTest extends AbstractTestCase {

	@Autowired
	OpenOrgService openOrgService;

	@Test
	public void testgetOrgInfo() {
		String orgCode = "500106000000";
		OrgInfo result = openOrgService.getOrgInfo(orgCode);
		System.out.println(result.getName());
		Assert.assertNotNull(result);
		
	}
	
	@Test
	public void testgetParentInfo() {
		String orgCode = "500106000001";
		OrgInfo result = openOrgService.getParentInfo(orgCode);
		System.out.println(result.getName());
		Assert.assertNotNull(result);
		
	}
	
	@Test
	public void testgetChildrenList() {

		String orgCode = "500106000000";
		List<OrgInfo> result = openOrgService.getChildrenList(orgCode);
		System.out.println(result.get(0).getName());
		Assert.assertNotNull(result.size()>=1);
		
	
		
	}
}
