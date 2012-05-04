package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.ActivityNotAssignedToDeveloperException;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import static junit.framework.Assert.*;

public class TestUnassignActivityFromDeveloper {
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
        activity = new Activity("Create more tests", Scheduler.getWeek(1), Scheduler.getWeek(2));
        projectLeaderSession.addActivityToProject(activity, project);
        projectLeaderSession.addDeveloperToProject(developer, project);
        projectLeaderSession.assignActivityToDeveloper(activity, developer);
    }

    @Test
    public void testAsProjectLeader() throws Exception {
        projectLeaderSession.unassignActivityFromDeveloper(activity, developer);
        assertFalse(developer.getCurrentActivities().contains(activity));
        assertFalse(activity.getDevelopers().contains(developer));
    }

    @Test
    public void testWithActivityNotAssignedToDeveloper() throws Exception {
        projectLeaderSession.unassignActivityFromDeveloper(activity, developer);
        try {
            projectLeaderSession.unassignActivityFromDeveloper(activity, developer);
            fail("Expected ActivityNotAssignedToDeveloperException");
        } catch (ActivityNotAssignedToDeveloperException e) {
            assertEquals("Can't unassign an activity from a developer that it's not assigned to", e.getMessage());
        }
    }
}
