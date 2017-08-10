/** 
 * @Description:[图标管理接口]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.icon.service.IconService.java
 * @ClassName:IconService
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午3:12:04
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:12:04  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.icon.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.BaseService;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.icon.entity.IconEntity;

/**
 * @ClassName: IconService 
 * @Description: [图标管理接口] 
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:12:04 
 * @since JDK 1.6 
 */
public interface IconService extends BaseService {

	/**
	 * 
	 * @Title: getIconDataGrid 
	 * @Description: [获取图标列表]
	 * @param iconEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return Map<String, Object>
	 */
	Map<String, Object> getIconDataGrid(IconEntity iconEntity, int pageNo,
			int pageSize, String sort, String direction) throws JumpException;

	


	/**
	 * 
	 * @Title: insertIcon 
	 * @Description: [新增图标]
	 * @param small
	 * @param big
	 * @param medium
	 * @param request
	 * @param iconEntity
	 * @return AjaxJson
	 * @throws JumpException
	 * @throws IOException
	 */
	AjaxJson insertIcon(MultipartFile small, MultipartFile big,MultipartFile medium,HttpServletRequest request, IconEntity iconEntity) throws JumpException, IOException;










	
	/**
	 * 
	 * @Title: updateIcon 
	 * @Description: [修改图标]
	 * @param small
	 * @param big
	 * @param medium
	 * @param request
	 * @param iconEntity
	 * @return AjaxJson
	 * @throws IOException
	 */
	AjaxJson updateIcon(MultipartFile small, MultipartFile big,
			MultipartFile medium, HttpServletRequest request,
			IconEntity iconEntity) throws IOException;



	/**
	 * 
	 * @Title: deleteIcon 
	 * @Description: [删除图标]
	 * @param iconEntity
	 * @return AjaxJson
	 */
	AjaxJson deleteIcon(IconEntity iconEntity);




}
