/** 
 * @Description:[图标接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.icon.service.impl.IconServiceImpl.java
 * @ClassName:IconServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:25:50
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:25:50  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */
package com.zjy.jopm.base.icon.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.DateUtil;
import com.unistc.utils.FileUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.IconService;
import com.zjy.jopm.base.quiUtil.QuiUtils;

/**
 * @ClassName: IconServiceImpl
 * @Description: [图标接口实现类]
 * @author Lu Guoqiang
 * @date 2016年5月9日 下午3:25:50
 * @since JDK 1.6
 */
@Service("iconService")
@Transactional
public class IconServiceImpl extends BaseServiceimpl implements IconService {

	/**
	 * @Title: getIconDataGrid
	 * @Description: [获取日志列表]
	 * @param iconEntity
	 * @param pageNo
	 * @param pageSize
	 * @param sort
	 * @param direction
	 * @return Map<String, Object>
	 * @throws JumpException
	 * @see com.zjy.jopm.base.service.IconService#getIconDataGrid(com.zjy.jopm.base.entity.IconEntity,
	 *      int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> getIconDataGrid(IconEntity iconEntity,
			int pageNo, int pageSize, String sort, String direction)
			throws JumpException {
		String hql = "from IconEntity where 1=1";
		List<Object> param = new ArrayList<Object>();
		if (StringUtil.isNotEmpty(iconEntity.getName())) {
			hql += " and name like ?";
			param.add("%" + iconEntity.getName() + "%");
		}
		if (StringUtil.isNotEmpty(sort)) {
			hql += " ORDER BY ? ";
			param.add(sort);
			if (Constants.DESC.equals(direction)) {
				hql += SortDirection.desc;
			} else if (Constants.ASC.equals(direction)) {
				hql += SortDirection.asc;
			} else {
				hql += SortDirection.desc;
			}
		}
		QueryCondition queryCondition = new QueryCondition(hql, param, pageNo,
				pageSize);
		PageList pageList = this.queryListByHqlWithPage(queryCondition);
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	/**
	 * 
	 * @Title: insertIcon
	 * @Description: [新增图标]
	 * @param request
	 * @param iconEntity
	 * @return AjaxJson
	 * @throws IOException 
	 * @see com.zjy.jopm.base.icon.service.IconService#insertIcon(javax.servlet.http.HttpServletRequest, com.zjy.jopm.base.icon.entity.IconEntity)
	 */
	@Override
	public AjaxJson insertIcon(MultipartFile small, MultipartFile big,MultipartFile medium,HttpServletRequest request, IconEntity iconEntity) throws IOException {
				AjaxJson result = new AjaxJson();
				boolean flag = checkIconNameExit(iconEntity.getName(),iconEntity.getId());
				if(!flag){
					result.setMessage("图标名称已经存在");
					result.setSuccess(false);
					return result;
				}
				if(small != null){
					String smallExtend = FileUtil.getExtend(small.getOriginalFilename());
					String smallPath = getFile(small,request, smallExtend);
					iconEntity.setIconPath(smallPath);
				}
				if(medium != null){
					String mediumExtend = FileUtil.getExtend(medium.getOriginalFilename());
					String mediumIconPath = getFile(medium,request, mediumExtend);
					iconEntity.setMediumIconPath(mediumIconPath);
				}
				if(big != null){				
					String bigExtend = FileUtil.getExtend(big.getOriginalFilename());			
					String bigIconPath = getFile(big,request, bigExtend);
					iconEntity.setBigIconPath(bigIconPath);
				}
				Boolean json = this.insertEntity(iconEntity);
				if(json){
					result.setMessage("保存成功");
					result.setSuccess(true);
					return result;
				}
				result.setMessage("保存失败");
				result.setSuccess(false);
				return result;
	}

