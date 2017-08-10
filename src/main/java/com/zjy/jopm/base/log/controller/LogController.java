/** 
 * @Description:[日志管理功能]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.log.controller.LogController.java
 * @ClassName:LogController
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午2:48:31
 * @UpdateUser:HogwartsRow  
 * @UpdateDate:2016年5月9日 下午2:48:31  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.log.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.exception.JumpException;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.log.entity.LogEntity;
import com.zjy.jopm.base.log.service.LogService;
import com.zjy.jopm.base.log.service.ext.LogExtService;

/**
 * @ClassName: LogController
 * @Description: [日志管理功能]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午2:48:31
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/logController")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@Autowired
	private LogExtService logExtService;

	/**
	 * @Title: logList
	 * @Description: 获取日志列表
	 * @param response
	 * @param request
	 * @return Map<String, Object>
	 * @throws JumpException
	 */
	@RequestMapping(value = "/logList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> logList(LogEntity logEntity,
			HttpServletResponse response, HttpServletRequest request) throws JumpException{
		int pageNo = 1;
		if(StringUtil.isNotEmpty(request.getParameter("pager.pageNo"))){
			pageNo = Integer.parseInt(request.getParameter("pager.pageNo"));
		}
		int pageSize = 10;
		if(StringUtil.isNotEmpty(request.getParameter("pager.pageSize"))){
			pageSize = Integer.parseInt(request.getParameter("pager.pageSize"));
		}
		String sort = request.getParameter("sort");
		if(StringUtil.isEmpty(sort)){
			sort = "id";
		}
		String direction = request.getParameter("direction");
		if(StringUtil.isEmpty(direction)){
			direction = Constants.DESC;
		}
		Map<String, Object> results = logService.getLogDataGrid(logEntity, pageNo, pageSize, sort,direction);
		response.addHeader("Access-Control-Allow-Origin", "*");
		return results;
	}
	

}
