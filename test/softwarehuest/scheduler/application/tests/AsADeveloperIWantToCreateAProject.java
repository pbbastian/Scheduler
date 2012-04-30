package softwarehuest.scheduler.application.tests;

import static org.junit.Assert.*;

import org.junit.*;
import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class AsADeveloperIWantToCreateAProject {
	@Test
	public void AsADeveloperIWantToCreateAProject() throws InvalidNameException, InvalidPinException, IncorrectCredentialsException {
		Scheduler scheduler = new Scheduler();
		Developer developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		Session session = scheduler.logIn(developer.getName(), developer.getPin());
		Project project = new Project("Test project");
		session.createProject(project);
		assertTrue(scheduler.getProjects().contains(project));
		assertNotNull(project.getId());
		assertEquals(developer, project.getAuthor());
	}
}
