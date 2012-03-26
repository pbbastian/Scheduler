package scheduler.core;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
	private List<Developer> developers = new ArrayList<Developer>();
	private List<Project> projects = new ArrayList<Project>();
	
	public Developer createDeveloper(String name, int id) {
		Developer developer = new Developer(id, name, this);
		developers.add(developer);
		return developer;
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public List<Project> getProjects() {
		return projects;
	}

}
