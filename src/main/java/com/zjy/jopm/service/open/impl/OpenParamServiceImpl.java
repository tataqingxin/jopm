package com.zjy.jopm.service.open.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.config.entity.Config;
import com.zjy.jopm.sdk.service.OpenParamService;


@Service("openParamService")
@Transactional
public class OpenParamServiceImpl extends BaseServiceimpl implements OpenParamService {

	@Override
	public String getValue(String key) {

		String hql = "from com.zjy.jopm.base.config.entity.Config where key=:key";
		List<Config> list = new ArrayList<Config>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key",key);
		list = this.queryListByHql(hql, params);
		
		if(!CollectionUtils.isEmpty(list)){
			Config uold = list.get(0);
			return uold.getValue();
		}
		return null;
	
	}

	

}
