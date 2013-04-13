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
import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.REQUEST_PARAMETER_NAME_REDIRECT_URL;
import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.context.Context;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.test.Verifies;
import org.openmrs.ui.framework.BasicUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

@PrepareForTest(Context.class)
@RunWith(PowerMockRunner.class)
public class LoginPageControllerTest {
	
	private static final String TEST_CONTEXT_PATH = "/openmrs";
	
	@Before
	public void setup() {
		mockStatic(Context.class);
	}
	
	private PageRequest createPageRequest(HttpServletRequest httpRequest) {
		return new PageRequest(null, null, (httpRequest != null) ? httpRequest : new MockHttpServletRequest(), null, null);
	}
	
	/**
	 * @see {@link LoginPageController#get(PageModel, UiUtils, PageRequest)}
	 */
	@Test
	@Verifies(value = "should redirect the user to the home page if they are already authenticated", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldRedirectTheUserToTheHomePageIfTheyAreAlreadyAuthenticated() throws Exception {
		when(Context.isAuthenticated()).thenReturn(true);
		String homeRedirect = "redirect:" + new BasicUiUtils().pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		assertEquals(homeRedirect, new LoginPageController().get(null, new BasicUiUtils(), createPageRequest(null)));
	}
	
	/**
	 * @see {@link LoginPageController#get(PageModel, UiUtils, PageRequest)}
	 */
	@Test
	@Verifies(value = "should show the user the login page if they are not authenticated", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldShowTheUserTheLoginPageIfTheyAreNotAuthenticated() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		assertNull(new LoginPageController().get(new PageModel(), new BasicUiUtils(), createPageRequest(null)));
	}
	
	/**
	 * @see {@link LoginPageController#post(String,String,UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should redirect the user back to the redirectUrl if any", method = "post(String,String,UiUtils,PageRequest)")
	public void post_shouldRedirectTheUserBackToTheRedirectUrlIfAny() throws Exception {
		stub(method(Context.class, "isAuthenticated")).toReturn(true);
		spy(Context.class);
		doNothing().when(Context.class);//do nothing when Context.authenticate is called in the controller
		
		String redirectUrl = TEST_CONTEXT_PATH + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL, redirectUrl);
		
		assertEquals("redirect:" + redirectUrl,
		    new LoginPageController().post("admin", "test", new BasicUiUtils(), createPageRequest(request)));
	}
	
	/**
	 * @see {@link LoginPageController#post(String, String, UiUtils, PageRequest)}
	 */
	@Test
	@Verifies(value = "should redirect the user to the home page if the redirectUrl is the login page", method = "post(String,String,UiUtils,PageRequest)")
	public void post_shouldRedirectTheUserToTheHomePageIfTheRedirectUrlIsTheLoginPage() throws Exception {
		stub(method(Context.class, "isAuthenticated")).toReturn(true);
		spy(Context.class);
		doNothing().when(Context.class);//do nothing when Context.authenticate is called in the controller
		
		String redirectUrl = new BasicUiUtils().pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
		String homeRedirect = "redirect:" + new BasicUiUtils().pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath("/openmrs");
		request.setParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL, redirectUrl);
		
		assertEquals(homeRedirect,
		    new LoginPageController().post("admin", "test", new BasicUiUtils(), createPageRequest(request)));
	}
	
	/**
	 * @see {@link LoginPageController#get(PageModel,UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should set redirectUrl in the page model if any was specified in the request", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldSetRedirectUrlInThePageModelIfAnyWasSpecifiedInTheRequest() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		
		String redirectUrl = TEST_CONTEXT_PATH + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath(TEST_CONTEXT_PATH);
		request.addParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL, redirectUrl);
		PageModel pageModel = new PageModel();
		
		new LoginPageController().get(pageModel, new BasicUiUtils(), createPageRequest(request));
		
		assertEquals(redirectUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
	}
	
	/**
	 * @see {@link LoginPageController#get(PageModel,UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should set the referer as the redirectUrl in the page model if no redirect param exists", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldSetTheRefererAsTheRedirectUrlInThePageModelIfNoRedirectParamExists() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		
		String refererUrl = TEST_CONTEXT_PATH + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath(TEST_CONTEXT_PATH);
		request.addHeader("Referer", refererUrl);
		PageModel pageModel = new PageModel();
		
		new LoginPageController().get(pageModel, new BasicUiUtils(), createPageRequest(request));
		
		assertEquals(refererUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
	}
	
	/**
	 * @see {@link LoginPageController#get(PageModel,UiUtils,PageRequest)}
	 */
	@Test
	@Verifies(value = "should set redirectUrl in the page model if any was specified in the session", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldSetRedirectUrlInThePageModelIfAnyWasSpecifiedInTheSession() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		
		String redirectUrl = TEST_CONTEXT_PATH + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath(TEST_CONTEXT_PATH);
		PageRequest pageRequest = createPageRequest(request);
		HttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
		request.setSession(httpSession);
		
		PageModel pageModel = new PageModel();
		new LoginPageController().get(pageModel, new BasicUiUtils(), pageRequest);
		
		assertEquals(redirectUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
	}
}
