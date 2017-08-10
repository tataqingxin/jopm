package com.zjy.jopm.service;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.zjy.jopm.service.open.OpenSecurityService;

public class OpenSecurityServiceImplTest extends AbstractTestCase {

	@Autowired
	OpenSecurityService openSecurityService;
	
	@Test
	public void testgetAllDicList() {
//		String requestUrl = "http://192.168.10.79:8085/jopm/webpage/base/user/user.html";
		String requestUrl = "http://192.168.10.79:8086/jump-framework-plus-demo/webpage/demo/demoList.html";
		String userName = "admin";
		boolean result = openSecurityService.hasVisitAuth(requestUrl,userName);
		Assert.assertTrue(result);
		
	}
}
