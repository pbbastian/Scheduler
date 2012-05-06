package softwarehuset.scheduler.application;

import softwarehuset.scheduler.domain.*;
import softwarehuset.scheduler.exceptions.*;
import softwarehuset.scheduler.domain.PrivateActivity;
import softwarehuset.scheduler.domain.ActivityTimePeriod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

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
        } else if (project.getName() == null) {
            throw new NullPointerException("Project name cannot be null");
        } else if (project.getName().length() < 1) {
            throw new ArgumentException(project.getName(), "Project name must have a length of at least 1");
        }
        project.setId(UUID.randomUUID().toString());
        project.setAuthor(developer);
        this.scheduler.getProjects().add(project);
    }

    public void chooseProjectLeader(Project project, Developer projectLeader) throws InsufficientRightsException, NonRegisteredDeveloperException, NonProjectLeaderException {
        if (!scheduler.isRegistered(projectLeader)) {
            throw new NonRegisteredDeveloperException(projectLeader, "Project leader must be a registered developer");
        }
        if (!project.getAuthor().equals(developer) && !project.getDevelopers().contains(developer)) {
            throw new InsufficientRightsException("Only the project author or a developer on the project can choose a project leader");
        }
        project.setProjectLeader(projectLeader);
        projectLeader.getProjects().add(project);
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
        project.addDeveloper(developer);
    }

    public void addActivityToProject(Activity activity, Project project) throws ArgumentException, NonProjectLeaderException, NonRegisteredProjectException {
        if (activity.getDescription() == null) {
            throw new NullPointerException("Activity description cannot be null");
        }
        if (activity.getStart() == null) {
            throw new NullPointerException("Activity creation date cannot be null");
        }
        if (activity.getEnd() == null) {
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
        project.addActivity(activity);
    }

    public void assignActivityToDeveloper(Activity activity, Developer developer) throws InsufficientRightsException, DeveloperNotInProjectException {
        if (!activity.getProject().getProjectLeader().equals(this.developer)) {
            throw new InsufficientRightsException("Only the project leader can assign activities to developers");
        }
        if (!activity.getProject().getDevelopers().contains(developer)) {
            throw new DeveloperNotInProjectException("The developer is not in the project that the activity belongs to");
        }
        developer.addActivity(activity);
    }

    public void setActivityStatus(Activity activity, Status status) throws InsufficientRightsException {
        if (status == null) {
            throw new NullPointerException("Activity status cannot be null");
        }
        if (!activity.getProject().getProjectLeader().equals(developer) && !activity.getDevelopers().contains(developer)) {
            throw new InsufficientRightsException("Only the project leader or a developer assigned to the activity can set its status");
        }
        activity.setStatus(status);
    }

    public void setProjectStatus(Project project, Status status) throws InsufficientRightsException, OngoingOrPausedActivitiesException {
        if (!project.getProjectLeader().equals(developer)) {
            throw new InsufficientRightsException("Only the project leader can set its status");
        }
        if (status == null) {
            throw new NullPointerException("Project status cannot be null");
        }
        List<Activity> ongoingOrPausedActivities = new ArrayList<Activity>();
        for (Activity activity : project.getActivities()) {
            if (activity.getStatus() == Status.ONGOING || activity.getStatus() == Status.PAUSED) {
                ongoingOrPausedActivities.add(activity);
            }
        }
        if (status.equals(Status.COMPLETED) && !ongoingOrPausedActivities.isEmpty()) {
            throw new OngoingOrPausedActivitiesException(ongoingOrPausedActivities,
                    "A project can't be marked as complete if any activities is marked as ongoing or paused");
        }
        project.setStatus(status);
    }

    public void removeActivity(Activity activity) throws InsufficientRightsException {
        if (!activity.getProject().getProjectLeader().equals(developer)) {
            throw new InsufficientRightsException("Only the project leader can remove activities");
        }
        activity.getProject().removeActivity(activity);
    }

    public void removeProject(Project project) throws InsufficientRightsException {
        if (!project.getAuthor().equals(developer) && !project.getProjectLeader().equals(developer)) {
            throw new InsufficientRightsException("Only the project author or leader can remove it");
        }
        scheduler.getProjects().remove(project);
        for (Developer developer : project.getDevelopers()) {
            developer.getProjects().remove(project);
            for (Activity activity : project.getActivities()) {
                developer.getCurrentActivities().remove(activity);
            }
        }
    }

    public void removeDeveloperFromProject(Developer developer, Project project) throws InsufficientRightsException {
        if (!project.getProjectLeader().equals(this.developer)) {
            throw new InsufficientRightsException("Only the project leader can remove developers from the project");
        }
        project.removeDeveloper(developer);
    }

    public void unassignActivityFromDeveloper(Activity activity, Developer developer) throws ActivityNotAssignedToDeveloperException {
        if (!activity.getDevelopers().contains(developer)) {
            throw new ActivityNotAssignedToDeveloperException("Can't unassign an activity from a developer that it's not assigned to");
        }
        developer.removeActivity(activity);
    }

    public void addPrivateActivity(PrivateActivity privateActivity) throws ArgumentException {
        if (privateActivity.getDescription() == null || privateActivity.getStart() == null || privateActivity.getEnd() == null) {
            throw new NullPointerException();
        }
        if (privateActivity.getDescription().length() < 1) {
            throw new ArgumentException(privateActivity.getDescription(), "Private activity description must have a length of minimum 1");
        }
        developer.addPrivateActivity(privateActivity);
    }

    public void removePrivateActivity(PrivateActivity activity) {
        developer.getPrivateActivities().remove(activity);
    }

	public List<Developer> getAvailableDevelopers(int maxPrivate, int maxProject) {
		List<Developer> availableDevelopers = new ArrayList<Developer>();
		for (Developer developer : scheduler.getDevelopers()) {
			if (developer != this.developer) {
				if (developer.getPrivateActivities().size() <= maxPrivate && developer.getCurrentActivities().size() <= maxProject) {
					availableDevelopers.add(developer);
				}
			}
			
		}
		return availableDevelopers;
	}

	public double getTimeSpentOnActivities(Project project) throws InsufficientRightsException {
		if (!this.developer.equals(project.getProjectLeader())) {
			throw new InsufficientRightsException("Only a project leader is allowed to do this.");
		}
		int timeSpent = 0;
		for (ActivityTimePeriod timePeriod : developer.getActivityTimePeriods()) {
            if (timePeriod.getActivity().getProject().equals(project)) {
                timeSpent += 1 + timePeriod.getToHour() - timePeriod.getFromHour();
            }
        }
		return timeSpent;
	}

	public void spendTimeOnProjectActivity(Project project, Activity activity, double time) throws Exception {
		if (!project.getDevelopers().contains(developer)) {
			throw new InsufficientRightsException("Only developers associated with this project are allowed to spend time.");
		}
	}
	
	public void generateProjectReport(Project project) throws IOException, InsufficientRightsException {
		String projectName = project.getName();
		int nrOfDevs = project.getDevelopers().size();
		int nrOfActivities = project.getActivities().size();
		double totalTimeSpent = getTimeSpentOnActivities(project);
		double timePerDev = totalTimeSpent/nrOfDevs;
		double timePerActivity = totalTimeSpent/nrOfActivities;
		
		
		String textOut = "<!DOCTYPE html PUBLIC >\n<head>\n<title>\nProject Report</title>\n</head>\n<body>\n<h1>Project Report : "+projectName+"</h1>\n<p>TOTAL TIME SPENT: "+totalTimeSpent+"<br>\nAvg. time spent by a Developer: "+timePerDev+"<br>\nAvg. time spent on an Activity: "+timePerActivity+"</p>\n</html>\n";
    	BufferedWriter out = new BufferedWriter(new FileWriter(projectName+"_report.html"));
    	out.write(textOut);
    	out.close();
	}

    public void requestAssistance(Activity activity, Developer assistingDeveloper) throws InsufficientRightsException, NonRegisteredDeveloperException {
        if (!this.developer.getCurrentActivities().contains(activity)) {
            throw new InsufficientRightsException("Only developers assigned to the activity can request assistance with it");
        }
        if (!scheduler.getDevelopers().contains(assistingDeveloper)) {
            throw new NonRegisteredDeveloperException(assistingDeveloper, "Assisting developer must be registered in the system");
        }
        assistingDeveloper.addActivity(activity);
    }

    public void spendTimeOnProjectActivity(ActivityTimePeriod timePeriod) throws InsufficientRightsException, ArgumentException {
        if (!developer.getCurrentActivities().contains(timePeriod.getActivity())) {
            throw new InsufficientRightsException("Only a developer assigned to the activity (or one assisting) can spend time on it.");
        }
        if (timePeriod.getFromHour() < 0 || !(timePeriod.getFromHour() < timePeriod.getToHour())) {
            throw new ArgumentException(new Integer(timePeriod.getFromHour()), "Start hour must be at least 0 and less than the end hour");
        }
        if (timePeriod.getToHour() > 23) {
            throw new ArgumentException(new Integer(timePeriod.getToHour()), "End hour must be at at most 23");
        }
        timePeriod.setDate(GregorianCalendar.getInstance());
        developer.getActivityTimePeriods().add(timePeriod);
    }

    public void removeActivityTimePeriod(ActivityTimePeriod timePeriod) throws InsufficientRightsException {
        if (!developer.getActivityTimePeriods().contains(timePeriod)) {
            throw new InsufficientRightsException("Only the developer that spend the time can remove it");
        }
        developer.getActivityTimePeriods().remove(timePeriod);
    }

    public boolean[] getUnregisteredHours() {
        boolean[] unregisteredHours = new boolean[24];
        for (ActivityTimePeriod timePeriod : developer.getActivityTimePeriods()) {
            for (int i = timePeriod.getFromHour(); i <= timePeriod.getToHour(); i++) {
                unregisteredHours[i] = true;
            }
        }
        return unregisteredHours;
    }
}
