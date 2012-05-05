package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.domain.Status;

import java.io.InputStream;
import java.io.PrintStream;

public class ProjectDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public ProjectDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        out.println("Currently selected project: " + project.getName());
        if (project.getStatus().equals(Status.ONGOING)) out.println("Status: Ongoing");
        else if (project.getStatus().equals(Status.PAUSED)) out.println("Status: Paused");
        else if (project.getStatus().equals(Status.CANCELED)) out.println("Status: Canceled");
        else if (project.getStatus().equals(Status.COMPLETED)) out.println("Status: Completed");
        else out.println("Status: Unknown");

        Choice[] choices = null;
        if (project.getProjectLeader().equals(session.getDeveloper())) {
            choices = new Choice[] {
                    new Choice("Create a new activity for this project", new CreateActivityDialog(scheduler, session, project, this)),
                    new Choice("Add a developer", null),
                    new Choice("View all activities", null),
                    new Choice("View activities assigned to me", null),
                    new Choice("View developers", null),
                    new Choice("Set the project status", null),
                    new Choice("Remove the project", null),
                    new Choice("Go back", previousDialog),
            };
        } else if (project.getAuthor().equals(session.getDeveloper())) {
            choices = new Choice[] {
                    new Choice("View all activities", null),
                    new Choice("View activities assigned to me", null),
                    new Choice("View developers", null),
                    new Choice("Remove the project", null),
                    new Choice("Go back", previousDialog),
            };
        }
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
