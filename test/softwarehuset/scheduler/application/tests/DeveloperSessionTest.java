package softwarehuset.scheduler.application.tests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class DeveloperSessionTest {
	Scheduler scheduler;
	Developer developer;
	DeveloperSession developerSession;
	
	@Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		developer = new Developer("Peter Bay Bastian", "qwerty");
		scheduler.register(developer);
		developerSession = scheduler.logIn(developer.getName(), developer.getPin());
	}
	
	@Test
	public void testGetProjectSessionWithNonRegisteredProject() throws Exception {
		Project project = new Project("Test project");
		try {
			developerSession.getProjectSession(project);
			fail("Expected NonRegisteredProjectException");
		} catch (NonRegisteredProjectException e) {
			assertEquals(e.getProject(), project);
			assertEquals("Project must be a registered project", e.getMessage());
		}
	}
}
