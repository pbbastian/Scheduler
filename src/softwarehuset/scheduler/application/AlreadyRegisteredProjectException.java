package softwarehuset.scheduler.application;

import softwarehuset.scheduler.domain.Project;

public class AlreadyRegisteredProjectException extends Exception {
	private Project project;
	
	public AlreadyRegisteredProjectException(Project project, String message) {
		super(message);
		this.project = project;
	}
	
	public Project getProject() {
		return project;
	}
}
