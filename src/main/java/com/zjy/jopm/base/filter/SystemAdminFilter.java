package com.zjy.jopm.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SystemAdminFilter implements Filter {
	
	private final static String SCOPE_KEY = "SYSTEM_USER_FLAG";
	private final static String SCOPE_VALUE = "1";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		
		response.setHeader("P3P","CP=CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR");  
		
		HttpSession session = request.getSession();
		
		String value = request.getParameter(SCOPE_KEY);
		Object object = session.getAttribute(SCOPE_KEY);
		
		if (object != null) {
			doDispather(request, response);
		} else if (SCOPE_VALUE.equals(value)) {
			session.setAttribute(SCOPE_KEY, SCOPE_VALUE);
			doDispather(request, response);
		} else {
			// System.out.println("wills");
			chain.doFilter(servletRequest, servletResponse);
		}
	}
	
	private void doDispather(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();  // /jopm/
		// StringBuffer requestURL = request.getRequestURL();
		
		
		String contextPath = request.getContextPath();
		requestURI = requestURI.replace(contextPath, "");
		if (("/").equals(requestURI)) {
			requestURI = "/webpage/base/login/login.html";
		}
		
		
		/*String contextPath = request.getContextPath(); // /jopm
		if ((contextPath + "/").equals(requestURI)) {
			requestURI = requestURI + "webpage/base/login/login.html";
		}*/
		
		
		// System.out.println(requestURI);
		
		// response.sendRedirect(requestURI);
		request.getRequestDispatcher(requestURI).forward(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
