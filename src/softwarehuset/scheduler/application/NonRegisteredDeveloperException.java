package softwarehuset.scheduler.application;

import softwarehuset.scheduler.domain.Developer;

public class NonRegisteredDeveloperException extends Exception {
	private Developer developer;
	
	public NonRegisteredDeveloperException(Developer developer, String message) {
		super(message);
		this.developer = developer;
	}
	
	public Developer getDeveloper() {
		return developer;
	}
}
