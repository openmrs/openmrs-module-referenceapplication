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
package org.openmrs.module.referenceapplication.converter;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserAppConverter implements Converter<String, UserApp> {
	
	@Override
	public UserApp convert(String appId) {
		if (StringUtils.isNotBlank(appId)) {
			return Context.getService(AppFrameworkService.class).getUserApp(appId);
		}
		return null;
	}
}
