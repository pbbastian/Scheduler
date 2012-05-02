package softwarehuset.scheduler.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class TestAddActivityToProject {
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
		Activity activity = new Activity("Create more tests", Week.get(1), Week.get(2));
		projectLeaderSession.addActivityToProject(activity, project);
		assertTrue(project.getActivities().contains(activity));
		assertEquals(projectLeader, activity.getAuthor());
		assertNotNull(activity.getId());
	}
	
	@Test
	public void testAsProjectLeaderWithNullActivityName() throws Exception {
		Activity activity = new Activity(null, Week.get(1), Week.get(2));
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Activity description cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testAsProjectLeaderWithNullCreationDate() throws Exception {
		Activity activity = new Activity("Create more tests", null, Week.get(2));
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Activity creation date cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testAsProjectLeaderWithNullDueDate() throws Exception {
		Activity activity = new Activity("Create more tests", Week.get(1), null);
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Activity due date cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testAsProjectLeaderWithTooShortActivityName() throws Exception {
		Activity activity = new Activity("", Week.get(1), Week.get(2));
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected ArgumentException");
		} catch (ArgumentException e) {
			assertEquals(activity.getDescription(), e.getArgument());
			assertEquals("Activity description must have a length of minimum 1", e.getMessage());
		}
	}
	
	@Test
	public void testAsNonProjectLeader() throws Exception {
		Activity activity = new Activity("Create more tests", Week.get(1), Week.get(2));
		try {
			developerSession.addActivityToProject(activity, project);
			fail("Expected NonProjectLeaderException");
		} catch (NonProjectLeaderException e) {
			assertEquals(developer, e.getDeveloper());
			assertEquals("Only the project leader can add activities", e.getMessage());
		}
	}
	
	@Test
	public void testWithNonRegisteredProject() throws Exception {
		Activity activity = new Activity("Create more tests", Week.get(1), Week.get(2));
		Project fakeProject = new Project("Fake project");
		fakeProject.setProjectLeader(projectLeader);
		try {
			projectLeaderSession.addActivityToProject(activity, fakeProject);
			fail("Expected NonRegisteredProjectException");
		} catch (NonRegisteredProjectException e) {
			assertEquals(fakeProject, e.getProject());
			assertEquals("Can't add an activity to a non-registered project", e.getMessage());
		}
	}
}
