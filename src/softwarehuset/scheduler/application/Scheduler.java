package softwarehuset.scheduler.application;

import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.ArgumentException;
import softwarehuset.scheduler.exceptions.IncorrectCredentialsException;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class Scheduler {
	private List<Developer> developers;
	private List<Project> projects;
	
	public Scheduler() {
		this.developers = new ArrayList<Developer>();
		this.projects = new ArrayList<Project>();
	}

    public static GregorianCalendar getWeek(int week, int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.WEEK_OF_YEAR, week);
        return calendar;
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
	
	public Session logIn(String name, String pin) throws IncorrectCredentialsException {
		for (Developer developer : developers) {
			if (developer.getName().equals(name) && developer.getPin().equals(pin)) {
				return new Session(this, developer);
			}
		}
		
		throw new IncorrectCredentialsException("No user with that name and pin was found");
	}
	
	public boolean isRegistered(Developer projectLeader) {
		return developers.contains(projectLeader);
	}
	
	public boolean isRegistered(Project project) {
		return projects.contains(project);
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public List<Project> getProjects() {
		return projects;
	}

    public List<Activity> getRemainingActivities(Project project) {
        List<Activity> remainingActivities = new ArrayList<Activity>();
        for (Activity activity : project.getActivities()) {
            if (!activity.getStatus().equals(Status.COMPLETED) && !activity.getStatus().equals(Status.CANCELED)) {
                remainingActivities.add(activity);
            }
        }
        return remainingActivities;
    }
}
