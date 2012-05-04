package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;

import java.io.InputStream;
import java.io.PrintStream;

public class SessionDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Dialog previousDialog;

    public SessionDialog(Scheduler scheduler, Session session, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        Choice[] choices = new Choice[] {
                new Choice("Create a new project and choose a project leader for it", new CreateProjectDialog(scheduler, session, this)),
                new Choice("Create a new private activity", null),
                new Choice("Select a project", new SelectProjectDialog(scheduler, session, this)),
                new Choice("View activities that has been assigned to me", null),
                new Choice("Log out", previousDialog),
        };
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
