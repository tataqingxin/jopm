package com.zjy.jopm.base.service;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;





import com.zjy.jopm.base.log.entity.LogEntity;
import com.zjy.jopm.base.log.service.LogService;
import com.zjy.jopm.base.log.service.ext.LogExtService;

import test.AbstractTestCase;

/**
 * 
 * @ClassName: LogServiceTest 
 * @Description: [功能描述] 
 * @author Sunset
 * @date 2016年5月12日 下午5:59:25 
 * @since JDK 1.6
 */
public class LogServiceTest extends AbstractTestCase{
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private LogExtService logExtService;
	/**
	 * 
	 * @Title: insertLogTest 
	 * @Description: [添加日志]
	 */
	@Test
	public void insertLogTest(){
		String logContent = "123321";
		String loglevel = "1";
		String operatetype = "1";
//		logExtService.insertLog(logContent, loglevel, operatetype);
//		LogEntity logEntity = logExtService.expandEntityByProperty(LogEntity.class, "content", logContent);
//		Assert.assertNotNull("查询", logEntity);//断言
	}
	/**
	 * 
	 * @Title: getLogDataGridTest 
	 * @Description: [获取日志列表Junit]
	 */
	@Test
	public void getLogDataGridTest(){
		String logContent = "12";
		String loglevel = "1";
		String operatetype = "1";
		logExtService.insertLog(logContent, loglevel, operatetype);
		LogEntity log = new LogEntity();
		log.setContent("12");
		Map<String, Object> map =logService.getLogDataGrid(log, 1,10, "","desc");
		Assert.assertNotNull("查询", map);//断言
	}
	
	
}
