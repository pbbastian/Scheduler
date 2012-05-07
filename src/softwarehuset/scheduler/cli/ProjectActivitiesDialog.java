package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class ProjectActivitiesDialog implements Dialog { // Peter
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public ProjectActivitiesDialog(Session session, Project project, Dialog previousDialog) {
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        int size = project.getActivities().size();
        Choice[] choices = new Choice[size+1];
        for (int i = 0; i < size; i++) {
            Activity activity = project.getActivities().get(i);
            String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                    + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription();
            choices[i] = new Choice(message, new ProjectActivityDialog(session, activity, this));
        }
        choices[size] = new Choice("Go back", previousDialog);
        new ChoiceDialog(choices, "Activities in this project:", "Select an activity: ").display(in, out);
    }
}
