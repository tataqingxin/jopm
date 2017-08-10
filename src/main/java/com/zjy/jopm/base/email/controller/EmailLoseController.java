package com.zjy.jopm.base.email.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unistc.core.common.controller.BaseController;
import com.unistc.core.common.model.AjaxJson;
import com.unistc.exception.JumpException;
import com.zjy.jopm.base.email.service.EmailLoseService;

@Controller
@RequestMapping("/emailLoseController")
public class EmailLoseController extends BaseController{
	
	@Autowired
	private EmailLoseService emailLoseService;
	
	@RequestMapping(value = "/validateEmailLose", method = RequestMethod.POST)
	@ResponseBody 
	public AjaxJson validateEmailLose(HttpServletRequest request, HttpServletResponse response) throws JumpException,Exception{
		String emailId = request.getParameter("emailId");
		AjaxJson json = emailLoseService.expandEmailLose(emailId);
		return json;
	}
	
}
