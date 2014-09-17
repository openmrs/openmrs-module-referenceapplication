package org.openmrs.module.referenceapplication;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.tasks.ProcessHL7InQueueTask;
import org.mockito.Matchers;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

public class ReferenceApplicationActivatorTest {
	
	@Mock
	SchedulerService schedulerService;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldNotCreateTaskWhenAlreadyRegistered() {
		TaskDefinition processHL7Task = Mockito.mock(TaskDefinition.class);
		when(processHL7Task.getTaskClass()).thenReturn(ProcessHL7InQueueTask.class.getName());
		when(schedulerService.getRegisteredTasks()).thenReturn(Arrays.asList(processHL7Task));
		
		new ReferenceApplicationActivator().setupHL7ProcessingTask(schedulerService);
		
		verify(schedulerService, never()).saveTask(Matchers.any(TaskDefinition.class));
	}
	
	@Test
	public void shouldSaveNewTaskIfNotAlreadyRegistered() {
		
		when(schedulerService.getRegisteredTasks()).thenReturn(new ArrayList<TaskDefinition>());
		
		new ReferenceApplicationActivator().setupHL7ProcessingTask(schedulerService);
		
		verify(schedulerService).saveTask(Matchers.argThat(new BaseMatcher<TaskDefinition>() {
			
			@Override
			public boolean matches(Object obj) {
				return obj instanceof TaskDefinition
				        && ProcessHL7InQueueTask.class.getName().equals(((TaskDefinition) obj).getTaskClass());
			}
			
			@Override
			public void describeTo(Description description) {				
			}
		}));
		
	}
}
