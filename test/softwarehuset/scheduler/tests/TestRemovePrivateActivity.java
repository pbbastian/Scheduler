package softwarehuset.scheduler.tests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.PrivateActivity;
import softwarehuset.scheduler.domain.Project;

import static junit.framework.Assert.*;

public class TestRemovePrivateActivity { // Kristian
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
    public void test() throws Exception { // Kristian
        PrivateActivity activity = new PrivateActivity("Vacation", Scheduler.getWeek(26, 2012), Scheduler.getWeek(36, 2012));
        developerSession.addPrivateActivity(activity);
        developerSession.removePrivateActivity(activity);
        assertFalse(developer.getPrivateActivities().contains(activity));
    }
}
