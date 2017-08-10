/** 
 * @Description:[机构接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.service.impl.OrganziationServiceImpl.java
 * @ClassName:OrganziationServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:29:24
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:29:24  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.JumpBeanUtil;
import com.unistc.utils.MD5;
import com.unistc.utils.ResourceUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.log.entity.LogEntity;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.role.entity.RoleUserRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.UserService;
import com.zjy.jopm.base.org.service.OrganizationService;

/**
 * @ClassName: OrganziationServiceImpl 
 * @Description: [机构接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:29:24 
 * @since JDK 1.6 
 */
@Service("organizationService")
@Transactional
public class OrganziationServiceImpl extends BaseServiceimpl implements OrganizationService {
	@Autowired
	private UserService userService;
	@Autowired
	private LogExtService logExtService;
	/**
     * 
    * @param sessionInfo 
     * @Title: OrganizationServiceTree 
    * @Description: 获取机构树 根据当前登录的用户
    * @param @param organizationEntity
    * @param @return  参数说明 
    * @return List<TreeNode> 返回类型 
    * @throws JumpException 异常类型
     */
	public AjaxJson OrganizationServiceTree(OrganizationEntity organizationEntity, SessionInfo sessionInfo){
		AjaxJson j=new AjaxJson();
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		String organizationEntityId=null;
		List<Object> param=new ArrayList<Object>();
		if(sessionInfo!=null){
			//当用户类型不同时候从不同的地方获取organizationEntityId
			if(sessionInfo.getUser().getOrganizationEntity()!=null){
				organizationEntityId= sessionInfo.getUser().getOrganizationEntity().getId();
			}
			List<OrganizationEntity> organizationList=new ArrayList<OrganizationEntity>();
			String hql = "from OrganizationEntity where 1=1";

			if(sessionInfo.getUser().getOrganizationEntity() == null && organizationEntity.getId() == null){	
				organizationList = this.queryListByHql(hql);
			}else{
				//如果当前用户为特殊用户 刷新根机构
				if(sessionInfo.getIdentity()==1){
					if(StringUtil.isEmpty(organizationEntity.getId())){//获取根节点
						hql+=" and parentOrganizationEntity.id is null";
					}else{
						hql+="and parentOrganizationEntity.id= ?";
						param.add(organizationEntity.getId());
					}
				}else{
					//如果为普通管理员，则刷新所属机构
					if(StringUtil.isEmpty(organizationEntity.getId())){//获取根节点
						hql+="and id =?";
						param.add(organizationEntityId);
					}else{
						hql+="and parentOrganizationEntity.id = ?";
						param.add(organizationEntity.getId());
					}
				}
				hql+=" ORDER BY range ";
				hql += SortDirection.asc;
				organizationList=this.queryListByHql(hql, param);
			}
			
			//组装机构树
			for(OrganizationEntity organization:organizationList){
				 TreeNode node = new TreeNode();
		   	      node.setId(organization.getId());
		   	      node.setName(organization.getName());
		   	      node.setParentId(organization.getParentOrganizationEntity()!=null?organization.getParentOrganizationEntity().getId():"");
		   	      if(organization.getChildOrganizationEntity().size()>0){
		   	    	  node.setIsParent(Boolean.valueOf(true));
		   	      }else{ 
		   	    	  node.setIsParent(Boolean.valueOf(false));
		   	    	  node.setTarget("frmright");
		   	      }
		   	      nodes.add(node);
			}
		}
		j.setObj(nodes);
		return j;
	}
	
