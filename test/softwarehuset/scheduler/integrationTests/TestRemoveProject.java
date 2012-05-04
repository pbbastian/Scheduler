package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

public class TestRemoveProject {
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
        activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity, project);
        projectLeaderSession.addDeveloperToProject(developer, project);
        projectLeaderSession.assignActivityToDeveloper(activity, developer);
    }
    
    @Test
    public void testAsAuthor() throws Exception {
        authorSession.removeProject(project);
        assertFalse(scheduler.getProjects().contains(project));
        assertFalse(developer.getProjects().contains(project));
        assertFalse(developer.getCurrentActivities().contains(activity));
    }
    
    @Test
    public void testAsProjectLeader() throws Exception {
        projectLeaderSession.removeProject(project);
        assertFalse(scheduler.getProjects().contains(project));
        assertFalse(developer.getProjects().contains(project));
        assertFalse(developer.getCurrentActivities().contains(activity));
    }
    
    @Test
    public void testAsNonAuthorOrProjectLeader() throws Exception {
        try {
            developerSession.removeProject(project);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only the project author or leader can remove it", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullProject() throws Exception {
        projectLeaderSession.removeProject(null);
    }
}
