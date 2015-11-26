package org.openmrs.module.referenceapplication.administrativenotification;

import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNull;

public class ConfigureAtlasNotificationProducerTest extends BaseModuleContextSensitiveTest {

    @Autowired
    ConfigureAtlasNotificationProducer producer;

    @Test
    public void testWhenAtlasModuleIsNotInstalled() throws Exception {
        assertNull(producer.generateNotifications());
    }

//    These tests were written when this test class (and its class) were in the Atlas module. They fail
//    now that we're in the Reference Application module, but I'm keeping them around in case someone wants
//    to figure out how to load the Atlas module's service during testing, and thus run these tests again
//
//    @Before
//    public void setUp() throws Exception {
//        new AtlasModuleActivator().started();
//        producer.clearCache();
//    }
//
//    @Test
//    public void testGenerateNotificationsWhenNotConfigured() throws Exception {
//        Context.getService(AtlasService.class).disableAtlasModule();
//        List<AdministrativeNotification> notifications = producer.generateNotifications();
//        assertThat(notifications.size(), is(1));
//        assertThat(notifications.get(0).getId(), is("atlas.configure"));
//    }
//
//    @Test
//    public void testGenerateNotificationsWhenConfigured() throws Exception {
//        Context.getService(AtlasService.class).enableAtlasModule();
//        List<AdministrativeNotification> notifications = producer.generateNotifications();
//        assertNull(notifications);
//    }
//
//    @Test
//    public void testGenerateNotificationsWhenStoppedAsking() throws Exception {
//        Context.getService(AtlasService.class).disableAtlasModule();
//        Context.getService(AtlasService.class).setStopAskingToConfigure(true);
//        List<AdministrativeNotification> notifications = producer.generateNotifications();
//        assertNull(notifications);
//    }

}