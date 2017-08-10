package com.zjy.jopm.sdk.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.StringUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.service.ApplicationService;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.sdk.entityVO.ApplicationEntityVO;
import com.zjy.jopm.sdk.service.AppDataService;

@Service("appDataService")
@Transactional
public class AppDataServiceImpl implements AppDataService {

	@Autowired
	private ApplicationService applicationService;
	
	@Override
	public ApplicationEntityVO getAppData(String instanceCode) {
		ApplicationEntity application = new ApplicationEntity();
		ApplicationEntityVO appVo = new ApplicationEntityVO();
		if(StringUtil.isNotEmpty(instanceCode)){
			application = applicationService.expandEntityByProperty(ApplicationEntity.class,"instanceCode",instanceCode);
		}
		if(null!=application){
			appVo.setAppSerial(application.getAppSerial());
			appVo.setCode(application.getCode());
			appVo.setCompanyCode(application.getCompanyCode());
			appVo.setCreatetime(application.getCreatetime());
			appVo.setDescription(application.getDescription());
			appVo.setDisabletime(application.getDisabletime());
			appVo.setInstanceCode(application.getInstanceCode());
			appVo.setName(application.getName());
			appVo.setOrganizationCode(application.getOrganizationEntity().getCode());
			appVo.setOrganizationId(application.getOrganizationEntity().getId());
			appVo.setOrganizationName(application.getOrganizationEntity().getName());
			appVo.setStatus(application.getStatus());
			appVo.setType(application.getType());
			appVo.setUrl(application.getUrl());
		}
		return appVo;
	}

	@Override
	public Map<String, Object> getAppDataList(String pageNum, String pageSize) {
		Map<String, Object> appDataMap = new HashMap<String, Object>();
		appDataMap = applicationService.getAppDataQuiGrid(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		List<Map<String,Object>>  reosData = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>>  result = new ArrayList<Map<String,Object>>();
		if(null != appDataMap){
			result = (List<Map<String, Object>>) appDataMap.get("rows");
			for (Map<String, Object> map : result) {
				Map<String,Object> dataMap = new HashMap<String, Object>();
				dataMap.put("id", map.get("id"));
				dataMap.put("code", map.get("code"));
				dataMap.put("name", map.get("name"));
				dataMap.put("instanceCode", map.get("instanceCode"));
				dataMap.put("organizationID", ((OrganizationEntity)map.get("organizationEntity")).getId());
				dataMap.put("organizationName", ((OrganizationEntity)map.get("organizationEntity")).getName());
				dataMap.put("organizationCode", ((OrganizationEntity)map.get("organizationEntity")).getCode());
				dataMap.put("status", map.get("status"));
				reosData.add(dataMap);
			}
			appDataMap.put("rows", reosData);
		}
		return appDataMap;
	}

}
