package org.openmrs.module.referenceapplication;

import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.openmrs.module.appframework.AppTestUtil;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.registrationapp.form.RegisterPatientFormBuilder;
import org.openmrs.module.registrationapp.model.NavigableFormStructure;
import org.openmrs.test.BaseModuleContextSensitiveTest;

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

}
