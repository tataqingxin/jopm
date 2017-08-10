package com.zjy.jopm.base.config.service;

import java.util.Map;


import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;

public interface ConfigService<T> extends BaseService {

	public Map<String, Object> getGrid(T t, int pageNo, int pageSize,
			String sort, String direction) throws JumpException;

	public boolean checkExists(T t);

}
