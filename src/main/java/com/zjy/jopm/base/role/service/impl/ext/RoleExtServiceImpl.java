/** 
 * @Description:[对外角色接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.role.service.impl.ext.RoleExtServiceImpl.java
 * @ClassName:RoleExtServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:30:11
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:30:11  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.role.service.impl.ext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.role.service.ext.RoleExtService;

/**
 * @ClassName: RoleExtServiceImpl 
 * @Description: [对外角色接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:30:11 
 * @since JDK 1.6 
 */
@Service("roleExtService")
@Transactional
public class RoleExtServiceImpl extends BaseServiceimpl implements RoleExtService {

}
