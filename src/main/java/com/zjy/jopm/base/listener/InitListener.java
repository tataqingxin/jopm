package com.zjy.jopm.base.listener;

import javax.servlet.ServletContextEvent;



import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.apache.zookeeper.server.ZooTrace;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.zjy.jopm.base.dict.service.ext.DictExtService;

/**
 * 系统初始化监听器,在系统启动时运行,进行一些初始化工作
 * @ClassName: InitListener 
 * @author xuanx
 * @date 2016年5月14日 上午9:52:16 
 * @since JDK 1.6
 */
public class InitListener implements javax.servlet.ServletContextListener {
	Logger logger = Logger.getLogger(InitListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("销毁DUBBO配置");
		try {
			ProtocolConfig.destroyAll();
		} catch (Exception e) {
			System.out.println("销毁DUBBO配置时出现问题：" + e.getMessage());
		}
	}
	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		DictExtService dictExtService = (DictExtService) webApplicationContext.getBean("dictExtService");
		//对数据字典进行缓存
		dictExtService.initAllDictionaryGroups();
	}

}
