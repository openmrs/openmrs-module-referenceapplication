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

import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring MVC controller that takes over /index.htm and processes requests to show the home page so
 * users don't see the legacy OpenMRS UI
 */
@Controller
public class HomePageController {
	
	@RequestMapping("/index.htm")
	public String overrideHomepage() {
		return "forward:/" + ReferenceApplicationConstants.MODULE_ID + "/home.page";
	}
	
	/**
	 * Process requests to show the home page
	 * 
	 * @param model
	 */
	public void controller(PageModel model, @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService) {
		model.addAttribute("apps", appFrameworkService.getAllApps());
	}
	
}
