/** 
 * @Description:[登陆功能]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.login.controller.LoginController.java
 * @ClassName:LoginController
 * @Author:Lu guoqiang 
 * @CreateDate:2016年5月15日 上午2:15:48
 * @UpdateUser: Lu guoqiang 
 * @UpdateDate:2016年5月15日 上午2:15:48  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.exception.JumpException;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.ResourceUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.ext.UserExtService;

/**
 * @ClassName: LoginController 
 * @Description: [登录功能] 
 * @author Lu guoqiang
 * @date 2016年5月15日 上午2:15:48 
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController {
	
	@Autowired
	private UserExtService userExtService;

	/**
	 * 
	 * @Title: _login 
	 * @Description: [登陆]
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/login/user", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson _login(HttpServletRequest request, HttpServletResponse response) throws JumpException{
		AjaxJson ajaxJson = new AjaxJson();
		String loginName = request.getParameter("username");// 用户名
		String loginPwd = request.getParameter("password");// 密码
		//检查是否为cas登录
		org.jasig.cas.client.authentication.AttributePrincipal principal = (org.jasig.cas.client.authentication.AttributePrincipal)request.getUserPrincipal();
		if(principal!=null){
		    @SuppressWarnings("unchecked")
		    Map<String, Object> map = principal.getAttributes();
			HttpSession session = ContextHolderUtil.getSession();
			SessionInfo sessionInfo = new SessionInfo();
			UserEntity userEntity = userExtService.expandEntity(UserEntity.class, map.get("id").toString());
			userEntity.setUserName(userEntity.getUserName());
			sessionInfo.setUser(userEntity);
			sessionInfo.setIdentity(Constants.NORMAL_ACCOUNT);
			sessionInfo.setOrganizationId(userEntity.getOrganizationEntity().getId());
			session.setAttribute(Globals.USER_SESSION, sessionInfo);
			return ajaxJson;
		}
		
		
		String captcha = null;// 验证码

		HttpSession session = ContextHolderUtil.getSession();
		//是否对验证码校验
		final Boolean enableRandCode = Boolean.valueOf(ResourceUtil.getConfigByName("enableRandCode")); 
		if(enableRandCode){
			captcha = request.getParameter("captcha");
			if(StringUtil.isEmpty(captcha)){
				ajaxJson.setMessage("验证码为空");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			} else if (!captcha.equalsIgnoreCase(String.valueOf(session.getAttribute(Constants.SESSION_KEY_OF_RAND_CODE)))) {
				ajaxJson.setMessage("验证码错误");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		}

		if(StringUtil.isEmpty(loginName) || StringUtil.isEmpty(loginPwd)){
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("用户名或密码为空");
			return ajaxJson;
		}
		
		return this.userExtService.login(loginName, loginPwd);
	}

	/**
	 * 
	 * @Title: _logout 
	 * @Description: [登出]
	 * @param request
	 * @param response
	 * @throws JumpException
	 */
	@RequestMapping(value = "/logout/user")
	@ResponseBody
	public AjaxJson _logout(HttpServletRequest request, HttpServletResponse response) throws JumpException{
		return this.userExtService.logout();
	}
	@RequestMapping(value = "/forgetPwd/sendMail")
	@ResponseBody
	public AjaxJson sendUpdPwdUrlMail(HttpServletRequest request, HttpServletResponse response) throws JumpException{
		String userName = request.getParameter("userName");
		return this.userExtService.sendUpPwdMail(userName,request);
	}
	
}
