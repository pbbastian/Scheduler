package softwarehuset.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

public class Project { // Kristian
	private String id;
	private String name;
	private Developer author;
	private Developer projectLeader;
	private Status status;
	private List<Developer> developers;
	private List<Activity> activities;
	
	public Project(String name) {
		this.name = name;
		this.developers = new ArrayList<Developer>();
		this.activities = new ArrayList<Activity>();
		this.status = Status.ONGOING;
	}
	
	public void addActivity(Activity activity) { // Peter
		activity.setProject(this);
		activities.add(activity);
	}
	
	public void addDeveloper(Developer developer) { // Kristian
		developers.add(developer);
		developer.getProjects().add(this);
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

	public void setProjectLeader(Developer projectLeader) { // Peter
		this.projectLeader = projectLeader;
		if (!developers.contains(projectLeader)) {
			developers.add(projectLeader);
		}
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public List<Activity> getActivities() {
		return activities;
	}

    public List<Activity> getActivities(Status status) { // Peter
        List<Activity> activitiesWithStatus = new ArrayList<Activity>();
        for (Activity activity : activities) {
            if (activity.getStatus().equals(status)) {
                activitiesWithStatus.add(activity);
            }
        }
        return activitiesWithStatus;
    }

    public void removeActivity(Activity activity) { // Kristian
        activities.remove(activity);
        for (Developer developer : activity.getDevelopers()) {
            developer.getCurrentActivities().remove(activity);
        }
    }

    public void removeDeveloper(Developer developer) { // Kristian
        for (Activity activity : activities) {
            developer.getCurrentActivities().remove(activity);
        }
        developer.getProjects().remove(this);
        developers.remove(developer);
    }
}
