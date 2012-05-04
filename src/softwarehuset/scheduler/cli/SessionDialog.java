package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;

import java.io.InputStream;
import java.io.PrintStream;

public class SessionDialog implements Dialog {
    private Session session;
    private Dialog previousDialog;

    public SessionDialog(Session session, Dialog previousDialog) {
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        Choice[] choices = new Choice[] {
                new Choice("Create a new project", new CreateProjectDialog(session, this)),
                new Choice("Select a project", null),
                new Choice("View activities that has been assigned to me", null),
                new Choice("Log out", previousDialog),
        };
        new ChoiceDialog(choices).display(in, out);
    }
}
