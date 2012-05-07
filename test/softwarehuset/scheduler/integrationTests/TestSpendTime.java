package softwarehuset.scheduler.integrationTests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.ActivityTimePeriod;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.ArgumentException;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.Assert.*;

public class TestSpendTime { // Peter
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
    public void testAsDeveloperAssignedToActivity() throws Exception { // Peter
        ActivityTimePeriod timePeriod = new ActivityTimePeriod(activity, 15, 16);
        developer1Session.spendTimeOnProjectActivity(timePeriod);
        assertTrue(developer1.getActivityTimePeriods().contains(timePeriod));
        assertEquals(GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR), timePeriod.getDate().get(Calendar.DAY_OF_YEAR));
        assertEquals(GregorianCalendar.getInstance().get(Calendar.YEAR), timePeriod.getDate().get(Calendar.YEAR));
    }

    @Test
    public void testAsAssistingDeveloper() throws Exception { // Peter
        developer1Session.requestAssistance(activity, developer2);
        ActivityTimePeriod timePeriod = new ActivityTimePeriod(activity, 15, 16);
        developer2Session.spendTimeOnProjectActivity(timePeriod);
        assertTrue(developer2.getActivityTimePeriods().contains(timePeriod));
        assertEquals(GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR), timePeriod.getDate().get(Calendar.DAY_OF_YEAR));
        assertEquals(GregorianCalendar.getInstance().get(Calendar.YEAR), timePeriod.getDate().get(Calendar.YEAR));
    }

    @Test
    public void testAsDeveloperNotAssignedToActivity() throws Exception { // Peter
        try {
            ActivityTimePeriod timePeriod = new ActivityTimePeriod(activity, 15, 16);
            developer2Session.spendTimeOnProjectActivity(timePeriod);
            fail("Expected InsufficientRightsException");
        } catch (InsufficientRightsException e) {
            assertEquals("Only a developer assigned to the activity (or one assisting) can spend time on it.", e.getMessage());
        }
    }

    @Test
    public void testWithStartHourLessThan0() throws Exception { // Peter
        try {
            ActivityTimePeriod timePeriod = new ActivityTimePeriod(activity, -5, 16);
            developer1Session.spendTimeOnProjectActivity(timePeriod);
        } catch (ArgumentException e) {
            assertEquals(-5, ((Integer)e.getArgument()).intValue());
            assertEquals("Start hour must be at least 0 and less than the end hour", e.getMessage());
        }
    }

    @Test
    public void testWithStartHourNotLessThanEndHour() throws Exception { // Peter
        try {
            ActivityTimePeriod timePeriod = new ActivityTimePeriod(activity, 16, 16);
            developer1Session.spendTimeOnProjectActivity(timePeriod);
        } catch (ArgumentException e) {
            assertEquals(16, ((Integer)e.getArgument()).intValue());
            assertEquals("Start hour must be at least 0 and less than the end hour", e.getMessage());
        }
    }

    @Test
    public void testWithEndHourLargerThan23() throws Exception { // Peter
        try {
            ActivityTimePeriod timePeriod = new ActivityTimePeriod(activity, 15, 24);
            developer1Session.spendTimeOnProjectActivity(timePeriod);
        } catch (ArgumentException e) {
            assertEquals(24, ((Integer)e.getArgument()).intValue());
            assertEquals("End hour must be at at most 23", e.getMessage());
        }
    }
}
