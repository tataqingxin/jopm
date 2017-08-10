/** 
 * @Description:[机构部门对外接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.service.ext.OrgExtServiceImpl.java
 * @ClassName:OrgExtServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月13日 下午3:38:14
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月13日 下午3:38:14  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.org.service.impl.ext;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.service.ext.OrgExtService;
import com.zjy.jopm.base.user.entity.UserDepartmentRelationEntity;

/**
 * @ClassName: OrgExtServiceImpl
 * @Description: [机构部门对外接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月13日 下午3:38:14
 * @since JDK 1.6
 */
@Service("orgExtService")
@Transactional
public class OrgExtServiceImpl extends BaseServiceimpl implements OrgExtService {
	/**
	 * @Title: getDepartmentListByUserId
	 * @Description: 根据用户id获取所属的部门
	 * @param @param userId
	 * @param @return 参数说明
	 * @return List<DepartmentEntity> 返回类型
	 * @throws JumpException
	 *             异常类型
	 */
	public List<DepartmentEntity> getDepartmentListByUserId(String userId) {
		List<DepartmentEntity> departList = new ArrayList<DepartmentEntity>();
		//判断userId是否有值，
		if (StringUtil.isNotEmpty(userId)) {
			String hql = "from UserDepartmentRelationEntity where 1=1 and userEntity.id=?";
			List<UserDepartmentRelationEntity> userDepartRelation = this
					.queryListByHql(hql, userId);
			for (UserDepartmentRelationEntity us : userDepartRelation) {
				if (us.getDepartmentEntity() != null) {
					departList.add(us.getDepartmentEntity());
				}
			}
		}
		return departList;
	}
}
