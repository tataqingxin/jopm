/** 
 * @Description:[日志接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.log.service.impl.LogServiceImpl.java
 * @ClassName:LogServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:26:23
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午3:26:23  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.log.entity.LogEntity;
import com.zjy.jopm.base.log.service.LogService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: LogServiceImpl
 * @Description: [日志接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:26:23
 * @since JDK 1.6
 */
@Service("logService")
@Transactional
public class LogServiceImpl extends BaseServiceimpl implements LogService {

	/**
	 * @Title: getLogDataGrid
	 * @Description: 获取日志列表
	 * @param logEntity
	 * @param pageNo
	 * @param pageSize
	 * @param order
	 * @return Map<String, Object>
	 * @throws JumpException
	 * @see com.zjy.jopm.base.service.LogService#getLogDataGrid(com.zjy.jopm.base.entity.LogEntity,
	 *      int, int, java.lang.String)
	 */
	@Override
	public Map<String, Object> getLogDataGrid(LogEntity logEntity, int pageNo, int pageSize, String sort,String direction) throws JumpException {
		String hql = "from LogEntity where 1=1 ";
		List<Object> param = new ArrayList<Object>();
		if (StringUtil.isNotEmpty(logEntity.getContent())) {
			hql += " and content like ?";
			param.add("%" + logEntity.getContent() + "%");
		}

		if (StringUtil.isNotEmpty(logEntity.getOperateTime())) {
			String startTime = logEntity.getOperateTime()+" 00:00:00";
			String endTime = logEntity.getOperateTime()+" 23:59:59";
			hql += " and operateTime between ? and ?";
			param.add(startTime);
			param.add(endTime);
		}
		if (StringUtil.isNotEmpty(sort)) {
			hql+=" ORDER BY operateTime ";
			//param.add(sort);
			if(Constants.DESC.equals(direction)){
				hql += SortDirection.desc;
			}else if(Constants.ASC.equals(direction)){
				hql += SortDirection.asc;
			}else{
				hql += SortDirection.desc;
			}
		}
		QueryCondition queryCondition = new QueryCondition(hql, param, pageNo, pageSize);
		PageList pageList = this.queryListByHqlWithPage(queryCondition);
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

}
