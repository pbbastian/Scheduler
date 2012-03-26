package scheduler.core;

import java.util.ArrayList;
import java.util.List;

import scheduler.core.Project;

public class Developer {
	private int id;
	private String name;
	private List<Project> projects = new ArrayList<Project>();
	private Scheduler scheduler;
	
	public Developer(int id, String name, Scheduler scheduler) {
		this.id = id;
		this.name = name;
		this.scheduler = scheduler;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public Project createProject(String name) {
		Project project = new Project(name, this);
		projects.add(project);
		scheduler.getProjects().add(project);
		return project;
	}

	public List<Project> getProjects() {
		return projects;
	}

}
