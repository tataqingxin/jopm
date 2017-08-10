/** 
 * @Description:[对外日志接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.log.service.impl.ext.LogExtServiceImpl.java
 * @ClassName:LogExtServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:26:23
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午3:26:23  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.log.service.impl.ext;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.utils.BrowserUtil;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.DateUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.log.entity.LogEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;

/**
 * @ClassName: LogExtServiceImpl 
 * @Description: [对外日志接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:26:23 
 * @since JDK 1.6 
 */
@Service("logExtService")
@Transactional
public class LogExtServiceImpl extends BaseServiceimpl implements LogExtService {

	/**
	 * 
	 * @Title: insertLog
	 * @Description: [添加日志]
	 * @param LogContent 内容
	 * @param loglevel 级别
	 * @param operatetype 类型
	 * @return true/false
	 * @see com.zjy.jopm.base.service.ext.LogExtService#insertLog(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void insertLog(String LogContent, String loglevel,
			String operatetype) {
			HttpServletRequest request = ContextHolderUtil.getRequest();
			if (null == request) {
				return;
			}
			String broswer = BrowserUtil.checkBrowse(request);
			
			
			SessionInfo sessionInfo = (SessionInfo)ContextHolderUtil.getSession().getAttribute(Globals.USER_SESSION);
			// 会话失效后的操作不记录日志
			if (null == sessionInfo) {
				return;
			}
	        // 特殊用户不记录日志
			if(Constants.SPECIAL_ACCOUNT == sessionInfo.getIdentity()){
	        	return;
	        }
	        
	        LogEntity log = new LogEntity();
	        log.setContent(LogContent);
	        log.setLogLevel(loglevel);
	        log.setOperateType(operatetype);
	        log.setBroswer(broswer);
	        log.setOperateTime(DateUtil.getDateTime(DateUtil.DEFAULT_DATETIME_FORMAT));
	        log.setUserEntity(sessionInfo.getUser());
	        this.insertEntity(log);
	}

}
