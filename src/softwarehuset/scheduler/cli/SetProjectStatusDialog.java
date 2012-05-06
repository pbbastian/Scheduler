package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.OngoingOrPausedActivitiesException;
import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;

public class SetProjectStatusDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public SetProjectStatusDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        Choice[] choices = new Choice[] {
                new Choice(Status.ONGOING.toString(), new InnerDialog(scheduler, session, project, Status.ONGOING, previousDialog)),
                new Choice(Status.PAUSED.toString(), new InnerDialog(scheduler, session, project, Status.PAUSED, previousDialog)),
                new Choice(Status.COMPLETED.toString(), new InnerDialog(scheduler, session, project, Status.COMPLETED, previousDialog)),
                new Choice(Status.CANCELED.toString(), new InnerDialog(scheduler, session, project, Status.CANCELED, previousDialog)),
                new Choice("Go back", previousDialog),
        };
        new ChoiceDialog(choices, "Available statuses:", "Select a status: ");
    }

    public class InnerDialog implements Dialog {
        private Scheduler scheduler;
        private Session session;
        private Project project;
        private Status status;
        private Dialog previousDialog;

        public InnerDialog(Scheduler scheduler, Session session, Project project, Status status, Dialog previousDialog) {
            this.scheduler = scheduler;
            this.session = session;
            this.project = project;
            this.status = status;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            try {
                session.setProjectStatus(project, status);
                out.println("You have successfully marked the project as '" + status.toString() + "'!");
            } catch (InsufficientRightsException e) {
                e.printStackTrace();  // This shouldn't be possible
            } catch (OngoingOrPausedActivitiesException e) {
                out.println("As the project still has ongoing or paused activities, you can't mark it as completed.");
            }
        }
    }
}
