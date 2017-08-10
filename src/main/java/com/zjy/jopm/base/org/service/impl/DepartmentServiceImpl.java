/** 
 * @Description:[部门接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.org.service.impl.DepartmentServiceImpl.java
 * @ClassName:DepartmentServiceImpl
 * @Author:Lu Guoqiang  
 * @CreateDate:2016年5月9日 下午3:23:36
 * @UpdateUser:Lu Guoqiang 
 * @UpdateDate:2016年5月9日 下午3:23:36  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.org.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.JumpBeanUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.log.service.ext.LogExtService;
import com.zjy.jopm.base.org.entity.DepartmentEntity;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.user.entity.UserDepartmentRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.org.service.DepartmentService;

/**
 * @ClassName: DepartmentServiceImpl 
 * @Description: [部门接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:23:36 
 * @since JDK 1.6 
 */
@Service("departmentService")
@Transactional
public class DepartmentServiceImpl extends BaseServiceimpl implements DepartmentService {
	
	@Autowired
	private LogExtService logExtService;
	  /**
     * 
    * @Title: departmentServiceTree 
    * @Description: 部门树
    * @param @param departId
    * @param @param sessionInfo
    * @param @return  参数说明 
    * @return List<TreeNode> 返回类型 
    * @throws JumpException 异常类型
     */
	public AjaxJson departmentServiceTree(DepartmentEntity departmentEntity,String orgId,boolean isCheck){
		AjaxJson j=new AjaxJson();
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		List<Object> param=new ArrayList<Object>();
		String departId=departmentEntity.getId();
		if(StringUtil.isNotEmpty(orgId)){
			List<DepartmentEntity> DepartmentEntityList=new ArrayList<DepartmentEntity>();
			String hql="from DepartmentEntity where 1=1";
			OrganizationEntity orgEntity=this.expandEntity(OrganizationEntity.class, orgId);
			if(orgEntity!=null&&StringUtil.isEmpty(departId)){//选择机构的时候 刷新机构下面的部门
				hql+=" and organizationEntity.id= ? and parentDepartmentEntity.id is null ";
				param.add(orgId);
			}else if(StringUtil.isNotEmpty(departId)){
				hql+=" and organizationEntity.id= ?  and parentDepartmentEntity.id=?";
				param.add(orgId);
				param.add(departId);
			}
			
			hql+=" ORDER BY range ";
			hql += SortDirection.asc;
			DepartmentEntityList=this.queryListByHql(hql, param);
			//组装部门树
			if(orgEntity!=null&&StringUtil.isEmpty(departId)){//选择机构的时候 刷新机构下面的部门
				  TreeNode node = new TreeNode();
		   	      node.setId(orgEntity.getId());
		   	      node.setName(orgEntity.getName());
		   	      node.setParentId("");
		   	      if(DepartmentEntityList.size()>0){
		   	    	  node.setIsParent(Boolean.valueOf(true));
		   	      }else{
		   	    	node.setIsParent(Boolean.valueOf(false));
		   	      }
		   	      node.setChecked(false);
		   	      nodes.add(node);
				hql+=" and organizationEntity.id= ? and parentDepartmentEntity.id is null ";
				param.add(orgId);
			}
			appendDepartTree(orgId, isCheck, nodes, DepartmentEntityList);
		}
		j.setObj(nodes);
		return j;
	}

	private void appendDepartTree(String orgId, boolean isCheck,
			List<TreeNode> nodes, List<DepartmentEntity> DepartmentEntityList) {
		for(DepartmentEntity department:DepartmentEntityList){
			  TreeNode node = new TreeNode();
		      node.setId(department.getId());
		      node.setName(department.getName());
		      node.setParentId(department.getParentDepartmentEntity()!=null?department.getParentDepartmentEntity().getId():orgId);
		     if(department.getChildDepartmentEntity().size()>0){
		    	  node.setIsParent(Boolean.valueOf(true));
		      }else{
		    	  node.setIsParent(Boolean.valueOf(false));
		    	  node.setTarget("frmright");
		      }
		      //判断用户部门表中 是否有对应的数据
		   	  if(isCheck){
		   	    List<UserDepartmentRelationEntity>	 udREntity=this.
		   	    		queryListByProperty(UserDepartmentRelationEntity.class,
		   	    		"departmentEntity.id", department.getId());
		   	    for(UserDepartmentRelationEntity ud:udREntity){
		   	    	UserEntity userEntit=ud.getUserEntity();
		   	    	String orgUserId=userEntit.getOrganizationEntity().getId();
		   	    	if(orgUserId.equals(orgId)){
		   	    	   node.setChecked(true);
		   	    	   break;
		   	    	}
		   	    }
		   	  }
		   	  if(department.getChildDepartmentEntity().size()>0){
		   		appendDepartTree(orgId, isCheck, nodes, department.getChildDepartmentEntity());
		   	  }
		      nodes.add(node);
		}
	}
	
