package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.PrivateActivity;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class ViewActivitiesForDeveloper implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Dialog previousDialog;

    public ViewActivitiesForDeveloper(Scheduler scheduler, Session session, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        if (session.getDeveloper().getPrivateActivities().isEmpty()) {
            out.println("You haven't been assigned to any private activities.");
        } else {
            out.println("\nPrivate activities:");
            for (PrivateActivity activity : session.getDeveloper().getPrivateActivities()) {
                out.println("  W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription());
            }
        }
        if (session.getDeveloper().getCurrentActivities().isEmpty()) {
            out.println("You haven't been assigned to any project activities.");
        } else {
            out.println("Activities from projects:");
            for (Activity activity : session.getDeveloper().getCurrentActivities()) {
                out.println("  W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription());
            }
            previousDialog.display(in, out);
        }
    }
}
