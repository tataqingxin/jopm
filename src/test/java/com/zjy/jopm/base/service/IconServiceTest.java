package com.zjy.jopm.base.service;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.zjy.jopm.base.dict.service.ext.DictExtService;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.IconService;
import com.zjy.jopm.base.icon.service.ext.IconExtService;

/**
 * @ClassName: IconServiceTest
 * @Description: [图标测试Junit]
 * @author Sunset
 * @date 2016年5月18日 下午5:38:15
 * @since JDK 1.6
 */
public class IconServiceTest extends AbstractTestCase {
	@Autowired
	private IconService iconService;
	
	@Autowired
	private DictExtService dictExtService;
	
	@Autowired
	private IconExtService iconExtService;
	
	/**
	 * 
	 * @Title: insertIconTest 
	 * @Description: [新增图标]
	 */
	@Test
	public void insertIconTest(){
		dictExtService.initAllDictionaryGroups();
		IconEntity icon = new IconEntity();
		icon.setName("1");
		icon.setMediumIconPath("2");
		icon.setBigIconPath("3");
		icon.setIconPath("1");
		icon.setType("girl");
		iconService.insertEntity(icon);
		IconEntity i = new IconEntity();
		i.setName("1");
		Map<String, Object> map =iconService.getIconDataGrid(i, 1,10, "","desc");
		Assert.assertNotNull("查询", map);//断言
	}
	
	/**
	 * 
	 * @Title: updateIconTest 
	 * @Description: [修改图标]
	 */
	@Test
	public void updateIconTest(){
		IconEntity icon = new IconEntity();
		icon.setName("1");
		icon.setIconPath("1");
		icon.setMediumIconPath("2");
		icon.setBigIconPath("3");
		icon.setType("girl");
		iconService.insertEntity(icon);
		IconEntity iconEntity = iconService.expandEntityByProperty(IconEntity.class, "name", icon.getName());
		Assert.assertNotNull("查询", iconEntity);//断言
	}
	
	
	
}
