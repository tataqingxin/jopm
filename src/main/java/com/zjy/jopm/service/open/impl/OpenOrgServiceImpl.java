package com.zjy.jopm.service.open.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.sdk.entityVO.OrgInfo;
import com.zjy.jopm.sdk.service.OpenOrgService;

@Service("openOrgService")
@Transactional
public class OpenOrgServiceImpl extends BaseServiceimpl implements
		OpenOrgService {

	@Override
	public OrgInfo getOrgInfo(String orgCode) {
		String hql = "from OrganizationEntity where code = :orgCode";

		List<OrganizationEntity> oldList = new ArrayList<OrganizationEntity>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", orgCode);
		oldList = this.queryListByHql(hql, params);

		if (!CollectionUtils.isEmpty(oldList)) {
			OrganizationEntity uold = oldList.get(0);
			OrgInfo unew = new OrgInfo();
			unew.setCode(uold.getCode());
			unew.setName(uold.getName());
			return unew;
		}
		return null;

	}

	@Override
	public OrgInfo getParentInfo(String orgCode) {

		String hql = "from OrganizationEntity where code = :orgCode";

		List<OrganizationEntity> oldList = new ArrayList<OrganizationEntity>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", orgCode);
		oldList = this.queryListByHql(hql, params);

		if (!CollectionUtils.isEmpty(oldList)) {
			OrganizationEntity uold = oldList.get(0)
					.getParentOrganizationEntity();
			OrgInfo unew = new OrgInfo();
			unew.setCode(uold.getCode());
			unew.setName(uold.getName());
			return unew;
		}
		return null;

	}

	@Override
	public List<OrgInfo> getChildrenList(String orgCode) {

		String hql = "from OrganizationEntity where parentOrganizationEntity.code = :orgCode";

		List<OrganizationEntity> oldList = new ArrayList<OrganizationEntity>();
		List<OrgInfo> newList = new ArrayList<OrgInfo>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", orgCode);
		oldList = this.queryListByHql(hql, params);

		if (!CollectionUtils.isEmpty(oldList)) {
			for (OrganizationEntity uold : oldList) {
				OrgInfo unew = new OrgInfo();
				unew.setCode(uold.getCode());
				unew.setName(uold.getName());
				newList.add(unew);
			}
		}
		return newList;

	}

}
