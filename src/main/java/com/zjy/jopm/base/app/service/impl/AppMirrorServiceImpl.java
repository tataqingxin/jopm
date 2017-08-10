package com.zjy.jopm.base.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unist.util.CollectionUtil;
import com.unist.util.StringUtil;
import com.unistc.core.common.hibernate.qbc.PageList;
import com.unistc.core.common.hibernate.qbc.QueryCondition;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.model.SortDirection;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.unistc.exception.JumpException;
import com.unistc.utils.JumpBeanUtil;
import com.zjy.jopm.base.app.entity.AppMirrorEntity;
import com.zjy.jopm.base.app.entity.ApplicationEntity;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.service.AppMirrorService;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.common.TreeNode;
import com.zjy.jopm.base.org.entity.OrganizationEntity;
import com.zjy.jopm.base.quiUtil.QuiUtils;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;

@Service("appMirrorService")
@Transactional
public class AppMirrorServiceImpl extends BaseServiceimpl implements AppMirrorService {

	@Override
	public Map<String, Object> getAppMirrorQuiGrid(AppMirrorEntity appMirror,
			int pageNo, int pageSize, String sort, String direction)
			throws JumpException {
		
		String hql="FROM AppMirrorEntity WHERE 1=1 " ;
		List<Object> param = new ArrayList<Object>();
		
		//判断是否根据应用ID查询
		if(StringUtil.isNotEmpty(appMirror.getApplicationEntity().getId())){
			hql += " AND applicationEntity.id = ?";
			param.add(appMirror.getApplicationEntity().getId());
		}
		
		//判断是否根据所属组织机构查询
		if(StringUtil.isNotEmpty(appMirror.getOrganizationEntity().getId())){
			hql += " AND organizationEntity.id = ?";
			param.add(appMirror.getOrganizationEntity().getId());
		}
		
		QueryCondition queryCondition= new QueryCondition(hql, param, pageNo, pageSize);
		PageList pageList=super.queryListByHqlWithPage(queryCondition);// 结果集
		
		return QuiUtils.quiDataGird(pageList, pageList.getCount());
	}

	

	@Override
	public AjaxJson addAppMirror(String orgIds, String appId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		// 在更新数据之前，获取目标应用已经被开通到了哪些机构
		String orgIdsHql = "SELECT organizationEntity.id FROM AppMirrorEntity WHERE 1=1 AND applicationEntity.id=? and isEffective = 'y'";
		List<String> orgIdsIndb = super.queryListByHql(orgIdsHql, appId);
		if (orgIdsIndb == null) {
			orgIdsIndb = new ArrayList<String>();
		}
		
		
		// 准备集合：需要新开通到的机构
		List<String> addOrgIds = new ArrayList<String>();
		
		// 准备集合：解除了哪些机构的开通关系
		// 预置所有机构开通数据都应当删除，通过后面的循环删除没有变化的机构，留下真正要删除的机构。
		List<String> delOrgIds = new ArrayList<String>(orgIdsIndb);
		
		// 参数字符串转集合
		String[] paramOrgIdArray = null;
		
		// 通过这个判断，将确定需要新增哪些机构，删除哪些机构。
		if (StringUtil.isNotEmpty(orgIds)) {
			paramOrgIdArray = orgIds.split(",");
			for (String string : paramOrgIdArray) {
				
				if (StringUtil.isEmpty(string)) {
					continue;
				}
				
				if (orgIdsIndb.contains(string)) {
					
					// 这个机构保留，从预置的删除集合中剔除
					delOrgIds.remove(string);
					
				} else {
					
					// 新开通一个机构
					addOrgIds.add(string);
				}
			}
			
		}
		
		// 处理新增开通机构
		for (String orgId : addOrgIds) {
			//判断AppMirrorEntity是否有此数据,有就改变状态,没有就插入数据
			String hql = "from AppMirrorEntity where organizationEntity.id = ? and applicationEntity.id = ?";
			List<Object> appMiList = super.queryListByHql(hql, orgId, appId);
			if(appMiList != null && appMiList.size() != 0){
				hql = "update AppMirrorEntity a set a.isEffective = 'y' where organizationEntity.id = ? and applicationEntity.id = ?" ;
				super.runUpdateByHql(hql, orgId, appId);
			}else{
				// TODO: 下面这段逻辑需要优化，我是照搬过来没做调整。
				AppMirrorEntity appMirror = new AppMirrorEntity();
				//随机生成uuid
				int uuCode = UUID.randomUUID().toString().hashCode();
				
				//有可能是负数
				if(uuCode<0){
					uuCode = -uuCode;
				}
				// 0 代表前面补充0     
				// 4 代表长度为4     
				// d 代表参数为正数型
				String code = String.format("%015d", uuCode);
				appMirror.setCode(code);
				OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, orgId);
				if(null == organizationEntity){
					ajaxJson.setSuccess(false);
					ajaxJson.setMessage("请选机构不存在，无法继续保存操作！");
					return ajaxJson;
				}
				appMirror.setOrganizationEntity(organizationEntity);
				
				ApplicationEntity application = super.expandEntity(ApplicationEntity.class, appId);
				if(null == application){
					ajaxJson.setSuccess(false);
					ajaxJson.setMessage("未选择应用，无法继续保存操作！");
					return ajaxJson;
				}
				appMirror.setApplicationEntity(application);
				Boolean flag = esitsAppMiior(orgId,appId);
				if(flag){
					this.insertEntity(appMirror);
				}
			}
			
			
		}
		
		
		// 处理要删除的机构，实际上就是收回应用在这个机构已经分配出去的权限，再删除已经开通的关系
		