	 /**
     * 
    * @Title: saveOrUpdateDepartment 
    * @Description: 保存部门
    * @param @param departmentEntity
    * @param @param parentId
    * @param @return  参数说明 
    * @return AjaxJson 返回类型 
    * @throws JumpException 异常类型
     */
	public AjaxJson saveOrUpdateDepartment(DepartmentEntity departmentEntity,
			String orgId, String departId){
		AjaxJson j = new AjaxJson();
		 boolean flag=true;
		String logContent="";
		departId=StringUtil.isEmpty(departId)?departmentEntity.getDepartId():departId;
		/*if(StringUtil.isNotEmpty(departId)){
			String rangHql="from DepartmentEntity where range=? and organizationEntity.id=?";
			List<Object> param=new ArrayList<Object>();
			param.add(departmentEntity.getRange());
			param.add(orgId);
			if(!orgId.equals(departId)){
				rangHql+=" and parentDepartmentEntity.id=?";
				param.add(departId);
			}else{
				rangHql+=" and parentDepartmentEntity.id is null";
			}
			List<DepartmentEntity> list=this.queryListByHql(rangHql, param);
			if(list.size()>0){
				j.setSuccess(false);
				j.setMessage("[排序]不能重复!");
				return j;
			}
		}*/
		
		
		//判断parentId 是否为空 为空 说明没有选择机构节点 保存失败
		if(StringUtil.isEmpty(orgId) || StringUtil.isEmpty(departId)){
			j.setSuccess(false);
			j.setMessage("请选择数据！");
			return j;
		}
		OrganizationEntity orgEntity=this.expandEntity(OrganizationEntity.class, orgId);
		 
		//校验字段
		if(StringUtil.isEmpty(departmentEntity.getName())){
			j.setSuccess(false);
			j.setMessage("[名称]不能为空!");
			return j;
		}else if(StringUtil.isEmpty(departmentEntity.getCode())){
			j.setSuccess(false);
			j.setMessage("[编码]不能为空!");
			return j;
		}
		
		//判断是否为机构  
		if(orgEntity!=null){
			departmentEntity.setOrganizationEntity(orgEntity);
			String message="";
			if(StringUtil.isNotEmpty(departmentEntity.getId())){//编辑
				DepartmentEntity depart=this.expandEntity(DepartmentEntity.class, departmentEntity.getId());
				if (StringUtil.isNotEmpty(depart.getCode())
						&& !departmentEntity.getCode().equals(depart.getCode())) {
					List<Object> param=new ArrayList<Object>();
					String hql="from DepartmentEntity where organizationEntity.id=? and code=?";
					param.add(orgId);
					param.add(departmentEntity.getCode());
					List<DepartmentEntity> department=this.queryListByHql(hql, param);
					if(department.size()>0){
						message+="[编码]不能重复!";
					}
					if(StringUtil.isNotEmpty(message)){
						j.setMessage(message);
						j.setSuccess(false);
						return j;
					}
				}
				
				if (StringUtil.isNotEmpty(depart.getName())
						&& !departmentEntity.getName().equals(depart.getName())) {
					List<Object> param=new ArrayList<Object>();
					String hql="from DepartmentEntity where organizationEntity.id=? and name=?";
					param=new ArrayList<Object>();
					param.add(orgId);
					param.add(departmentEntity.getName());
					List<DepartmentEntity>  department=this.queryListByHql(hql, param);
					if(department.size()>0){
						message+="[名称]不能重复!";
					}
					
					if(StringUtil.isNotEmpty(message)){
						j.setMessage(message);
						j.setSuccess(false);
						return j;
					}
				}
				departmentEntity.setParentDepartmentEntity(depart.getParentDepartmentEntity());
				JumpBeanUtil.copyBeanNotNull2Bean(departmentEntity, depart);
				flag=this.updateEntity(depart);
				logContent = "更新部门["+departmentEntity.getName()+"]," + j.getMessage();
			}else{//保存
				//判断上级部门id是否为空
				if(StringUtil.isNotEmpty(departId)){
					DepartmentEntity depart=this.expandEntity(DepartmentEntity.class, departId);
					departmentEntity.setParentDepartmentEntity(depart);
				} 
				List<Object> param=new ArrayList<Object>();
				String hql="from DepartmentEntity where organizationEntity.id=? and code=?";
				param.add(orgId);
				param.add(departmentEntity.getCode());
				List<DepartmentEntity> department=this.queryListByHql(hql, param);
				if(department.size()>0){
					message+="[编码]不能重复!";
				}
				hql="from DepartmentEntity where organizationEntity.id=? and name=?";
				param=new ArrayList<Object>();
				param.add(orgId);
				param.add(departmentEntity.getName());
				department=this.queryListByHql(hql, param);
				if(department.size()>0){
					message+="[名称]不能重复!";
				}
				
				if(StringUtil.isNotEmpty(message)){
					j.setMessage(message);
					j.setSuccess(false);
					return j;
				}
//				departmentEntity.setId(null);
				flag=this.insertEntity(departmentEntity);
				logContent = "保存部门["+departmentEntity.getName()+"]," + j.getMessage();
			}
		}else{
			j.setSuccess(false);
			j.setMessage("请选择机构！");
			return j;
		}
		if(flag){
			j.setSuccess(true);
			j.setMessage("操作成功!");
			//记录日志
			this.logExtService.insertLog(logContent, Globals.LOG_LEAVEL_INFO.toString(), Globals.LOG_TYPE_INSERT.toString());
		}else{
			j.setSuccess(false);
			j.setMessage("操作失败！");
		}
		return j;
	}
	
