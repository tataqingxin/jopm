package com.zjy.jopm.base.app.timing;

import java.text.ParseException;
import java.util.List;

import com.unistc.utils.Platform;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.service.FunctionService;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;

public class TimingCleanEffApp {
	
	public void cleanEffAppjob() throws ParseException{
		FunctionService service = Platform.getBean("functionService");
		
		String hql = "from AppMirrorEntity where isEffective = 'n'";
		
		List<AppMirrorEntity> appMirrList = service.queryListByHql(hql);
		
		hql = "from RoleFunctionRelationEntity where functionEntity.applicationEntity.id = ?";
		
		for (AppMirrorEntity appMirrorEntity : appMirrList) {
			List<RoleFunctionRelationEntity> roleFunList = service.queryListByHql(hql, appMirrorEntity.getApplicationEntity().getId());
			for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunList) {
				service.deleteEntity(roleFunctionRelationEntity);
			}
		}
		
		for (AppMirrorEntity appMirrorEntity : appMirrList) {
			service.deleteEntity(appMirrorEntity);
		}
	}
}
