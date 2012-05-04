package softwarehuset.scheduler.integrationTests;

import static org.junit.Assert.*;

import org.junit.*;

import softwarehuset.scheduler.application.*;
import softwarehuset.scheduler.domain.*;
import softwarehuset.scheduler.exceptions.ArgumentException;

public class TestRegisterDeveloper {
	Scheduler scheduler;
	
	@Before
	public void setUp() {
		scheduler = new Scheduler();
	}
	
	@Test
	public void testWithValidDeveloper() throws ArgumentException {
		Developer developer = new Developer("Peter Bay Bastian", "12345");
		scheduler.register(developer);
		assertNotNull(developer.getId());
		assertTrue(scheduler.getDevelopers().contains(developer));
	}
	
	@Test
	public void testWhenNameIsNull() throws ArgumentException {
		Developer developer = new Developer(null, "12345");
		try {
			scheduler.register(developer);
			fail("Expected ArgumentException");
		} catch (NullPointerException e) {
			assertEquals("Developer name cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testWhenNameIsTooShort() {
		Developer developer = new Developer("", "12345");
		try {
			scheduler.register(developer);
			fail("Expected ArgumentException");
		} catch (ArgumentException e) {
			assertEquals(developer.getName(), e.getArgument());
			assertEquals("Name must have a length of minimum 1", e.getMessage());
		}
	}
	
	@Test
	public void testWhenPinIsNull() throws ArgumentException {
		Developer developer = new Developer("Peter Bay Bastian", null);
		try {
			scheduler.register(developer);
			fail("Expected ArgumentException");
		} catch (NullPointerException e) {
			assertEquals("Developer pin cannot be null", e.getMessage());
		}
	}
	
	@Test
	public void testWhenPinIsTooShort() {
		Developer developer = new Developer("Peter Bay Bastian", "123");
		try {
			scheduler.register(developer);
			fail("Expected ArgumentException");
		} catch (ArgumentException e) {
			assertEquals(developer.getPin(), e.getArgument());
			assertEquals("Pin must have a length of minimum 4", e.getMessage());
		}
	}
}
