package com.zjy.jopm.base.service;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.init.service.InitService;
/**
 * 
 * @ClassName: InitServiceTest 
 * @Description: InitService单元测试
 * @author xuanx
 * @date 2016年5月14日 下午5:07:42 
 * @since JDK 1.6
 */
public class InitDataServiceTest extends AbstractTestCase{
	@Autowired
	private InitService initService;
	
	
	@Test
    public void InitDataInfoTest(){
		String orgName="紫金云";
		String orgCode="zjy";
		String userName="admin";
		String passWord="123456";
//		AjaxJson j=initService.InitDataInfo(orgName, orgCode, userName, passWord);
//		Assert.assertNotNull("查询",j);
	}
}
