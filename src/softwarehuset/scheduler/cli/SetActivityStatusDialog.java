package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;

public class SetActivityStatusDialog implements Dialog { // Peter
    private Session session;
    private Activity activity;
    private Dialog previousDialog;

    public SetActivityStatusDialog(Session session, Activity activity, Dialog previousDialog) {
        this.session = session;
        this.activity = activity;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        Choice[] choices = new Choice[]{
                new Choice(Status.ONGOING.toString(), new InnerDialog(session, activity, Status.ONGOING, previousDialog)),
                new Choice(Status.PAUSED.toString(), new InnerDialog(session, activity, Status.PAUSED, previousDialog)),
                new Choice(Status.COMPLETED.toString(), new InnerDialog(session, activity, Status.COMPLETED, previousDialog)),
                new Choice(Status.CANCELED.toString(), new InnerDialog(session, activity, Status.CANCELED, previousDialog)),
                new Choice("Go back", previousDialog),
        };
        new ChoiceDialog(choices, "Available activities:", "Select an activity: ").display(in, out);
    }

    public class InnerDialog implements Dialog {
        private Session session;
        private Activity activity;
        private Status status;
        private Dialog previousDialog;

        public InnerDialog(Session session, Activity activity, Status status, Dialog previousDialog) {
            this.session = session;
            this.activity = activity;
            this.status = status;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            try {
                session.setActivityStatus(activity, status);
                out.println("You have successfully set the status of the activity '" + activity.getDescription() + "' to '" + status.toString() + "'!");
                previousDialog.display(in, out);
            } catch (InsufficientRightsException e) {
                e.printStackTrace();  // This shouldn't be possible
            }
        }
    }
}