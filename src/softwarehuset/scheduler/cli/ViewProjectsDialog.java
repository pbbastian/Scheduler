package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class ViewProjectsDialog implements Dialog { // Kristian
    private Scheduler scheduler;
    private Session session;
    private Dialog previousDialog;

    public ViewProjectsDialog(Scheduler scheduler, Session session, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        ArrayList<Choice> projectChoices = new ArrayList<Choice>(session.getDeveloper().getProjects().size());
        for (Project project : session.getDeveloper().getProjects()) {
            String description = project.getName() + " (";
            if (project.getProjectLeader().equals(session.getDeveloper())) {
                description += "project leader";
            } else if (project.getAuthor().equals(session.getDeveloper())) {
                description += "author";
            } else {
                description += "developer";
            }
            description += ")";
            projectChoices.add(new Choice(description, new ProjectDialog(scheduler, session, project, this)));
        }
        projectChoices.add(new Choice("Go back", previousDialog));

        Choice[] projectChoicesArray = new Choice[projectChoices.size()];
        new ChoiceDialog(projectChoices.toArray(projectChoicesArray), "Available projects:", "Select a project: ").display(in, out);
    }
}
