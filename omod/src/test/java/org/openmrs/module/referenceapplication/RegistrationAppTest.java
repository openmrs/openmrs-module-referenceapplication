package org.openmrs.module.referenceapplication;

import org.junit.Test;
import org.openmrs.module.appframework.AppTestUtil;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.registrationapp.model.NavigableFormStructure;
import org.openmrs.module.registrationapp.page.controller.RegisterPatientPageController;

import static junit.framework.Assert.assertNotNull;

/**
 *
 */
public class RegistrationAppTest {

    @Test
    public void testConfigOfRegistrationApp() throws Exception {
        AppDescriptor app = AppTestUtil.getAppDescriptor("referenceapplication.registrationapp.registerPatient");

        NavigableFormStructure formStructure = new RegisterPatientPageController().buildFormStructure(app);
        assertNotNull(formStructure);
    }

}
