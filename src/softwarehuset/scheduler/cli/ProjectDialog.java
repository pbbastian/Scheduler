package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;

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
        out.println("Currently selected project: " + project.getName());
        Choice[] choices = new Choice[] {
                new Choice("Create a new activity for this project", null),
                new Choice("Add a developer to this project", null),
                new Choice("Select an activity that's in this this project", null),
                new Choice("Select a developer who's in this project", null),
                new Choice("Set the project status", null),
                new Choice("Remove the project", null),
                new Choice("Go back", previousDialog),
        };
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
