package softwarehuset.scheduler.tests;

import static org.junit.Assert.*;
import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;
import softwarehuset.scheduler.exceptions.ArgumentException;
import softwarehuset.scheduler.exceptions.IncorrectCredentialsException;

public class TestLogIn { // Peter
	Scheduler scheduler;
	Developer developer;
	
	@Before
	public void setUp() throws Exception { // Peter
		scheduler = new Scheduler();
		developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
	}
	
	@Test
	public void testWithCorrectCredentials() throws Exception { // Peter
		Session session = scheduler.logIn(developer.getName(), developer.getPin());
		assertEquals(developer, session.getDeveloper());
	}
	
	@Test
	public void logInWithIncorrectPin() throws Exception { // Peter
		String fakePassword = "nope";
		try {
			scheduler.logIn(developer.getName(), fakePassword);
			fail("Expected IncorrectCredentialsException");
		} catch (IncorrectCredentialsException e) {
			assertEquals("No user with that name and pin was found", e.getMessage());
		}
	}
	
	@Test
	public void logInWithIncorrectName() throws Exception { // Peter
		String fakeName = "nope";
		try {
			scheduler.logIn(fakeName, developer.getPin());
			fail("Expected IncorrectCredentialsException");
		} catch (IncorrectCredentialsException e) {
			assertEquals("No user with that name and pin was found", e.getMessage());
		}
	}
}
