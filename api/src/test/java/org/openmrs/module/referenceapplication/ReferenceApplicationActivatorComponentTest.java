package org.openmrs.module.referenceapplication;

import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.utils.MetadataUtil;
import org.openmrs.module.referencemetadata.ReferenceMetadataConstants;
import org.openmrs.module.referencemetadata.ReferenceMetadataProperties;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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

    @Autowired
    private LocationService locationService;

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

    @Test
    public void testAddLoginLocationTagToUnknownLocationIfThereAreNoLoginLocations() throws Exception {
        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Reference_Application_Location_Tags");
        final String tag = "Login Location";
        LocationTag loginTag = locationService.getLocationTagByUuid(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN_UUID);
        assertEquals(0, locationService.getLocationsByTag(loginTag).size());

        Location location = locationService.getLocation("Unknown Location");
        assertFalse(location.hasTag(tag));
        new ReferenceApplicationActivator().started();
        assertTrue(location.hasTag(tag));
    }

    /**
     * Tests that if there are no login locations and no unknown location then atleast one of the
     * existing locations gets tagged as a login location
     *
     * @throws Exception
     */
    @Test
    public void testAddLoginLocationTagToAtleastOneLocationIfThereAreNoLoginLocations() throws Exception {
        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Reference_Application_Location_Tags");
        Location unknownLocation = locationService.getLocation("Unknown Location");
        unknownLocation.setName("new name");
        locationService.saveLocation(unknownLocation);
        unknownLocation = locationService.getLocation("Unknown Location");
        assertNull(unknownLocation);
        final String tag = "Login Location";
        LocationTag loginTag = locationService.getLocationTagByUuid(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN_UUID);
        assertNull(locationService.getLocationByUuid(ReferenceMetadataConstants.UNKNOWN_LOCATION_UUID));
        assertEquals(0, locationService.getLocationsByTag(loginTag).size());
        new ReferenceApplicationActivator().started();
        assertEquals(1, locationService.getLocationsByTag(loginTag).size());
    }

}
