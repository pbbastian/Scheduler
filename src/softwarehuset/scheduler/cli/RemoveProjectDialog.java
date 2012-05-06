package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;

public class RemoveProjectDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public RemoveProjectDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        new ChoiceDialog(new Choice[] {
            new Choice("Yes", new ContinueDialog()),
            new Choice("No", previousDialog)
        }, "Are you sure?", "Select an answer: ").display(in, out);
        
        // If we get here, the answer is "Yes"
        try {
            session.removeProject(project);
            out.println("You have successfully removed the project named '" + project.getName() + "'!");
        } catch (InsufficientRightsException e) {
            e.printStackTrace();  // This shouldn't be possible
        }
    }
}
