package com.zjy.jopm.base.config.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.config.entity.Config;
import com.zjy.jopm.base.config.service.ConfigService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

@Service("configService")
@Transactional
public class ConfigServiceImpl extends BaseServiceimpl implements
		ConfigService<Config> {

	@Override
	public Map<String, Object> getGrid(Config t, int pageNo, int pageSize,
			String sort, String direction) throws JumpException {

		String hql = "FROM " + t.getClass().getName() + " WHERE 1=1 ";
		List<Object> param = new ArrayList<Object>();

		QueryCondition queryCondition = new QueryCondition(hql, param, pageNo,
				pageSize);
		PageList pageList = super.queryListByHqlWithPage(queryCondition);// 结果集
		return QuiUtils.quiDataGird(pageList, pageList.getCount());

	}

	@Override
	public boolean checkExists(Config t) {
		String hql = "FROM " + t.getClass().getName() + " t WHERE t.key = ?";

		List<T> ls = this.queryListByHql(hql, t.getKey());

		if (ls != null && ls.size() > 0) {
			return true;
		}
		return false;
	}

}
