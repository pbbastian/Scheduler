package softwarehuset.scheduler.tests;


import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.ActivityTimePeriod;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestGenerateProjectReport { // Kristian
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
        developerSession.spendTimeOnProjectActivity(new ActivityTimePeriod(activity, 8, 17));
    }
    
    @Test
    public void testgenerateReport() throws Exception { // Kristian
    	//for (Developer dev : project.getDevelopers()) {
		//	System.out.println(dev.getName());
		//}
    	
    	projectLeaderSession.generateProjectReport(project);
    	
    	String expectedReport = "<!DOCTYPE html PUBLIC >\n<head>\n<title>\nProject Report</title>\n</head>\n<body>\n<h1>Project Report : "+project.getName()+"</h1>\n<p>TOTAL TIME SPENT: 10<br>\nAvg. time spent by a Developer: 5.0<br>\nAvg. time spent on an Activity: 10.0</p>\n</html>\n";
    	BufferedReader in = new BufferedReader(new FileReader(project.getName()+"_report.html"));
    	String textIn = "";
        String str;
        while ((str = in.readLine()) != null) {
            textIn = textIn+str+"\n";
        }
        in.close();
        assertEquals(expectedReport, textIn);
    }
}
