package softwarehuset.scheduler.application;

import java.util.UUID;

import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

public class Session {
	private Developer developer;
	private Scheduler scheduler;
	
	Session(Scheduler scheduler, Developer developer) {
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
	
	public void chooseProjectLeader(Project project, Developer projectLeader) throws InsufficientRightsException, NonRegisteredDeveloperException {
		if (!scheduler.isRegistered(projectLeader)) {
			throw new NonRegisteredDeveloperException(projectLeader, "Project leader must be a registered developer");
		}
		if (!project.getAuthor().equals(developer) && !project.getDevelopers().contains(developer)) {
			throw new InsufficientRightsException("Only the project author or a developer on the project can choose a project leader");
		}
		project.setProjectLeader(projectLeader);
	}

	public Developer getDeveloper() {
		return developer;
	}

	public void addDeveloperToProject(Developer developer, Project project) throws NonProjectLeaderException, NonRegisteredDeveloperException {
		if (!scheduler.isRegistered(developer)) {
			throw new NonRegisteredDeveloperException(developer, "Developer must be a registered developer");
		}
		if (!project.getProjectLeader().equals(this.developer)) {
			throw new NonProjectLeaderException(this.developer, "Only the project leader can add developers");
		}
		project.getDevelopers().add(developer);
	}

	public void addActivityToProject(Activity activity, Project project) throws ArgumentException, NonProjectLeaderException, NonRegisteredProjectException {
		if (activity.getDescription() == null) {
			throw new NullPointerException("Activity description cannot be null");
		}
		if (activity.getCreationDate() == null) {
			throw new NullPointerException("Activity creation date cannot be null");
		}
		if (activity.getDueDate() == null) {
			throw new NullPointerException("Activity due date cannot be null");
		}
		if (activity.getDescription().length() < 1) {
			throw new ArgumentException(activity.getDescription(), "Activity description must have a length of minimum 1");
		}
		if (!project.getProjectLeader().equals(developer)) {
			throw new NonProjectLeaderException(developer, "Only the project leader can add activities");
		}
		if (!scheduler.isRegistered(project)) {
			throw new NonRegisteredProjectException(project, "Can't add an activity to a non-registered project");
		}
		activity.setAuthor(developer);
		activity.setId(UUID.randomUUID().toString());
		project.getActivities().add(activity);
	}
	
	public void endProject(Project project) throws NonProjectLeaderException {
		if (!project.getProjectLeader().equals(developer)) {
			throw new NonProjectLeaderException(developer, "Only the project leader can end projects");
		}
		project.setOngoing(false);
	}
}
