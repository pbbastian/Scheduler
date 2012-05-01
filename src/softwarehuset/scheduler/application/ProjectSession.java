package softwarehuset.scheduler.application;

import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

public class ProjectSession {
	private Scheduler scheduler;
	private DeveloperSession developerSession;
	private Project project;
	
	ProjectSession(Scheduler scheduler, DeveloperSession developerSession, Project project) {
		this.scheduler = scheduler;
		this.developerSession = developerSession;
		this.project = project;
	}
	
	public void chooseProjectLeader(Developer projectLeader) throws InsufficientRightsException, InvalidProjectLeader, NonRegisteredDeveloperException {
		if (!scheduler.getDevelopers().contains(projectLeader)) {
			throw new NonRegisteredDeveloperException(projectLeader, "Project leader must be a registered developer");
		}
		if (!project.getAuthor().equals(developerSession.getDeveloper()) && !project.getDevelopers().contains(developerSession.getDeveloper())) {
			throw new InsufficientRightsException("Only the project author or a developer on the project can choose a project leader");
		}
		project.setProjectLeader(projectLeader);
	}
}
