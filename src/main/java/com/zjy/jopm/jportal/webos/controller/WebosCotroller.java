package com.zjy.jopm.jportal.webos.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.unistc.core.common.model.AjaxJson;
import com.unistc.utils.ContextHolderUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Globals;
import com.zjy.jopm.base.common.SessionInfo;
import com.zjy.jopm.base.user.entity.UserEntity;
import com.zjy.jopm.base.user.service.ext.UserExtService;
import com.zjy.jopm.jportal.webos.service.WebosService;

/**
 * @Description: 提供为webos调用的接口
 * @author: fan.ysh 2014-10-21
 * @version V1.0
 */
@Controller
@RequestMapping("/webosController")
public class WebosCotroller {
	
	@Autowired
	private WebosService webosService;
	

	@Autowired
	private UserExtService userExtService;
	
	/**
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/checkUserIsAdmin")
	@ResponseBody
	public String checkUserIsAdmin(String userId) {
		String isAdmin = "";
		if(StringUtil.isNotEmpty(userId)){
			isAdmin= webosService.checkUserIsAdmin(userId);
		}
		return isAdmin;
	}
	
	/**
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public AjaxJson getUserInfo(HttpServletRequest request) {
		String userId = "";
		HttpSession session = ContextHolderUtil.getSession();
		SessionInfo sessionInfo = null;
		if (null != session) {
			sessionInfo= (SessionInfo)session.getAttribute(Globals.USER_SESSION);
			userId = sessionInfo.getUser().getId();
		}
		AjaxJson json = new AjaxJson();
		UserEntity user = webosService.expandEntity(UserEntity.class, userId);
		if(user==null){
			json.setSuccess(false);
			json.setMessage("没有找到对象");
		}else{
			json.setSuccess(true);
			json.setObj(user);
		}
		List<String> ids=userExtService.getDepartIdsByUser(user.getId());
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("departIds", ids);
		map.put("organizationId", user.getOrganizationEntity().getId());
		json.setAttributes(map);
		return json;
	}
	
	/**
	 * @TODO 获取菜单组
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getBaseMenu")
	@ResponseBody
	public Object getBaseMenu(HttpServletRequest request, String userId,
			String departId, String systemId, String mf) {
		return webosService.getBaseMenu(request,userId, systemId,mf, departId);
	}
	
	/**
	 * @TODO 获取菜单组
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllMenu")
	@ResponseBody
	public Object getAllMenu(HttpServletRequest request, String userId,
			String departId, String systemId, String mf, String deskfunctionIds) {
		return webosService.getAllMenu(request,userId, systemId,departId,deskfunctionIds);
	}
	
	@RequestMapping(value = "/checkLeftMenuIsAuthority")
	@ResponseBody
	public Object checkLeftMenuIsAuthority(HttpServletRequest request,
			String userId, String departId, String systemId, String mf,
			String functionIds) {
		
		return webosService.checkLeftMenuIsAuthority(request,userId,departId,systemId,mf,functionIds);
	}
	
	@RequestMapping(value = "/checkDeskMenuIsAuthority")
	@ResponseBody
	public Object checkDeskMenuIsAuthority(HttpServletRequest request,
			String userId, String departId, String systemId, String mf,
			String functionIds) {
		return webosService.checkDeskMenuIsAuthority(request,userId,departId,systemId,mf,functionIds);
	}
	@RequestMapping(value = "/searchClause")
	@ResponseBody
	public Object searchClause(HttpServletRequest request, String userId,
			String departId, String systemId, String mf, String functionIds) {
		return webosService.searchClause(request,userId,departId,systemId,mf,functionIds);
	}
	
	@RequestMapping(value = "/downloadApp")
	@ResponseBody
	public Object downloadApp(HttpServletRequest request ) {
		JSONObject requestBody = new JSONObject();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
		        StringBuilder sb = new StringBuilder();
		        while((line = br.readLine())!=null){
		            sb.append(line);
		        }
		        requestBody = JSONObject.fromObject(sb.toString());
		        // 将资料解码
		} catch (IOException e) {
			e.printStackTrace();
		}
		return webosService.downloadApp(requestBody);
	}
}
