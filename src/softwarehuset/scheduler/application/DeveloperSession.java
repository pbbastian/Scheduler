package softwarehuset.scheduler.application;

import java.util.UUID;

import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

public class DeveloperSession {
	private Developer developer;
	private Scheduler scheduler;
	
	DeveloperSession(Scheduler scheduler, Developer developer) {
		this.scheduler = scheduler;
		this.developer = developer;
	}
	
	public void registerProject(Project project) throws ArgumentException, NullPointerException, AlreadyRegisteredProjectException {
		if (scheduler.getProjects().contains(project)) {
			throw new AlreadyRegisteredProjectException(project, "Project is already registered");
		}
		else if (project.getName() == null) {
			throw new NullPointerException("Project name cannot be null");
		} else if (project.getName().length() < 1) {
			throw new ArgumentException(project.getName(), "Project name must have a length of at least 1");
		}
		project.setId(UUID.randomUUID().toString());
		project.setAuthor(developer);
		this.scheduler.getProjects().add(project);
	}

	public Developer getDeveloper() {
		return developer;
	}
	
	public ProjectSession getProjectSession(Project project) throws NonRegisteredProjectException {
		if (!scheduler.getProjects().contains(project)) {
			throw new NonRegisteredProjectException(project, "Project must be a registered project");
		}
		return new ProjectSession(scheduler, this, project);
	}
}
