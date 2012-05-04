package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import static junit.framework.Assert.*;

public class TestRemoveActivity {
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
        projectLeaderSession.removeActivity(activity);
        assertFalse(project.getActivities().contains(activity));
        assertFalse(developer.getCurrentActivities().contains(activity));
    }
    
    @Test
    public void testAsNonProjectLeader() {
        try {
            developerSession.removeActivity(activity);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only the project leader can remove activities", e.getMessage());
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testWithNullActivity() throws Exception {
        projectLeaderSession.removeActivity(null);
    }
}
