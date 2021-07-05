package org.openmrs.module.referenceapplication.administrativenotification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class DuplicateConceptNotificationTest extends BaseModuleContextSensitiveTest  {
     
	@Autowired
	DuplicateConceptNotification producer;
	
	@Before
   public void setUp() throws Exception {
	initializeInMemoryDatabase();
  }
	
	 @Test
	    public void shouldNotProduceNotificationWhenDuplicateConceptsDoesNotExist() throws Exception {
		 executeDataSet("NoConceptsDuplicates.xml");
			authenticate();
	        assertNull(producer.generateNotifications());
	    }
	 
	 
	 @Test
	    public void shouldProduceNotificationWhenDuplicateConceptsExist() throws Exception {

			executeDataSet("ConceptsDuplicates.xml");
			authenticate();
			
			 List<AdministrativeNotification> notifications = producer.generateNotifications();
	        assertEquals(notifications.size(), 1);
	        assertEquals(notifications.get(0).getId(), "DuplicateConcepts.id");
	      
	    }
	 
	    ;

}
