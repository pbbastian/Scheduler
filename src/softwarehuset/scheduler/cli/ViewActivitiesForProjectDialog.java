package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;

public class ViewActivitiesForProjectDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;

    public ViewActivitiesForProjectDialog(Scheduler scheduler, Session session, Project project) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();

    }
}
