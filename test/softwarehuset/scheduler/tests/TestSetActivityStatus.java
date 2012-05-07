package softwarehuset.scheduler.tests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class TestSetActivityStatus { // Peter
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
        activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity, project);
        projectLeaderSession.addDeveloperToProject(developer, project);
        projectLeaderSession.assignActivityToDeveloper(activity, developer);
    }
    
    @Test
    public void testAsAssignedDeveloper() throws Exception { // Peter
        developerSession.setActivityStatus(activity, Status.COMPLETED);
        assertEquals(Status.COMPLETED, activity.getStatus());
    }
    
    @Test
    public void testAsProjectLeader() throws Exception { // Peter
        projectLeaderSession.setActivityStatus(activity, Status.COMPLETED);
        assertEquals(Status.COMPLETED, activity.getStatus());
    }
    
    @Test
    public void testAsNonAssignedDeveloperOrProjectLeader() throws Exception { // Peter
        try {
            authorSession.setActivityStatus(activity, Status.COMPLETED);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only the project leader or a developer assigned to the activity can set its status", e.getMessage());
        }
    }

    @Test
    public void testWithNullStatus() throws Exception { // Peter
        try {
            authorSession.setActivityStatus(activity, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Activity status cannot be null", e.getMessage());
        }
    }
}
