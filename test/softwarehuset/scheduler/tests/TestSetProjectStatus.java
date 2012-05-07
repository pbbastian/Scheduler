package softwarehuset.scheduler.tests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.exceptions.OngoingOrPausedActivitiesException;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import static junit.framework.Assert.*;

public class TestSetProjectStatus { // Kristian
    Scheduler scheduler;
    Developer author;
    Developer projectLeader;
    Developer developer;
    Session authorSession;
    Session projectLeaderSession;
    Session developerSession;
    Project project;

    @Before
    public void setUp() throws Exception { // Kristian
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
    public void testAsProjectLeader() throws Exception { // Kristian
        projectLeaderSession.setProjectStatus(project, Status.COMPLETED);
        assertEquals(Status.COMPLETED, project.getStatus());
    }

    @Test
    public void testAsNonProjectLeader() throws Exception { // Kristian
        try {
            authorSession.setProjectStatus(project, Status.COMPLETED);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only the project leader can set its status", e.getMessage());
        }
    }

    @Test
    public void testWithNullStatus() throws Exception { // Kristian
        try {
            projectLeaderSession.setProjectStatus(project, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Project status cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithOngoingActivity() throws Exception { // Kristian
        Activity activity1 = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity1, project);
        projectLeaderSession.addDeveloperToProject(developer, project);
        projectLeaderSession.assignActivityToDeveloper(activity1, developer);

        Activity activity2 = new Activity("Create even more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity2, project);
        projectLeaderSession.addDeveloperToProject(developer, project);
        projectLeaderSession.assignActivityToDeveloper(activity2, developer);
        projectLeaderSession.setActivityStatus(activity2, Status.PAUSED);

        try {
            projectLeaderSession.setProjectStatus(project, Status.COMPLETED);
            fail("Expected OngoingOrPausedActivitiesException");
        } catch (OngoingOrPausedActivitiesException e) {
            assertTrue(e.getActivities().contains(activity1));
            assertTrue(e.getActivities().contains(activity2));
        }
    }
}