	/**
	 * 
	* @Title: delDepartmentEntity 
	* @Description:删除部门
	* @param @param id
	* @param @return  参数说明 
	* @return AjaxJson 返回类型 
	* @throws JumpException 异常类型
	 */
	public AjaxJson delDepartmentEntity(String id){
		 AjaxJson j = new AjaxJson();
		 boolean flag=true;
		 //判断id是否为空
		 DepartmentEntity department=new DepartmentEntity();
		 String message="";
		 if(StringUtil.isNotEmpty(id)){
			 //判断机构不允许删除
			 OrganizationEntity orgEntity=this.expandEntity(OrganizationEntity.class, id);
			 if(orgEntity!=null){
				 j.setSuccess(false);
				 j.setMessage("[机构]不允许删除！"); 
				 return j;
			 }
			 //判断机构下面是否有部们 
			 List<DepartmentEntity> departmentEntity=this.queryListByProperty(DepartmentEntity.class,"parentDepartmentEntity.id",id);
			 if(departmentEntity.size()>0){
				 message+="[部门],";
			 }
			 //判断机构下面是否有人
			 List<UserDepartmentRelationEntity> userEntity=this.queryListByProperty(UserDepartmentRelationEntity.class,"departmentEntity.id",id);
			 if(userEntity.size()>0){
				 message+="[人员],";
			 }
			 
			 if(StringUtil.isNotEmpty(message)){
//		    		message=message.substring(0, message.lastIndexOf(","));
			 }
			 
			 //删除
			 if(StringUtil.isNotEmpty(message)){
				 j.setSuccess(false);
				 j.setMessage("该部门下绑定了"+message+"不允许删除！"); 
			 }else{
				 department=this.expandEntity(DepartmentEntity.class, id);
				 String name=department.getName();
				 flag=this.deleteEntity(department);
				 if(flag){
						j.setSuccess(true);
						j.setMessage("操作成功!");
						//记录日志
						String logContent = "删除部门["+name+"]," + j.getMessage();
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
