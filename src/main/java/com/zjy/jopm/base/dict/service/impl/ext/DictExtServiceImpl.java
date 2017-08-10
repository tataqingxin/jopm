/** 
 * @Description:[字典对外接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.service.ext.DictExtServiceImpl.java
 * @ClassName:DictExtServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月13日 下午3:35:20
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月13日 下午3:35:20  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.dict.service.impl.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.dict.service.ext.DictExtService;

/**
 * @ClassName: DictExtServiceImpl 
 * @Description: [字典对外接口] 
 * @author Lu Guoqiang
 * @date 2016年5月13日 下午3:35:20 
 * @since JDK 1.6 
 */
@Service("dictExtService")
@Transactional
public class DictExtServiceImpl extends BaseServiceimpl implements DictExtService {
	/**
	 * 
	* @Title: initAllTypeGroups 
	* @Description: 对数据字典进行缓存
	* @param   参数说明 
	* @return void 返回类型 
	* @throws JumpException 异常类型
	 */
	public void initAllDictionaryGroups(){
	   List<DictionaryGroupEntity> typeGroups = this.queryListByClass(DictionaryGroupEntity.class);
        for (DictionaryGroupEntity dictionaryGroupEntity : typeGroups) {
        	DictionaryGroupEntity.allTypeGroups.put(dictionaryGroupEntity.getCode(), dictionaryGroupEntity);
            List<DictionaryEntity> types = this.queryListByProperty(DictionaryEntity.class, "dictionaryGroupEntity.id", dictionaryGroupEntity.getId());
            DictionaryGroupEntity.allTypes.put(dictionaryGroupEntity.getCode(), types);
        }
	}
	
	/**
	 * 刷新字典数值缓存
	 * @Title: refleshDictionarysCach
	 * @Description: [功能描述]
	 * @param type 
	 * @see com.zjy.jopm.base.dict.service.ext.DictExtService#refleshDictionarysCach(com.zjy.jopm.base.dict.entity.DictionaryEntity)
	 */
	public void refleshDictionarysCach(DictionaryEntity type){
		 DictionaryGroupEntity dictionaryGroupEntity = type.getDictionaryGroupEntity();
	     DictionaryGroupEntity typeGroupEntity = this.expandEntity(DictionaryGroupEntity.class, dictionaryGroupEntity.getId());
	     List<DictionaryEntity> types = this.queryListByProperty(DictionaryEntity.class, "dictionaryGroupEntity.id", dictionaryGroupEntity.getId());
	     DictionaryGroupEntity.allTypes.put(typeGroupEntity.getCode(), types);
	}
	/**
	 * 刷新字典分组缓存
	 * @Title: refleshDictionaryGroupCach
	 * @Description: [功能描述] 
	 * @see com.zjy.jopm.base.dict.service.ext.DictExtService#refleshDictionaryGroupCach()
	 */
	public void refleshDictionaryGroupCach(){
		DictionaryGroupEntity.allTypeGroups.clear();
         List<DictionaryGroupEntity> typeGroups = this.queryListByClass(DictionaryGroupEntity.class);
        for (DictionaryGroupEntity typeGroup : typeGroups) {
        	DictionaryGroupEntity.allTypeGroups.put(typeGroup.getCode(), typeGroup);
        }
	}
	
	/**
	 * 根据分组code，获取数值
	 */
	public List<Map<String, Object>> getTypeList(String groupCode){
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		if(StringUtil.isNotEmpty(groupCode)){
			SessionInfo sessionInfo = (SessionInfo)ContextHolderUtil.getSession().getAttribute(Globals.USER_SESSION);
			List<DictionaryEntity> dictionaryEntityList = DictionaryGroupEntity.allTypes.get(groupCode);
			if(Constants.NORMAL_ACCOUNT == sessionInfo.getIdentity()){
				for (DictionaryEntity dictionaryEntity : dictionaryEntityList) {
					Map<String, Object> results = new HashMap<String, Object>();
					if (Constants.DICT_GROUP_APP_TYPE.equals(groupCode) || Constants.DICT_GROUP_ROLE_TYPE.equals(groupCode)) {
						if(!"1".equals(dictionaryEntity.getCode())){
							results.put("key", dictionaryEntity.getName());
							results.put("value", dictionaryEntity.getCode());
							list.add(results);
						}
					}else{
						results.put("key", dictionaryEntity.getName());
						results.put("value", dictionaryEntity.getCode());
						list.add(results);
					}
				}
				return list;
			} else {
				for(DictionaryEntity dictionaryEntity:dictionaryEntityList){
					Map<String, Object> results = new HashMap<String, Object>();
					results.put("key", dictionaryEntity.getName());
					results.put("value", dictionaryEntity.getCode());
					list.add(results);
				}
			}
		}
		return list;
	}
}
