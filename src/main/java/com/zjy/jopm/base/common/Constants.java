/** 
 * @Description:[公共常量]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.common.Constants.java
 * @ClassName:Constants
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月11日 下午3:53:49
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月11日 下午3:53:49  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.common;

import com.unistc.core.common.model.SortDirection;

/**
 * @ClassName: Constants 
 * @Description: [公共常量] 
 * @author Lu Guoqiang
 * @date 2016年5月11日 下午3:53:49 
 * @since JDK 1.6 
 */
public class Constants {

	/**
	 * 状态：启用/禁用
	 */
	public static final String ENABLE_STATUS = "1";
	public static final String DISABLE_STATUS = "0";
	
	/**
	 * 排序
	 */
	public static final String DESC = SortDirection.desc.toString();
	public static final String ASC = SortDirection.asc.toString();
	
	/**
	 * 验证码
	 */
	public static final String SESSION_KEY_OF_RAND_CODE = "CAPTCHA";
	
	/**
	 * 用户身份
	 */
	public static final Integer SPECIAL_ACCOUNT = 1;
	public static final Integer NORMAL_ACCOUNT = 0;
	
	/**
	 * 字典分组
	 */
	public static final String DICT_GROUP_APP_TYPE = "appType";
	public static final String DICT_GROUP_ROLE_TYPE = "roleType";
	public static final String DICT_GROUP_ICON_TYPE = "iconType";
	public static final String DICT_GROUP_STATUS = "status";
	public static final String DICT_GROUP_SEX = "sex";
	
	/**
	 * 类型:系统/自定义
	 */
	public static final String SYSTEM_TYPE = "system";
	public static final String CUSTOM_TYPE = "custom";
	
	/**
	 * 权限树类型
	 */
	public static final String[] PERMISSION_TREE_TYPE = {"app", "func", "oper"};
}
