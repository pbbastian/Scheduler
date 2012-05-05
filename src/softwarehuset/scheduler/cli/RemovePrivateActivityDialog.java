package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.PrivateActivity;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class RemovePrivateActivityDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private PrivateActivity privateActivity;
    private Dialog previousDialog;

    public RemovePrivateActivityDialog(Scheduler scheduler, Session session, PrivateActivity privateActivity, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.privateActivity = privateActivity;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        out.print("Are you sure you want to remove the private activity (yes/no)? ");
        if (new Scanner(in).next().equalsIgnoreCase("yes")) {
            session.removePrivateActivity(privateActivity);
            out.print("You have successfully removed the private activity with the description '" + privateActivity.getDescription() + "'!");
            previousDialog.display(in, out);
        } else {
            previousDialog.display(in, out);
        }
    }
}
