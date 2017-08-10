package com.zjy.jopm.base.config.controller;

import java.util.Map;

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
import com.unistc.utils.JumpBeanUtil;
import com.unistc.utils.StringUtil;
import com.zjy.jopm.base.common.Constants;
import com.zjy.jopm.base.config.entity.Config;
import com.zjy.jopm.base.config.service.ConfigService;

/**
 * 业务系统配置
 * 
 * @author root
 * 
 */
@Controller
@RequestMapping("/configController")
public class ConfigController extends BaseController {

	@Autowired
	private ConfigService<Config> configService;

	/**
	 * 
	 * @Title: roleList
	 * @Description: [查询列表]
	 * @param roleEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws JumpException
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> roleList(Config obj, HttpServletRequest request,
			HttpServletResponse response) throws JumpException {
		int pageNo = 1;
		if (StringUtil.isNotEmpty(request.getParameter("pager.pageNo"))) {
			pageNo = Integer.parseInt(request.getParameter("pager.pageNo"));
		}
		int pageSize = 10;
		if (StringUtil.isNotEmpty(request.getParameter("pager.pageSize"))) {
			pageNo = Integer.parseInt(request.getParameter("pager.pageSize"));
		}

		String sort = request.getParameter("sort");
		if (StringUtil.isEmpty(sort)) {
			sort = "id";
		}

		String direction = request.getParameter("direction");
		if (StringUtil.isEmpty(direction)) {
			direction = Constants.DESC;
		}

		return this.configService.getGrid(obj, pageNo, pageSize, sort,
				direction);
	}

	/**
	 * 
	 * @Description: [新增实体]
	 * @throws JumpException
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson add(Config obj, HttpServletRequest request,
			HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		boolean isExists = configService.checkExists(obj);

		if (isExists) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMessage("键已经存在！");
			return ajaxJson;
		}
		boolean res = configService.insertEntity(obj);
		ajaxJson.setSuccess(res);
		ajaxJson.setMessage("操作成功！");
		return ajaxJson;
	}

	/**
	 * 
	 * @Description: [修改实体]
	 * @throws JumpException
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson updateRole(Config obj, HttpServletRequest request,
			HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();

		Config curr = configService.expandEntity(Config.class, obj.getId());
		
		if(!curr.getKey().equals(obj.getKey())){
			
			boolean isExists = configService.checkExists(obj);
			if (isExists) {
				curr = null;
				ajaxJson.setSuccess(false);
				ajaxJson.setMessage("键已经存在！");
				return ajaxJson;
			}
		}
		curr.setId(null);
		JumpBeanUtil.copyBeanNotNull2Bean(obj, curr);
		boolean res = configService.updateEntity(curr);

		ajaxJson.setSuccess(res);
		return ajaxJson;
	}

	/**
	 * 
	 * @Title:
	 * @Description: [删除实体]
	 * @throws JumpException
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson del(Config obj, HttpServletRequest request,
			HttpServletResponse response) throws JumpException {
		AjaxJson ajaxJson = new AjaxJson();
		boolean res = configService.deleteEntityById(Config.class, obj.getId());
		ajaxJson.setSuccess(res);
		return ajaxJson;
	}
}
