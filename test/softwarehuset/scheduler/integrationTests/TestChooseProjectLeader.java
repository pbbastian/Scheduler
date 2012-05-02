package softwarehuset.scheduler.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class TestChooseProjectLeader {
	Scheduler scheduler;
	Developer developer1;
	Developer developer2;
	
	@Before
	public void setUp() throws ArgumentException, IncorrectCredentialsException {
		scheduler = new Scheduler();
		developer1 = new Developer("Peter Bay Bastian", "12345");
		developer2 = new Developer("Kristian Dam-Jensen", "qwerty");
		scheduler.register(developer1);
		scheduler.register(developer2);
	}
	
	@Test
	public void testAsProjectAuthor() throws Exception {
		Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
		Project project = new Project("Peters Projekt");
		session.registerProject(project);
		session.chooseProjectLeader(project, developer2);
		assertEquals(developer2, project.getProjectLeader());
	}
	
//	@Test
//	public void testAsProjectDeveloper() throws Exception {
//		DeveloperSession developerSession = scheduler.logIn(developer1.getName(), developer1.getPin());
//		Project project = new Project("Peters Projekt");
//		developerSession.registerProject(project);
//		ProjectSession projectSession = developerSession.getProjectSession(project);
//		projectSession.chooseProjectLeader(developer1);
//		session.addDeveloper(developer2);
//		
//		developerSession = scheduler.logIn(developer2.getName(), developer2.getPin());
//		projectSession.chooseProjectLeader(developer2);
//	}
	
	@Test
	public void testAsNonProjectDeveloperOrAuthor() throws Exception {
		Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
		Project project = new Project("Peters Projekt");
		session.registerProject(project);
		
		session = scheduler.logIn(developer2.getName(), developer2.getPin());
		try {
			session.chooseProjectLeader(project, developer2);
			fail("Expected InsufficientRightsException");
		} catch (InsufficientRightsException e) {
			assertEquals("Only the project author or a developer on the project can choose a project leader", e.getMessage());
		}
	}
	
	@Test
	public void testAsNonRegisteredDeveloper() throws Exception {
		Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
		Project project = new Project("Peters Projekt");
		session.registerProject(project);
		Developer fakeDeveloper = new Developer("Un Registered", "12345");
		try {
			session.chooseProjectLeader(project, fakeDeveloper);
			fail("Expected NonRegisteredDeveloperException");
		} catch (NonRegisteredDeveloperException e) {
			assertEquals(fakeDeveloper, e.getDeveloper());
			assertEquals("Project leader must be a registered developer", e.getMessage());
		}
	}
}
