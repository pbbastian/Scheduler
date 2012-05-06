package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class RemoveActivityDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public RemoveActivityDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        Choice[] choices = new Choice[project.getActivities().size()];

        for (int i = 0; i < project.getActivities().size(); i++) {
            Activity activity = project.getActivities().get(i);
            String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                    + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": "
                    + activity.getDescription() + " (" + project.getStatus().toString() + ")";
            choices[i] = new Choice(message, new InnerDialog(session, activity, previousDialog));
        }

        new ChoiceDialog(choices, "Available activities:", "Select an activity: ").display(in, out);
    }

    public class InnerDialog implements Dialog {
        private Session session;
        private Activity activity;
        private Dialog previousDialog;

        public InnerDialog(Session session, Activity activity, Dialog previousDialog) {
            this.session = session;
            this.activity = activity;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            try {
                session.removeActivity(activity);
                out.println("You have successfully removed the activity with the description '" + activity.getDescription() + "'!");
                previousDialog.display(in, out);
            } catch (InsufficientRightsException e) {
                e.printStackTrace();  // This shouldn't be possible
            }
        }
    }
}