		// 查一下应用都有哪些功能
//		String appFunctionIdsHql = "SELECT fe.id FROM FunctionEntity fe where fe.applicationEntity.id = ?";
//		List<String> appFunctionIds = super.queryListByHql(appFunctionIdsHql, appId);
		
//		if (CollectionUtil.isNotEmpty(appFunctionIds) && CollectionUtil.isNotEmpty(delOrgIds)) {
//			
//			// 查到权限，删除
//			String roleFunctionRelHql = "FROM RoleFunctionRelationEntity rfre where rfre.functionEntity.id in (:functionIds) and rfre.roleEntity.organizationEntity.id in (:delOrgIds)";
//			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("functionIds", appFunctionIds);
//			param.put("delOrgIds", delOrgIds);
//			List<RoleFunctionRelationEntity> roleFunctionRel = super.queryListByHql(roleFunctionRelHql, param);
//			super.deleteEntityBatch(roleFunctionRel);
//			
//		}
		
		if (CollectionUtil.isNotEmpty(delOrgIds)) {
			String appOrgRelHql = "update AppMirrorEntity ame set ame.isEffective = 'n' where ame.organizationEntity.id in (:delOrgIds) and ame.applicationEntity.id = :appId";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("delOrgIds", delOrgIds);
			param.put("appId", appId);
			super.runUpdateByHql(appOrgRelHql, param);
//			List<Object> appOrgRel = super.queryListByHql(appOrgRelHql, param);
//			super.deleteEntityBatch(appOrgRel);
		}
			
