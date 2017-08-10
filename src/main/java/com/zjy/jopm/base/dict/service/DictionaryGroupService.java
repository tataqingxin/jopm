/** 
 * @Description:[字典分组管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.service.DictionaryGroupService.java
 * @ClassName:DictionaryGroupService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:11:34
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午3:11:34  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.service;

import java.util.Map;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;

/**
 * @ClassName: DictionaryGroupService 
 * @Description: [字典管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:11:34 
 * @since JDK 1.6 
 */
public interface DictionaryGroupService extends BaseService {
    /**
     * 
    * @Title: getDctionaryGroupQuiGrid 
    * @Description: 字典分组数据查询
    * @param @param dictionaryGroupEntity
    * @param @param pageNo
    * @param @param pageSize
    * @param @param order
    * @param @return  参数说明 
    * @return Map<String,Object> 返回类型 
    * @throws JumpException 异常类型
     */
	public Map<String, Object> getDctionaryGroupQuiGrid(
			DictionaryGroupEntity dictionaryGroupEntity, String pageNo,
			String pageSize, String order)throws JumpException;
    /**
     * 
    * @Title: saveOrUpdateDctionaryGroup 
    * @Description: 保存 更新字典分组
    * @param @param dictionaryGroupEntity
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	public boolean saveOrUpdateDctionaryGroup(
			DictionaryGroupEntity dictionaryGroupEntity);
	/**
	* 
	* @Title: delDctionaryGroup 
	* @Description: 删除字典分组
	* @param @param groupId
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	public AjaxJson delDctionaryGroup(String groupId);
	/**
	 * 根据分组id获取实体
	* @Title: dictionaryGroup 
	* @Description: [功能描述]
	* @param @param groupId
	* @param @return  参数说明 
	* @return Map<String,Object> 返回类型 
	* @throws JumpException 异常类型
	 */
	public DictionaryGroupEntity dictionaryGroup(String groupId);
	
	public AjaxJson checkDctionaryGroupForCode(String code);

}
