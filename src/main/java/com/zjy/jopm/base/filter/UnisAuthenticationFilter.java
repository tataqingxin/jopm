/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.zjy.jopm.base.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

/**
 * 重写权限过滤器，增加对无需cas即可访问的配置功能。
 * 
 * @author liufeng
 *
 */
public class UnisAuthenticationFilter extends AbstractCasFilter {

    /**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;
    
    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();
    
    private List<String> excludeUrls=new ArrayList<String>();
    private UrlPathMatcher pathMatcher=UrlPathMatcher.getInstance();
    private  String contextPath;
    private String clientLoginUrl;
    
    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            
            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
                } catch (final Exception e) {
                    throw new ServletException(e);
                }
            }
            contextPath=filterConfig.getServletContext().getContextPath();
            this.initExcludeUrls(filterConfig);
        }
    }

    /**
     * 加载无需跳转到cas server地址的url配置
     * @param filterConfig
     */
    private void initExcludeUrls(final FilterConfig filterConfig)
    {
    	String excludeUrlsStr=getPropertyFromInitParams(filterConfig, "excludeUrls", "");
    	this.clientLoginUrl=getPropertyFromInitParams(filterConfig, "clientLoginUrl", "");
    	String []excludeUrlsArray=excludeUrlsStr.split(",");
    	for(String url:excludeUrlsArray)
    	{
    		if(url!=null&&!url.equals(""))
    		{
    			this.excludeUrls.add(url);
    		}
    	}
    }
    
    
    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;
        //String url=request.getRequestURI();
        String projectName = request.getContextPath().substring(1,request.getContextPath().length());
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json; charset=UTF-8");
        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }
        //增加对额外的url进行过滤，如果访问地址在此地址列表中，直接方行，不跳转到cas进行登录
        if(this.isExclusion(request.getRequestURI()))
        {
        	filterChain.doFilter(request, response);
            return;
        }

        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request,getArtifactParameterName());
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
            filterChain.doFilter(request, response);
            return;
        }

         String modifiedServiceUrl;

        if (this.gateway) {
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }


        
         String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);

        if(clientLoginUrl!=null&&!clientLoginUrl.trim().equals(""))
        {
        	urlToRedirectTo+="&clientLoginUrl="+this.clientLoginUrl;
        }
        
        urlToRedirectTo = urlToRedirectTo.substring(0,urlToRedirectTo.indexOf(projectName)+projectName.length())+"%2F";
        response.sendRedirect(urlToRedirectTo);
        
    }

    public boolean isExclusion(String requestURI) {
        if (excludeUrls == null) {
            return false;
        }

        if (contextPath != null && requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
            if (!requestURI.startsWith("/")) {
                requestURI = "/" + requestURI;
            }
        }

        for (String pattern : excludeUrls) {
            if (pathMatcher.matches(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }
    
    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }
    
    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
    	this.gatewayStorage = gatewayStorage;
    }
}
