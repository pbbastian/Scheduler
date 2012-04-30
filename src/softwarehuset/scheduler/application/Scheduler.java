package softwarehuset.scheduler.application;

import java.util.*;

import softwarehuset.scheduler.domain.*;

public class Scheduler {
	private List<Developer> developers;
	private List<Project> projects;
	
	public Scheduler() {
		this.developers = new ArrayList<Developer>();
		this.projects = new ArrayList<Project>();
	}
	
	public void register(Developer developer) throws ArgumentException {
		if (developer.getName() == null) {
			throw new NullPointerException("Developer name cannot be null");
		} else if (developer.getName().length() < 1) {
			throw new ArgumentException(developer.getName(), "Name must have a length of minimum 1");
		}
		
		if (developer.getPin() == null) {
			throw new NullPointerException("Developer pin cannot be null");
		} else if (developer.getPin().length() < 4) {
			throw new ArgumentException(developer.getPin(), "Pin must have a length of minimum 4");
		}
		developer.setId(UUID.randomUUID().toString());
		developers.add(developer);
	}
	
	public DeveloperSession logIn(String name, String pin) throws IncorrectCredentialsException {
		for (Developer developer : developers) {
			if (developer.getName().equals(name) && developer.getPin().equals(pin)) {
				return new DeveloperSession(this, developer);
			}
		}
		
		throw new IncorrectCredentialsException("No user with that name and pin was found");
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(List<Developer> developers) {
		this.developers = developers;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
}
