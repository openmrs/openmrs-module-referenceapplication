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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.appframework.config.AppFrameworkConfig;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllComponentsState;
import org.openmrs.module.appframework.repository.AllFreeStandingExtensions;
import org.openmrs.module.appframework.repository.AllUserApps;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appframework.service.AppFrameworkServiceImpl;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.test.Verifies;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

public class HomePageControllerTest extends BaseModuleWebContextSensitiveTest {

    @Autowired
    LocationService locationService;

    @Autowired
    ProviderService providerService;

    /**
     * @see HomePageController#controller(org.openmrs.ui.framework.page.PageModel model,
     * org.openmrs.module.appframework.service.AppFrameworkService appFrameworkService,
     * org.openmrs.module.appui.UiSessionContext sessionContext)
     */
    @Test
    @Verifies(value = "should limit which apps are shown on the homepage based on location", method = "controller(PageModel,AppFrameworkService,UiSessionContext)")
    public void controller_shouldLimitWhichAppsAreShownOnTheHomePageBasedOnLocation() throws Exception {

        AppFrameworkConfig appFrameworkConfig = mock(AppFrameworkConfig.class);

        PageModel pageModel = new PageModel();

        Location location = new Location();
        location.addTag(new LocationTag("tag1", "tag1"));
        location.addTag(new LocationTag("tag2", "tag2"));

        String locationUuid = location.getUuid();

        List<Extension> extensions = new ArrayList<Extension>();

        Extension extension = new Extension("ext1", "app1", ReferenceApplicationConstants.HOME_PAGE_EXTENSION_POINT_ID, "link", "label", "url", 1);
        extension.setRequire("hasMemberWithProperty(sessionLocation.tags, 'display', 'tag1')");
        when(appFrameworkConfig.isEnabled(extension)).thenReturn(Boolean.TRUE);
        extensions.add(extension);

        extension = new Extension("ext2", "app1", ReferenceApplicationConstants.HOME_PAGE_EXTENSION_POINT_ID, "link", "label", "url", 2);
        extension.setRequire("hasMemberWithProperty(sessionLocation.tags, 'display', 'tag2')");
        when(appFrameworkConfig.isEnabled(extension)).thenReturn(Boolean.TRUE);
        extensions.add(extension);

        extension = new Extension("ext3", "app1", ReferenceApplicationConstants.HOME_PAGE_EXTENSION_POINT_ID, "link", "label", "url", 3);
        extension.setRequire("hasMemberWithProperty(sessionLocation.tags, 'display', 'tag3')");
        when(appFrameworkConfig.isEnabled(extension)).thenReturn(Boolean.TRUE);
        extensions.add(extension);

        extension = new Extension("ext4", "app1", ReferenceApplicationConstants.HOME_PAGE_EXTENSION_POINT_ID, "link", "label", "url", 4);
        extension.setRequire("sessionLocation.uuid=='" + locationUuid + "'");
        when(appFrameworkConfig.isEnabled(extension)).thenReturn(Boolean.TRUE);
        extensions.add(extension);

        extension = new Extension("ext5", "app1", ReferenceApplicationConstants.HOME_PAGE_EXTENSION_POINT_ID, "link", "label", "url", 5);
        extension.setRequire("sessionLocation.uuid=='000'");
        when(appFrameworkConfig.isEnabled(extension)).thenReturn(Boolean.TRUE);
        extensions.add(extension);

        AllFreeStandingExtensions freeStandingExtensions = mock(AllFreeStandingExtensions.class);
        when(freeStandingExtensions.getExtensions()).thenReturn(extensions);

        AllComponentsState componentsState = mock(AllComponentsState.class);

        AllAppDescriptors appDescriptors = mock(AllAppDescriptors.class);
        when(appDescriptors.getAppDescriptors()).thenReturn(new ArrayList<AppDescriptor>());

        AllUserApps userApps = mock(AllUserApps.class);

        AppFrameworkService frameworkService = new AppFrameworkServiceImpl(null, appDescriptors, freeStandingExtensions, componentsState, null, null, appFrameworkConfig, userApps);

        UiSessionContext sessionContext = new UiSessionContext(locationService, providerService, new MockHttpServletRequest());
        sessionContext.setSessionLocation(location);

        new HomePageController().controller(pageModel, frameworkService, sessionContext);

        assertEquals(3, ((ArrayList) pageModel.get("extensions")).size());

        assertTrue(((ArrayList) pageModel.get("extensions")).contains(extensions.get(0)));
        assertTrue(((ArrayList) pageModel.get("extensions")).contains(extensions.get(1)));
        assertFalse(((ArrayList) pageModel.get("extensions")).contains(extensions.get(2)));
        assertTrue(((ArrayList) pageModel.get("extensions")).contains(extensions.get(3)));
        assertFalse(((ArrayList) pageModel.get("extensions")).contains(extensions.get(4)));
    }

}
