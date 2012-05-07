package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivitiesAssignedToMeInProjectDialog implements Dialog { // Kristian
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public ActivitiesAssignedToMeInProjectDialog(Session session, Project project, Dialog previousDialog) {
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println("Activities in this project assigned to you:");
        List<Activity> activitiesAssignedToMe = new ArrayList<Activity>();
        for (Activity activity : project.getActivities()) {
            if (activity.getDevelopers().contains(session.getDeveloper())) {
                activitiesAssignedToMe.add(activity);
            }
        }
        int size = activitiesAssignedToMe.size();
        Choice[] choices = new Choice[size+1];
        for (int i = 0; i < size; i++) {
            Activity activity = activitiesAssignedToMe.get(i);
            String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                    + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription();
            choices[i] = new Choice(message, new ProjectActivityDialog(session, activity, this));
        }
        choices[size] = new Choice("Go back", previousDialog);
        new ChoiceDialog(choices, "Activities in this project:", "Select an activity: ").display(in, out);
    }
}
