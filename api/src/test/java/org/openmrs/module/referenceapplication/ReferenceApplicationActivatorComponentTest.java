package org.openmrs.module.referenceapplication;

import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.referencemetadata.ReferenceMetadataProperties;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertThat;
import static org.openmrs.test.OpenmrsMatchers.hasUuid;

/**
 *
 */
public class ReferenceApplicationActivatorComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private EmrApiProperties emrApiProperties;

    @Autowired
    @Qualifier("adminService")
    private AdministrationService administrationService;

    @Test
    public void testSetUpAdtGlobalProperties() throws Exception {
        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Reference_Application_Visit_and_Encounter_Types");

        ReferenceApplicationActivator referenceApplicationActivator = new ReferenceApplicationActivator();
        referenceApplicationActivator.setupEmrApiGlobalProperties(administrationService);

        assertThat(emrApiProperties.getAdmissionEncounterType(), hasUuid(ReferenceMetadataProperties.ADMISSION_ENCOUNTER_TYPE_UUID));
        assertThat(emrApiProperties.getExitFromInpatientEncounterType(), hasUuid(ReferenceMetadataProperties.DISCHARGE_ENCOUNTER_TYPE_UUID));
        assertThat(emrApiProperties.getTransferWithinHospitalEncounterType(), hasUuid(ReferenceMetadataProperties.TRANSFER_ENCOUNTER_TYPE_UUID));
        assertThat(emrApiProperties.getCheckInEncounterType(), hasUuid(ReferenceMetadataProperties.CHECK_IN_ENCOUNTER_TYPE_UUID));

        assertThat(emrApiProperties.getAtFacilityVisitType(), hasUuid(ReferenceMetadataProperties.FACILITY_VISIT_TYPE_UUID));
    }

}