	private String getFile(MultipartFile fileName,HttpServletRequest request, String extend) throws IOException {
		String myfilename = DateUtil.getDateTime(DateUtil.DEFAULT_YMDHMS_FORMAT)
				+ StringUtil.random(8) + "."+extend;
		String filePath = "upload/icon/image/";
		// 文件保存路径  
		String savePath = request.getSession().getServletContext().getRealPath("/") + filePath;  
		File savefile = new File(savePath);
		if(!savefile.exists()){
			savefile.mkdirs();
		}
		String saveP=savefile+ "/"+myfilename;
		File saveSmall = new File(saveP);
		// 转存文件  
		FileCopyUtils.copy(fileName.getBytes(), saveSmall);
		return filePath+myfilename;
	}
	

	
	private boolean checkIconNameExit(String name, String id) {
			List<IconEntity> typeGroup = this.queryListByProperty(IconEntity.class, "name", name);
			if(typeGroup.size()>0){
				if(StringUtil.isNotEmpty(id)){
					IconEntity expandEntity = this.expandEntity(IconEntity.class, id);
					if(name.equals(expandEntity.getName())){
						return true;
					}
					return false;
				}
					return false;
			}
			return true;
		
	}

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
	 * @see com.zjy.jopm.base.icon.service.IconService#updateIcon(org.springframework.web.multipart.MultipartFile, org.springframework.web.multipart.MultipartFile, org.springframework.web.multipart.MultipartFile, javax.servlet.http.HttpServletRequest, com.zjy.jopm.base.icon.entity.IconEntity)
	 */
	@Override
	public AjaxJson updateIcon(MultipartFile small, MultipartFile big,
			MultipartFile medium, HttpServletRequest request,
			IconEntity iconEntity) throws IOException {
		AjaxJson result = new AjaxJson();
		boolean flag = checkIconNameExit(iconEntity.getName(),iconEntity.getId());
		if(!flag){
			result.setMessage("图标名称已经存在");
			result.setSuccess(false);
			return result;
		}
		String smallPath="";
		String mediumIconPath="";
		String bigIconPath="";
		if(small!=null){
			String smallExtend = FileUtil.getExtend(small.getOriginalFilename());
			smallPath = getFile(small,request, smallExtend);
		}
		if(medium!=null){
			String mediumExtend = FileUtil.getExtend(medium.getOriginalFilename());
			mediumIconPath = getFile(medium,request, mediumExtend);
		}
		if(big!=null){
			String bigExtend = FileUtil.getExtend(big.getOriginalFilename());
			bigIconPath = getFile(big,request, bigExtend);
		}
			IconEntity icon = this.expandEntity(IconEntity.class, iconEntity.getId());
			icon.setDescription(iconEntity.getDescription());
			icon.setName(iconEntity.getName());
			icon.setType(iconEntity.getType());
			icon.setMediumIconPath(mediumIconPath ==""?icon.getMediumIconPath():mediumIconPath );
			icon.setBigIconPath(bigIconPath =="" ?icon.getBigIconPath():bigIconPath);
			icon.setIconPath(smallPath == "" ?icon.getIconPath():smallPath);
			Boolean json = this.updateEntity(icon);
			if(!json){
				result.setMessage("保存失败");
				result.setSuccess(false);
			}
			result.setMessage("保存成功");
			result.setSuccess(true);
			return result;
	}

	
	/**
	 * 
	 * @Title: deleteIcon
	 * @Description: [删除图标]
	 * @param iconEntity
	 * @return AjaxJson
	 * @see com.zjy.jopm.base.icon.service.IconService#deleteIcon(com.zjy.jopm.base.icon.entity.IconEntity)
	 */
	@Override
	public AjaxJson deleteIcon(IconEntity iconEntity) {
		AjaxJson ajax = new AjaxJson();
		String app="from ApplicationEntity where iconEntity.id=?";
		List<ApplicationEntity> appIconList = this.queryListByHql(app, new Object[]{iconEntity.getId()});
		if(appIconList.size()>0){
			ajax.setSuccess(false);
			ajax.setMessage("应用下正在使用图标，不可删除");
			return ajax;
		}
		String function="from FunctionEntity where iconEntity.id=?";
		List<FunctionEntity> funIconList = this.queryListByHql(function, new Object[]{iconEntity.getId()});
		if(funIconList.size()>0){
			ajax.setSuccess(false);
			ajax.setMessage("功能下正在使用图标，不可删除");
			return ajax;
		}
		Boolean flag = this.deleteEntity(iconEntity);
		if(!flag){
			ajax.setSuccess(false);
			ajax.setMessage("删除失败");
		}
		ajax.setMessage("删除成功");
		return ajax;
	}


}
