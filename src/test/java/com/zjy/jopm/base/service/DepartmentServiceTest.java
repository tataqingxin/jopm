package com.zjy.jopm.base.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.service.DepartmentService;
import com.zjy.jopm.base.org.service.ext.OrgExtService;
/**
 * 
 * @ClassName: DepartmentServiceTest 
 * @Description: 机构单元测试
 * @author xuanx
 * @date 2016年5月12日 下午6:23:08 
 * @since JDK 1.6
 */
public class DepartmentServiceTest  extends AbstractTestCase{
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private OrgExtService orgExtService;
	
	@Test
	public void departmentServiceTreeTest(){
		DepartmentEntity e=new DepartmentEntity();
		AjaxJson node = departmentService.departmentServiceTree(e, "1",true);
		Assert.assertNotNull("查询",node);
	}
	
	@Test
	public void getDepartmentListByUserIdTest(){
		String id="1";
		List<DepartmentEntity> list=orgExtService.getDepartmentListByUserId(id);
		Assert.assertNotNull("查询",list);
	}

}
