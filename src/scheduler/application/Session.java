package scheduler.application;

import java.util.UUID;

import scheduler.domain.Developer;
import scheduler.domain.Project;

public class Session {
	private Developer developer;
	private Scheduler scheduler;
	
	Session(Scheduler scheduler, Developer developer) {
		this.scheduler = scheduler;
		this.developer = developer;
	}
	
	public void createProject(Project project) {
		project.setId(UUID.randomUUID().toString());
		project.setAuthor(developer);
		this.scheduler.getProjects().add(project);
	}

	public Developer getDeveloper() {
		return developer;
	}
}
