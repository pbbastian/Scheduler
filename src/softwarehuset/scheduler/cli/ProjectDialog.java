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
        out.println();
        out.println("Currently selected project: " + project.getName());
        out.println("Status: " + project.getStatus().toString());

        Choice[] choices = null;
        if (project.getProjectLeader().equals(session.getDeveloper())) {
            choices = new Choice[]{
                    new Choice("Create a new activity for this project", new CreateActivityDialog(scheduler, session, project, this)),
                    new Choice("Add a developer", new AddDeveloperDialog(scheduler, session, project, this)),
                    new Choice("View activities assigned to me", new ActivitiesAssignedToMeInProjectDialog(session, project, this)),
                    new Choice("View all activities", new ProjectActivitiesDialog(session, project, this)),
                    new Choice("View developers", new ProjectDevelopersDialog(session, project, this)),
                    new Choice("Set the project status", new SetProjectStatusDialog(scheduler, session, project, this)),
                    new Choice("Remove the project", new RemoveProjectDialog(scheduler, session, project, previousDialog)),
                    new Choice("Go back", previousDialog),
            };
        } else if (project.getAuthor().equals(session.getDeveloper())) {
            choices = new Choice[]{
                    new Choice("View all activities", null),
                    new Choice("View activities assigned to me", null),
                    new Choice("View developers", null),
                    new Choice("Remove the project", new RemoveProjectDialog(scheduler, session, project, previousDialog)),
                    new Choice("Go back", previousDialog),
            };
        } else {
            choices = new Choice[]{
                    new Choice("View all activities", null),
                    new Choice("View activities assigned to me", null),
                    new Choice("View developers", null),
                    new Choice("Go back", previousDialog),
            };
        }
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
