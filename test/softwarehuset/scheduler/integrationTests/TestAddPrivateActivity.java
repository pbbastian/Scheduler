package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.PrivateActivity;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.ArgumentException;

import static junit.framework.Assert.*;

public class TestAddPrivateActivity {
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
    public void testWithValidPrivateActivity() throws Exception {
        PrivateActivity privateActivity = new PrivateActivity("Ferie", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        developerSession.addPrivateActivity(privateActivity);
        assertTrue(developer.getPrivateActivities().contains(privateActivity));
        assertTrue(privateActivity.getDeveloper().equals(developer));
    }
    
    @Test(expected = NullPointerException.class)
    public void testWithNullPrivateActivity() throws Exception {
        developerSession.addPrivateActivity(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullPrivateActivityDescription() throws Exception {
        developerSession.addPrivateActivity(new PrivateActivity(null, Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012)));
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullPrivateActivityStart() throws Exception {
        developerSession.addPrivateActivity(new PrivateActivity("Ferie", null, Scheduler.getWeek(2, 2012)));
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullPrivateActivityEnd() throws Exception {
        developerSession.addPrivateActivity(new PrivateActivity("Ferie", Scheduler.getWeek(1, 2012), null));
    }
    
    @Test
    public void testWithTooShortPrivateActivityDescription() {
        PrivateActivity privateActivity = new PrivateActivity("", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        try {
            developerSession.addPrivateActivity(privateActivity);
        } catch (ArgumentException e) {
            assertEquals(privateActivity.getDescription(), e.getArgument());
            assertEquals("Private activity description must have a length of minimum 1", e.getMessage());
        }
    }
}
