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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestProjectTimeStatistics { // Kristian
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
        activity = new Activity("Create more tests", Scheduler.getWeek(1, 2012), Scheduler.getWeek(2, 2012));
        projectLeaderSession.addActivityToProject(activity, project);
        projectLeaderSession.addDeveloperToProject(developer, project);
        projectLeaderSession.assignActivityToDeveloper(activity, developer);
        developerSession.spendTimeOnProjectActivity(new ActivityTimePeriod(activity, 7, 16));
        developerSession.spendTimeOnProjectActivity(new ActivityTimePeriod(activity, 14, 16));
    }
    @Test
    public void timeSpentOnProject() throws Exception { // Kristian
    	double time = projectLeaderSession.getTimeSpentOnActivities(project);
    	assertTrue(time == 13);
    }
    
    @Test
    public void timeSpentOnProjectNotProjectLeader() throws Exception { // Kristian
    	try {
    		developerSession.getTimeSpentOnActivities(project);
            fail("Expected InsufficientRightsException");
    	} catch (InsufficientRightsException e) {
            assertEquals("Only a project leader is allowed to do this.", e.getMessage());
        }
    }
    
    @Test
    public void timeSpentOnProjectNullProject() throws Exception { // Kristian
    	try {
    		projectLeaderSession.getTimeSpentOnActivities(null);
            fail("NullPointerException was expected, never came");
    	} catch (NullPointerException e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
    }
}