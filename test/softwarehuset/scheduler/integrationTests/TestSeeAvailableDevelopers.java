package softwarehuset.scheduler.integrationTests;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import static org.junit.Assert.assertTrue;

public class TestSeeAvailableDevelopers {
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
    public void setUp() throws Exception{
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
    public void testAsProjectLeader() throws Exception {
    	List<Developer> availableDevelopers;
    	int maxPersonal = 0;
    	int maxProject = 1;
    	
    	availableDevelopers = projectLeaderSession.getAvailableDevelopers(maxPersonal, maxProject);
    	assertTrue(availableDevelopers.size() == 2);
    	assertTrue(availableDevelopers.contains(developer) && availableDevelopers.contains(author));
    }
}
