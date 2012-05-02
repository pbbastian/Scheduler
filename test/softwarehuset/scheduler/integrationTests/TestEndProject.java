package softwarehuset.scheduler.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class TestEndProject {
	Scheduler scheduler;
	Developer author;
	Developer projectLeader;
	Developer developer;
	Session authorSession;
	Session projectLeaderSession;
	Session developerSession;
	Project project;
	Activity activity;
	
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
		activity = new Activity("Create more tests", Week.get(1), Week.get(2));
		projectLeaderSession.addActivityToProject(activity, project);
	}
	
	@Test
	public void testAsProjectLeader() throws Exception {
		assertTrue(project.isOngoing());
		projectLeaderSession.endProject(project);
		assertFalse(project.isOngoing());
	}
	
	@Test
	public void testAsNonProjectLeader() throws Exception {
		try {
			developerSession.endProject(project);
			fail("Expected NonProjectLeaderException");
		} catch (NonProjectLeaderException e) {
			assertEquals(developer, e.getDeveloper());
			assertEquals("Only the project leader can end projects", e.getMessage());
		}
	}
}
