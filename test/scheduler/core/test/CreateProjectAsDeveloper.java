package scheduler.core.test;

import static org.junit.Assert.*;

import org.junit.*;

import scheduler.core.*;

public class CreateProjectAsDeveloper extends StandardContext {	
	@Test
	public void createProjectAsDeveloper() {
		Developer developer = scheduler.createDeveloper("Peter Bay Bastian", 1);
		Project project = developer.createProject("Test project");
		assertNotNull(project);
		assertNotNull(developer.getProjects().get(0));
		assertNotNull(scheduler.getProjects().get(0));
		assertEquals(developer, project.getAuthor());
	}
}
