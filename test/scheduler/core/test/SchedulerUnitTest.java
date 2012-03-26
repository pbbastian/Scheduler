package scheduler.core.test;

import org.junit.*;

import scheduler.core.Developer;
import static org.junit.Assert.*;

public class SchedulerUnitTest extends StandardContext {
	@Test
	public void createDeveloperShouldReturnDeveloper() {
		Developer developer = scheduler.createDeveloper("Peter Bay Bastian", 1);
		
		assertNotNull(developer);
		assertEquals(1, developer.getId());
		assertEquals("Peter Bay Bastian", developer.getName());
	}
	
	@Test
	public void createDeveloperShouldAddDeveloperToList() {
		scheduler.createDeveloper("Peter Bay Bastian", 1);
		Developer developer = scheduler.getDevelopers().get(0);
		
		assertNotNull(developer);
		assertEquals(1, developer.getId());
		assertEquals("Peter Bay Bastian", developer.getName());
	}
}
