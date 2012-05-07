package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;

public class RemoveActivityDialog implements Dialog { // Kristian
    private Session session;
    private Activity activity;
    private Dialog previousDialog;

    public RemoveActivityDialog(Session session, Activity activity, Dialog previousDialog) {
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