package softwarehuset.scheduler.tests;

import org.junit.Before;
import org.junit.Test;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.ArgumentException;
import softwarehuset.scheduler.exceptions.IncorrectCredentialsException;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;
import softwarehuset.scheduler.exceptions.NonRegisteredDeveloperException;

import static org.junit.Assert.*;

public class TestChooseProjectLeader { // Peter
	Scheduler scheduler;
	Developer developer1;
	Developer developer2;
	
	@Before
	public void setUp() throws Exception { // Peter
		scheduler = new Scheduler();
		developer1 = new Developer("Peter Bay Bastian", "12345");
		developer2 = new Developer("Kristian Dam-Jensen", "qwerty");
		scheduler.register(developer1);
		scheduler.register(developer2);
	}
	
	@Test
	public void testAsProjectAuthor() throws Exception { // Peter
		Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
		Project project = new Project("Peters Projekt");
		session.registerProject(project);
		session.chooseProjectLeader(project, developer2);
		assertEquals(developer2, project.getProjectLeader());
        assertTrue(developer2.getProjects().contains(project));
        assertTrue(project.getDevelopers().contains(developer2));
	}
	
	@Test
	public void testAsProjectDeveloper() throws Exception { // Peter
        Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
        Project project = new Project("Peters Projekt");
        session.registerProject(project);
        session.chooseProjectLeader(project, developer1);
        session.addDeveloperToProject(developer2, project);

        Session session2 = scheduler.logIn(developer2.getName(), developer2.getPin());
        session2.chooseProjectLeader(project, developer2);

        assertEquals(developer2, project.getProjectLeader());
        assertTrue(developer2.getProjects().contains(project));
        assertTrue(project.getDevelopers().contains(developer2));
	}
	
	@Test
	public void testAsNonProjectDeveloperOrAuthor() throws Exception { // Peter
		Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
		Project project = new Project("Peters Projekt");
		session.registerProject(project);
		
		session = scheduler.logIn(developer2.getName(), developer2.getPin());
		try {
			session.chooseProjectLeader(project, developer2);
			fail("Expected InsufficientRightsException");
		} catch (InsufficientRightsException e) {
			assertEquals("Only the project author or a developer on the project can choose a project leader", e.getMessage());
		}
	}
	
	@Test
	public void testAsNonRegisteredDeveloper() throws Exception { // Peter
		Session session = scheduler.logIn(developer1.getName(), developer1.getPin());
		Project project = new Project("Peters Projekt");
		session.registerProject(project);
		Developer fakeDeveloper = new Developer("Un Registered", "12345");
		try {
			session.chooseProjectLeader(project, fakeDeveloper);
			fail("Expected NonRegisteredDeveloperException");
		} catch (NonRegisteredDeveloperException e) {
			assertEquals(fakeDeveloper, e.getDeveloper());
			assertEquals("Project leader must be a registered developer", e.getMessage());
		}
	}
}
