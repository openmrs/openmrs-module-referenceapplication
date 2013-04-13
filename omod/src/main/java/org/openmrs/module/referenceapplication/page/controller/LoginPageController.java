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
package org.openmrs.module.referenceapplication.page.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring MVC controller that takes over /login.htm and processes requests to authenticate a user
 */
@Controller
public class LoginPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping("/login.htm")
	public String overrideLoginpage() {
		//TODO The referer should actually be captured from here since we are doign a redirect
		return "forward:/" + ReferenceApplicationConstants.MODULE_ID + "/login.page";
	}
	
	/**
	 * @should redirect the user to the home page if they are already authenticated
	 * @should show the user the login page if they are not authenticated
	 */
	public String get(UiUtils ui, PageRequest request) {
		
		if (Context.isAuthenticated()) {
			return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		}
		
		return null;
	}
	
	/**
	 * Processes requests to authenticate a user
	 * 
	 * @param username
	 * @param password
	 * @param ui {@link UiUtils} object
	 * @param request {@link PageRequest} object
	 * @return
	 * @should redirect the user back to the referer url if any
	 * @should redirect the user to the home page if the referer is the login page
	 */
	public String post(@RequestParam(value = "username", required = false) String username,
	                   @RequestParam(value = "password", required = false) String password, UiUtils ui, PageRequest request) {
		
		try {
			
			Context.authenticate(username, password);
			
			if (Context.isAuthenticated()) {
				if (log.isDebugEnabled())
					log.debug("User has successfully authenticated");
				
				String redirectUrl = request.getRequest().getHeader("Referer");
				
				if (StringUtils.isNotBlank(redirectUrl)) {
					//don't redirect back to the login page on success nor an external url
					if (!redirectUrl.contains("login.")) {
						if (log.isDebugEnabled())
							log.debug("Redirecting user to " + redirectUrl);
						
						return "redirect:" + redirectUrl;
					} else {
						if (log.isDebugEnabled())
							log.debug("Redirect contains 'login.', redirecting to home page");
					}
				}
				
				return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
			}
		}
		catch (ContextAuthenticationException ex) {
			if (log.isDebugEnabled())
				log.debug("Failed to authenticate user");
			
			request.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
			    ui.message(ReferenceApplicationConstants.MODULE_ID + ".error.login.fail"));
		}
		
		if (log.isDebugEnabled())
			log.debug("Sending user back to login page");
		
		//TODO limit login attempts by IP Address
		
		return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
	}
}
