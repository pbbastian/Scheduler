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
        out.println();
        Choice[] choices = new Choice[] {
                new Choice("Create a new project and choose a project leader for it", new CreateProjectDialog(scheduler, session, this)),
                new Choice("Create a new private activity", new CreatePrivateActivity(scheduler, session, this)),
                new Choice("View projects", new ViewProjectsDialog(scheduler, session, this)),
                new Choice("View private activities", new ViewPrivateActivitiesDialog(scheduler, session, session.getDeveloper(), this)),
                new Choice("View activities that has been assigned to me", new ViewAllActivitiesForDeveloperDialog(scheduler, session, this)),
                new Choice("Log out", previousDialog),
        };
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