	  /**
     * 
    * @Title: saveOrUpdateOrganization 
    * @Description: 保存或者更新
    * @param @param organizationEntity
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	public AjaxJson saveOrUpdateOrganization(OrganizationEntity organizationEntity){
		AjaxJson j = new AjaxJson();
		boolean flag=true;
		String logContent="";
		
		//验证排序是否重复
		/*if(StringUtil.isNotEmpty(organizationEntity.getOrgarizationId())){
			String rangHql="from OrganizationEntity where parentOrganizationEntity.id=? and range=?";
			List<Object> param=new ArrayList<Object>();
			param.add(organizationEntity.getOrgarizationId());
			param.add(organizationEntity.getRange());
			List<OrganizationEntity> list=this.queryListByHql(rangHql, param);
			if(list.size()>0){
				j.setSuccess(false);
				j.setMessage("[排序]不能重复!");
				return j;
			}
		}*/
		//校验字段
		if(StringUtil.isEmpty(organizationEntity.getName())){
			j.setSuccess(false);
			j.setMessage("[名称]不能为空!");
			return j;
		}else if(StringUtil.isEmpty(organizationEntity.getCode())){
			j.setSuccess(false);
			j.setMessage("[编码]不能为空!");
			return j;
		}
		//操作数据库
		String message="";
		if(StringUtil.isNotEmpty(organizationEntity.getId())){//编辑
			OrganizationEntity entity=this.expandEntity(OrganizationEntity.class, organizationEntity.getId());
			if (StringUtil.isNotEmpty(entity.getCode())
					&& !organizationEntity.getCode().equals(entity.getCode())) {
				List<OrganizationEntity> org=this.queryListByProperty(OrganizationEntity.class, "code", organizationEntity.getCode());
				if(org.size()>0){
					message+="[编码]不能重复!";
				}
				
				if(StringUtil.isNotEmpty(message)){
					j.setMessage(message);
					j.setSuccess(false);
					return j;
				}
			}
			
			if (StringUtil.isNotEmpty(entity.getName())
					&& !organizationEntity.getName().equals(entity.getName())) {
				List<OrganizationEntity> org=this.queryListByProperty(OrganizationEntity.class, "name", organizationEntity.getName());
				if(org.size()>0){
					message+="[名称]不能重复!";
				}
				
				if(StringUtil.isNotEmpty(message)){
					j.setMessage(message);
					j.setSuccess(false);
					return j;
				}
			}
			organizationEntity.setParentOrganizationEntity(entity.getParentOrganizationEntity());
			/*if(StringUtil.isNotEmpty(entity.getParentOrganizationEntity())){
				String rangHql="from OrganizationEntity where parentOrganizationEntity.id=? and range=?";
				List<Object> param=new ArrayList<Object>();
				param.add(entity.getParentOrganizationEntity().getId());
				param.add(organizationEntity.getRange());
				List<OrganizationEntity> list=this.queryListByHql(rangHql, param);
				if(list.size()>1){
					j.setSuccess(false);
					j.setMessage("[排序]不能重复!");
					return j;
				}else if(list.size()>1){
					j.setSuccess(false);
					j.setMessage("[排序]不能重复!");
					return j;
				}
			}*/
			//记录日志
			JumpBeanUtil.copyBeanNotNull2Bean(organizationEntity, entity);
			flag=this.updateEntity(entity);
			logContent = "更新机构：["+organizationEntity.getName()+"]," + j.getMessage();
		}else{//保存
			//验证编码是否唯一
			List<OrganizationEntity> org=this.queryListByProperty(OrganizationEntity.class, "code", organizationEntity.getCode());
			if(org.size()>0){
				message+="[编码]不能重复!";
			}
			org=this.queryListByProperty(OrganizationEntity.class, "name", organizationEntity.getName());
			if(org.size()>0){
				message+="[名称]不能重复!";
			}
			if(StringUtil.isNotEmpty(message)){
				j.setMessage(message);
				j.setSuccess(false);
				return j;
			}
			
			organizationEntity.setId(null);
			String parentId=organizationEntity.getOrgarizationId();
			//判断parentId 是否为空 为空 说明没有选择机构节点 保存失败
			List<OrganizationEntity> organizationList = super.queryListByClass(OrganizationEntity.class);
			//如果数据库为空,不进行下列判断
			if(organizationList != null && organizationList.size() != 0){	
				if(StringUtil.isEmpty(parentId)){
					j.setSuccess(false);
					j.setMessage("请选择要录入的机构！");
					return j;
				}
			}
			OrganizationEntity parentEntity=this.expandEntity(OrganizationEntity.class, parentId);
			organizationEntity.setParentOrganizationEntity(parentEntity);
			logContent = "保存机构["+organizationEntity.getName()+"]," + j.getMessage();
			flag=this.saveEntity(organizationEntity);
			//插入该机构的系统用户
			UserEntity user = new UserEntity(organizationEntity.getCode(),new MD5().getMD5ofStr(ResourceUtil.getConfigByName("orgPassword")), 
					null, organizationEntity.getCode(), null, null, null,"1", null, new java.util.Date(), null, "0", "1", organizationEntity);
			this.insertEntity(user);
			//跟系统用户绑定机构管理员的角色
			String hql1 = "from RoleEntity where code = 'jg'";
			RoleEntity role = this.expandEntityByHql(hql1);
			RoleUserRelationEntity roleUserRelationEntity = new RoleUserRelationEntity();
			roleUserRelationEntity.setRoleEntity(role);
			roleUserRelationEntity.setUserEntity(user);
			super.insertEntity(roleUserRelationEntity);

			/*String hql2 = "SELECT DISTINCT functionEntity.id FROM RoleFunctionRelationEntity WHERE roleEntity.id = ?";
			List<String> functionIds = super.queryListByHql(hql2, role.getId());
			Map<String, Object> _param = new HashMap<String, Object>();
			_param.put("ids", functionIds.size()>0?functionIds:"");
			String hql3 = "SELECT DISTINCT applicationEntity.id FROM FunctionEntity WHERE id IN (:ids)";
			List<String> applicationIds = super.queryListByHql(hql3, _param);
			_param.put("ids", applicationIds.size()>0?applicationIds:"");
			String hql4 = "from ApplicationEntity where id IN (:ids)";
			List<ApplicationEntity> applications = super.queryListByHql(hql4, _param);
			for (ApplicationEntity applicationEntity : applications) {
				AppMirrorEntity appMirror = new AppMirrorEntity();
				//随机生成uuid
				int uuCode = UUID.randomUUID().toString().hashCode();
				//有可能是负数
				if(uuCode<0){
					uuCode = -uuCode;
				}
				String code = String.format("%015d", uuCode);
				appMirror.setCode(code);
				appMirror.setOrganizationEntity(organizationEntity);
				appMirror.setApplicationEntity(applicationEntity);
				super.insertEntity(appMirror);				
			}*/
			
			
		}
		if(flag){
			j.setSuccess(true);
			j.setMessage("操作成功!");
			this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		}else{
			j.setSuccess(false);
			j.setMessage("操作失败！");
		}
		return j;
	}
	
	/**
	 * 
	* @Title: delOrganizationEntity 
	* @Description: 删除机构
	* @param @param id
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	public AjaxJson delOrganizationEntity(String id){
		 AjaxJson j = new AjaxJson();
		 boolean flag=true;
		 //判断id是否为空
		 OrganizationEntity organizationEntity=new OrganizationEntity();
		 String message="";
		 if(StringUtil.isNotEmpty(id)){
			 //只有拥有部门和普通用户的才不能删
			 //判断机构下面是否有部们 
			 List<DepartmentEntity> departmentEntity=this.queryListByProperty(DepartmentEntity.class,"organizationEntity.id",id);
			 if(departmentEntity.size()>0){
				 message+="[部门],";
			 }
			 //判断机构下面是否有人,
//			 List<UserEntity> userEntity=this.queryListByProperty(UserEntity.class,"organizationEntity.id",id);
			 //查找普通角色
			 String hql = "from UserEntity where organizationEntity.id = ? and type = '0'";
			 List<UserEntity> userEntity = this.queryListByHql(hql, id);
			 if(userEntity.size()>0){
				 message+="[人员],";
			 }
			 //判断机构下面是否有应用
//			 List<ApplicationEntity> applicationEntity=this.queryListByProperty(ApplicationEntity.class,"organizationEntity.id",id);
//			 if(applicationEntity.size()>0){
//				 message+="[应用],";
//			 }
			 //判断应用下面是否有角色
//			 List<RoleEntity> roleEntity=this.queryListByProperty(RoleEntity.class,"organizationEntity.id",id);
//			 if(roleEntity.size()>0){
//				 message+="[角色],";
//			 }
			 
			 if(StringUtil.isNotEmpty(message)){
//		    		message=message.substring(0, message.lastIndexOf(","));
			 }
			 //删除
			 if(StringUtil.isNotEmpty(message)){
				 j.setSuccess(false);
				 j.setMessage("该机构下绑定了"+message+"不允许删除！"); 
			 }else{
				 //删除角色以及角色功能的关系
				 List<RoleEntity> roleEntity=this.queryListByProperty(RoleEntity.class,"organizationEntity.id",id);
				 for (RoleEntity role : roleEntity) {
					 String hql4 = "from RoleFunctionRelationEntity where roleEntity.id = ?";
					 List<RoleFunctionRelationEntity> roleFunRelation = this.queryListByHql(hql4, role.getId());
					 this.deleteEntityBatch(roleFunRelation);
				 }
				 this.deleteEntityBatch(roleEntity);
				 //删除应用
				 List<ApplicationEntity> applicationEntity=this.queryListByProperty(ApplicationEntity.class,"organizationEntity.id",id);
				 this.deleteEntityBatch(applicationEntity);

				 //删除绑定的系统用户
				 String hqll = "from UserEntity where organizationEntity.id = ? and type = '1'";
				 UserEntity user = this.expandEntityByHql(hqll, id);
				 String hql2 = "from RoleUserRelationEntity where userEntity.id = ?";
				 List<RoleUserRelationEntity> roleUserRelationEntity = this.queryListByHql(hql2, user.getId());
				 for (RoleUserRelationEntity roleUser : roleUserRelationEntity) {
					this.deleteEntity(roleUser);
				 }
				 //删除镜像
				 String hql3 = "from AppMirrorEntity where organizationEntity.id = ?";
				 List<AppMirrorEntity> mirrorList = this.queryListByHql(hql3, id);
				 for (AppMirrorEntity appMirrorEntity : mirrorList) {
					 this.deleteEntity(appMirrorEntity);
				 }
				 //删除日志
				 List<LogEntity> logList = this.queryListByProperty(
					 LogEntity.class, "userEntity.id", user.getId());
				 for (LogEntity logEntity : logList) {
					 this.deleteEntity(logEntity);
				 }
				 
				 userService.deleteUser(user.getId());
				 
				 organizationEntity=this.expandEntity(OrganizationEntity.class, id);
				 String name=organizationEntity.getName();
				 flag=this.deleteEntity(organizationEntity);				 
				 if(flag){
						j.setSuccess(true);
						j.setMessage("操作成功!");
						//记录日志
						String logContent = "删除机构["+name+"]," + j.getMessage();
						this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
				 }else{
					j.setSuccess(false);
					j.setMessage("操作失败！");
				}
			 }
		 }else{
			 j.setSuccess(false);
			 j.setMessage("选择要删除的机构！"); 
		 }
		 
		 return j;
	}

}
