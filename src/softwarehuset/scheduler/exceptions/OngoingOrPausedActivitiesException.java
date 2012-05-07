package softwarehuset.scheduler.exceptions;

import softwarehuset.scheduler.domain.Activity;

import java.util.List;

public class OngoingOrPausedActivitiesException extends Exception {
    private List<Activity> activities;

    public OngoingOrPausedActivitiesException(List<Activity> activities, String message) {
        super(message);
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
