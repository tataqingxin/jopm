/** 
 * @Description:[权限拦截器]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.interceptor.AuthInterceptor.java
 * @ClassName:AuthInterceptor
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月18日 上午9:59:49
 * @UpdateUser:Lu Guoqiang
 * @UpdateDate:2016年5月18日 上午9:59:49  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.utils.ContextHolderUtil;
import com.zjy.jopm.base.app.entity.FunctionEntity;
import com.zjy.jopm.base.app.entity.ResourceEntity;
import com.zjy.jopm.base.app.service.ext.AppExtService;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;

/**
 * @ClassName: AuthInterceptor 
 * @Description: [权限拦截器] 
 * @author Lu Guoqiang
 * @date 2016年5月18日 上午9:59:49 
 * @since JDK 1.6 
 */
public class AuthInterceptor implements HandlerInterceptor {
	
	@Autowired
	private AppExtService appExtService;
	
	private List<String> excludeUrls;

	/** 
	 * @Title: preHandle
	 * @Description: [功能描述]
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String requestUrl= getRequestPath(request);// 用户访问的资源地址
		
		if (excludeUrls.contains(requestUrl)) {
			//如果该请求在白名单钟，直接返回true
			return true;
		}
		
		HttpSession session = ContextHolderUtil.getSession();
		SessionInfo sessioninfo = (SessionInfo) session.getAttribute(Globals.USER_SESSION);
		if (null == sessioninfo || null == sessioninfo.getUser()) {
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("提醒：访问受限，用户未登陆或会话已失效");
			response.getWriter().write(JSONObject.toJSONString(ajaxJson));
			return false;
		}
		
		if (sessioninfo.getUser()!=null ) {
			//特殊用户
			if (sessioninfo.getIdentity() == Constants.SPECIAL_ACCOUNT) {
				return true;
			}
			
			//正常用户
			if(!hasVisitAuth(requestUrl, sessioninfo)){
				AjaxJson ajaxJson = new AjaxJson();
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("提醒：访问受限，用户权限不足");
				response.getWriter().write(JSONObject.toJSONString(ajaxJson));
				//request.getRequestDispatcher("/webpage/common/noAuth.html").forward(request, response);
				return false;
			}
		}
		
		return true;
	}

	/** 
	 * @Title: postHandle
	 * @Description: [功能描述]
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception 
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/** 
	 * @Title: afterCompletion
	 * @Description: [功能描述]
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception 
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @Title: hasVisitAuth 
	 * @Description: [判断用户是否有访问资源地址的权限]
	 * @param requestUrl
	 * @param sessionInfo
	 * @return
	 */
	private boolean hasVisitAuth(String requestUrl, SessionInfo sessionInfo){
		// 是否是功能表中管理的URL
		boolean isFunctionUrl = false;
		String hql = "FROM FunctionEntity WHERE 1=1";
		List<FunctionEntity> functionEntityList = this.appExtService.queryListByHql(hql);
		for (FunctionEntity functionEntity : functionEntityList) {
			if(functionEntity.getUrl().contains(requestUrl)){
				isFunctionUrl = true;
				break;
			}
		}
		
		// 是否是功能资源表中管理的URL
		boolean isResourceUrl = false;
		hql = "FROM ResourceEntity WHERE 1=1";
		List<ResourceEntity> resourceEntityList = this.appExtService.queryListByHql(hql);
		for (ResourceEntity resourceEntity : resourceEntityList) {
			if(resourceEntity.getUrl().contains(requestUrl)){
				isResourceUrl = true;
				break;
			}
		}
		
		// 未绑定功能菜单和功能按钮的资源地址，可以直接访问
		if (!isFunctionUrl && !isResourceUrl) {
			return true;
		} 
		
		if (isFunctionUrl) {
			List<String> functionList = sessionInfo.getFunctionList();
			if (null != functionList && functionList.contains(requestUrl)) {
				return true;
			}
		}
		
		if (isResourceUrl) {
			List<String> resourceList = sessionInfo.getResourceList();
			if (null != resourceList && resourceList.contains(requestUrl)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 获得请求路径
	 * @param request
	 * @return
	 */
	private static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		if (requestPath.indexOf("?") > -1) {
			requestPath = requestPath.substring(0, requestPath.indexOf("?"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}

	/**
	 * @return the excludeUrls
	 */
	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	/**
	 * @param excludeUrls the excludeUrls to set
	 */
	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	
}
