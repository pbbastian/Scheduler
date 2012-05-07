package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.PrivateActivity;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewAllActivitiesForDeveloperDialog implements Dialog { // Peter
    private Scheduler scheduler;
    private Session session;
    private Dialog previousDialog;

    public ViewAllActivitiesForDeveloperDialog(Scheduler scheduler, Session session, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        if (session.getDeveloper().getProjects().isEmpty()) {
            out.println("\nYou aren't in any projects and therefor haven't been assigned any activities.");
            previousDialog.display(in, out);
        } else {
            out.println("\nActivities assigned to you:");
            for (Project project : session.getDeveloper().getProjects()) {
                out.println("  In project '" + project.getName() + "':");
                boolean any = false;
                for (Activity activity : project.getActivities()) {
                    if (activity.getDevelopers().contains(session.getDeveloper())) {
                        any = true;
                        String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                                + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": "
                                + activity.getDescription() + " (" + project.getStatus().toString() + ")";
                        out.println("    " + message);
                    }
                }
                if (!any) {
                    out.println("    None.");
                }
            }
            previousDialog.display(in, out);
        }
    }
}
