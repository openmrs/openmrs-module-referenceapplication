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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.REQUEST_PARAMETER_NAME_REDIRECT_URL;
import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.databene.commons.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.test.Verifies;
import org.openmrs.ui.framework.BasicUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.session.Session;
import org.openmrs.util.LocationUtility;
import org.openmrs.util.PrivilegeConstants;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

@PrepareForTest({ Context.class, LocationUtility.class })
@RunWith(PowerMockRunner.class)
public class LoginPageControllerTest {

	private static final String TEST_CONTEXT_PATH = "/openmrs";

	private static final String USERNAME = "admin";

	private static final String PASSWORD = "test";

	private static final Integer SESSION_LOCATION_ID = 2;

	private LocationService locationService;
	
	private static final String GET_LOCATIONS = "Get Locations";

	private final UiUtils uiUtils = new UiUtils() {

		@Override
		public String pageLink(String providerName, String pageName) {
			return new BasicUiUtils().pageLink(providerName, pageName);
		}

		@Override
		public String message(String code, Object... args) {
			return null;
		}
	};

	private UiSessionContext sessionContext;

    private AppFrameworkService appFrameworkService;

	@Before
	public void setup() {
		mockStatic(Context.class);
		locationService = mock(LocationService.class);
		sessionContext = mock(UiSessionContext.class);
        appFrameworkService = mock(AppFrameworkService.class);
    }

