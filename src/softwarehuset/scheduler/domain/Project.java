package softwarehuset.scheduler.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project {
	private String id;
	private String name;
	private Developer author;
	private Developer projectLeader;
	private boolean ongoing;
	private List<Developer> developers;
	private List<Activity> activities;
	
	public Project(String name) {
		this.name = name;
		this.developers = new ArrayList<Developer>();
		this.activities = new ArrayList<Activity>();
		this.ongoing = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Developer getAuthor() {
		return author;
	}

	public void setAuthor(Developer author) {
		this.author = author;
	}

	public Developer getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(Developer projectLeader) {
		this.projectLeader = projectLeader;
		if (!developers.contains(projectLeader)) {
			developers.add(projectLeader);
		}
	}
	
	public boolean isOngoing() {
		return ongoing;
	}
	
	public void setOngoing(boolean ongoing) {
		this.ongoing = ongoing;
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public List<Activity> getActivities() {
		return activities;
	}
}
