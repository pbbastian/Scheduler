package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivitiesAssignedToMeInProjectDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public ActivitiesAssignedToMeInProjectDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
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
        if (activitiesAssignedToMe.isEmpty()) {
            out.println("  None.");
        } else {
            for (Activity activity : activitiesAssignedToMe) {
                String message = "  W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": "
                        + activity.getDescription() + " (" + project.getStatus().toString() + ")\n"
                        + "    Developers assigned to the activity: ";
                for (Developer assignedDeveloper : activity.getDevelopers()) {
                    message += assignedDeveloper.getName() + ", ";
                }
                message = message.substring(0, message.length()-2);
                out.println(message);
            }
        }
        previousDialog.display(in, out);
    }
}
