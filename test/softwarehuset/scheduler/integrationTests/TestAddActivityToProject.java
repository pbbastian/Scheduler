package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.ArgumentException;
import softwarehuset.scheduler.exceptions.NonProjectLeaderException;
import softwarehuset.scheduler.exceptions.NonRegisteredProjectException;

import static org.junit.Assert.*;

public class TestAddActivityToProject { // Peter
	Scheduler scheduler;
	Developer author;
	Developer projectLeader;
	Developer developer;
	Session authorSession;
	Session projectLeaderSession;
	Session developerSession;
	Project project;
	
	@Before
	public void setUp() throws Exception { // Peter
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
	public void testAsProjectLeader() throws Exception { // Peter
		Activity activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
		projectLeaderSession.addActivityToProject(activity, project);
		assertTrue(project.getActivities().contains(activity));
		assertEquals(project, activity.getProject());
		assertEquals(projectLeader, activity.getAuthor());
		assertNotNull(activity.getId());
        assertEquals(Status.ONGOING, activity.getStatus());
	}
	
	@Test
	public void testAsProjectLeaderWithNullActivityName() throws Exception { // Peter
		Activity activity = new Activity(null, Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Activity description cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testAsProjectLeaderWithNullCreationDate() throws Exception { // Peter
		Activity activity = new Activity("Create more tests", null, Scheduler.getWeek(2, 2012));
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Activity creation date cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testAsProjectLeaderWithNullDueDate() throws Exception { // Peter
		Activity activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), null);
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("Activity due date cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testAsProjectLeaderWithTooShortActivityName() throws Exception { // Peter
		Activity activity = new Activity("", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
		try {
			projectLeaderSession.addActivityToProject(activity, project);
			fail("Expected ArgumentException");
		} catch (ArgumentException e) {
			assertEquals(activity.getDescription(), e.getArgument());
			assertEquals("Activity description must have a length of minimum 1", e.getMessage());
		}
	}
	
	@Test
	public void testAsNonProjectLeader() throws Exception { // Peter
		Activity activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
		try {
			developerSession.addActivityToProject(activity, project);
			fail("Expected NonProjectLeaderException");
		} catch (NonProjectLeaderException e) {
			assertEquals(developer, e.getDeveloper());
			assertEquals("Only the project leader can add activities", e.getMessage());
		}
	}
	
	@Test
	public void testWithNonRegisteredProject() throws Exception { // Peter
		Activity activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
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
