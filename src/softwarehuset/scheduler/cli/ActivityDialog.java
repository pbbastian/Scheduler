package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;

import java.io.InputStream;
import java.io.PrintStream;

public class ActivityDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Activity activity;

    public ActivityDialog(Scheduler scheduler, Session session, Activity activity) {
        this.scheduler = scheduler;
        this.session = session;
        this.activity = activity;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();

    }
}
