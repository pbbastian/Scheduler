package scheduler.core.test;

import org.junit.*;

import scheduler.core.Scheduler;

public abstract class StandardContext {
	public Scheduler scheduler;
	
	@Before
	public void setUp() {
		scheduler = new Scheduler();
	}
}
