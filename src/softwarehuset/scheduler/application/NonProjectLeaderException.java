package softwarehuset.scheduler.application;

import softwarehuset.scheduler.domain.Developer;

public class NonProjectLeaderException extends Exception {
	private Developer developer;
	
	public NonProjectLeaderException(Developer developer, String message) {
		super(message);
		this.developer = developer;
	}

	public Developer getDeveloper() {
		return developer;
	}
	
}
