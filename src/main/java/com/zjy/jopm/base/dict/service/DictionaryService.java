/** 
 * @Description:[字典数值管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.service.DictionaryService.java
 * @ClassName:DictionaryService
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:11:13
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月9日 下午3:11:13  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.service;

import java.util.List;
import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;

/**
 * @ClassName: DictionaryService 
 * @Description: [字典数值管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:11:13 
 * @since JDK 1.6 
 */
public interface DictionaryService extends BaseService {
    /**
     * 
    * @Title: getDictionaryList 
    * @Description: [根据分组code 来获取数值
    * @param @param code
    * @param @return  参数说明 
    * @return Map<String,Object> 返回类型 
    * @throws JumpException 异常类型
     */
	List<Map<String, Object>> getDictionaryList(String code);
     
	/**
	 * 根据字典分组code来获取数值
	* @Title: getDictionaryQuiGrid 
	* @Description: [功能描述]
	* @param @param dictionaryEntity
	* @param @param pageNo
	* @param @param pageSize
	* @param @param order
	* @param @param groupCode 
	* @param @return  参数说明 
	* @return Map<String,Object> 返回类型 
	* @throws JumpException 异常类型
	 */
	Map<String, Object> getDictionaryQuiGrid(DictionaryEntity dictionaryEntity,
			String pageNo, String pageSize, String order,String groupId);
    /**
     * 
    * @Title: dictionary 
    * @Description: 根据数值id获取实体
    * @param @param id
    * @param @return  参数说明 
    * @return DictionaryEntity 返回类型 
    * @throws JumpException 异常类型
     */
	DictionaryEntity dictionary(String id);
    /**
     * 
    * @Title: saveOrUpdateDctionaryGroup 
    * @Description: 保存 编辑数值 
    * @param @param dictionaryEntity
    * @param @param groupId 分组id
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson saveOrUpdateDctionary(DictionaryEntity dictionaryEntity,
			String groupId);
    /**
     * 
    * @Title: delDctionary 
    * @Description: 删除数值
    * @param @param id
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	AjaxJson delDctionary(String id);

}
