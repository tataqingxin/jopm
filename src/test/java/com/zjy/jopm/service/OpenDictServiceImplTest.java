package com.zjy.jopm.service;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.zjy.jopm.service.open.OpenParamService;

public class OpenDictServiceImplTest extends AbstractTestCase {

	@Autowired
	OpenParamService openParamService;
	
	@Test
	public void testgetValue() {
		String key = "test";
		String result = openParamService.getValue(key);
		System.out.println(result);
		Assert.assertNotNull(result);
		
	}
}
