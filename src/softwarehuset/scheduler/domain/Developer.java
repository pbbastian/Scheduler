package softwarehuset.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

public class Developer { // Peter
	private String id;
	private String name;
	private String pin;
	private List<Project> projects;
	private List<PrivateActivity> privateActivities;
	private List<Activity> currentActivities;
    private List<ActivityTimePeriod> activityTimePeriods;

    public Developer(String name, String pin) {
		this.name = name;
		this.pin = pin;
		this.projects = new ArrayList<Project>();
		this.privateActivities = new ArrayList<PrivateActivity>();
		this.currentActivities = new ArrayList<Activity>();
        this.activityTimePeriods = new ArrayList<ActivityTimePeriod>();
	}
    
    public void addActivity(Activity activity) { // Kristian
        activity.getDevelopers().add(this);
        currentActivities.add(activity);
    }
	
	public boolean equals(Developer otherDeveloper) {
		return otherDeveloper.getId().equals(id);
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

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public List<Project> getProjects() {
		return projects;
	}

    public List<Project> getProjects(Status status) { // Peter
        List<Project> projectsWithStatus = new ArrayList<Project>();
        for (Project project : projects) {
            if (project.getStatus().equals(status)) {
                projectsWithStatus.add(project);
            }
        }
        return projectsWithStatus;
    }

	public List<PrivateActivity> getPrivateActivities() {
		return privateActivities;
	}

	public List<Activity> getCurrentActivities() {
		return currentActivities;
	}

    public void removeActivity(Activity activity) { // Kristian
        activity.getDevelopers().remove(this);
        currentActivities.remove(activity);
    }

    public void addPrivateActivity(PrivateActivity privateActivity) { // Peter
        privateActivity.setDeveloper(this);
        privateActivities.add(privateActivity);
    }


    public List<ActivityTimePeriod> getActivityTimePeriods() {
        return activityTimePeriods;
    }
}
