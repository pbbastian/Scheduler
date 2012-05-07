package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;

public class RemoveDeveloperFromProjectDialog implements Dialog { // Peter
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Developer developer;
    private Dialog previousDialog;

    public RemoveDeveloperFromProjectDialog(Session session, Project project, Developer developer, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.developer = developer;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        try {
            session.removeDeveloperFromProject(developer, project);
            out.println("You have successfully removed '" + developer.getName() + "' from the project!");
            previousDialog.display(in, out);
        } catch (InsufficientRightsException e) {
            e.printStackTrace();  // This shouldn't be possible
        }
    }
}
