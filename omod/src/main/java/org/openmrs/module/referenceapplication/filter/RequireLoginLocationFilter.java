/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.referenceapplication.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Intercepts authenticated requests to check if the logged in user has selected a location and will
 * redirect them to the login page so they can select one in case they haven't yet.
 */
public class RequireLoginLocationFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequireLoginLocationFilter.class);
	
	private static String loginRequestUri;
	
	private static String logoutRequestUri;
	
	private static String[] allowedRequestURIs;
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing RequireLoginLocationFilter...");
		}
	}
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
		
		if (loginRequestUri == null) {
			UiUtils uiUtils = Context.getRegisteredComponent("uiUtils", UiUtils.class);
			loginRequestUri = uiUtils.pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
		}
		if (logoutRequestUri == null) {
			logoutRequestUri = "/" + WebConstants.CONTEXT_PATH + "/logout";
		}
		
		if (allowedRequestURIs == null) {
			allowedRequestURIs = new String[] { loginRequestUri, logoutRequestUri };
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (!skipFilter(httpRequest.getRequestURI())) {
			if (Context.isAuthenticated() && Context.getUserContext().getLocationId() == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Redirecting user to location selection page");
				}
				
				//The user needs to select a login location
				HttpServletResponse resp = ((HttpServletResponse) response);
				resp.setStatus(HttpStatus.OK.value());
				resp.sendRedirect(loginRequestUri);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}
	
	/**
	 * Determines if the filter should be skipped for the specified request uri.
	 * in config.xml file, the filter-mapping is configured to match *.page  and /
	 * 
	 * @param requestUri the request uri to check
	 * @return true if the filter should be skipped otherwise false
	 */
	private boolean skipFilter(String requestUri) {
		return ArrayUtils.indexOf(allowedRequestURIs, requestUri) > -1;
	}
	
	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		if (logger.isDebugEnabled()) {
			logger.debug("Destroying RequireLoginLocationFilter...");
		}
	}
	
}
