/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.referenceapplication;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.FailedAuthenticationHandler;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.page.Redirect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("refAppHandler")
public class RefAppFailedAuthenticationHandler implements FailedAuthenticationHandler {
	
	public String handle(PageRequest pageRequest, List<String> requiredPrivileges, String redirectUrl) {
		
		if (StringUtils.isNotBlank(redirectUrl)) {
			//Currently there is no action required on our part
			return null;
		}
		
		HttpSession session = pageRequest.getRequest().getSession();
		if (Context.isAuthenticated()) {
			//Since will are sending the user to the login page, log
			//them out so that they can login with another account
			Context.logout();
			session.invalidate();
			session = pageRequest.getRequest().getSession(true);
			session.setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
			    "referenceapplication.error.insufficientPrivileges");
		}
		
		//Redirect the user to the page they requested for after they login with a new account
		String afterLoginRedirect = new Redirect(pageRequest.getRequest()).getUrl();
		session.setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL, afterLoginRedirect);
		
		return new Redirect("referenceapplication", "login", null).getUrl();
	}
}
