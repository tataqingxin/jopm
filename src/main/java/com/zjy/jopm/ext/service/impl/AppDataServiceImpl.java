package com.zjy.jopm.ext.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.service.ApplicationService;
import com.zjy.jopm.ext.service.AppDataService;

@Service("extAppService")
public class AppDataServiceImpl implements AppDataService {

	@Autowired
	private ApplicationService applicationService;
	
	@Override
	public List<String> getAppInstanceCode() {
		String hql = "FROM ApplicationEntity WHERE 1=1 ";
		List<String> appInstanceCodeList = new ArrayList<String>();
		List<ApplicationEntity> appList = applicationService.queryListByHql(hql);
		if(null!=appList){
			for (ApplicationEntity application : appList) {
				appInstanceCodeList.add(application.getInstanceCode());
			}
		}
		return appInstanceCodeList;
	}

}
