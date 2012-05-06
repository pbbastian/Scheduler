package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SetActivityStatusDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public SetActivityStatusDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        List<Choice> choiceList = new ArrayList<Choice>();
        for (Activity activity : project.getActivities()) {
            if (project.getProjectLeader().equals(session.getDeveloper()) || activity.getDevelopers().contains(session.getDeveloper())) {
                choiceList.add(new Choice(activity.getDescription(), new SelectStatusDialog(scheduler, session, activity, previousDialog)));
            }
        }
        choiceList.add(new Choice("Go back", previousDialog));
        Choice[] choices = choiceList.toArray(new Choice[choiceList.size()]);
    }

    public class SelectStatusDialog implements Dialog {
        private Scheduler scheduler;
        private Session session;
        private Activity activity;
        private Dialog previousDialog;

        public SelectStatusDialog(Scheduler scheduler, Session session, Activity activity, Dialog previousDialog) {
            this.scheduler = scheduler;
            this.session = session;
            this.activity = activity;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            Choice[] choices = new Choice[]{
                    new Choice(Status.ONGOING.toString(), new SetStatusDialog(scheduler, session, activity, Status.ONGOING, previousDialog)),
                    new Choice(Status.PAUSED.toString(), new SetStatusDialog(scheduler, session, activity, Status.PAUSED, previousDialog)),
                    new Choice(Status.COMPLETED.toString(), new SetStatusDialog(scheduler, session, activity, Status.COMPLETED, previousDialog)),
                    new Choice(Status.CANCELED.toString(), new SetStatusDialog(scheduler, session, activity, Status.CANCELED, previousDialog)),
                    new Choice("Go back", previousDialog),
            };
            new ChoiceDialog(choices, "Available activities:", "Select an activity: ");
        }

        public class SetStatusDialog implements Dialog {
            private Scheduler scheduler;
            private Session session;
            private Activity activity;
            private Status status;
            private Dialog previousDialog;

            public SetStatusDialog(Scheduler scheduler, Session session, Activity activity, Status status, Dialog previousDialog) {
                this.scheduler = scheduler;
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
}
