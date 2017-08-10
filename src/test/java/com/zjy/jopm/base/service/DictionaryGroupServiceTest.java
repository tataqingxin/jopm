package com.zjy.jopm.base.service;


import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;














import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.dict.service.DictionaryGroupService;
import com.zjy.jopm.base.dict.service.DictionaryService;


/**
 * 字典测试
 * @ClassName: DictionaryGroupServiceTest 
 * @Description: [功能描述] 
 * @author xuanx
 * @date 2016年5月9日 下午5:46:32 
 * @since JDK 1.6
 */
public class DictionaryGroupServiceTest extends AbstractTestCase{
	
	@Autowired
	DictionaryGroupService dictionaryGroupService;
	@Autowired
	private DictionaryService dictionaryService;
	
	@Test
	public void getDctionaryGroupQuiGridTest(){
		DictionaryGroupEntity dic=new DictionaryGroupEntity();
		dic.setName("aa");
		Map<String, Object> map=dictionaryGroupService.getDctionaryGroupQuiGrid(dic, "1", "10", "");
		Assert.assertNotNull("查询", map);//断言
	}
	
	
	@Test
	public void saveOrUpdateDctionaryGroupForSaveTest(){
		DictionaryGroupEntity dic=this.saveObjct();
		boolean j =  dictionaryGroupService.saveOrUpdateDctionaryGroup(dic);
		Assert.assertNotNull("操作", j);
	}
	
	@Test
	public void saveOrUpdateDctionaryGroupForUpdateTest(){
		DictionaryGroupEntity dic=this.saveObjct();
		dic.setName("555555");
		boolean j =  dictionaryGroupService.saveOrUpdateDctionaryGroup(dic);
		Assert.assertNotNull("操作", j);
	}
	
	@Test
	public void dictionaryGroupTest(){
		DictionaryGroupEntity dic=dictionaryGroupService.
				 expandEntityByProperty(DictionaryGroupEntity.class, "code", "00012");
		if(dic!=null){
			DictionaryGroupEntity map =  dictionaryGroupService.dictionaryGroup(dic.getId());
			Assert.assertNotNull("操作", map); 
		}
	}
	
	@Test
	public void getDictionaryListTest(){
		DictionaryGroupEntity dic=dictionaryGroupService.
				 expandEntityByProperty(DictionaryGroupEntity.class, "code", "00012");
		if(dic!=null){
			List<Map<String, Object>> j = dictionaryService.getDictionaryList(dic.getCode());
			Assert.assertNotNull("操作", j);
		}
	}
	
	@Test
	public void delDctionaryGroupTest(){
		DictionaryGroupEntity dic=dictionaryGroupService.
				 expandEntityByProperty(DictionaryGroupEntity.class, "code", "00012");
		if(dic!=null){
			AjaxJson j =  dictionaryGroupService.delDctionaryGroup(dic.getId());
			Assert.assertNotNull("操作", j);
		}
	}
	
	
	
	public DictionaryGroupEntity saveObjct(){
		DictionaryGroupEntity dic=new DictionaryGroupEntity();
		dic.setName("test1");
		dic.setCode("00012");
		return dic;
	}
}
