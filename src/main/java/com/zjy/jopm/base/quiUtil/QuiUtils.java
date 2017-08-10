package com.zjy.jopm.base.quiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.utils.JumpBeanUtil;

public class QuiUtils {
	
	/**
	 * 组装qui列表数据
	 * @param className 类名称
	* @Title: quiDataGird 
	* @Description: 
	* {"pager.pageNo":1,"pager.totalRows":13,"rows":[{"id":xx","name":"xx","code":"xx"}........]} 
	* @param pageList
	* @param total
	* @return Map<String,Object> 返回类型 
	 */
	public static Map<String, Object> quiDataGird(PageList pageList,int total){
		Map<String, Object> results=new HashMap<String, Object>();
		results.put("pager.pageNo", pageList.getCurPageNO());
		results.put("pager.totalRows", total);
		results.put("rows", ConvertList(pageList));
		return results;
	}
    
	/**
	 * 转换list为[{"id":xx","name":"xx","code":"xx"}........]
	 * @Title: ConvertList 
	 * @Description: [功能描述]
	 * @param pageList
	 * @return
	 */
	private static List<Map<String,Object>> ConvertList(PageList pageList) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
		 List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		 Map<String, Object> map = null;
		 for(int i = 0 ; i < pageList.getResultList().size();i++){
			 map = new HashMap<String, Object>();
			 JumpBeanUtil.copyBean2Map(map, pageList.getResultList().get(i));
			 result.add(map);
		 }
		return result;
	}

}
