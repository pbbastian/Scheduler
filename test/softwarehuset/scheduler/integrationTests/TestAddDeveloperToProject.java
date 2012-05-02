package softwarehuset.scheduler.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class TestAddDeveloperToProject {
	Scheduler scheduler;
	Developer author;
	Developer projectLeader;
	Developer developer;
	Session authorSession;
	Session projectLeaderSession;
	Session developerSession;
	Project project;
	
	@Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
		author = new Developer("Peter Bay Bastian", "12345");
		projectLeader = new Developer("Kristian Dam-Jensen", "qwerty");
		developer = new Developer("Deve Loper", "qwerty12345");
		scheduler.register(author);
		scheduler.register(projectLeader);
		scheduler.register(developer);
		authorSession = scheduler.logIn(author.getName(), author.getPin());
		projectLeaderSession = scheduler.logIn(projectLeader.getName(), projectLeader.getPin());
		developerSession = scheduler.logIn(developer.getName(), developer.getPin());
		project = new Project("Test Project");
		authorSession.registerProject(project);
		authorSession.chooseProjectLeader(project, projectLeader);
	}
	
	@Test
	public void testAsProjectLeader() throws Exception {
		projectLeaderSession.addDeveloperToProject(developer, project);
		assertTrue(project.getDevelopers().contains(developer));
	}
	
	@Test
	public void testAsNonProjectLeader() throws Exception {
		try {
			developerSession.addDeveloperToProject(author, project);
			fail("Expected NonProjectLeaderException");
		} catch (NonProjectLeaderException e) {
			assertEquals(developer, e.getDeveloper());
			assertEquals("Only the project leader can add developers", e.getMessage());
		}
	}
	
	@Test
	public void testWithNonRegisteredDeveloper() throws Exception {
		Developer fakeDeveloper = new Developer("Fake Developer", "12345");
		try {
			projectLeaderSession.addDeveloperToProject(fakeDeveloper, project);
			fail("Expected NonRegisteredDeveloperException");
		} catch (NonRegisteredDeveloperException e) {
			assertEquals(fakeDeveloper, e.getDeveloper());
			assertEquals("Developer must be a registered developer", e.getMessage());
		}
	}
}