	private PageRequest createPageRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		HttpServletRequest request = (httpRequest != null) ? httpRequest : new MockHttpServletRequest();
		HttpServletResponse response = (httpResponse != null) ? httpResponse : new MockHttpServletResponse();
		return new PageRequest(null, null, request, response, new Session(new MockHttpSession()));
	}

	/**
	 * This should ony be called from test methods where authentication will be successful but wish
	 * to test other things e.g if the session location or redirect are properly set
	 */
	private void setupMocksForSuccessfulAuthentication(boolean locationHasLoginTag) throws Exception {
		stub(method(Context.class, "isAuthenticated")).toReturn(true);
		Location location = Mockito.spy(new Location(SESSION_LOCATION_ID));
		when(location.hasTag(Mockito.eq(EmrApiConstants.LOCATION_TAG_SUPPORTS_LOGIN))).thenReturn(locationHasLoginTag);
		when(locationService.getLocation(Mockito.eq(SESSION_LOCATION_ID))).thenReturn(location);
		spy(Context.class);
		doNothing().when(Context.class, "addProxyPrivilege", PrivilegeConstants.VIEW_LOCATIONS);
		doNothing().when(Context.class, "removeProxyPrivilege", PrivilegeConstants.VIEW_LOCATIONS);
		doNothing().when(Context.class, "addProxyPrivilege", GET_LOCATIONS);
		doNothing().when(Context.class, "removeProxyPrivilege", GET_LOCATIONS);
		doNothing().when(Context.class, "authenticate", USERNAME, PASSWORD);
	}

	/**
	 * @see LoginPageController#get(org.openmrs.ui.framework.page.PageModel,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest, String,
	 *      org.openmrs.api.LocationService,
	 *      org.openmrs.module.appframework.service.AppFrameworkService)
	 */
	@Test
	@Verifies(value = "should redirect the user to the home page if they are already authenticated", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldRedirectTheUserToTheHomePageIfTheyAreAlreadyAuthenticated() throws Exception {
		when(Context.isAuthenticated()).thenReturn(true);
		String homeRedirect = "redirect:" + uiUtils.pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		assertEquals(homeRedirect,
		    new LoginPageController().get(null, uiUtils, createPageRequest(null, null), null, null, appFrameworkService));
	}

	/**
	 * @see LoginPageController#get(org.openmrs.ui.framework.page.PageModel,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest, String,
	 *      org.openmrs.api.LocationService,
	 *      org.openmrs.module.appframework.service.AppFrameworkService)
	 */
	@Test
	@Verifies(value = "should show the user the login page if they are not authenticated", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldShowTheUserTheLoginPageIfTheyAreNotAuthenticated() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		assertNull(new LoginPageController().get(new PageModel(), uiUtils, createPageRequest(null, null), null, null, appFrameworkService));
	}

	/**
	 * @see LoginPageController#get(org.openmrs.ui.framework.page.PageModel,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest, String,
	 *      org.openmrs.api.LocationService,
	 *      org.openmrs.module.appframework.service.AppFrameworkService)
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

		new LoginPageController().get(pageModel, uiUtils, createPageRequest(request, null), null, null, appFrameworkService);

		assertEquals(redirectUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
	}

	/**
	 * @see LoginPageController#get(org.openmrs.ui.framework.page.PageModel,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest, String,
	 *      org.openmrs.api.LocationService,
	 *      org.openmrs.module.appframework.service.AppFrameworkService)
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

		new LoginPageController().get(pageModel, uiUtils, createPageRequest(request, null), null, null, appFrameworkService);

		assertEquals(refererUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
	}

	/**
	 * @see LoginPageController#get(org.openmrs.ui.framework.page.PageModel,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest, String,
	 *      org.openmrs.api.LocationService,
	 *      org.openmrs.module.appframework.service.AppFrameworkService)
	 */
	@Test
	@Verifies(value = "should set redirectUrl in the page model if any was specified in the session", method = "get(PageModel,UiUtils,PageRequest)")
	public void get_shouldSetRedirectUrlInThePageModelIfAnyWasSpecifiedInTheSession() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);

		String redirectUrl = TEST_CONTEXT_PATH + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath(TEST_CONTEXT_PATH);
		PageRequest pageRequest = createPageRequest(request, null);
		HttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
		request.setSession(httpSession);

		PageModel pageModel = new PageModel();
		new LoginPageController().get(pageModel, uiUtils, pageRequest, null, null, appFrameworkService);

		assertEquals(redirectUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
	}

	/**
	 * @see LoginPageController#post(String, String, Integer, org.openmrs.api.LocationService,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest,
	 *      org.openmrs.module.appui.UiSessionContext)
	 */
	@Test
    @Ignore
	@Verifies(value = "should redirect the user back to the redirectUrl if any", method = "post(String,String,UiUtils,PageRequest)")
	public void post_shouldRedirectTheUserBackToTheRedirectUrlIfAny() throws Exception {
		setupMocksForSuccessfulAuthentication(true);

		final String redirectUrl = TEST_CONTEXT_PATH + "/referenceapplication/patient.page";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL, redirectUrl);
		PageRequest pageRequest = createPageRequest(request, null);

		assertEquals("redirect:" + redirectUrl, new LoginPageController().post(USERNAME, PASSWORD, SESSION_LOCATION_ID,
		    locationService, uiUtils, pageRequest, sessionContext));

	}

	/**
	 * @see LoginPageController#post(String, String, Integer, org.openmrs.api.LocationService,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest,
	 *      org.openmrs.module.appui.UiSessionContext)
	 */
	@Test
    @Ignore
	@Verifies(value = "should redirect the user to the home page if the redirectUrl is the login page", method = "post(String,String,UiUtils,PageRequest)")
	public void post_shouldRedirectTheUserToTheHomePageIfTheRedirectUrlIsTheLoginPage() throws Exception {
		setupMocksForSuccessfulAuthentication(true);

		final String redirectUrl = uiUtils.pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
		final String homeRedirect = "redirect:" + uiUtils.pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath("/openmrs");
		request.setParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL, redirectUrl);
		PageRequest pageRequest = createPageRequest(request, null);

		assertEquals(homeRedirect, new LoginPageController().post(USERNAME, PASSWORD, SESSION_LOCATION_ID, locationService,
		    uiUtils, pageRequest, sessionContext));

	}

	/**
	 * @verifies send the user back to the login page when authentication fails
	 * @see LoginPageController#post(String, String, Integer, org.openmrs.api.LocationService,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest,
	 *      org.openmrs.module.appui.UiSessionContext)
	 */
	@Test
	public void post_shouldSendTheUserBackToTheLoginPageWhenAuthenticationFails() throws Exception {
		when(Context.isAuthenticated()).thenReturn(false);
		MockHttpServletRequest request = new MockHttpServletRequest();
		String page = new LoginPageController().post(null, null, SESSION_LOCATION_ID, locationService, uiUtils,
		    createPageRequest(request, null), sessionContext);
		assertEquals("redirect:" + uiUtils.pageLink("referenceapplication", "login"), page);
	}

	/**
	 * @verifies send the user back to the login page if an invalid location is selected
	 * @see LoginPageController#post(String, String, Integer, org.openmrs.api.LocationService,
	 *      org.openmrs.ui.framework.UiUtils, org.openmrs.ui.framework.page.PageRequest,
	 *      org.openmrs.module.appui.UiSessionContext)
	 */
	@Test
    @Ignore
	public void post_shouldSendTheUserBackToTheLoginPageIfAnInvalidLocationIsSelected() throws Exception {
		setupMocksForSuccessfulAuthentication(false);
		MockHttpServletRequest request = new MockHttpServletRequest();
		String page = new LoginPageController().post(USERNAME, PASSWORD, SESSION_LOCATION_ID, locationService, uiUtils,
		    createPageRequest(request, null), sessionContext);
		assertEquals("redirect:" + uiUtils.pageLink("referenceapplication", "login"), page);
	}

	/**
     * @see LoginPageController#get(PageModel,UiUtils,PageRequest,String,LocationService,AppFrameworkService)
     * @verifies not set the referer as the redirectUrl in the page model if referer URL is outside context path
     */
    @Test
    public void get_shouldNotSetTheRefererAsTheRedirectUrlInThePageModelIfRefererUrlIsOutsideContextPath() throws Exception {
    	when(Context.isAuthenticated()).thenReturn(false);
    	
    	String refererUrl = "http://openmrs.org/demo/";
    	MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath(TEST_CONTEXT_PATH);
		request.addHeader("Referer", refererUrl);
		PageModel pageModel = new PageModel();
		new LoginPageController().get(pageModel, uiUtils, createPageRequest(request, null), null, null, appFrameworkService);
		
		assertEquals("", pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
    }

	/**
     * @see LoginPageController#get(PageModel,UiUtils,PageRequest,String,LocationService,AppFrameworkService)
     * @verifies set the referer as the redirectUrl in the page model if referer URL is within context path
     */
    @Test
    public void get_shouldSetTheRefererAsTheRedirectUrlInThePageModelIfRefererUrlIsWithinContextPath() throws Exception {
    	when(Context.isAuthenticated()).thenReturn(false);
    	
    	String redirectUrl = TEST_CONTEXT_PATH +"/demo/";
		String refererUrl = "http://openmrs.org" + redirectUrl;    	
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath(TEST_CONTEXT_PATH);
		request.addHeader("Referer", refererUrl);
		PageModel pageModel = new PageModel();
		new LoginPageController().get(pageModel, uiUtils, createPageRequest(request, null), null, null, appFrameworkService);
		
		assertEquals(redirectUrl, pageModel.get(REQUEST_PARAMETER_NAME_REDIRECT_URL));
    }
}
