package com.zjy.jopm.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.zjy.jopm.service.open.OpenDictService;
import com.zjy.jopm.service.open.vo.Dictionary;
import com.zjy.jopm.service.open.vo.Role;
import com.zjy.jopm.service.open.vo.User;

public class OpenParamServiceImplTest extends AbstractTestCase {

	@Autowired
	OpenDictService openDictService;
	
	@Test
	public void testgetAllDicList() {
		List<Dictionary> result = openDictService.getAllDicList();
		System.out.println(result.size());
		Assert.assertNotNull(result.size()>=1);
		
	}
}
