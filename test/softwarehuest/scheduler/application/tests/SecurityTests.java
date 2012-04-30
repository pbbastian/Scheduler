package softwarehuest.scheduler.application.tests;

import static org.junit.Assert.*;

import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;

public class SecurityTests {
	Scheduler scheduler;
	
	@Before
	public void setUp() {
		scheduler = new Scheduler();
	}
	
	@Test
	public void registerDeveloper() throws InvalidNameException, InvalidPinException {
		Developer developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		assertNotNull(developer.getId());
		assertTrue(scheduler.getDevelopers().contains(developer));
	}
	
	@Test(expected=InvalidNameException.class)
	public void registerDeveloperWithNoName() throws InvalidNameException, InvalidPinException {
		Developer developer = new Developer("", "12345");
		scheduler.register(developer);
	}
	
	@Test(expected=InvalidNameException.class)
	public void registerDeveloperWithNullName() throws InvalidNameException, InvalidPinException {
		Developer developer = new Developer(null, "12345");
		scheduler.register(developer);
	}
	
	@Test(expected=InvalidPinException.class)
	public void registerDeveloperWithShortPin() throws InvalidNameException, InvalidPinException {
		Developer developer = new Developer("Peter Bay Bastian", "123");
		scheduler.register(developer);
	}
	
	@Test(expected=InvalidPinException.class)
	public void registerDeveloperWithNullPin() throws InvalidNameException, InvalidPinException {
		Developer developer = new Developer("Peter Bay Bastian", null);
		scheduler.register(developer);
	}
	
	@Test
	public void logInAsDeveloper() throws InvalidNameException, InvalidPinException, IncorrectCredentialsException {
		Developer developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		Session session = scheduler.logIn(developer.getName(), developer.getPin());
		assertEquals(developer, session.getDeveloper());
	}
	
	@Test(expected=IncorrectCredentialsException.class)
	public void logInWithIncorrectPin() throws InvalidNameException, InvalidPinException, IncorrectCredentialsException {
		Developer developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		Session session = scheduler.logIn(developer.getName(), "nope");
	}
	
	@Test(expected=IncorrectCredentialsException.class)
	public void logInWithIncorrectName() throws InvalidNameException, InvalidPinException, IncorrectCredentialsException {
		Developer developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		Session session = scheduler.logIn("nope", developer.getPin());
	}
}
