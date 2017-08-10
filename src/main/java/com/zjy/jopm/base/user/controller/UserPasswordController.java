package com.zjy.jopm.base.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unist.util.StringUtil;
import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.zjy.jopm.base.user.service.UserPasswordService;
import com.zjy.jopm.sdk.service.OpenParamService;
import com.zjy.jopm.sdk.util.CasSessionUtil;

@Controller
@RequestMapping("/userPasswordController")
public class UserPasswordController extends BaseController {

	@Autowired
	private UserPasswordService userPasswordService;

	@Autowired
	private OpenParamService openParamService;
	
	private static final String KEY_SYSTEM_URL_LOGOUT = "SYSTEM_URL_LOGOUT";

	@ResponseBody
	@RequestMapping(value = "/reset")
	public Object resetPassword(HttpServletRequest request, HttpServletResponse response) {

		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(false);
		
		CasSessionUtil util = new CasSessionUtil(request);
		
		if (StringUtil.isEmpty(util.getUserId())) {
			ajaxJson.setMessage("用户信息为空，请刷新重试");
			return ajaxJson;
		}

		String passwordOld = request.getParameter("passwordOld");
		String passwordNew = request.getParameter("passwordNew");

		if (StringUtil.isEmpty(passwordNew) || StringUtil.isEmpty(passwordOld)) {
			ajaxJson.setMessage("关键参数为空，请重试");
			return ajaxJson;
		}

		return userPasswordService.updateUserPasswordRecord(util.getUserId(), passwordOld, passwordNew);
	}

	@RequestMapping(value = "logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Cookie[] cookies = request.getCookies();
		for(int i=0;i<cookies.length;i++)     {   
			Cookie cookie = new Cookie(cookies[i].getName(), null);   
			cookie.setMaxAge(0);   
			cookie.setPath(cookies[i].getPath());
			response.addCookie(cookie);   
		}
		request.getSession().invalidate();
		response.sendRedirect(openParamService.getValue(KEY_SYSTEM_URL_LOGOUT));
	}

	public UserPasswordService getUserPasswordService() {
		return userPasswordService;
	}

	public void setUserPasswordService(UserPasswordService userPasswordService) {
		this.userPasswordService = userPasswordService;
	}

	public OpenParamService getOpenParamService() {
		return openParamService;
	}

	public void setOpenParamService(OpenParamService openParamService) {
		this.openParamService = openParamService;
	}

}
