/** 
 * @Description:[字典数值接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.dict.service.impl.DictionaryServiceImpl.java
 * @ClassName:DictionaryServiceImpl
 * @Author:Lu Guoqiang  
 * @CreateDate:2016年5月9日 下午3:24:52
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:24:52  
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
import com.zjy.jopm.base.dict.service.DictionaryService;
import com.zjy.jopm.base.dict.service.ext.DictExtService;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: DictionaryServiceImpl
 * @Description: [字典数值接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:24:52
 * @since JDK 1.6
 */
@Service("dictionaryService")
@Transactional
public class DictionaryServiceImpl extends BaseServiceimpl implements
		DictionaryService {

	@Autowired
	private DictExtService dictExtService;
	
	@Autowired
	private LogExtService logExtService;

	/**
	 * @Title: getDictionaryList
	 * @Description: [根据分组code 来获取数值
	 * @param @param code
	 * @param @return 参数说明
	 * @return Map<String,Object> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public List<Map<String, Object>> getDictionaryList(String code) {
		//获取分组
		DictionaryGroupEntity dic = this.expandEntityByProperty(
				DictionaryGroupEntity.class, "code", code);
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		if (dic != null) {
			List<DictionaryEntity> dictionaryEntityList = this
					.queryListByProperty(DictionaryEntity.class,
							"dictionaryGroupEntity.id", dic.getId());
			for (DictionaryEntity dics : dictionaryEntityList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("key", dics.getCode());
				map.put("value", dics.getName());
				maps.add(map);
			}
		}
		return maps;
	}

	/**
	 * 根据字典分组code来获取数值
	 * 
	 * @Title: getDictionaryQuiGrid
	 * @Description: [功能描述]
	 * @param @param dictionaryEntity
	 * @param @param pageNo
	 * @param @param pageSize
	 * @param @param order
	 * @param @param groupCode
	 * @param @return 参数说明
	 * @return Map<String,Object> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public Map<String, Object> getDictionaryQuiGrid(
			DictionaryEntity dictionaryEntity, String pageNo, String pageSize,
			String order, String groupId) {
		Map<String, Object> results = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(groupId)) {
			DictionaryGroupEntity dic = this.expandEntity(
					DictionaryGroupEntity.class, groupId);
			if (dic != null) {
				List<Object> param = new ArrayList<Object>();
				String hql = "from DictionaryEntity  where 1=1 and dictionaryGroupEntity.id=?";
				param.add(dic.getId());
				int pNO = Integer.valueOf(pageNo);
				int pSize = Integer.valueOf(pageSize);
				if (StringUtil.isNotEmpty(dictionaryEntity.getName())) {
					hql += " and name like ?";
					param.add("%" + dictionaryEntity.getName() + "%");
				}

				if (StringUtil.isNotEmpty(dictionaryEntity.getCode())) {
					hql += " and code like ?";
					param.add("%" + dictionaryEntity.getCode() + "%");
				}
				QueryCondition queryCondition = new QueryCondition(hql, param,
						pNO, pSize);
				PageList maps = this.queryListByHqlWithPage(queryCondition);
				//组装数据
				results = QuiUtils.quiDataGird(maps, maps.getCount());
			}
		} else {
			results.put("pager.pageNo", 1);
			results.put("pager.totalRows", 0);
			List<Map<String, Object>> listRows = new ArrayList<Map<String, Object>>();
			results.put("rows", listRows);
		}
		return results;
	}

	/**
	 * @Title: dictionary
	 * @Description: 根据数值id获取实体
	 * @param @param id
	 * @param @return 参数说明
	 * @return DictionaryEntity 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public DictionaryEntity dictionary(String id) {
		DictionaryEntity dictionary = this.expandEntity(DictionaryEntity.class,
				id);
		return dictionary;
	}

	/**
	 * @Title: saveOrUpdateDctionaryGroup
	 * @Description: 保存 编辑数值
	 * @param @param dictionaryEntity
	 * @param @param groupId 分组id
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson saveOrUpdateDctionary(DictionaryEntity dictionaryEntity,
			String groupId) {
		AjaxJson j = new AjaxJson();
		boolean flag = false;
		String logContent="";
		if (StringUtil.isNotEmpty(groupId)) {
			DictionaryGroupEntity dic = this.expandEntity(
					DictionaryGroupEntity.class, groupId);//获取分组
			if (dic != null) {
				dictionaryEntity.setDictionaryGroupEntity(dic);
			}
			if (StringUtil.isNotEmpty(dictionaryEntity.getId())) {
				DictionaryEntity de = this.expandEntity(DictionaryEntity.class,
						dictionaryEntity.getId());
				if (StringUtil.isNotEmpty(dictionaryEntity.getCode())
						&& !de.getCode().equals(dictionaryEntity.getCode())) {
					String hql="from DictionaryEntity where dictionaryGroupEntity.id=? and code=?";
					List<Object> param = new ArrayList<Object>();
					param.add(dictionaryEntity.getDictionaryGroupEntity().getId());
					param.add(dictionaryEntity.getCode());
					List<DictionaryEntity> dics = this.queryListByHql(hql, param);
					if (dics.size() > 0) {
						j.setSuccess(false);
						j.setMessage("数值编码已存在！");
						return j;
					}
				}
				if (StringUtil.isNotEmpty(dictionaryEntity.getName())
						&& !de.getName().equals(dictionaryEntity.getName())) {
					String hql="from DictionaryEntity where dictionaryGroupEntity.id=? and name=?";
					List<Object> param = new ArrayList<Object>();
					param.add(dictionaryEntity.getDictionaryGroupEntity().getId());
					param.add(dictionaryEntity.getName());
					List<DictionaryEntity> dics = this.queryListByHql(hql, param);
					if (dics.size() > 0) {
						j.setSuccess(false);
						j.setMessage("参数名称已存在！");
						return j;
					}
				}
				JumpBeanUtil.copyBeanNotNull2Bean(dictionaryEntity, de);
				flag = this.updateEntity(de);
				logContent = "更新字典数值：["+dictionaryEntity.getName()+"]," + j.getMessage();
				//刷新缓存
				dictExtService.refleshDictionarysCach(dictionaryEntity);
			} else {
				List<DictionaryEntity> dictionary = this.queryListByProperty(
						DictionaryEntity.class, "dictionaryGroupEntity.id",
						groupId);
				boolean isRepeat = false;
				String message="";
				for (DictionaryEntity dicEntity : dictionary) {
					if (dicEntity.getCode().equals(dictionaryEntity.getCode())) {//当数值已经有相同编码 不允许保存
						isRepeat = true;
						message="数值编码已存在！";
						break;
					}
					if (dicEntity.getName().equals(dictionaryEntity.getName())) {//当数值已经有相同编码 不允许保存
						isRepeat = true;
						message="数值名称已存在！";
						break;
					}
				}
				if (isRepeat) {
					j.setSuccess(false);
					j.setMessage(message);
					return j;
				}
				flag = this.insertEntity(dictionaryEntity);
				logContent = "保存字典数值：["+dictionaryEntity.getName()+"]," + j.getMessage();
				//刷新缓存
				dictExtService.refleshDictionarysCach(dictionaryEntity);
			}
		} else {
			j.setSuccess(false);
			j.setMessage("没有找到对象！请选择分组数据");
		}
		if (flag) {
			j.setSuccess(true);
			j.setMessage("操作成功！");
			//加日志
		} else {
			j.setSuccess(false);
			j.setMessage("操作失败！");
			//加日志
		}
		//记录日志
		this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
						
		return j;
	}

	/**
	 * @Title: delDctionary
	 * @Description: 删除数值
	 * @param @param id
	 * @param @return 参数说明
	 * @return AjaxJson 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public AjaxJson delDctionary(String id) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(id)) {
			DictionaryEntity dictionary = this.expandEntity(
					DictionaryEntity.class, id);
			if (dictionary != null) {
				String name = dictionary.getName();
				boolean flag = this.deleteEntity(dictionary);
				//加日志
				if (flag) {
					j.setSuccess(true);
					j.setMessage("[" + name + "]数值删除成功！");
					//刷新缓存
					dictExtService.refleshDictionarysCach(dictionary);
					//记录日志
					String logContent = "删除字典数值["+name+"]," + j.getMessage();
					this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
				} else {
					j.setSuccess(false);
					j.setMessage("[" + name + "]数值删除失败！");
				}
			} else {
				j.setSuccess(false);
				j.setMessage("没有找到对象！");
			}
		} else {
			j.setSuccess(false);
			j.setMessage("没有找到对象！");
		}
		return j;
	}
}
