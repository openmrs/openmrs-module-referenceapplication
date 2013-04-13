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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring MVC controller that takes over /index.htm and processes requests to show the home page so
 * users don't see the legacy OpenMRS UI
 */
@Controller
public class HomePageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping("/index.htm")
	public String overrideHomepage() {
		return "forward:/" + ReferenceApplicationConstants.MODULE_ID + "/home.page";
	}
	
	/**
	 * Process requests to show the home page
	 * 
	 * @param model
	 * @param appFrameworkService
	 * @param request
	 * @param ui
	 * @throws IOException
	 */
	public void controller(PageModel model, @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService,
	                       PageRequest request, UiUtils ui) throws IOException {
		if (!Context.isAuthenticated()) {
			request.getResponse().sendRedirect(ReferenceApplicationConstants.MODULE_ID + "/login.page");
			return;
		}
		
		model.addAttribute("extensions",
		    appFrameworkService.getAllEnabledExtensions(ReferenceApplicationConstants.HOME_PAGE_EXTENSION_POINT_ID));
	}
	
}
