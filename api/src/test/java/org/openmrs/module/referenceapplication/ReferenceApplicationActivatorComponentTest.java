package org.openmrs.module.referenceapplication;

import org.junit.Assert;
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
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.tasks.ProcessHL7InQueueTask;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

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
    
    @Autowired
    private SchedulerService schedulerService;

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
     * Tests that if there are no login locations and no unknown location then at least one of the
     * existing locations gets tagged as a login location
     *
     * @throws Exception
     */
    @Test
    public void testAddLoginLocationTagToAtleastOneLocationIfThereAreNoLoginLocations() throws Exception {
        final String newLocationName = "new name";
        final String previousLocationName = "Unknown Location";

        MetadataUtil.setupSpecificMetadata(getClass().getClassLoader(), "Reference_Application_Location_Tags");
        Location unknownLocation = locationService.getLocation(previousLocationName);
        unknownLocation.setName(newLocationName);
        locationService.saveLocation(unknownLocation);
        unknownLocation = locationService.getLocation(previousLocationName);
        assertNull(unknownLocation);
        assertEquals(newLocationName, locationService.getLocationByUuid(ReferenceMetadataConstants.UNKNOWN_LOCATION_UUID).getName());

        LocationTag loginTag = locationService.getLocationTagByUuid(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN_UUID);
        assertEquals(0, locationService.getLocationsByTag(loginTag).size());
        new ReferenceApplicationActivator().started();
        assertEquals(1, locationService.getLocationsByTag(loginTag).size());
    }
    
    /**
     * Tests that if process hl7 task is set up correctly
     *
     * @throws Exception
     */
	@Test
	public void testSetupOfProcessHL7Task() throws Exception {
		new ReferenceApplicationActivator().started();
		Collection<TaskDefinition> registeredTasks = schedulerService.getRegisteredTasks();
		TaskDefinition processHL7Task = null;
		for (TaskDefinition registeredTask : registeredTasks) {
			if (ProcessHL7InQueueTask.class.getName().equals(registeredTask.getTaskClass())) {
				processHL7Task = registeredTask;
			}
		}
		Assert.assertNotNull(processHL7Task);
	}

}
