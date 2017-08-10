package com.zjy.jopm.base.service;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.UserService;
import com.zjy.jopm.base.user.service.ext.UserExtService;
/**
 * 
 * @ClassName: UserServiceTest 
 * @Description: userService单元测试
 * @author xuanx
 * @date 2016年5月14日 下午5:07:42 
 * @since JDK 1.6
 */
public class UserServiceTest extends AbstractTestCase{
	@Autowired
	private UserService userService;
	
	@Autowired
	private  UserExtService userExtService;
	
	@Test
    public void getUserListQuiGridTest(){
		UserEntity user=userService.expandEntity(UserEntity.class, "1");
		String pageNo="1"; 
		String pageSize="10";
		String sort="desc";
		String direction="id";
		String isInclude="1";//0 不包含 1 包含
		String orgId="";
//		Map<String, Object> map=userService.getUserListQuiGrid(user, pageNo, pageSize, sort, direction, isInclude, orgId);
//		Assert.assertNotNull("查询",map);
	}
	
	@Test
    public void getOrgUserTreeByorgId(){
		String orgId=" ";
		String roleId=" ";
		AjaxJson liset=userExtService.getOrgUserTreeByorgId(orgId, roleId);
		Assert.assertNotNull("查询",liset);
	}
	
	@Test
    public void resetPassWrodTest(){
		String ids=" ";
//		AjaxJson j=userService.resetPassWrod(ids);
//		Assert.assertNotNull("查询",j);
	}
	
	@Test
    public void userTest(){
		String id="";
		List<String> ids=userExtService.getDepartIdsByUser(id);
		Assert.assertNotNull("查询",ids);
	}
}
