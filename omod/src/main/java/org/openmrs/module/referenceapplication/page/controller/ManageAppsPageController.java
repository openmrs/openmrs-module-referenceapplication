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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManageAppsPageController {
	
	public void get(PageModel model, @SpringBean("appFrameworkService") AppFrameworkService service) {
		addModelAttributes(model, service);
	}
	
	public String post(PageModel model, @RequestParam("id") String id, @RequestParam("action") String action,
	                   @SpringBean("appFrameworkService") AppFrameworkService service, HttpSession session, UiUtils ui) {
		
		String successMsgCode = "referenceapplication.app.manageApps." + action + ".success";
		String failMessageCode = "referenceapplication.app.manageApps." + action + ".fail";
		try {
			if ("enable".equals(action)) {
				service.enableApp(id);
			} else if ("disable".equals(action)) {
				service.disableApp(id);
			} else if ("delete".equals(action)) {
				service.purgeUserApp(service.getUserApp(id));
			}
			
			InfoErrorMessageUtil.flashInfoMessage(session, ui.message(successMsgCode, id));
			
			return "redirect:referenceapplication/manageApps.page";
		}
		catch (Exception e) {
			session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ui.message(failMessageCode, id));
		}
		
		addModelAttributes(model, service);
		
		return null;
	}
	
	private void addModelAttributes(PageModel model, AppFrameworkService service) {
		List<AppDescriptor> allApps = service.getAllApps();
		List<AppDescriptor> enabledApps = service.getAllEnabledApps();
		List<String> userAppIds = new ArrayList<String>();
		List<AppModel> apps = new ArrayList<AppModel>();
		for (UserApp userApp : service.getUserApps()) {
			userAppIds.add(userApp.getAppId());
		}
		for (AppDescriptor ad : allApps) {
			apps.add(new AppModel(ad.getId(), enabledApps.contains(ad), !userAppIds.contains(ad.getId())));
		}
		model.addAttribute("apps", apps);
	}
	
	public class AppModel {
		
		private String id;
		
		private boolean enabled;
		
		private boolean builtIn;
		
		public AppModel(String id, boolean enabled, boolean builtIn) {
			this.id = id;
			this.enabled = enabled;
			this.builtIn = builtIn;
		}
		
	}
}
