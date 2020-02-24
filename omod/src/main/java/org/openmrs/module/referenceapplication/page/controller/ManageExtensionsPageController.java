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

import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ManageExtensionsPageController {

	List<String> extensionsThatCannotBeStopped = Arrays.asList("coreapps.systemAdministrationApp");

	public void get(PageModel model, @SpringBean("appFrameworkService") AppFrameworkService service) {
		addModelAttributes(model, service);
	}
	
	public String post(PageModel model, @RequestParam("id") String id, @RequestParam("action") String action, 
	                   @SpringBean("appFrameworkService") AppFrameworkService service, HttpSession session, UiUtils ui) {
		String successMsgCode = "referenceapplication.app.manageExtensions." + action + ".success";
		String failMessageCode = "referenceapplication.app.manageExtensions." + action + ".fail";
		try {
			if ("enable".equals(action)) {
				service.enableExtension(id);
			} else if ("disable".equals(action)) {
				service.disableExtension(id);
			} else if ("delete".equals(action)) {
				service.purgeUserApp(service.getUserApp(id));
			}
			
			InfoErrorMessageUtil.flashInfoMessage(session, ui.message(successMsgCode, id));

			return "redirect:referenceapplication/manageExtensions.page";
		}
		catch (Exception e) {
			session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ui.message(failMessageCode, id));
		}
		
		addModelAttributes(model, service);
		
		return null;
	}
	
	private void addModelAttributes(PageModel model, AppFrameworkService service) {
		List<Extension> allExtensions = service.getAllExtensions(null);
		List<Extension> enabledExtensions = service.getAllEnabledExtensions();
		List<String> userExtensionIds = new ArrayList<String>();
		List<ExtensionModel> extensions = new ArrayList<ExtensionModel>();
		for (Extension userExtension : service.getExtensionsForCurrentUser()) {
			userExtensionIds.add(userExtension.getId());
		}
		for (Extension extension : allExtensions) {
			extensions.add(new ExtensionModel(extension.getId(), enabledExtensions.contains(extension), !userExtensionIds.contains(extension.getId())));
		}
		model.addAttribute("extensions", extensions);
	}
	
	public class ExtensionModel {
		
		private String id;
		
		private boolean enabled;
		
		private boolean builtIn;

		private boolean cannotBeStopped;

		public ExtensionModel(String id, boolean enabled, boolean builtIn) {
			this.id = id;
			this.enabled = enabled;
			this.builtIn = builtIn;
			this.cannotBeStopped = extensionsThatCannotBeStopped.contains(this.id) ? Boolean.TRUE : Boolean.FALSE;
		}
		
	}
}
