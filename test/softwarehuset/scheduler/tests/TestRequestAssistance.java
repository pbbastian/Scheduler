package softwarehuset.scheduler.tests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;
import softwarehuset.scheduler.exceptions.NonRegisteredDeveloperException;

import static junit.framework.Assert.*;

public class TestRequestAssistance { // Peter
    Scheduler scheduler;
    Developer author;
    Developer projectLeader;
    Developer developer1;
    Developer developer2;
    Session authorSession;
    Session projectLeaderSession;
    Session developer1Session;
    Session developer2Session;
    Project project;
    Activity activity;

    @Before
    public void setUp() throws Exception { // Peter
        scheduler = new Scheduler();
        author = new Developer("Peter Bay Bastian", "12345");
        projectLeader = new Developer("Kristian Dam-Jensen", "qwerty");
        developer1 = new Developer("Deve Loper", "qwerty12345");
        developer2 = new Developer("Loper Deve", "12345qwerty");
        scheduler.register(author);
        scheduler.register(projectLeader);
        scheduler.register(developer1);
        scheduler.register(developer2);
        authorSession = scheduler.logIn(author.getName(), author.getPin());
        projectLeaderSession = scheduler.logIn(projectLeader.getName(), projectLeader.getPin());
        developer1Session = scheduler.logIn(developer1.getName(), developer1.getPin());
        developer2Session = scheduler.logIn(developer2.getName(), developer2.getPin());
        project = new Project("Test Project");
        authorSession.registerProject(project);
        authorSession.chooseProjectLeader(project, projectLeader);
        activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity, project);
        projectLeaderSession.addDeveloperToProject(developer1, project);
        projectLeaderSession.assignActivityToDeveloper(activity, developer1);
    }

    @Test
    public void testAsDeveloperInProjectAssignedToActivity() throws Exception { // Peter
        developer1Session.requestAssistance(activity, developer2);
        assertTrue(developer2.getCurrentActivities().contains(activity));
        assertTrue(activity.getDevelopers().contains(developer2));
    }

    @Test
    public void testAsDeveloperInProjectNotAssignedToActivity() throws Exception { // Peter
        try {
            projectLeaderSession.requestAssistance(activity, developer2);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only developers assigned to the activity can request assistance with it", e.getMessage());
        }
    }

    @Test
    public void testWithUnregisteredDeveloper() throws Exception { // Peter
        Developer fakeDeveloper = new Developer("Fake Dev", "123");
        try {
            developer1Session.requestAssistance(activity, fakeDeveloper);
            fail("Expected NonRegisteredDeveloperException");
        } catch (NonRegisteredDeveloperException e) {
            assertEquals("Assisting developer must be registered in the system", e.getMessage());
            assertEquals(fakeDeveloper, e.getDeveloper());
        }
    }
}
