package softwarehuset.scheduler.tests;

import static org.junit.Assert.*;

import org.junit.*;
import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;
import softwarehuset.scheduler.exceptions.AlreadyRegisteredProjectException;
import softwarehuset.scheduler.exceptions.ArgumentException;

public class TestCreateProject { // Peter
	Scheduler scheduler;
	Developer developer;
	Session session;
	
	@Before
	public void setUp() throws Exception { // Peter
		scheduler = new Scheduler();
		developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		session = scheduler.logIn(developer.getName(), developer.getPin());
	}
	
	@Test
	public void testWhenProjectIsValid() throws Exception { // Peter
		Project project = new Project("Test project");
		session.registerProject(project);
		assertTrue(scheduler.getProjects().contains(project));
		assertNotNull(project.getId());
		assertEquals(developer, project.getAuthor());
		assertEquals(Status.ONGOING, project.getStatus());
	}
	
	@Test
	public void testWhenProjectNameIsNull() throws Exception { // Peter
		Project project = new Project(null);
		try {
			session.registerProject(project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Project name cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testWhenProjectNameIsTooShort() throws Exception { // Peter
		Project project = new Project("");
		try {
			session.registerProject(project);
			fail("Expected ArgumentException");
		} catch (ArgumentException e) {
			assertEquals(project.getName(), e.getArgument());
			assertEquals("Project name must have a length of at least 1", e.getMessage());
		}
	}
	
	@Test
	public void testWhenProjectIsAlreadyRegistered() throws Exception { // Peter
		Project project = new Project("Test project");
		session.registerProject(project);
		try {
			session.registerProject(project);
			fail("Expected AlreadyRegisteredProjectException");
		} catch (AlreadyRegisteredProjectException e) {
			assertEquals(project, e.getProject());
			assertEquals("Project is already registered", e.getMessage());
		}
	}
}
