package com.zjy.jopm.base.common;



/**  
* 项目名称：jeecg
* 类名称：Globals   
* 类描述：  全局变量定义
* 创建人：jeecg      
* @version    
*
 */
public final class Globals {
	/**
	 * 保存用户到SESSION
	 */
	public static String USER_SESSION="USER_SESSION";
	/**
	 * 人员类型
	 */
	public static Short USER_NORMAL=1;//正常
	public static Short USER_FORBIDDEN=0;//禁用
	public static Short USER_ADMIN=-1;//超级管理员
	/**
	 *日志级别定义
	 */
	public static Short LOG_LEAVEL_INFO=1;
	public static Short LOG_LEAVEL_WARRING=2;
	public static Short LOG_LEAVEL_ERROR=3;
	 /**
	  * 日志类型
	  */
	 public static Short LOG_TYPE_LOGIN=1; //登陆
	 public static Short LOG_TYPE_EXIT=2;  //退出
	 public static Short LOG_TYPE_INSERT=3; //插入
	 public static Short LOG_TYPE_DEL=4; //删除
	 public static Short LOG_TYPE_UPDATE=5; //更新
	 public static Short LOG_TYPE_UPLOAD=6; //上传
	 public static Short LOG_TYPE_OTHER=7; //其他
	 
	 
	 /**
	  * 词典分组定义
	  */
	 public static String TYPEGROUP_DATABASE="database";//数据表分类
	 
	 /**
	  * 权限等级
	  */
	 public static Short FUNCTION_LEAVE_ONE=0;//一级权限
	 public static Short FUNCTION_LEAVE_TWO=1;//二级权限
	 
	 /**
	  * 权限等级前缀
	  */
	 public static String FUNCTION_ORDER_ONE="ofun";//一级权限
	 public static String FUNCTION_ORDER_TWO="tfun";//二级权限

	 /**
	  * 配置系统是否开启按钮权限控制
	  */
	 public static boolean BUTTON_AUTHORITY_CHECK = false;
	 
	 /**
	  * 配置是否加入应用和用户的网络分类
	  */
	 public static boolean NETWORK_TYPE_CHECK=false;
	 
	 /**
	  * 配置是否启用三员管理
	  */
	 public static boolean THREE_MEMBER_MANAGER=false;
	 
}
