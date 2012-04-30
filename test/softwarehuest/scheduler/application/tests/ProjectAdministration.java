package softwarehuest.scheduler.application.tests;

import static org.junit.Assert.*;

import org.junit.*;
import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class ProjectAdministration {
	Scheduler scheduler;
	Developer developer;
	Session session;
	
	@Before
	public void setUp() throws InvalidNameException, InvalidPinException, IncorrectCredentialsException {
		scheduler = new Scheduler();
		developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		session = scheduler.logIn(developer.getName(), developer.getPin());
	}
	
	@Test
	public void createProjectAsDeveloper() {
		Project project = new Project("Test project");
		session.createProject(project);
		assertTrue(scheduler.getProjects().contains(project));
		assertNotNull(project.getId());
		assertEquals(developer, project.getAuthor());
	}
}
