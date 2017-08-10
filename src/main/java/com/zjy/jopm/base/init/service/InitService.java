package com.zjy.jopm.base.init.service;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
/**
 * 
 * @ClassName: InitService 
 * @Description: [初始化 service] 
 * @author xuanx
 * @date 2016年5月17日 上午10:32:40 
 * @since JDK 1.6
 */
public interface InitService extends BaseService {
    /**
     * 
    * @Title: saveUserOrgInfo 
    * @Description: [初始化机构 人员信息 保存 ]
    * @param @param orgName 机构名称
    * @param @param orgCode 机构编码
    * @param @param userName 系统管理员用户
    * @param @param passWord 密码
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	public AjaxJson InitDataInfo(String orgName, String orgCode, String userName,
			String passWord);

}
