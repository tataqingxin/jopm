package com.zjy.jopm.base.email.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.utils.DateUtil;
import com.zjy.jopm.base.email.entity.EmailLoseEntity;
import com.zjy.jopm.base.email.service.EmailLoseService;

@Service("emailLoseService")
@Transactional
public class EmailLoseServiceImpl extends BaseServiceimpl implements EmailLoseService{

	@Override
	public AjaxJson expandEmailLose(String emailId) throws Exception {
		AjaxJson json = new AjaxJson();
		EmailLoseEntity expandEntity = this.expandEntity(EmailLoseEntity.class, emailId);
		if(expandEntity==null){
			json.setMessage("链接已经失效");
			json.setSuccess(false);
		}else{
			String startTime = DateUtil.getDateTime(expandEntity.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
			String currentTime = DateUtil.getDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
			if(jisuan(startTime,currentTime)){ 
	              this.deleteEntity(expandEntity);
	         }else{ 
	        	json.setMessage("链接已经失效");
	 			json.setSuccess(false); 
	         } 
		}
		return json;
	}
	
	public static boolean jisuan(String date1, String date2) throws Exception { 
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        java.util.Date start = sdf.parse(date1); 
        java.util.Date end = sdf.parse(date2); 
        long cha = end.getTime() - start.getTime(); 
        double result = cha * 1.0 / (1000 * 60 * 60); 
        if(result<=24){ 
             //System.out.println("可用");   
             return true; 
        }else{ 
             //System.out.println("已过期");  
             return false; 
        } 
    } 

}
