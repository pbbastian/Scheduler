package softwarehuset.scheduler.exceptions;

import softwarehuset.scheduler.domain.Project;

public class NonRegisteredProjectException extends Exception {
	private Project project;
	
	public NonRegisteredProjectException(Project project, String message) {
		super(message);
		this.project = project;
	}
	
	public Project getProject() {
		return project;
	}
}
