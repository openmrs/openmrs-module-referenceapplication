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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.context.Context;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.test.Verifies;
import org.openmrs.ui.framework.BasicUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageRequest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;

@PrepareForTest(Context.class)
@RunWith(PowerMockRunner.class)
public class LoginPageControllerTest {
	
	@Before
	public void setup() {
		mockStatic(Context.class);
	}
	
	/**
	 * @see {@link LoginPageController#get(UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should redirect the user to the home page if they are already authenticated", method = "get(UiUtils,PageRequest)")
	public void get_shouldRedirectTheUserToTheHomePageIfTheyAreAlreadyAuthenticated() throws Exception {
		when(Context.isAuthenticated()).thenReturn(true);
		String homeRedirect = "redirect:" + new BasicUiUtils().pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		assertEquals(homeRedirect,
		    new LoginPageController().get(new BasicUiUtils(), new PageRequest(null, null, null, null, null)));
	}
	
	/**
	 * @see {@link LoginPageController#get(UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should show the user the login page if they are not authenticated", method = "get(UiUtils,PageRequest)")
	public void get_shouldShowTheUserTheLoginPageIfTheyAreNotAuthenticated() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		assertNull(new LoginPageController().get(new BasicUiUtils(), new PageRequest(null, null, null, null, null)));
	}
	
	/**
	 * @see {@link LoginPageController#post(String,String,UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should redirect the user back to the referer url if any", method = "post(String,String,UiUtils,PageRequest)")
	public void post_shouldRedirectTheUserBackToTheRefererUrlIfAny() throws Exception {
		stub(method(Context.class, "isAuthenticated")).toReturn(true);
		spy(Context.class);
		doNothing().when(Context.class);//do nothing when Context.authenticate is called in the controller
		
		final String contextPath = "/openmrs";
		String referer = contextPath + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Referer", referer);
		
		assertEquals("redirect:" + referer, new LoginPageController().post("admin", "test", new BasicUiUtils(),
		    new PageRequest(null, null, request, null, null)));
	}
	
	/**
	 * @see {@link LoginPageController#post(String,String,UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should redirect the user to the home page if the referer is the login page", method = "post(String,String,UiUtils,PageRequest)")
	public void post_shouldRedirectTheUserToTheHomePageIfTheRefererIsTheLoginPage() throws Exception {
		stub(method(Context.class, "isAuthenticated")).toReturn(true);
		spy(Context.class);
		doNothing().when(Context.class);//do nothing when Context.authenticate is called in the controller
		
		String referer = new BasicUiUtils().pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
		String homeRedirect = "redirect:" + new BasicUiUtils().pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath("/openmrs");
		request.addHeader("Referer", referer);
		
		assertEquals(homeRedirect, new LoginPageController().post("admin", "test", new BasicUiUtils(), new PageRequest(null,
		        null, request, null, null)));
	}
}
