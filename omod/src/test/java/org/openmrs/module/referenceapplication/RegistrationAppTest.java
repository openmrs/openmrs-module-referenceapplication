package org.openmrs.module.referenceapplication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.openmrs.module.appframework.AppTestUtil;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.factory.AppConfigurationLoaderFactory;
import org.openmrs.module.appframework.factory.AppFrameworkFactory;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllAppTemplates;
import org.openmrs.module.registrationapp.form.RegisterPatientFormBuilder;
import org.openmrs.module.registrationapp.model.NavigableFormStructure;

/**
 *
 */
public class RegistrationAppTest {

	@Test
	public void testConfigOfRegistrationApp() throws Exception {
		AppDescriptor app = AppTestUtil.getAppDescriptor("referenceapplication.registrationapp.registerPatient");
		NavigableFormStructure formStructure = RegisterPatientFormBuilder.buildFormStructure(app);
		assertNotNull(formStructure);
	}

	@Test
	public void testCoreAppsTemplateIsAvailable() throws IOException {
		Validator validator = mock(Validator.class);
		when(validator.validate(anyObject())).thenReturn(Collections.<ConstraintViolation<Object>> emptySet());
        AllAppDescriptors allAppDescriptors = new AllAppDescriptors(validator);
        AllAppTemplates allAppTemplates = new AllAppTemplates(validator);
		List<AppFrameworkFactory> factories = Arrays.<AppFrameworkFactory> asList(new AppConfigurationLoaderFactory());
		for (AppFrameworkFactory appFrameworkFactory : factories) {
            allAppTemplates.clear();
            List<AppTemplate> appTemplates = appFrameworkFactory.getAppTemplates();
            allAppTemplates.add(appTemplates);
            List<AppDescriptor> appDescriptors = appFrameworkFactory.getAppDescriptors();
            allAppDescriptors.add(appDescriptors);
            // This throws an exception for referenceapplication.vitals if coreapps module is not present.
			allAppDescriptors.setAppTemplatesOnInstances(allAppTemplates);
		}
		assertNotNull(allAppDescriptors.getAppDescriptor("referenceapplication.vitals"));
	}
}
