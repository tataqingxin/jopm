/** 
 * @Description:[字典分组管理接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.service.impl.DictionaryGroupServiceImpl.java
 * @ClassName:DictionaryGroupServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:24:19
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:24:19  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.dict.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.JumpBeanUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.base.dict.service.DictionaryGroupService;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: DictionaryGroupServiceImpl
 * @Description: [字典分组管理接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:24:19
 * @since JDK 1.6
 */
@Service("DictionaryGroupService")
@Transactional
public class DictionaryGroupServiceImpl extends BaseServiceimpl implements DictionaryGroupService {
	
	@Autowired
	private LogExtService logExtService;
	
	/**
	 * @Title: getDctionaryGroupQuiGrid
	 * @Description: 字典分组数据查询
	 * @param @param dictionaryGroupEntity
	 * @param @param pageNo
	 * @param @param pageSize
	 * @param @param order
	 * @param @return 参数说明
	 * @return Map<String,Object> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public Map<String, Object> getDctionaryGroupQuiGrid(
			DictionaryGroupEntity dictionaryGroupEntity, String pageNo,
			String pageSize, String order) throws JumpException {
		Map<String, Object> results = new HashMap<String, Object>();
		String hql = "from DictionaryGroupEntity  where 1=1";
		int pNO = Integer.valueOf(pageNo);
		int pSize = Integer.valueOf(pageSize);
		List<Object> param = new ArrayList<Object>();
		if (StringUtil.isNotEmpty(dictionaryGroupEntity.getName())) {
			hql += " and name like ?";
			param.add("%" + dictionaryGroupEntity.getName() + "%");
		}

		if (StringUtil.isNotEmpty(dictionaryGroupEntity.getCode())) {
			hql += " and code like ?";
			param.add("%" + dictionaryGroupEntity.getCode() + "%");
		}
		QueryCondition queryCondition = new QueryCondition(hql, param, pNO,
				pSize);
		PageList maps = this.queryListByHqlWithPage(queryCondition);
		//组装数据
		results = QuiUtils.quiDataGird(maps, maps.getCount());
		return results;
	}

	/**
	 * @Title: saveOrUpdateDctionaryGroup
	 * @Description: 保存 更新字典分组
	 * @param @param dictionaryGroupEntity
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public boolean saveOrUpdateDctionaryGroup(
			DictionaryGroupEntity dictionaryGroupEntity) {
		boolean flag = false;
		String logContent="";
		AjaxJson ajaxJson = new AjaxJson();
		if (StringUtil.isNotEmpty(dictionaryGroupEntity.getId())) {
			DictionaryGroupEntity dic=this.expandEntity(DictionaryGroupEntity.class, dictionaryGroupEntity.getId());
			JumpBeanUtil.copyBeanNotNull2Bean(dictionaryGroupEntity, dic);
			flag = this.updateEntity(dic);
			logContent = "更新字典分组：["+dictionaryGroupEntity.getName()+"]," + ajaxJson.getMessage();
		} else {
			logContent = "保存字典分组：["+dictionaryGroupEntity.getName()+"]," + ajaxJson.getMessage();
			flag = this.insertEntity(dictionaryGroupEntity);
		}
		//记录日志
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
				
		
		return flag;
	}
	
	
	/**
	* 
	* @Title: delDctionaryGroup 
	* @Description: 删除字典分组
	* @param @param groupId
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	public AjaxJson delDctionaryGroup(String groupId){
		//判断分组id是否为空
		AjaxJson j = new AjaxJson();
		if(StringUtil.isNotEmpty(groupId)){
			//判断分组下 是否有数值
			List<DictionaryEntity> dictionaryEntitys=this.queryListByProperty(DictionaryEntity.class, "dictionaryGroupEntity.id", groupId);
			if(dictionaryEntitys.size()>0){
				j.setSuccess(false);
				j.setMessage("分组下有数据！不允许删除！");
			}else{
				DictionaryGroupEntity dictionaryGroup=this.expandEntity(DictionaryGroupEntity.class, groupId);
				if(dictionaryGroup!=null){
					String name=dictionaryGroup.getName();
					boolean flag=this.deleteEntity(dictionaryGroup);
					if(flag){
						j.setSuccess(true);
						j.setMessage("["+name+"]分组删除成功！");
						//记录日志
						String logContent = "删除字典分组["+name+"]," + j.getMessage();
						this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
					}else{
						j.setSuccess(false);
						j.setMessage("["+name+"]分组删除失败！");
					}
				}else{
					j.setSuccess(false);
					j.setMessage("没有找到对象！");
				}
			} 
			//删除
		}else{
			j.setSuccess(false);
			j.setMessage("没有找到对象！");
		}
		return j;
	}
	
	
	/**
	 * 根据分组id获取实体
	* @Title: dictionaryGroup 
	* @Description: [功能描述]
	* @param @param groupId
	* @param @return  参数说明 
	* @return Map<String,Object> 返回类型 
	* @throws JumpException 异常类型
	 */
	public DictionaryGroupEntity dictionaryGroup(String groupId){
		DictionaryGroupEntity dictionary=this.expandEntity(DictionaryGroupEntity.class, groupId);
		return dictionary;
	}

	/**
	 * 
	 * @Title: checkDctionaryGroupForCode
	 * @Description: 根据code 判断是否重复
	 * @param code
	 * @return 
	 * @see com.zjy.jopm.base.dict.service.DictionaryGroupService#checkDctionaryGroupForCode(java.lang.String)
	 */
	public AjaxJson checkDctionaryGroupForCode(String code) {
		 AjaxJson j = new AjaxJson();
		 if(StringUtil.isNotEmpty(code)){
			  List<DictionaryGroupEntity> typeGroup = this.queryListByProperty(DictionaryGroupEntity.class, "code", code);
			  if(typeGroup.size()>0){
					j.setSuccess(false);
					j.setMessage("分组已存在！");
				}
		 }else{
			 j.setSuccess(false);
			 j.setMessage("请填写编码值!");
		 }
		return j;
	}

}
