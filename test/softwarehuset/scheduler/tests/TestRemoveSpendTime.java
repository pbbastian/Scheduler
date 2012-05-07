package softwarehuset.scheduler.tests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.ActivityTimePeriod;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import static junit.framework.Assert.*;

public class TestRemoveSpendTime { // Peter
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
    ActivityTimePeriod timePeriod;

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
        timePeriod = new ActivityTimePeriod(activity, 15, 16);
        developer1Session.spendTimeOnProjectActivity(timePeriod);
    }

    @Test
    public void testAsTimePeriodOwner() throws Exception { // Peter
        developer1Session.removeActivityTimePeriod(timePeriod);
        assertFalse(developer1.getActivityTimePeriods().contains(timePeriod));
    }

    @Test
    public void testAsNonTimePeriodOwner() throws Exception { // Peter
        try {
            developer2Session.removeActivityTimePeriod(timePeriod);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only the developer that spend the time can remove it", e.getMessage());
        }
    }
}
