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
		return "forward:/" + ReferenceApplicationConstants.MODULE_ID + "/login.page";
	}
	
	/**
	 * @should redirect the user to the home page if they are already authenticated
	 */
	public String get(UiUtils ui, PageRequest request) {
		
		if (Context.isAuthenticated()) {
			return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		}
		
		return null;
	}
	
	public String post(@RequestParam(value = "username", required = false) String username,
	                   @RequestParam(value = "password", required = false) String password, UiUtils ui, PageRequest request) {
		
		try {
			Context.authenticate(username, password);
			if (Context.isAuthenticated()) {
				if (log.isDebugEnabled())
					log.debug("User has successfully authenticated");
				
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
