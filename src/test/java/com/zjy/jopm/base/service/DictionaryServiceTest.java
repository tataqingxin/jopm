package com.zjy.jopm.base.service;


import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractTestCase;

import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.service.DictionaryService;


/**
 * 字典测试
 * @ClassName: DictionaryServiceTest 
 * @Description: [功能描述] 
 * @author xuanx
 * @date 2016年5月9日 下午5:46:32 
 * @since JDK 1.6
 */
public class DictionaryServiceTest extends AbstractTestCase{
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Test
	public void getDctionaryQuiGridTest(){
		DictionaryEntity dic=new DictionaryEntity();
		dic.setName("aa"); 
		Map<String, Object> map=dictionaryService.getDictionaryQuiGrid(dic, "1", "10", "","111");
		Assert.assertNotNull("查询", map);//断言
	}
	
	
	@Test
	public void saveOrUpdateDctionaryForSaveTest(){
		DictionaryEntity dic=this.saveObjct();
		AjaxJson j =  dictionaryService.saveOrUpdateDctionary(dic,"");
		Assert.assertNotNull("操作", j);
	}
	
	@Test
	public void saveOrUpdateDctionaryForUpdateTest(){
		DictionaryEntity dic=this.saveObjct();
		dic.setName("555555");
		AjaxJson j =  dictionaryService.saveOrUpdateDctionary(dic,"");
		Assert.assertNotNull("操作", j);
	}
	
	@Test
	public void dictionaryTest(){
		DictionaryEntity dic=dictionaryService.
				 expandEntityByProperty(DictionaryEntity.class, "code", "00012");
		if(dic!=null){
			DictionaryEntity map =  dictionaryService.dictionary(dic.getId());
			Assert.assertNotNull("操作", map); 
		}
	}
	
	@Test
	public void delDctionaryTest(){
		DictionaryEntity dic=dictionaryService.
				 expandEntityByProperty(DictionaryEntity.class, "code", "00012");
		if(dic!=null){
			AjaxJson j =  dictionaryService.delDctionary(dic.getId());
			Assert.assertNotNull("操作", j);
		}
	}
	
	public DictionaryEntity saveObjct(){
		DictionaryEntity dic=new DictionaryEntity();
		dic.setName("test1");
		dic.setCode("00012");
		return dic;
	}
}
