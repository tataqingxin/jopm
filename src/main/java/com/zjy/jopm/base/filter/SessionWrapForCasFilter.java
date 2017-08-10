package com.zjy.jopm.base.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unistc.utils.Platform;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.app.entity.OperationResourceRelationEntity;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.role.entity.RoleEntity;
import com.zjy.jopm.base.role.entity.RoleFunctionRelationEntity;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.ext.UserExtService;

@Service
public class SessionWrapForCasFilter implements Filter {

	@Autowired
	private UserExtService userExtService;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		// HttpServletResponse response = (HttpServletResponse) res;
		
		if (userExtService == null) {
			userExtService = Platform.getBean("userExtService");
		}
		
		AttributePrincipal userMsgInCAS = (AttributePrincipal) request.getUserPrincipal();
		if (userMsgInCAS == null || StringUtil.isEmpty(userMsgInCAS.getName()) || userMsgInCAS.getAttributes().isEmpty()) {
			// there is no user msg in cas session, let it go.
			chain.doFilter(req, res);
			return;
		} else if(request.getSession().getAttribute(Globals.USER_SESSION) == null) {
			
			// simulator to login.
			
			String userName = userMsgInCAS.getName();
			
			// Map<String, Object> message = userMsgInCAS.getAttributes();

			SessionInfo sessionInfo = new SessionInfo();
			
			UserEntity userEntity = userExtService.expandEntityByHql("from UserEntity where userName = ?", userName);
			if (userEntity == null || StringUtil.isEmpty(userEntity.getId())) {
				// it is impossible
				chain.doFilter(req, res);
				return;
			}
			
			// @TODO: CONTACT WITH LUGUOQIANG FOR THE FOLLOWING CODE!!!
			
			sessionInfo.setUser(userEntity);
			sessionInfo.setIdentity(Constants.NORMAL_ACCOUNT);
			// 防止部分用户没有所属机构造成的空指针异常
			if(userEntity.getOrganizationEntity() != null){
				sessionInfo.setOrganizationId(userEntity.getOrganizationEntity().getId());
			}
			List<String> functionList = new ArrayList<String>();
			List<String> resourceList = new ArrayList<String>();
			Map<String, Object> _param = null;

			//获取用户拥有的所有角色
			String hql = "SELECT DISTINCT roleEntity.id FROM RoleUserRelationEntity WHERE userEntity.id = ?";
			List<String> roleIds = userExtService.queryListByHql(hql, userEntity.getId());
			if (null != roleIds && roleIds.size() == 0) {
				
				// impossible too.
				
				chain.doFilter(req, res);
				return;
			}
			
			//设置用户角色
			_param = new HashMap<String, Object>();
			_param.put("roleIds", roleIds.size() > 0 ? roleIds : "");
			hql = "FROM RoleEntity WHERE id IN (:roleIds)";
			List<RoleEntity> roleEntityList = userExtService.queryListByHql(hql, _param);
			sessionInfo.setRoleList(roleEntityList);
			
			for (String roleId : roleIds) {
				List<String> _operationIds = new ArrayList<String>();// 操作集合
				
				_param = new HashMap<String, Object>();
				_param.put("roleId", roleId);
				hql = "FROM RoleFunctionRelationEntity WHERE roleEntity.id = :roleId";
				List<RoleFunctionRelationEntity> roleFunctionRelationEntityList = userExtService.queryListByHql(hql, _param);
				for (RoleFunctionRelationEntity roleFunctionRelationEntity : roleFunctionRelationEntityList) {
					if (null != roleFunctionRelationEntity.getFunctionEntity()) {
						String functionUrl = roleFunctionRelationEntity.getFunctionEntity().getUrl();
						if (!functionList.contains(functionUrl)) {
							functionList.add(functionUrl);// 设置用户功能操作权限
						}
					}
					if (null != roleFunctionRelationEntity.getOperationEntity()) {
						String operationId = roleFunctionRelationEntity.getOperationEntity().getId();
						if (!_operationIds.contains(operationId)) {
							_operationIds.add(operationId);
						}
					}
				}
				
				if (_operationIds.size() > 0) {
					_param.put("operationIds", _operationIds.size()>0?_operationIds:"");
					hql = "FROM OperationResourceRelationEntity WHERE operationEntity.id IN (:operationIds)";
					List<OperationResourceRelationEntity> operationResourceRelationEntityList = userExtService.queryListByHql(hql, _param);
					for (OperationResourceRelationEntity operationResourceRelationEntity : operationResourceRelationEntityList) {
						if (null != operationResourceRelationEntity.getResourceEntity()) {
							resourceList.add(operationResourceRelationEntity.getResourceEntity().getUrl());// 设置用户功能操作权限
						}
					}
				} 
			}
			sessionInfo.setFunctionList(functionList);
			sessionInfo.setResourceList(resourceList);

			request.getSession().setAttribute(Globals.USER_SESSION, sessionInfo);
			
			// @TODO: CONTACT WITH LUGUOQIANG FOR THE BELOW CODE!!!
			
			
			chain.doFilter(req, res);
			return;
		} else {
			// there is general user message in session, let it go.
			chain.doFilter(req, res);
			return;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	public UserExtService getUserExtService() {
		return userExtService;
	}

	public void setUserExtService(UserExtService userExtService) {
		this.userExtService = userExtService;
	}

}
