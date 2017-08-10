/** 
 * @Description:[字典对外接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.service.ext.DictExtService.java
 * @ClassName:DictExtService
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月13日 下午3:34:24
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月13日 下午3:34:24  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.service.ext;


import java.util.List;
import java.util.Map;

import com.unistc.core.common.service.BaseService;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;

/**
 * @ClassName: DictExtService 
 * @Description: [字典对外接口] 
 * @author Lu Guoqiang
 * @date 2016年5月13日 下午3:34:24 
 * @since JDK 1.6 
 */
public interface DictExtService extends BaseService {
	/**
	 * 
	* @Title: initAllTypeGroups 
	* @Description: 对数据字典进行缓存
	* @param   参数说明 
	* @return void 返回类型 
	* @throws JumpException 异常类型
	 */
	public void initAllDictionaryGroups();
	
	/**
	 * 刷新字典数值缓存
	 * @param type
	 */
	public void refleshDictionarysCach(DictionaryEntity type);
	/**
	 * 刷新字典分组缓存
	 */
	public void refleshDictionaryGroupCach();
	
	/**
	 * 根据分组code，获取数值
	 */
	public List<Map<String, Object>> getTypeList(String groupCode);
	
    

}
