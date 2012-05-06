package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.ActivityTimePeriod;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import java.util.List;

import static junit.framework.Assert.*;

public class TestGetRemainingActivities {
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

    @Before
    public void setUp() throws Exception {
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
    }

    @Test
    public void testWithNoCompletedActivities() {
        List<Activity> remainingActivities = scheduler.getRemainingActivities(project);
        assertTrue(remainingActivities.isEmpty());
    }

    @Test
    public void testWith2CompletedActivities() throws Exception {
        Activity activity1 = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity1, project);
        Activity activity2 = new Activity("Create even more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity2, project);

        List<Activity> remainingActivities = scheduler.getRemainingActivities(project);
        assertEquals(2, remainingActivities.size());
        assertTrue(remainingActivities.contains(activity1));
        assertTrue(remainingActivities.contains(activity2));
    }
}