		ajaxJson.setSuccess(true);
		ajaxJson.setMessage("操作成功！");
		
		
		/*String ids[] = null;
		if(StringUtil.isNotEmpty(orgIds)){
			ids = orgIds.split(",");
		}else{
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("请选择机构！");
			return ajaxJson;
		}
		if(null!=ids){
			String hql = "FROM AppMirrorEntity WHERE 1=1 AND applicationEntity.id=? ";
			List<AppMirrorEntity> appMirrorList = new ArrayList<AppMirrorEntity>();
			appMirrorList = this.queryListByHql(hql, new Object[]{appId});
			this.deleteEntityBatch(appMirrorList);
		}
		for (String orgId : ids) {
			AppMirrorEntity appMirror = new AppMirrorEntity();
			//随机生成uuid
			int uuCode = UUID.randomUUID().toString().hashCode();
			
			//有可能是负数
			if(uuCode<0){
				uuCode = -uuCode;
			}
			// 0 代表前面补充0     
	        // 4 代表长度为4     
	        // d 代表参数为正数型
			String code = String.format("%015d", uuCode);
			appMirror.setCode(code);
			OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, orgId);
			if(null == organizationEntity){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("请选机构不存在，无法继续保存操作！");
				return ajaxJson;
			}
			appMirror.setOrganizationEntity(organizationEntity);
			
			ApplicationEntity application = super.expandEntity(ApplicationEntity.class, appId);
			if(null == application){
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("未选择应用，无法继续保存操作！");
				return ajaxJson;
			}
			appMirror.setApplicationEntity(application);
			Boolean flag = esitsAppMiior(orgId,appId);
			if(flag){
				this.insertEntity(appMirror);
			}
		}*/
		return ajaxJson;
	}
	
	private Boolean esitsAppMiior(String orgId, String appId) {
		Boolean flag = false;
		String hql = "from AppMirrorEntity where 1=1 ";
		List<Object> param = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(appId)){
			hql +=" and applicationEntity.id =?";
			param.add(appId);
		}
		if(StringUtil.isNotEmpty(orgId)){
			hql +=" and organizationEntity.id =?";
			param.add(orgId);
		}
		AppMirrorEntity appMirror = new AppMirrorEntity();
		appMirror = expandEntityByHql(hql, param);
		if(null != appMirror){
			flag = false;
		}else{
			flag = true;
		}
		return flag;
	}



	@Override
	public AjaxJson insertAppMirror(AppMirrorEntity appMirror) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();

		String organizationId = appMirror.getOrganizationEntity().getId();
		OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, organizationId);
		if(null == organizationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("机构不存在，无法继续保存操作");
			return ajaxJson;
		}
		
		appMirror.setOrganizationEntity(organizationEntity);
		
		String appId = appMirror.getApplicationEntity().getId();
		ApplicationEntity application = super.expandEntity(ApplicationEntity.class, appId);
		
		if(null == application){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续保存操作");
			return ajaxJson;
		}
		
		appMirror.setApplicationEntity(application);
		
		//随机生成uuid
		int uuCode = UUID.randomUUID().toString().hashCode();
		
		//有可能是负数
		if(uuCode<0){
			uuCode = -uuCode;
		}
		
		// 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
		String code = String.format("%015d", uuCode);
		
		appMirror.setCode(code);
		
		Boolean success = super.insertEntity(appMirror);
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("保存过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson updateAppMirror(AppMirrorEntity appMirrorEntity) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		AppMirrorEntity appMirror = super.expandEntity(AppMirrorEntity.class, appMirrorEntity.getId());
		
		if (null == appMirror) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("镜像不存在，无法继续更改操作");
			return ajaxJson;
		}
		
		String organizationId = appMirror.getOrganizationEntity().getId();
		OrganizationEntity organizationEntity = super.expandEntity(OrganizationEntity.class, organizationId);
		if(null == organizationEntity){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("机构不存在，无法继续保存操作");
			return ajaxJson;
		}
		
		appMirrorEntity.setOrganizationEntity(organizationEntity);
		
		String appId = appMirror.getApplicationEntity().getId();
		ApplicationEntity application = super.expandEntity(ApplicationEntity.class, appId);
		
		if(null == application){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用不存在，无法继续保存操作");
			return ajaxJson;
		}
		
		appMirrorEntity.setApplicationEntity(application);
		
		JumpBeanUtil.copyBeanNotNull2Bean(appMirrorEntity, appMirror);
		
		Boolean success = super.updateEntity(application);
		
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("更改过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		return ajaxJson;
	}

	@Override
	public AjaxJson expandAppMirror(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		AppMirrorEntity appMirror = super.expandEntity(AppMirrorEntity.class, id);
		if(null == appMirror){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用镜像不存在，可能已删除");
			return ajaxJson;
		}
		
		ajaxJson.setObj(appMirror);
		
		return ajaxJson;
	}

	@Override
	public AjaxJson deleteAppMirror(String id) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		
		AppMirrorEntity appMirror = super.expandEntity(AppMirrorEntity.class, id);
		if(null == appMirror){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("应用镜像不存在，可能已删除");
			return ajaxJson;
		}
		
		boolean success = super.deleteEntity(appMirror);
		
		if(!success){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("删除过程中遇到错误，请稍后重试");
			return ajaxJson;
		}
		
		return ajaxJson;
	}

	@Override
	public AjaxJson OrganizationServiceTree(OrganizationEntity organizationEntity, SessionInfo sessionInfo,String orgID) {
		AjaxJson j=new AjaxJson();
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		List<Object> param=new ArrayList<Object>();
		
//		if(orgID == null){
//			String hql = "from OrganizationEntity where parentOrganizationEntity.id is null";
//			List<OrganizationEntity> organizationList = super.queryListByHql(hql);		
//			for (OrganizationEntity org : organizationList) {
//				TreeNode node = new TreeNode();
//				node.setId(org.getId());
//				node.setName(org.getName());
//				node.setIsParent(false);
//				node.setNocheck(false);
//				nodes.add(node);
//			}
//		}
		if(sessionInfo!=null){
			//如果当前用户为特殊用户 刷新根机构
			List<OrganizationEntity> organizationList=new ArrayList<OrganizationEntity>();
			String hql = "";
			if(orgID == null){
				organizationList = super.queryListByClass(OrganizationEntity.class);
			}else{			
				hql="from OrganizationEntity where 1=1";
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
						param.add(orgID);
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
		   	      node.setNocheck(false);
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



	@Override
	public AjaxJson getOrgList(String appId, String orgId)throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		Map<String, Object> orgIdMap = new HashMap<String, Object>();
		List<String> orgIds = new ArrayList<String>();
		String hql = "from AppMirrorEntity where 1=1 and isEffective <> 'n'";
		List<Object> param = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(appId)){
			hql +=" and applicationEntity.id=?";
		}
		param.add(appId);
		List<AppMirrorEntity> appMirrorList = new ArrayList<AppMirrorEntity>();
		appMirrorList = super.queryListByHql(hql, param);
		if(null!=appMirrorList){
			for (AppMirrorEntity appMirror : appMirrorList) {
				orgIds.add(appMirror.getOrganizationEntity().getId()); 
			}
			orgIdMap.put("checkedOrgIds",orgIds);
			ajaxJson.setAttributes(orgIdMap);
		}
		return ajaxJson;
	}



	@Override
	public AjaxJson appMirrorCancel(String appId) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		Boolean flag = false; 
		List<Object> param = new ArrayList<Object>();
		String hql = "from AppMirrorEntity where 1=1";
		if(StringUtil.isNotEmpty(appId)){
			hql +=" and applicationEntity.id=?";
		}
		param.add(appId);
		ApplicationEntity app =  super.expandEntity(ApplicationEntity.class, appId);
		List<AppMirrorEntity> appMirrorList = new ArrayList<AppMirrorEntity>();
		appMirrorList = super.queryListByHql(hql, param);

		// 新增处理逻辑：清理游客桌面数据，清理服务接口与权限数据
		cancelVisitorDesktop(appId);
		cancelServiceAndAuth(appId);
		
		flag = cancelOrganization(appId);
		if(flag){
			flag = this.deleteEntityBatch(appMirrorList);
			if(flag){
				flag = this.deleteEntity(app);
			}
		}else{
			ajaxJson.setSuccess(flag);
			ajaxJson.setMessage("注销失败！请稍后尝试");
		}
		
		return ajaxJson;
	}
	
	private void cancelServiceAndAuth(String appId) {
		
		List<Object> saeList = this.queryListByHql("FROM ServiceAuthEntity sae where sae.applicationEntity.id = ?", appId);
		if (CollectionUtil.isNotEmpty(saeList)) {
			this.deleteEntityBatch(saeList);
		}
		
		List<Object> saeList2 = this.queryListByHql("FROM ServiceAuthEntity sae where sae.serviceInterfaceEntity.applicationEntity.id = ?", appId);
		if (CollectionUtil.isNotEmpty(saeList2)) {
			this.deleteEntityBatch(saeList2);
		}
		
		
		List<Object> sieList = this.queryListByHql("FROM ServiceInterfaceEntity sie where sie.applicationEntity.id = ?", appId);
		if (CollectionUtil.isNotEmpty(sieList)) {
			this.deleteEntityBatch(sieList);
		}
		
	}

	private Boolean cancelOrganization(String appId) {
		Boolean flag = false; 
		List<FunctionEntity> functionList = this.queryListByHql("FROM FunctionEntity where 1=1 and applicationEntity.id=?", new Object[]{appId});
		if(functionList.size()>0){
			for (FunctionEntity function : functionList) {
				String hql = "FROM RoleFunctionRelationEntity WHERE 1=1 AND functionEntity.id=?";
				List<RoleFunctionRelationEntity> roleFunctionList = this.queryListByHql(hql, new Object[]{function.getId()});
				if(null!=roleFunctionList){
					flag = this.deleteEntityBatch(roleFunctionList);
					if(!flag){
						break;
					}
				}
			}
			if(flag){
				flag = this.deleteEntityBatch(functionList);
			}
		}else{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 注销应用时，清理游客桌面的数据
	 * @param appId 应用ID
	 */
	private void cancelVisitorDesktop(String appId) {
		
		List<Object> visitorEntityList = this.queryListByHql("FROM VisitorEntity ve where ve.applicationEntity.id = ?", appId);
		if (CollectionUtil.isNotEmpty(visitorEntityList)) {
			this.deleteEntityBatch(visitorEntityList);
		}
		
	}

}
