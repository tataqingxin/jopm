package com.zjy.jopm.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.zjy.jopm.service.open.OpenUserService;
import com.zjy.jopm.service.open.vo.Role;
import com.zjy.jopm.service.open.vo.User;

public class OpenUserServiceImplTest extends AbstractTestCase {

	@Autowired
	OpenUserService openUserService;

	@Test
	public void testgetUserInfo() {
		String userName = "admin";
		User result = openUserService.getUserInfo(userName);
		System.out.println(result.getUserName());
		Assert.assertNotNull(result.getUserName());
		
	}
	
	@Test
	public void testgetUserRoles() {
		String userName = "admin";
		List<Role> result = openUserService.getUserRoles(userName);
		System.out.println(result.get(0).getName());
		Assert.assertTrue(result.size()>=1);
		
	}
	
	@Test
	public void testgetUserList() {
		String param = "500106000000";
		List<User> result = openUserService.getUserList(param);
		Assert.assertTrue(result.size()>=1);
		
	}
}
