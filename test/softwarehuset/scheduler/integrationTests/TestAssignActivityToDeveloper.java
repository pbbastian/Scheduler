package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.DeveloperNotInProjectException;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestAssignActivityToDeveloper {
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
    }
    
    @Test
    public void testAsProjectLeader() throws Exception {
        projectLeaderSession.assignActivityToDeveloper(activity, developer);
        assertTrue(developer.getCurrentActivities().contains(activity));
        assertTrue(activity.getDevelopers().contains(developer));
    }
    
    @Test
    public void testAsNonProjectLeader() throws Exception {
        try {
            developerSession.assignActivityToDeveloper(activity, developer);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only the project leader can assign activities to developers", e.getMessage());
        }
    }
    
    @Test
    public void testWithDeveloperNotInProject() throws Exception {
        Activity anotherActivity = new Activity("Don't create more tests", Scheduler.getWeek(1), Scheduler.getWeek(2));
        Project anotherProject = new Project("Another project");
        authorSession.registerProject(anotherProject);
        authorSession.chooseProjectLeader(anotherProject, projectLeader);
        projectLeaderSession.addActivityToProject(anotherActivity, anotherProject);
        try {
            projectLeaderSession.assignActivityToDeveloper(anotherActivity, developer);
            fail("Expected DeveloperNotInProjectException");
        } catch (DeveloperNotInProjectException e) {
            assertEquals("The developer is not in the project that the activity belongs to", e.getMessage());
        }
    }
}
