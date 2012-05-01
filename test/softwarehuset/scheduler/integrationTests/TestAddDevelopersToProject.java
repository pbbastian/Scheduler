package softwarehuset.scheduler.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class TestAddDevelopersToProject {
	Scheduler scheduler;
	Developer author;
	Developer developer1;
	Developer developer2;
	DeveloperSession authorSession;
	DeveloperSession developer1Session;
	DeveloperSession developer2Session;
	Project project;
	
	@Before
	public void setUp() {
		scheduler = new Scheduler();
	}
}
